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
	 
	public ChatRoom(ClientProxy c) //Für das Raumerstellen vom User aus
	{
		clientProxyList = new ArrayList<ClientProxy>();
		clientProxyList.add(c);
	}
	
	public void inviteClient()
	{
		
		//Nachricht an Client: Du wurdest eingeladen -> Willst du beitreten?
	}
	
	public void addClient(ClientProxy c)
	{
		clientProxyList.add(c);
		
		c.sendCommand("jor", name);
		
		String pam = c.getNickname() + ";" + this.name;
		c.sendCommand("ncm", pam);
		
		System.out.println("ChatRoom: Ende addClient");
		//Nachricht an alle im Raum: Jemand neues ist da
	}
	
	public void distributeMessage(String msg)
	{
		for (ClientProxy clientProxy : clientProxyList)
		{
			clientProxy.sendMessage(msg);
		}
	}
	
	//-------------------------- Ab da ist das die Arbeit Anderer
	
	protected boolean searchUser(String nick)
	{
		boolean check = false;
		
		for (ClientProxy clientProxy : clientProxyList)
		{
			if(nick.equals(clientProxy.getNickname()))
			{
				check = true;
			}
		}
		
		return check;
	}
	
	protected void closeRoom()
	{
		//Alle Clients ausm Raum schmeißen
		for (ClientProxy clientProxy : clientProxyList)
		{
			clientProxy.closeClient();
		}
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

	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
	public ArrayList<ClientProxy> getClientProxyList()
	{
		return clientProxyList;
	}

	public void setClientProxyList(ArrayList<ClientProxy> clientProxyList)
	{
		this.clientProxyList = clientProxyList;
	}
	

}
