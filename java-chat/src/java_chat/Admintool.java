package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Admintool extends Client
{
	
	
	
	public Admintool() throws UnknownHostException, IOException
	{
		super();
		// TODO Auto-generated constructor stub
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
		
	}
	
	

	
}
