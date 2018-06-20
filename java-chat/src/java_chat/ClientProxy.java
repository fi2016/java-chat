package java_chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;

public class ClientProxy implements Runnable
{

	private Socket socket;
	private Server server;
	private String nickname;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread t;
	private HashMap<String, Timestamp> messageBuffer;

	public ClientProxy(Socket socket, Server server)
	{
		messageBuffer = new HashMap<String, Timestamp>();
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

				read(in.readUTF());
			}
			catch (IOException e)
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
		System.out.println(request);

		String[] protocol = request.split("\u001e");

		if (protocol[0].substring(0, 3).equals("TSP") && protocol[1].substring(0, 3).equals("CHN") && protocol[2].substring(0, 3).equals("MSG"))
		{
			Timestamp tsp = Timestamp.valueOf(protocol[0].substring(3, protocol[0].length()));
			String chn = protocol[1].substring(3, protocol[1].length());
			String msg = protocol[2].substring(3, protocol[2].length());

			String nikmsg = nickname + ": " + msg;
			
			String fullmessage = "TSP" + tsp + "\u001eCHN" + chn + "\u001eMSG" + nikmsg; //Message zusammengesetzt nach Protokoll		
			
			if (checkSpam(msg, tsp))
			{
				System.out.println("Diese Nachricht war SPAM!");
			}
			else
			{
				server.distributeMessage(fullmessage, chn);
			}
		}
		else if(protocol[0].substring(0, 3).equals("CMD") && protocol[1].substring(0, 3).equals("PAM"))
		{	
			server.checkCommandType(protocol[0], protocol[1], this);	
		}
		else
		{
			System.out.println("Ungültiges Protokoll in der ClientProxy (read-Methode)!");
		}
	}

	private boolean checkSpam(String msg, Timestamp tsp)
	{
		for (Timestamp t : messageBuffer.values())
		{
			if (t.getNanos() >= tsp.getNanos() - 100)
			{
				return true;
			}
			else
			{
				if (messageBuffer.containsKey(msg))
				{
					return true;
				}
				else
				{
					messageBuffer.put(msg, tsp);
					return false;
				}
			}
		}
		return false;
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
			out.flush();

		}
		catch (IOException e)
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
		}
		catch (IOException e)
		{
			System.out.println("Failed closing client!");
			e.printStackTrace();
		}
	}
	

	protected void sendCommand(String cmd, String pam)
	{
		try
		{
			if(out == null)
			{
				out = new ObjectOutputStream(socket.getOutputStream());
			}
			String message = "CMD" + cmd + "\u001ePAM" + pam; 
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			if(checkSpam(pam,timestamp))
			{
				System.out.println("THIS IS SPA(RTA!!!)M");
			}
			else
			{
				out.writeUTF(message);
				out.flush();
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void setNickname(String nick)
	{
		nickname = nick;
	}

	public String getNickname()
	{
		return nickname;
	}
}
