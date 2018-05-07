package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Admintool extends Client
{	
	private ArrayList<Client> clients = new ArrayList<Client>();
	
	public Admintool(Server server) throws UnknownHostException, IOException
	{
		super();
		
	}

	public void kickClient(Client client)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		client.showNotification(message);
		client.closeClient();
	}
	
	public void banClient(Client client)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		client.showNotification(message);
		client.addBlacklist();
		client.closeClient();
	}
	
	public void closeChatroom(ChatRoom chatRoom)
	{
		chatRoom.closeRoom();
	}
}
