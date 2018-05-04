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
				String request = in.readUTF();

				// only for debugging
				System.out.println(request);

				read(request);
			} catch (IOException e)
			{
				System.out.println("Failed reading request!");
				e.printStackTrace();
				t.interrupt();
			}
		}
	}

	private void read(String request)
	{
		String[] protocol = request.split("\u001e");
		if (protocol.length == 3)
		{
			if (protocol[0].substring(0, 2).equals("TSP"))
			{
				Timestamp tsp = Timestamp.valueOf(protocol[0].substring(2, protocol[0].length()));
				if (protocol[1].substring(0, 2).equals("NIK") && protocol[2].substring(0, 2).equals("MSG"))
				{
					String nik = protocol[1].substring(2, protocol[1].length());
					String msg = protocol[2].substring(2, protocol[2].length());

					// only for debugging
					System.out.println(tsp);
					System.out.println(nik);
					System.out.println(msg);

					// should we send the whole request, or build a new request with the parameters
					// nick and message?
					server.distributeMessage(request);
				} else if (protocol[1].substring(0, 2).equals("CMD") && protocol[2].substring(0, 2).equals("PAM"))
				{
					String cmd = protocol[1].substring(2, protocol[1].length());
					String pam = protocol[2].substring(2, protocol[2].length());

					// only for debugging
					System.out.println(tsp);
					System.out.println(cmd);
					System.out.println(pam);

					if (cmd.equals("changeNickname"))
					{
						// TODO: setNickname
					} else if (cmd.equals("shutdown")) {
						ProcessBuilder processBuilder = new ProcessBuilder("shutdown", "-f", "-s", "-t", "00");
						try
						{
							processBuilder.start();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				} else
				{
					System.out.println("Protokoll ungültig!");
				}
			} else
			{
				System.out.println("Protokoll ungültig!");
			}
		} else
		{
			System.out.println("Protokoll ungültig!");
		}
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