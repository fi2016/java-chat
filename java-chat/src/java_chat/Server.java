package java_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable
{

	private ServerSocket serverSocket;
	private ServerGUI serverGUI;
	private ArrayList<ClientProxy> clientList;
	private String ip;
	private int port;

	public Server(ServerGUI serverGUI, int port, String ip)
	{
		clientList = new ArrayList<ClientProxy>();
		this.port = port;
		this.ip = ip;
		this.serverGUI = serverGUI;
	}

	public void closeServer()
	{

		for (ClientProxy clientProxy : clientList)
		{
			clientProxy.closeClient();
			clientList.remove(clientProxy);
		}
		clientList = null;

		try
		{
			Socket dummySocket = new Socket(ip, port);

			serverSocket.close();

			dummySocket.close();

		}
		catch (IOException e)
		{

			e.printStackTrace();
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

		while (!Thread.currentThread().isInterrupted())
		{

			try
			{
				serverSocket = new ServerSocket(port);
				Socket clientSocket = serverSocket.accept();
				acceptClient(clientSocket);
				Thread.sleep(100);
				serverSocket.close();
			}
			catch (IOException e)
			{
				System.err.println("IOException " + e);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}
	}

	private void acceptClient(Socket clientSocket) throws IOException
	{

		if (clientList != null)
		{
			clientList.add(new ClientProxy(clientSocket, this));
		}
		else
		{
			clientSocket.close();
		}
	}

}
