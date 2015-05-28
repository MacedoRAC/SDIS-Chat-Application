package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import Database.Database;
import Database.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {

	//private static ArrayList<User> users = new ArrayList<User>();
	private static Database users;
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
		
		try {
			server =  HttpServer.create(new InetSocketAddress(InetAddress.getByName(args[0]),port), 0);		
		} catch (IOException e) {
			System.out.println("@Server:error creating server: "+e);
			e.printStackTrace();
		}

		/* Create database - automatically populated */
		users = new Database();
		
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
					

					if(!findUser(email))
					{
						System.out.println("@Server/login:no match was found for that email-pass pair");
						response="false";
						//code=404;
					}
					else
					{
						if(!user.equals("")) //if user doesn't put any username keeps the last one
							users.getDb().get(email).setUsername(user);

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

					if(!findUser(email))
					{
						users.add(email, new User(email,pass));
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
		}
	}
		
	private static boolean findUser(String email)
	{
		if(users.getDb().containsKey(email))
			return true;
		else
			return false;
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
}
