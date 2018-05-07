package java_chat;

import java.util.ArrayList;

public class ChatRoom
{
	private String name;
	private String password = null;
	
	private ArrayList<ClientProxy> clientProxyList;
	
	public ChatRoom()
	{
		clientProxyList = new ArrayList<ClientProxy>();
	}
	
	private void inviteClient()
	{
		//Nachricht an Client: Du wurdest eingeladen -> Willst du beitreten?
	}
	
	private void addClient(ClientProxy c)
	{
		clientProxyList.add(c);
		
		//Nachricht an Client: Du wurdest hinzugefügt
		//Nachricht an alle im Raum: Jemand neues ist da
	}
	
	private void closeRoom()
	{
		//Alle Clients ausm Raum schmeißen
	}	
	
	
	private void kickClient()
	{
		
	}
	
	private void banClient()
	{
		
	}
	
	private void muteClient()
	{
		
	}

	

}
