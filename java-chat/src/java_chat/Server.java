
package java_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements Runnable
{
	private ArrayList<ClientProxy> clientList; // Schwachsinn da Public room
												// ClientList hat bitte ändern
												// bzw entfernen
	private ServerSocket serverSocket;
	private ArrayList<ChatRoom> roomList;
	private String ip;
	private int port;
	private AdmintoolGUI admintoolGUI;
	private SpartanPhalanx spartanPhalanx;
	private boolean nickInUse;
	private HashMap<String, Long> blacklist = new HashMap<>();
	private ArrayList<String> adminList = new ArrayList<String>();
	
	

	public Server(int port, String ip)
	{
		clientList = new ArrayList<ClientProxy>();
		roomList = new ArrayList<ChatRoom>();
		createRoom("public");
		spartanPhalanx = new SpartanPhalanx();
		this.port = port;
		this.ip = ip;
		adminList.add("Deine Mudda");
		adminList.add("Darth Vader");
		adminList.add("MilianFortnite");
		adminList.add("Mini49");
		adminList.add("n0ize");
		adminList.add("xXPuSsYD3Str0y3rXx");
	}

	public void distributeMessage(String msg, String chn)
	{
		for (ChatRoom room : roomList)
		{
			if (room.getName().equals(chn))
			{
				room.distributeMessage(msg);
				
			}
		}
	}

	public void closeServer()
	{
		if (clientList.isEmpty() != true)
		{
			for (ClientProxy clientProxy : clientList)
			{
				clientProxy.closeClient();
				clientList.remove(clientProxy);
			}
			clientList = null;
		}
		try
		{
			Socket dummySocket = new Socket(ip, port);
			serverSocket.close();
			dummySocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void openAdmintool() throws UnknownHostException, IOException
	{
		admintoolGUI = new AdmintoolGUI(this);
		admintoolGUI.setVisible(true);
	}

	public void closeClient(ClientProxy client)
	{

		client.closeClient();
		// distributeMessage(client.getNickname() + "hat sich abgemeldet!");
		clientList.remove(client);
	}

	@Override
	public void run()
	{

		while (!Thread.currentThread().isInterrupted())
		{
			try
			{
				serverSocket = new ServerSocket(port);
				Socket clientSocket = serverSocket.accept();
				acceptClient(clientSocket);
				Thread.sleep(100);
				serverSocket.close();
			}
			catch (IOException e)
			{
				System.err.println("IOException " + e);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}
	}

	private void acceptClient(Socket clientSocket) throws IOException
	{
		if (clientList != null)
		{
			if (spartanPhalanx.identifyDDos(clientSocket.getInetAddress().toString()))
			{
				clientSocket.close();
			}
			else
			{
				ClientProxy c = new ClientProxy(clientSocket, this);
				clientList.add(c);
				roomList.get(0).addClient(c);
				// checkAdmin();
			}
		}
		else
		{
			clientSocket.close();
		}
	}

	private void checkAdmin()
	{
		for (ClientProxy cp : clientList)
		{
			String nickname = cp.getNickname();
			for (String admin : adminList)
			{
				if (nickname.equals(admin))
				{
					cp.sendMessage("CMD\u001eENA\u001e4True");
					// Client anpassen das er weis was das ist *hust* Nico
					// machmal *hust*
				}
			}
		}
	}

	private void createRoom(String name)
	{
		boolean vergeben = false;
		for (ChatRoom room : roomList)
		{
			if (room.getName().equals(name))
			{
				vergeben = true;
			}
		}

		if (vergeben == false)
		{
			ChatRoom c = new ChatRoom();
			c.setName(name);
			roomList.add(c);
		}
		else
		{
			// Fehlermeldung zurückgeben
		}
	}

	public ArrayList<ChatRoom> getRoomList()
	{
		return roomList;
	}

	public void setRoomList(ArrayList<ChatRoom> roomList)
	{
		this.roomList = roomList;
	}

	protected void addBlacklist(String clip)
	{
		blacklist.put(clip, System.currentTimeMillis());
	}

	public ArrayList<ClientProxy> getClientList()
	{
		return clientList;
	}
	
	protected void checkCommandType(String cmd)
	{
		String[] array = cmd.split("\u001e");
		
		String command = array[0].substring(3, 5);
		String parameter = array[1].substring(3, array[1].length());
		
		switch (command)
		{
		case "add":
			addUser(parameter);
			break;
			
		case "del":
			deleteUser(parameter);
			break;

		case "alt":
			changeUsername(parameter);
			break;
			
		case "ena":
			
			break;

		default:
			break;
		}
	}
	
	protected void changeUsername(String nick)
	{
		for (ChatRoom room : roomList)
		{
			if(room.getName().equals("public"))
			{
				
				nickInUse = room.searchUsername();
				
			}
		}
		if(nickInUse == false)
		{
			//Nick in Proxy setzen
			//RoomList updaten
		}
		else
		{
			//Fehlermeldung
		}
	}
	
	protected void deleteUser(String nick)
	{
		
	}
	
	protected void addUser(String nick)
	{
		for (ChatRoom room : roomList)
		{
			if(room.getName().equals("public"))
			{
				
				nickInUse = room.searchUsername();
				
			}
		}
		if(nickInUse == false)
		{
			//Nick in Proxy setzen
			//RoomList updaten
		}
		else
		{
			//Fehlermeldung
		}
	}
}
