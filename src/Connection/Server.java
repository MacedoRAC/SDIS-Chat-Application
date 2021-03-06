package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import Database.Channel;
import Database.Database;
import Database.Message;
import Database.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {

	private static Database db;
	private static HttpServer server;
	private static ArrayList<Request> activeReqs= new ArrayList<Request>();
	
	public static void main(String[] args) {

		if(args.length<2)
		{
			System.out.println("Usage: java Server <address> <port>");
			//										0         1
			return;
		}
				
		int port = Integer.parseInt(args[1]);
		
		try {
			//server =  HttpServer.create(new InetSocketAddress(InetAddress.getByName(args[0]),port), 0);
			server =  HttpServer.create(new InetSocketAddress("localhost",8000), 0);
		} catch (IOException e) {
			System.out.println("@Server:error creating server: "+e);
			e.printStackTrace();
		}

		/* Create database - automatically populated */
		db = new Database();
		
		server.createContext("/login", new LoginHandler());
		server.createContext("/signup", new SignupHandler());
		server.createContext("/friend", new FriendHandler());
		server.createContext("/channel", new ChannelHandler());
		server.setExecutor(null);
        server.start();
	}
	
	public static class SignupHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange request) {
			Runnable thread = new SignupThread(request);
			new Thread(thread).start();
		}
	}	
	public static class LoginHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange request) {
			Runnable thread = new LoginThread(request);
			new Thread(thread).start();
		}	
	}
	public static class FriendHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange request) {
			Runnable thread = new FriendThread(request);
			new Thread(thread).start();
		}
	}
	public static class ChannelHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange request) throws IOException {
			Runnable thread = new ChannelThread(request);
			new Thread(thread).start();
		}
		
	}
	
	private static class SignupThread implements Runnable {
		
		private HttpExchange request;
		
		SignupThread(HttpExchange request)
		{
			this.request = request;
		}

		@Override
		public void run() {
			System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":request received");
			String method = request.getRequestMethod();
			System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+" Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+" Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			if(method.equals("PUT"))
			{
				int indE = query.indexOf("email=");
				if(indE == -1)
				{
					System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":bad request received - can't find \"email=\"");
					response="error - no valid email query";
					code=400;
				}
								
				int indP = query.indexOf("pass=");
				if(indP == -1)
				{
					System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":bad request received - can't find \"pass=\"");
					response="error - no valid pass query";
					code=400;
				}
								
				if(response.equals("true"))
				{
					String email=query.substring(indE+"email=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					int passI=Integer.parseInt(pass);

					if(!findUser(email))
					{
						db.addUser(email, new User(email,passI));
						System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":entry added");
					}
					else
					{
						System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":email already in use");
						response="false";
						//code=403;
					}
				}
			}
			else
			{
				System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":bad request received - invalid method (\""+method+"\")");
				response="error - invalid method";
				code=400;
			}
			
			try {
				request.sendResponseHeaders(code, response.length());				
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":response sent \""+response+"\"");
				os.close();
				
			} catch (IOException e) {
				System.out.println("@Server/signup/#+"+Thread.currentThread().getId()+":error sending response: " + e);
				e.printStackTrace();
			}
		}
	}
	private static class LoginThread implements Runnable {
		
		private HttpExchange request;
		
		LoginThread(HttpExchange request)
		{
			this.request = request;
		}
		
		@Override
		public void run() {

			System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":request received");
			String method = request.getRequestMethod();
			System.out.println("@Server/login/#+"+Thread.currentThread().getId()+" Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("@Server/login/#+"+Thread.currentThread().getId()+" Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			if(method.equals("GET"))
			{
				int indE = query.indexOf("email=");
				if(indE == -1)
				{
					System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":bad request received - can't find \"email=\"");
					response="error - no valid email query";
					code=400;
				}
				
				int indU = query.indexOf("user=");
				if(indU == -1)
				{
					System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":bad request received - can't find \"user=\"");
					response="error - no valid user query";
					code=400;
				}
				
				int indP = query.indexOf("pass=");
				if(indP == -1)
				{
					System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":bad request received - can't find \"pass=\"");
					response="error - no valid pass query";
					code=400;
				}
				
				if(response.equals("true"))
				{
					String email=query.substring(indE+"email=".length(),indU-2);
					String user=query.substring(indU+"user=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					int passI=Integer.parseInt(pass);
					

					if(!findUser(email) || db.getUsers().get(email).getPassword()!=passI)
					{
						System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":no match was found for that email-pass pair");
						response="false";
						//code=404;
					}
					else
					{
						if(!user.equals("")) //if user doesn't put any username keeps the last one
							db.getUsers().get(email).setUsername(user);
						else
							response+=" <username>"+db.getUsers().get(email).getUsername()+"</username>";

						System.out.println("@Server/login/#+"+Thread.currentThread().getId()+": email-pass pair matched");
					}
				}
				
			}
			else
			{
				System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":bad request received - invalid method (\""+method+"\")");
				response="error - invalid method";
				code=400;
			}
			
			try {
				request.sendResponseHeaders(code, response.length());
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":response sent \"" + response + "\"");
				os.close();
			} catch (IOException e) {
				System.out.println("@Server/login/#+"+Thread.currentThread().getId()+":error sending response: "+e);
				e.printStackTrace();
			}
		}
		
	}	
	private static class FriendThread implements Runnable {
		
		private HttpExchange request;
		
		FriendThread(HttpExchange request)
		{
			this.request = request;
		}

		@Override
		public void run() {
			System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":request received");
			String method = request.getRequestMethod();
			System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+" Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+" Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			
			int indT = query.indexOf("type=");
			if(indT == -1)
			{
				System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - can't find \"type=\"");
				response="error - no valid type query";
				code=400;
			}
			
			int indE = query.indexOf("email=");
			if(indE == -1)
			{
				System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - can't find \"email=\"");
				response="error - no valid email query";
				code=400;
			}
			
			String type=query.substring(indT+"type=".length(), indE - 2);
			String email=query.substring(indE+"email=".length());
						
			if(response.equals("true"))
			{					
				if(method.equals("PUT") || method.equals("DELETE"))
				{					
					int indF = query.indexOf("friend=");
					if (indF == -1)
					{
						System.out
								.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - can't find \"friend=\"");
						response = "error - no valid friend query";
						code = 400;
					}

					email = query.substring(indE + "email=".length(), indF - 1);
					String friend = query.substring(indF + "friend=".length());

					if(!findUser(email))
					{
						System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - email not registered \""+email+"\"");
						response="error - email not registered \""+email+"\"";
						code=400;
					}
					
					if(!findUser(friend))
					{
						System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - friend not registered \""+friend+"\"");
						response="error - friend not registered \""+friend+"\"";
						code=400;
					}
					
					if (type.equals("request") && method.equals("PUT"))
					{
						//ANSWER TO friend's CHECK REQUEST
						int ind = findActRequest("check", friend, "");
						if(ind>=0)
						{
							String response2="<request>\n";
							response2+="<email>"+email+"</email>\n";
							response2+="</request>\n";
							
							try {
								HttpExchange request2 = activeReqs.get(ind).getRequest();
								request2.sendResponseHeaders(200, response2.length());				
								OutputStream os2 = request2.getResponseBody();
								os2.write(response2.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+friend+" \""+response2+"\"");
								os2.close();
																
								db.getUsers().get(friend).addFriendRequest(email);
								
							} catch (IOException e) {
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response to "+friend);
								e.printStackTrace();
							}
						}
						else
						{
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":can't find friend's check request (probably offline)");
							response="error - friend offline or non active checkFriend request";					
						}
						
						try {
							request.sendResponseHeaders(200, response.length());
							OutputStream os = request.getResponseBody();
							os.write(response.getBytes());
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+email+" \""+response+"\"");
							os.close();
						} catch (IOException e) {
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response to "+email);
							e.printStackTrace();
						}
						
					}
					else if (type.equals("accept") && method.equals("PUT"))
					{
	
						//ANSWER TO friend's CHECK REQUEST
						int ind = findActRequest("check", friend, "");
						if(ind>=0)
						{
							String response2="<accept>\n";
							response2+="<email>"+friend+"</email>\n";
							response2+="</accept>\n";
							
							try {
								if(findUser(email) && findUser(friend))
								{
									db.getUsers().get(email).addFriend(friend);
									db.getUsers().get(email).remFriendRequest(friend);
									db.getUsers().get(friend).addFriend(email);
								}								
								HttpExchange request2 = activeReqs.get(ind).getRequest();
								request2.sendResponseHeaders(200, response2.length());				
								OutputStream os2 = request2.getResponseBody();
								os2.write(response2.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+friend+" \""+response2+"\"");
								os2.close();
								activeReqs.remove(ind);
								
								request.sendResponseHeaders(200, response.length());				
								OutputStream os = request.getResponseBody();
								os.write(response.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+email+" \""+response+"\"");
								os.close();
								
								
								
							} catch (IOException e) {
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response: " + e);
								e.printStackTrace();
							}
						}
						else
						{
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":can't find friend's check request (probably offline)");
						}
						
					}
					else if (type.equals("refuse") && method.equals("PUT"))
					{
						int ind = findActRequest("check", friend, "");
						if(ind>=0)
						{
							String response2="<refuse>\n";
							response2+="<email>"+friend+"</email>\n";
							response2+="</refuse>\n";
							
							if(findUser(email) && findUser(friend)) 
							{
								db.getUsers().get(email).remFriendRequest(friend);
							}
							
							HttpExchange request2 = activeReqs.get(ind).getRequest();
							try {
								request2.sendResponseHeaders(200, response2.length());
								OutputStream os2 = request2.getResponseBody();
								os2.write(response2.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+friend+" \""+response2+"\"");
								os2.close();
								activeReqs.remove(ind);
								
								request.sendResponseHeaders(200, response.length());				
								OutputStream os = request.getResponseBody();
								os.write(response.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+email+" \""+response+"\"");
								os.close();
							} catch (IOException e) {
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response: " + e);
								e.printStackTrace();
							}				
						}
						else
						{
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":can't find friend's check request (probably offline)");
						}
					}
					else if(type.equals("remove") && method.equals("DELETE"))
					{
						//ANSWER TO friend's CHECK REQUEST
						int ind = findActRequest("check", friend, "");
						if(ind>=0)
						{
							String response2="<remove>\n";
							response2+="<email>"+friend+"</email>\n";
							response2+="</remove>\n";
							
							try {
								if(findUser(email) && findUser(friend))
								{
									db.getUsers().get(email).remFriend(friend);
									db.getUsers().get(friend).remFriend(email);
								}								
								HttpExchange request2 = activeReqs.get(ind).getRequest();
								request2.sendResponseHeaders(200, response2.length());				
								OutputStream os2 = request2.getResponseBody();
								os2.write(response2.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+friend+" \""+response2+"\"");
								os2.close();
								activeReqs.remove(ind);
								
								request.sendResponseHeaders(200, response.length());				
								OutputStream os = request.getResponseBody();
								os.write(response.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+email+" \""+response+"\"");
								os.close();
																
							} catch (IOException e) {
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response: " + e);
								e.printStackTrace();
							}
						}
						else
						{
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":can't find friend's check request (probably offline)");
						}
					}
					else
					{
						System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - invalid type");
						response = "error - no valid type query";
						code = 400;
					}
				}
				else if(method.equals("GET"))
				{				
					if(type.startsWith("check"))//TODO use equals instead of startsWith
					{
						activeReqs.add(new Request("check",email,request));
					}
					else if(type.equals("ask"))
					{
						if(!findUser(email))
						{
							System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - email not registered \""+email+"\"");
							response="error - email not registered \""+email+"\"";
							code=400;
						}
						else
						{
							response+="\n";
							ArrayList<String> friends = db.getUsers().get(email).getFriends();
							for(int i=0;i<friends.size();i++)
							{
								response+="<email>"+friends.get(i)+"</email>\n";
							}
							
							try {
								request.sendResponseHeaders(200, response.length());
								OutputStream os = request.getResponseBody();
								os.write(response.getBytes());
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent to "+email+" \""+response+"\"");
								os.close();
							} catch (IOException e) {
								System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response: " + e);
								e.printStackTrace();
							}				

							
						}
					}
				}
				else
				{
					System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":bad request received - invalid method (\""+method+"\")");
					response="error - invalid method";
					code=400;
				}
			}
			else
			{
				try {
					request.sendResponseHeaders(code, response.length());				
					OutputStream os = request.getResponseBody();
					os.write(response.getBytes());
					System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":response sent \""+response+"\"");
					os.close();
					
				} catch (IOException e) {
					System.out.println("@Server/friend/#+"+Thread.currentThread().getId()+":error sending response: " + e);
					e.printStackTrace();
				}
			}
		}
	}
	private static class ChannelThread implements Runnable {

		private HttpExchange request;
		
		ChannelThread(HttpExchange request)
		{
			this.request = request;
		}	
		
		@Override
		public void run() {
					
			System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":request received");
			String method = request.getRequestMethod();
			System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+" Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+" Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			
			int indT = query.indexOf("type=");
			if(indT == -1)
			{
				System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - can't find \"type=\"");
				response="error - no valid type query";
				code=400;
			}
			
			int indE = query.indexOf("email=");
			if(indE == -1)
			{
				System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - can't find \"email=\"");
				response="error - no valid email query";
				code=400;
			}
			
			String type=query.substring(indT+"type=".length(), indE - 1);
			String email=query.substring(indE+"email=".length());
			
			if(response.equals("true"))
			{
				if(method.equals("PUT"))
				{
					if(type.equals("create"))
					{
						int indN = query.indexOf("name=");
						if (indN == -1)
						{
							System.out
									.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - can't find \"name=\"");
							response = "error - no valid name query";
							code = 400;
						}

						int indF = query.indexOf("friend=");
						if (indF == -1)
						{
							System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - can't find \"friend=\"");
							response = "error - no valid friend query";
							code = 400;
						}

						email = query.substring(indE + "email=".length(), indN - 1);
						String name = query.substring(indN + "name=".length(), indF -1);
						String friend = query.substring(indF + "friend=".length());
						
						if(!findUser(email))
						{
							System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - email not registered \""+email+"\"");
							response="error - email not registered \""+email+"\"";
							code=400;
						}
						
						if(!findUser(friend))
						{
							System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - friend not registered \""+friend+"\"");
							response="error - friend not registered \""+friend+"\"";
							code=400;
						}
						
						if(response.equals("true"))
						{
							Channel cnl;
							if(name.isEmpty()) cnl = new Channel(db.getChannelIDs());
							else cnl = new Channel(name,db.getChannelIDs());
							cnl.addUser(email);
							cnl.addUser(friend);
							
							db.addChannel(cnl.getId(), cnl);
							
							response+="\n<channelID>"+cnl.getId()+"</channelID>";
						}
						
						
					}
					else if(type.equals("send"))
					{
						int indC = query.indexOf("channelid=");
						if (indC == -1)
						{
							System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - can't find \"channelid=\"");
							response = "error - no valid name query";
							code = 400;
						}
						else
						{
							email = query.substring(indE + "email=".length(), indC - 1);
							String channelid = query.substring(indC + "channelid=".length());
							
							if(!findUser(email))
							{
								System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - email not registered \""+email+"\"");
								response="error - email not registered \""+email+"\"";
								code=400;
							}
							else
							{
								String body = InStreamToString(request.getRequestBody());
								System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+" Body:\""+body+"\"");
								db.getChannels().get(channelid).addMessage(new Message(body,email));
							}
							
						}
						
					}
					else
					{
						System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - invalid type");
						response = "error - no valid type query";
						code = 400;
					}
				}
				else
				{
					System.out.println("@Server/channel/#+"+Thread.currentThread().getId()+":bad request received - invalid method (\""+method+"\")");
					response="error - invalid method";
					code=400;
				}
			}
			
			try {
				request.sendResponseHeaders(code, response.length());
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server/friend/#+"+ Thread.currentThread().getId() + ":response sent \""+ response + "\"");
				os.close();

			} catch (IOException e) {
				System.out.println("@Server/friend/#+"+ Thread.currentThread().getId()+ ":error sending response: " + e);
				e.printStackTrace();
			}
			
			ArrayList<Channel> arr = new ArrayList<Channel>(db.getChannels().values());
			
			System.out.println("Channels["+arr.size()+"]");
			System.out.println("\tname="+arr.get(0).getName());
			System.out.println("\tusers["+arr.get(0).getUsers().size()+"]");
			System.out.println("\t"+arr.get(0).getUsers().get(0));
			System.out.println("\t"+arr.get(0).getUsers().get(1));
		}
		
	}
	
	public static boolean findUser(String email)
	{
		return db.getUsers().containsKey(email);
	}
	
	public static int findActRequest(String type, String sender, String receiver) {
		
		for(int i=0;i<activeReqs.size();i++)
		{
			if(activeReqs.get(i).getType().equals(type)
					&& activeReqs.get(i).getSender().equals(sender)
					&& activeReqs.get(i).getReceiver().equals(receiver)
					) return i;
		}
		return -1;
	}
		
	public static String InStreamToString(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
}
