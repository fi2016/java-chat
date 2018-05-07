package java_chat;

import java.util.ArrayList;

public class Room
{
	private String name;
	private ArrayList<String> memberList;
	private ArrayList<String> history;
	
	public Room()
	{
		memberList = new ArrayList<String>();
		history = new ArrayList<String>();
		history.add("Room opened");
	}
}
