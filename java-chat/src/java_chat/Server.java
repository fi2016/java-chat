
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
		if (clientList.isEmpty() != true)
		{
			for (ClientProxy clientProxy : clientList)
			{
				clientProxy.sendCommand("EXT","We have shutdowned the server.");
				clientProxy.closeClient();
				clientList.remove(clientProxy);
			}
			clientList = null;
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
			try
			{
				serverSocket = new ServerSocket(port);
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		while (!Thread.currentThread().isInterrupted())
		{
			try  {
				Socket clientSocket = serverSocket.accept();
				acceptClient(clientSocket);
				Thread.sleep(100);
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
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			}
		}
		else
		{
			clientSocket.close();
		}
	}

	private void checkAdmin(ClientProxy cp)
	{
		//NICKNAME IST IMMERNOCH NULL
			String nickname = cp.getNickname();
			for (String admin : adminList)
			{
				if (nickname.equals(admin))
				{
					AdmintoolGUI aGUI = new AdmintoolGUI(this);
					aGUI.setVisible(true);
					cp.sendMessage("CMD\u001eENA\u001e4True");
					// Client anpassen das er weis was das ist *hust* Nico
					// machmal *hust*
				}
			}
	}

	private void handoverRoom()
	{
		
	}
	
	private void createRoom(String name, ClientProxy cp)
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
			c.getClientProxyList().add(cp);
			
			roomList.add(c);
			
			cp.sendCommand("new", name);
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
	
	protected void checkCommandType(String cmd, String pam, ClientProxy cp)
	{
				
		String command = cmd.substring(3, cmd.length());
		String parameter = pam.substring(3, pam.length());

		System.out.println("Server Anfang Checkcommandtype   " + cmd);
		String[] array = cmd.split("\u001e");
		
		switch (command)

		{
		case "add":
			addUser(parameter, cp);
			break;
			
		case "del":
			deleteUser(parameter);
			break;

		case "alt":
			changeUsername(parameter, cp);
			break;
			
		case "ena":
			
			break;
		
		case "cnr":
			System.out.println("Server Vor Reate Room " + cmd);
			createRoom(parameter, cp);
			System.out.println("Server checktype: " + cmd);
			break;

		default:
			break;
		}
	}
	
	protected void changeUsername(String nick, ClientProxy c)
	{
		ChatRoom temp = null;
		
		for (ChatRoom room : roomList)
		{
			if(room.getName().equals("public"))
			{
				temp = room;
				nickInUse = room.searchUser(nick);	
			}
		}
		if(nickInUse == false)
		{
			//String oldNick = c.getNickname();
			c.setNickname(nick);
			
			ArrayList<ClientProxy> cpList = temp.getClientProxyList();
			for (ClientProxy cp : cpList)
			{
				cp.sendMessage("CMDalt\u100ePAM" + nick);
			}
		}
		else
		{
			//Fehlermeldung
		}
	}//push try
	
	protected void deleteUser(String nick)
	{
		
	}
	
	protected void addUser(String nick, ClientProxy c)
	{
		ChatRoom temp = null;
		
		for (ChatRoom room : roomList)
		{
			if(room.getName().equals("public"))
			{
				temp = room;
				nickInUse = room.searchUser(nick);
			}
		}
		if(nickInUse == false)
		{
			c.setNickname(nick);
			checkAdmin(c);
			
			ArrayList<ClientProxy> cpList = temp.getClientProxyList();
			for (ClientProxy cp : cpList)
			{
				cp.sendMessage("CMDadd\u001ePAM" + nick);
			}
		}
		else
		{
			//Fehlermeldung an den einen CP°!!
		}
	}
}
