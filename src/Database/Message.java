package Database;

import java.util.Date;

/**
 * Created by André on 01/06/2015.
 */
public class Message {
	
	private String content;
	private String sender;
	private Date date;
	
	public Message(String content, String sender)
	{
		this.content=content;
		this.sender=sender;
		this.date = new Date();
	}
	
	public Message(String content,String sender, Date date)
	{
		this.content=content;
		this.sender=sender;
		this.date = date;
	}
	
	public String getContent()
	{
		return content;
	}	
	public String getSender()
	{
		return sender;
	}	
	public Date getDate()
	{
		return date;
	}
	
	public String toString() {
		return "["+date.toString()+" - "+sender+"]: "+content;
	}
	
}
