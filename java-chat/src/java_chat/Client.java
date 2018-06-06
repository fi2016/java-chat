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
	private ClientGUI client;
	private Socket socket;
	private ObjectOutputStream out;
	private String nickname;
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

	public Client() throws UnknownHostException, IOException
	{
		t = new Thread(this);
		t.setName("ClientReadingThread");
	}
	
	protected void connectServer(String server) throws UnknownHostException, IOException
	{
		socket = new Socket(server, 8008);
		t.start();
	}
	
	protected void sendMessage(String message, String roomName) throws IOException
	{
		if(out == null)
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		message = "TSP" + timestamp + "\u001eNIK"  + nickname   + "\u001eCHN" + roomName +  "\u001eMSG" + message; 
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
					in = new ObjectInputStream(socket.getInputStream());
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
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void showNotification(String message)
	{
		client.showNotification(message);
	}
	
	protected void addBlacklist()
	{
		//TO-DO: IP an Server schicken um gesperrt zu werden
	}
	
	protected void getIp()
	{
		ip = socket.getRemoteSocketAddress().toString();
		server.addBlacklist();
		
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
		String request = (String) obj;
		
		String[] protocol = request.split("\u001e");
		
		if(protocol[0].substring(0, 3).equals("TSP") && protocol[1].substring(0, 3).equals("NIK") && protocol[2].substring(0, 3).equals("CHN") && protocol[3].substring(0, 3).equals("MSG")) 
		{
			
			// von Carry + Daniel gemacht. Nicht sicher, ob richtig so
			Timestamp tsp = Timestamp.valueOf(protocol[0].substring(3, protocol[0].length()));
			String nik = protocol[1].substring(3, protocol[1].length());	
			String chn = protocol[2].substring(3, protocol[2].length());	
			String msg = protocol[3].substring(3, protocol[3].length());	
			
			for (Room room : this.getRoomList())
			{
				if(room.getName().equals(chn))
				{
					room.getHistory().add(request);
				}
			}
			System.out.println("Client nach for" + msg);
		}
		else 
		{
			//System.out.println(protocol[0].substring(0, 3));
			//System.out.println("TSP: "+protocol[0] + " MSG: " + protocol[1]);
			//System.out.println("Protokoll ungültig!");
		}
	}
	
	protected void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
}
