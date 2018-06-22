package java_chat;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Admintool
{
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
	private ArrayList<ChatRoom> rooms = new ArrayList<ChatRoom>();
	private Server server;

	public Admintool(Server server, ArrayList<ClientProxy> clientList, ArrayList<ChatRoom> roomList )
	{
		this.server = server;
		clients = clientList;
		rooms = roomList;
		refreshLists();
	}

	protected void kickClient(ClientProxy clientProxy)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		//command ruft closeClient auf und zeigt die nachricht
		clientProxy.sendCommand("KIK", message);
		refreshLists();
	}

	protected void banClient(ClientProxy clientProxy)
	{
		String message = JOptionPane.showInputDialog(null, "Grund eingeben");
		//command ruft getIP + closeClient auf und zeigt die nachricht
		clientProxy.sendCommand("BAN", message); 
		refreshLists();
	}

	protected void closeChatroom(ChatRoom chatRoom)
	{
		rooms.remove(chatRoom);
		chatRoom.closeRoom();		
	}

	private void refreshLists()
	{
		clients.clear();
		for (ClientProxy client : server.getClientList())
		{
			clients.add(client);
		}
		rooms.clear();
		for(ChatRoom cr : server.getRoomList())
		{
			rooms.add(cr);
		}
	}

	public ArrayList<ChatRoom> getRoomList()
	{
		return rooms;
	}
}

