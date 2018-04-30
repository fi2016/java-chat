package java_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server implements Runnable
{



	private ServerSocket serverSocket;
	
	private ServerGUI serverGUI;
	private ArrayList<ClientProxy> clientList;
	private String ip;
	private int port;
	

	public Server()
	{
		clientList = new ArrayList<ClientProxy>();
	}
	
	public Boolean startServer(ServerGUI serverGUI, int port, String ip)
	{
		try
		{
			(new Thread(new Server())).start();
			
			this.port = port;
			this.ip = ip;
			this.serverGUI = serverGUI;
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public void closeServer()
	{
		
		try
		{
			for (ClientProxy clientProxy : clientList)
			{
				closeClient(clientProxy);
			}
			clientList = null;
			
			Thread.currentThread().interrupt();
			
			//DummyConnect um Socket zu schlieﬂen
			Socket dummySocket = new Socket(ip, 8008);
			dummySocket.close();
			serverSocket.close();
		} catch (UnknownHostException e)
		{
			System.err.println("UnknownHostException " + e);
			
		} catch (IOException e)
		{
			System.err.println("IOException " + e);
			
		}
		
				
	}
	
	public void distributeMessage(String msg)
	{	
		for (ClientProxy clientProxy : clientList)
		{
			clientProxy.sendMessage(msg);
		}
	}
	
	public void closeClient(ClientProxy client)
	{
		
		client.closeClient();
		distributeMessage(client.getNickname() + "hat sich abgemeldet!");
		clientList.remove(client);
	}
	
	@Override
	public void run()
	{
		
		while(!Thread.currentThread().isInterrupted())
		{
			
			try  
			{
				serverSocket = new ServerSocket(Integer.valueOf(port));

				acceptClient();
				
				Thread.sleep(100);
				
				serverSocket.close();
				
			}
			catch (IOException e)
			{
				System.err.println("Exception beim Listen auf Port: " + port);
	
				System.err.println(e.getMessage());
				
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();	
			}
		}
	}

	private void acceptClient() throws IOException
	{
		
		Socket clientSocket = serverSocket.accept();
		if(clientList != null)
		{
			clientList.add(new ClientProxy(clientSocket,this));
		}
	}
	
}
