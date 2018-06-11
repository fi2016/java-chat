package java_chat;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Admintool
{
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
	private Server server;

	public Admintool(Server server)
	{
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
		client.getIp();
		client.closeClient();
		refreshLists();
	}

	protected void closeChatroom(ChatRoom chatRoom)
	{
		chatRoom.closeRoom();
	}

	private void refreshLists()
	{
		clients.clear();
		for (ClientProxy client : server.getClientList())
		{
			clients.add(client);
		}
	}

	public ArrayList<Room> getRoomList()
	{
		// TODO Auto-generated method stub
		return null;
	}
}

