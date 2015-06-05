package Database;

import java.util.Date;

/**
 * Created by Andr� on 01/06/2015.
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
	
}
