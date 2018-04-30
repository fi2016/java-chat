package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProxy implements Runnable
{
	private Socket socket;
	private Server server;

	private String nickname;

	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	private Thread t;

	public ClientProxy() // Konstruktor, um Thread zu starten
	{
	}

	public ClientProxy(Socket socket, Server server)
	{
		this.socket = socket;
		this.server = server;

		t = new Thread(new ClientProxy());
		t.start(); // Starte Lesen-Thread
	}

	@Override
	public void run()
	{
		while (t.isInterrupted() == false)
		{
			if (in == null)
			{
				in = new ObjectInputStream(socket.getInputStream());
			}
		
			String message = in.readUTF();
		}
	}

	protected void sendMessage(String message)
	{
		if (out == null)
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}

		out.writeUTF(message);
	}

	protected void closeClient()
	{
		try
		{
			// Protokoll
			t.interrupt();
			out.close();
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
