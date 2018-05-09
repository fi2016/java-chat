/*"Neuer Raum erstellen"
- ClientGUI: GUI Knöpfe





//in Client Funktion zum Erstellen eines Rooms (-> Room-Klasse) (eigene Liste führen)
//Client: "Ich erstelle einen Raum"-Funktion (-> ChatRoom-Klasse)
*/
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
	//	c.joinRoom()
		//Nachricht an Client: Du wurdest hinzugefügt
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
	protected void closeRoom()
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
