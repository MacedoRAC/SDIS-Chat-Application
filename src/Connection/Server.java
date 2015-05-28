package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import Database.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {

	private static ArrayList<User> users = new ArrayList<User>();
	private static HttpServer server;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length<2)
		{
			System.out.println("Usage: java Server <address> <port>");
			//										0         1
			return;
		}
				
		int port = Integer.parseInt(args[1]);
		
		populateDatabase();
		
		try {
			server =  HttpServer.create(new InetSocketAddress(InetAddress.getByName(args[0]),port), 0);		
		} catch (IOException e) {
			System.out.println("@Server:error creating server: "+e);
			e.printStackTrace();
		}
		
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
				int indE = query.indexOf("email=");
				if(indE == -1)
				{
					System.out.println("@Server/login:bad request received - can't find \"email=\"");
					response="error - no valid email query";
					code=400;
				}
				
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
					String email=query.substring(indE+"email=".length(),indU-2);
					String user=query.substring(indU+"user=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					
					int ind=findUser(email);
					if(ind<0 || (ind>0 && !users.get(ind).getPassword().equals(pass)))
					{
						System.out.println("@Server/login:no match was found for that email-pass pair");
						response="false";
						//code=404;
					}
					else
					{
						users.get(ind).setUsername(user);
						System.out.println("@Server/login: email-pass pair matched");
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
				System.out.println("@Server/login:response sent \"" + response + "\"");
				os.close();
			} catch (IOException e) {
				System.out.println("@Server/login:error sending response: "+e);
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
				int indE = query.indexOf("email=");
				if(indE == -1)
				{
					System.out.println("@Server/signup:bad request received - can't find \"email=\"");
					response="error - no valid email query";
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
					String email=query.substring(indE+"email=".length(),indP-2);
					String pass=query.substring(indP+"pass=".length());
					
					int ind=findUser(email);
					if(ind<0)
					{
						users.add(new User(email,pass));
						System.out.println("@Server/signup:entry added");
					}
					else
					{
						System.out.println("@Server/signup:email already in use");
						response="false";
						//code=403;
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
				System.out.println("@Server:error sending response: " + e);
				e.printStackTrace();
			}

			printDatabase();
		}

		private void printDatabase() {

			for(int i = 0; i<users.size(); i++){
				System.out.println(i + " " + users.get(i).getEmail() + " " + users.get(i).getUsername() + " " + users.get(i).getPassword());
			}
		}

	}
		
	private static int findUser(String email)
	{
		for(int i=0;i<users.size();i++)
		{
			if(users.get(i).getEmail().equals(email)) return i;
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

		users.add(new User("burn@simpsons.us","Burn", "1234"));
		users.add(new User("homer@simpsons.us","Homer", "1234"));
		users.add(new User("louis@simpsons.us","Louis", "1234"));
		users.add(new User("bart@simpsons.us","Bart", "1234"));
		users.add(new User("lisa@simpsons.us","Lisa", "1234"));
	}

}
