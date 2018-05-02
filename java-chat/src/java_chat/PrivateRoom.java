package java_chat;

import java.util.ArrayList;

public class PrivateRoom
{
	private String name;
	private String password = null;
	
	private ArrayList<ClientProxy> clientProxyList;
	
	public PrivateRoom()
	{
		clientProxyList = new ArrayList<ClientProxy>();
	}
	
	private void inviteClient(ClientProxy c)
	{
		
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
	
	private void closeRoom()
	{
		
	}
}
