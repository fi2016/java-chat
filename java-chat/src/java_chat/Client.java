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

	public Client(ClientGUI gui) throws UnknownHostException, IOException
	{
		t = new Thread(this);
		this.client = gui;
		t.setName("ClientReadingThread");
	}
	
	protected void connectServer(String server) throws UnknownHostException, IOException
	{
		socket = new Socket(server, 8008);
		t.start();
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
						System.out.println("einmal hats geklappt");
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
		client.showNotification(message);
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
		String request = (String) obj;
		System.out.println("requst:" + request);

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
		else
		{
			System.out.println("Ungültiges Protokoll im Client (read-Methode)!");
		}
	}
	
	protected void distributeMessage(Timestamp tsp, String chn, String msg)
	{
		for (Room room : roomList)
		{
			System.out.println("vor if" + msg);
			System.out.println("room " + room.getName() + "!");
			System.out.println("CHN " + chn+ "!");
			if(room.getName().equals(chn))
			{
				System.out.println("driN");
				//room.getHistory().add(msg); // tsp +
				room.addMessage(msg);
				client.showHistory();
				System.out.println(msg);
			}
			System.out.println("nach if" + msg);
		}
	}
	
	protected void sendCommand(String cmd)
	{
		
	}
	
	protected void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
}
