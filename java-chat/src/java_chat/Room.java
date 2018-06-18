package java_chat;

import java.util.ArrayList;

public class Room
{
	private String name;
	private ArrayList<String> memberList;
	private ArrayList<String> history;
	
	public Room()
	{
	}
	
	public Room(String name)
	{
		memberList = new ArrayList<String>();
		history = new ArrayList<String>();
		history.add("Room opened");
		this.name = name;
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
	
	public void addMessage(String msg)
	{
		history.add(msg);
	}
	public void setHistory(ArrayList<String> history)
	{
		this.history = history;
	}

	
}
