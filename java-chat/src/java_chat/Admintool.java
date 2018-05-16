package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Admintool extends Client
{	
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
	private Server server;
	private Client client;
	
	
	public Admintool(Server server) throws UnknownHostException, IOException
	{
		super();
		this.server = server;
		client = new Client();
	}

	protected void connect(String server)
	{
		try
		{
			client.connectServer(server);
		}
		catch (UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Der angegebene Host ist nicht bekannt");
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "IO Exception AdmintoolGUI beim connecten");
		}
	}
	
	protected void disconnect()
	{
		closeClient();
	}
	
	protected void kickClient(Client client)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		client.showNotification(message);
		client.closeClient();
		refreshLists();
	}
	
	protected void banClient(Client client)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		client.showNotification(message);
		server.addBlacklist(client);
		client.getIp();
		client.closeClient();
		refreshLists();
	}
	
	protected void closeChatroom(ChatRoom chatRoom)
	{
		chatRoom.closeRoom();
		refreshRooms();
	}
	
	private void refreshLists()
	{
		clients.clear();
		for (ClientProxy client : server.getClientList())
		{
			clients.add(client);
		}
	}
	
	protected void refreshRooms()
	{
		
	}
}
