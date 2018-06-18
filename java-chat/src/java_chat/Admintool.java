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

	protected void kickClient(ClientProxy clientProxy)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		clientProxy.showNotification(message);
		clientProxy.closeClient();
		refreshLists();
	}

	protected void banClient(ClientProxy clientProxy)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		clientProxy.showNotification(message);
		clientProxy.getIp();
		clientProxy.closeClient();
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

