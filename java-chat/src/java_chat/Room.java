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
	
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<String> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<String> memberList)
	{
		this.memberList = memberList;
	}
	public ArrayList<String> getHistory()
	{
		return history;
	}
	public void setHistory(ArrayList<String> history)
	{
		this.history = history;
	}
	public Room(String name)
	{
		super();
		this.name = name;
	}
	
}
