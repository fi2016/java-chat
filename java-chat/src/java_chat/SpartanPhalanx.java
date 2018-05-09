package java_chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.*;

public class SpartanPhalanx implements Runnable
{
	private HashMap<String, LocalDateTime> connectionProtocol;
	private ArrayList<String> connectionBanList;
	private Thread t;
	
	public SpartanPhalanx()
	{
		connectionProtocol = new HashMap<String, LocalDateTime>();
		connectionBanList = new ArrayList<String>();
		t = new Thread(this);
		t.setName("DumpBlacklist");
		t.start();
	}
	
	protected Boolean identifyDDos(String ip)
	{	
		//Check Client Banned
		if(!connectionBanList.contains(ip))
		{
			if(connectionProtocol.containsKey(ip))
			{
				LocalDateTime lastLogin = connectionProtocol.get(ip);
				LocalDateTime now = LocalDateTime.now();
				
				if(now.getSecond() - lastLogin.getSecond() <= 2)
				{
					connectionProtocol.put(ip,now);
					return false;
				}
				else
				{
					connectionBanList.add(ip);
					connectionProtocol.put(ip,now);
					return true;
				}
			}
			else
			{
				connectionProtocol.put(ip, LocalDateTime.now());
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	public void run()
	{
		try
		{
			Thread.sleep(2000);
			connectionProtocol.clear();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	protected Boolean identifySpam()
	{
		
		return null;
	}
}
