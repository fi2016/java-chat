package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Admintool extends Client
{	
	private ArrayList<Client> clients = new ArrayList<Client>();
	private Server server;
	
	public Admintool(Server server) throws UnknownHostException, IOException
	{
		super();
		this.server = server;
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
		//server.addBlacklist(client);
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
