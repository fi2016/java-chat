package java_chat;

import java.io.IOException;
import java.net.UnknownHostException;

public class Admintool extends Client
{
	
	
	
	public Admintool() throws UnknownHostException, IOException
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public void kickClient(Client client)
	{
		client.closeClient();
	}
	
	public void banClient(Client client)
	{
		
	}
	
	public void closeChatroom(ChatRoom chatRoom)
	{
		
	}
	
	

	
}
