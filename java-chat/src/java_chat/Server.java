package java_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements Runnable
{
	private ArrayList<ClientProxy> clientList;
	private ServerSocket serverSocket;
	private ArrayList<ChatRoom> roomList;
	private String ip;
	private int port;
	private AdmintoolGUI admintoolGUI;
	private SpartanPhalanx spartanPhalanx;
	private long time;
	HashMap<String, Long> blacklist = new HashMap<>();
	private SpartanPhalanx spartanPhalan;
	private ArrayList<String> adminList = new ArrayList<String>();

	public Server(ServerGUI serverGUI, int port, String ip)
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

	public void addBlacklist()
	{
		time = System.currentTimeMillis();
		blacklist.put(ip, time);
	}

	public void closeServer()
	{
		for (ClientProxy clientProxy : clientList)
		{
			clientProxy.closeClient();
			clientList.remove(clientProxy);
		}
		clientList = null;
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
				checkAdmin();
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
			if (room.getName() == name)
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

	protected void addBlacklist(Client client)
	{

	}

	public ArrayList<ClientProxy> getClientList()
	{
		return clientList;
	}
}
