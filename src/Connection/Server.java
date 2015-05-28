package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {

	private static ArrayList< Pair<String,String> > entries = new ArrayList< Pair<String,String> >();
	private static HttpServer server;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//DEBUGGIN ONLY - WHOLE MAIN METHOD TO BE REMOVED
		if(args.length<2)
		{
			System.out.println("Usage: java Server <address> <port>");
			//										0         1
			return;
		}
				
		int port = Integer.parseInt(args[1]);
		
		try {
			server =  HttpServer.create(new InetSocketAddress(InetAddress.getByName(args[0]),port), 0);		
		} catch (IOException e) {
			System.out.print("@Server:error creating server");
			e.printStackTrace();
		}
		
		//entries.add( new Pair<String,String>("Serkite","12345"));
		
		server.createContext("/login", new LoginHandler());
		server.createContext("/signup", new SignupHandler());
		server.setExecutor(null);
        server.start();
	}
	
	static class LoginHandler implements HttpHandler {
		public void handle(HttpExchange request) {
			System.out.println("@Server/login:request received");
			String method = request.getRequestMethod();
			System.out.println("Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			if(method.equals("GET"))
			{
				int indU = query.indexOf("user=");
				if(indU == -1)
				{
					System.out.println("@Server/login:bad request received - can't find \"user=\"");
					response="error - no valid user query";
					code=400;
				}
				int indP = query.indexOf("pass=");
				if(indP == -1)
				{
					System.out.println("@Server/login:bad request received - can't find \"pass=\"");
					response="error - no valid pass query";
					code=400;
				}
				
				if(response.equals("true"))
				{
					String user=query.substring(indU+"user=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					
					int ind=findUser(user);
					if(ind<0 || (ind>0 && !entries.get(ind).getSecond().equals(pass)))
					{
						System.out.println("@Server/login:no match was found for that user-pass pair");
						response="false";
						code=404;
					}
					else
					{
						System.out.println("@Server/login: user-pass pair matched");
					}
				}
				
			}
			else
			{
				System.out.println("@Server/login:bad request received - invalid method (\""+method+"\")");
				response="error - invalid method";
				code=400;
			}
			
			try {
				request.sendResponseHeaders(code, response.length());
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server:response sent \"" + response + "\"");
				os.close();
			} catch (IOException e) {
				System.out.println("@Server:error sending response");
				e.printStackTrace();
			}
		}
			
	}
	
	static class SignupHandler implements HttpHandler {
		public void handle(HttpExchange request) {
			System.out.println("@Server/signup:request received");
			String method = request.getRequestMethod();
			System.out.println("Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("Query:\""+query+"\"");
			
			String response="true";
			int code=200;
			if(method.equals("PUT"))
			{
				int indU = query.indexOf("user=");
				if(indU == -1)
				{
					System.out.println("@Server/signup:bad request received - can't find \"user=\"");
					response="error - no valid user query";
					code=400;
				}
				int indP = query.indexOf("pass=");
				if(indP == -1)
				{
					System.out.println("@Server/signup:bad request received - can't find \"pass=\"");
					response="error - no valid pass query";
					code=400;
				}
				
				if(response.equals("true"))
				{
					String user=query.substring(indU+"user=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					
					int ind=findUser(user);
					if(ind<0)
					{
						entries.add(new Pair<String,String>(user,pass));
						System.out.println("@Server/signup:entry added");
					}
					else
					{
						System.out.println("@Server/signup:username already in use");
						response="false";
						code=403;
					}
				}
			}
			else
			{
				System.out.println("@Server/signup:bad request received - invalid method (\""+method+"\")");
				response="error - invalid method";
				code=400;
			}
			
			try {
				request.sendResponseHeaders(code, response.length());				
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server:response sent \""+response+"\"");
				os.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print("@Server:error sending response\n");
			}
		}
	}
	
	/*static class SignupHandler implements HttpHandler {
		public void handle(HttpExchange request) {
			System.out.println("@Server/signup:request received");
			String method = request.getRequestMethod();
			System.out.println("Method:\""+method+"\"");
			String query = request.getRequestURI().getQuery();
			System.out.println("Query:\""+query+"\"");
			String body = InStreamToString(request.getRequestBody());
			System.out.println("Body:\""+body+"\"");
			
			String response="true";
			int code=200;
			if(method.equals("PUT"))
			{
				int indUB = body.indexOf("<username>");
				int indUE = body.indexOf("</username>");
				if(indUB==-1 || indUE==-1)
				{
					System.out.println("@Server/signup:bad request received - can't find \"<username>\" or \"</username>\" tags");
					response="error - no <username> or </username> tags";
					code=400;
				}
				
				int indPB = body.indexOf("<password>");
				int indPE = body.indexOf("</password>");
				if(indPB==-1 || indPE==-1)
				{
					System.out.println("@Serve/signup:bad request received - can't find \"<password>\" or \"</password>\" tags");
					response="error - no <password> or </password> tags";
					code=400;
				}
				
				if(response.equals("true"))
				{
					String user=query.substring(indUB+"<username>".length(),indUE);
					String pass=query.substring(indPB+"<password>".length(),indPE);
					
					int ind=findUser(user);
					if(ind<0)
					{
						entries.add(new Pair<String,String>(user,pass));
						System.out.println("@Server/signup:entry added");
					}
					else
					{
						System.out.println("@Server/signup:username already in use");
						response="false";
						code=403;
					}
				}
			}
			else
			{
				System.out.println("@Server/signup:bad request received - invalid method (\""+method+"\")");
				response="error - invalid method";
				code=400;
			}
			
			try {
				request.sendResponseHeaders(code, response.length());				
				OutputStream os = request.getResponseBody();
				os.write(response.getBytes());
				System.out.println("@Server:response sent \""+response+"\"");
				os.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print("@Server:error sending response\n");
			}
		}
	}*/
	
	private static int findUser(String username)
	{
		for(int i=0;i<entries.size();i++)
		{
			if(entries.get(i).getFirst().equals(username)) return i;
		}
		return -1;
	}
	
	private static String InStreamToString(InputStream is) {
		 
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

	private static void populateDatabase(){

		entries.add(new Pair<>("Burn", "1234"));
		entries.add(new Pair<>("Homer", "1234"));
		entries.add(new Pair<>("Louis", "1234"));
		entries.add(new Pair<>("Bart", "1234"));
		entries.add(new Pair<>("Lisa", "1234"));
	}

}
