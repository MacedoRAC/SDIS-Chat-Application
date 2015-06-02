package Connection;

import com.sun.net.httpserver.HttpExchange;

public class Request {
	
	private boolean ready=false;
	private String type="";
	private String sender="";
	private String receiver="";
	private String answer="";
	private HttpExchange request;
	
	public Request(String type, String sender, HttpExchange request) {
		this.type=type;
		this.sender=sender;
		this.setRequest(request);
	}
	public Request(String type, String sender, String receiver, HttpExchange request) {
		this.type=type;
		this.sender=sender;
		this.receiver=receiver;
		this.setRequest(request);
	}
	
	public boolean isReady() {
		return ready;
	}
	public String getType() {
		return type;
	}
	public String getSender() {
		return sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public String getAnswer() {
		return answer;
	}
	public HttpExchange getRequest() {
		return request;
	}
	
	public void setState(boolean state) {
		this.ready = state;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setRequest(HttpExchange request) {
		this.request = request;
	}
		
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Request))
            return false;
        if (obj == this)
            return true;

        Request rhs = (Request) obj;
        return this.isReady()==rhs.isReady()
        		&& this.type.equals(rhs.type)
        		&& this.sender.equals(rhs.sender)
        		&& this.receiver.equals(rhs.receiver);
    }
	
}
