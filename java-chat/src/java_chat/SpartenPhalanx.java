package java_chat;

import java.util.HashMap;
import java.time.*;

public class SpartenPhalanx implements Runnable
{
	private HashMap<String, LocalDateTime> connectionProtocol;
	private Thread t;
	
	public SpartenPhalanx()
	{
		connectionProtocol = new HashMap<String, LocalDateTime>();
		t = new Thread(this);
		t.setName("DumpBlacklist");
		t.start();
	}
	
	protected Boolean identifyDDos(String ip)
	{
		if(connectionProtocol.containsKey(ip))
		{
			return true;
		}
		else
		{
			connectionProtocol.put(ip, LocalDateTime.now());
			return false;
		}
	}

	@Override
	public void run()
	{
		try
		{
			Thread.currentThread().sleep(2000);
			connectionProtocol.clear();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
