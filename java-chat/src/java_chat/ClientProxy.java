package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;

public class ClientProxy implements Runnable
{

	private Socket socket;
	private Server server;
	private String nickname;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread t;

	public ClientProxy(Socket socket, Server server)
	{
		this.socket = socket;
		this.server = server;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run()
	{
		while (!t.isInterrupted())
		{
			try
			{
				if (in == null)
				{
					in = new ObjectInputStream(socket.getInputStream());
				}
				try
				{
					read(in.readObject());
				} catch (ClassNotFoundException e)
				{
					System.out.println("Class not found");
					e.printStackTrace();
					t.interrupt();
				}
			} catch (IOException e)
			{
				System.out.println("Failed reading request!");
				e.printStackTrace();
				t.interrupt();
			}
		}
	}

	private void read(Object obj)
	{
		String request = (String) obj;
		
		String[] protocol = request.split("\u001e");
		if (protocol.length == 2) 
		{
			if (protocol[0].substring(0, 2).equals("TSP") && protocol[1].substring(0, 2).equals("MSG")) 
			{
				Timestamp tsp = Timestamp.valueOf(protocol[0].substring(2, protocol[0].length()));
				String msg = protocol[1].substring(2, protocol[0].length());	
				
				checkSpam(msg,tsp);
			} else 
			{
				System.out.println("Protokoll ung�ltig!");
			}
		} else 
		{
			System.out.println("Protokoll ung�ltig!");
		}
		
		//�berpr�fen des timestamps & channels (spam)
		//�berpr�fen ob immer das selbe geschickt wird
		
		sendMessage(request);
	}

	

	private void checkSpam(String msg, Timestamp tsp)
	{
		// TODO Auto-generated method stub
		
	}

	protected void sendMessage(String message)
	{
		try
		{
			if (out == null)
			{
				out = new ObjectOutputStream(socket.getOutputStream());
			}
			out.writeUTF(message);
		} catch (IOException e)
		{
			System.out.println("Failed sending response!");
			e.printStackTrace();
		}
	}

	protected void closeClient()
	{
		try
		{
			t.interrupt();
			out.close();
			socket.close();
		} catch (IOException e)
		{
			System.out.println("Failed closing client!");
			e.printStackTrace();
		}
	}

	public String getNickname()
	{
		return nickname;
	}
}