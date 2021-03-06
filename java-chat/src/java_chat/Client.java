package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Client implements Runnable
{
	private ClientGUI clientGui;
	private Socket socket;
	private ObjectOutputStream out;
	private String nickname = "newNick";
	private ObjectInputStream in;
	private Thread t;
	private String request;
	private String ip;
	private Server server;
	
	private ArrayList<Room> roomList = new ArrayList<Room>();
	
	public ArrayList<Room> getRoomList()
	{
		return roomList;
	}

	public void setRoomList(ArrayList<Room> roomList)
	{
		this.roomList = roomList;
	}

	public Client(ClientGUI gui) throws UnknownHostException, IOException
	{
		t = new Thread(this);
		this.clientGui = gui;
		t.setName("ClientReadingThread");
	}
	
	protected void connectServer(String server) throws UnknownHostException, IOException
	{
		socket = new Socket(server, 8008);
		t.start();
		
		sendCommand("add", clientGui.textFieldNickname.getText());
	}
	
	protected void sendMessage(String message, String chn) throws IOException
	{
		if(out == null)
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		message = "TSP" + timestamp + "\u001eCHN" + chn +  "\u001eMSG" + message; 
		
		out.writeUTF(message);
		out.flush();
	}
	
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			try
			{
				if (in == null)
				{
					if(socket.isConnected())
					{
						in = new ObjectInputStream(socket.getInputStream());
					}	
				}
		
				read(in.readUTF());
				
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void showNotification(String message)
	{
		clientGui.showNotification(message);
	}
	
	protected void addBlacklist()
	{
		//TO-DO: IP an Server schicken um gesperrt zu werden
	}
	
	protected void getIp()
	{
		ip = socket.getRemoteSocketAddress().toString();
		server.addBlacklist(ip);
		
	}
	
	protected void closeClient()
	{
		t.interrupt();
		try
		{
			out.close();
			in.close();
			socket.close();
		}
		catch(IOException e)
		{
			System.out.println("Fehler beim schließen!");
		}
	}
	
	protected void read(Object obj)
	{
		request = (String) obj;

		String[] protocol = request.split("\u001e");

		if (protocol[0].substring(0, 3).equals("TSP") && protocol[1].substring(0, 3).equals("CHN") && protocol[2].substring(0, 3).equals("MSG"))
		{
			
			Timestamp tsp = Timestamp.valueOf(protocol[0].substring(3, protocol[0].length()));
			String chn = protocol[1].substring(3, protocol[1].length());
			String msg = protocol[2].substring(3, protocol[2].length());			
			
			System.out.println("TSP: " +tsp);
			System.out.println("CHN: " +chn);
			System.out.println("MSG: " +msg);
			
			this.distributeMessage(tsp, chn, msg);
		}
		else if (protocol[0].substring(0, 3).equals("CMD") && protocol[1].substring(0, 3).equals("PAM"))
		{
			String pam = protocol[1].substring(3, protocol[1].length());
			switch (protocol[0].substring(3, protocol[0].length()))
			{	
				case "kik":
					clientGui.showNotification(pam);
					closeClient();
					break;
				case "ban":
					clientGui.showNotification(pam);
					getIp();
					closeClient();
				case "alt":
				changeNick(pam);
					break;
				
				case "add":
					addNick(pam);
					break;
					
				case "del":
					deleteNick(pam);
					break;
					
				case "cnr":
					createRoom(pam);
					break;	
					
				case "new":
					addRoomtoList(pam);
					break;
					
				case "jor":
					createRoom(pam);
					break;
					
				case "ncm":
					String[] parts = request.split(";");
					String name = parts[0];
					String chn = parts[1];
					
					addMembertoList(name, chn);
					break;
					
				case "sdr":
					getRooms(pam);
					break;
				
				case "EXT":
					clientGui.showNotification(pam);
					closeClient();
					break;

				default:
					System.out.println(protocol);
				break;
			}
		}
		else
		{
			System.out.println("Ungültiges Protokoll im Client (read-Methode)!");
		}
	}
	
	private void deleteNick(String nick)
	{
		if(nickname.equals(nick))
		{
			closeClient();
		}
		else
		{
			for (Room room : roomList)
			{
				room.getMemberList().remove(nick);
			}
		}
	}

	private void addNick(String newNick)
	{
		if(nickname.equals("newNick"))
		{
			nickname = newNick;
			clientGui.setOldNick(nickname);
		}
		else
		{
			for (Room room : roomList)
			{
				room.getMemberList().add(nickname);
			}
		}
	}

	private void changeNick(String newNick)
	{
		String oldNick = clientGui.getOldNick();
		
		if(oldNick.equals(nickname))
		{
			nickname = newNick;
		}
		else
		{
			for (Room room : roomList)
			{
				if(room.getMemberList().contains(oldNick))
				{
					oldNick = newNick;
				}
			}
		}
	}
	
	protected void distributeMessage(Timestamp tsp, String chn, String msg)
	{
		for (Room room : roomList)
		{
			if(room.getName().equals(chn))
			{
				room.addMessage(msg);
				clientGui.updateHistory(msg);
			}
		}
	}
	
	protected void sendCommand(String cmd, String pam)
	{
		try
		{
			if(out == null)
			{
				out = new ObjectOutputStream(socket.getOutputStream());
			}
			String message = "CMD" + cmd + "\u001ePAM" + pam; 
			out.writeUTF(message);
			out.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	protected void createRoom(String name)
	{
		System.out.println("Client: Anfang createRoom");
		Room r = new Room(name);
		clientGui.createTab(r);
		getRoomList().add(r);
		System.out.println("Client: Ende createRoom");
	}
	
	
	private void addRoomtoList(String name)
	{
		clientGui.addRoomtoRoomlist(name);
	}
	
	private void addMembertoList(String name, String chn)
	{
		Room r = findRoom(chn);
		
		if(r != null)
		{
			r.getMemberList().add(name);
		}
	}
	
	private Room findRoom(String chn)
	{		
		for (Room room : roomList)
		{
			if (room.getName().equals(chn))
			{
				return room;
			}
		}
		return null;
	}
	
	private void getRooms(String rooms)
	{
		String[] parts = rooms.split(";");
		
		for (String chn : parts)
		{
			addRoomtoList(chn);
		}
		
	}
	
	protected void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
}
