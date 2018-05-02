package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable
{
	private ClientGUI client;
	private Socket socket;
	private ObjectOutputStream out;
	private String nickname;
	private ObjectInputStream in;
	private Thread t;
	
	public Client() throws UnknownHostException, IOException
	{
		t = new Thread(this);
		t.setName("ClientReadingThread");
		t.start();
	}
	
	protected void connectServer(String server) throws UnknownHostException, IOException
	{
		socket = new Socket(server, 8008);//push für nico
	}
	
	private void sendMessage(String message) throws IOException
	{
		if(out == null)
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		out.writeUTF(message);
	}
	
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			try
			{
				read();
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			catch (NullPointerException e)
			{
				
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void closeClient()
	{
		
		//not finished
	}
	
	private void read() throws IOException
	{
		String message = null;
		if (out == null) 
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		out.writeUTF(message);
	}
	
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
}
