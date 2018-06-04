package java_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
		
		if (protocol[0].substring(0, 3).equals("TSP") && protocol[1].substring(0, 3).equals("MSG")) 
		{
			Timestamp tsp = Timestamp.valueOf(protocol[0].substring(3, protocol[0].length()));
			String msg = protocol[1].substring(3, protocol[1].length());	
			
			if(checkSpam(msg,tsp))
			{
				System.out.println("Diese Nachricht war SPAM!");
			}
			else
			{
				sendMessage(request);
			}
		}
		else if(protocol[0].substring(0, 3).equals("TSP") && protocol[1].substring(0, 3).equals("NIK") && protocol[2].substring(0, 3).equals("CHN") && protocol[3].substring(0, 3).equals("MSG")) 
		{
			// von Carry + Daniel gemacht. Nicht sicher, ob richtig so
			Timestamp tsp = Timestamp.valueOf(protocol[0].substring(3, protocol[0].length()));
			String nik = protocol[1].substring(3, protocol[1].length());	
			String chn = protocol[2].substring(3, protocol[2].length());	
			String msg = protocol[3].substring(3, protocol[3].length());	
			
			if(checkSpam(msg,tsp))
			{
				System.out.println("Diese Nachricht war SPAM!");
			}
			else
			{
				sendMessage(request);
			}
		}
		else 
		{
			System.out.println(protocol[0].substring(0, 3));
			System.out.println("TSP: "+protocol[0] + " MSG: " + protocol[1]);
			System.out.println("Protokoll ungültig!");
		}
		
	}
		
		
		//überprüfen des timestamps & channels (spam)
		//überprüfen ob immer das selbe geschickt wird
		
	

	

	private boolean checkSpam(String msg, Timestamp tsp)
	{
		
		for (Timestamp t : messageBuffer.values())
		{
			if(t.getNanos() >= tsp.getNanos() - 100)
			{
				return true;
			}
			else
			{
				if(messageBuffer.containsKey(msg))
				{
					return true;
				}
				else
				{
					messageBuffer.put(msg,tsp);
					return false;
				}
			}
			//Spam selbe Nachricht
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