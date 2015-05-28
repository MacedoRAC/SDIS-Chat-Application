package Connection;

import GUI.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Client {

	static String urlS;
	private static OutputStreamWriter out;
	private static BufferedReader in;
	private static Main gui;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		buildURL(args[0],Integer.parseInt(args[1]));
		
		gui = new Main();
		String[]argsFX = new String[0];
		gui.run(argsFX);
		/*
		String ret;
		System.out.println("@Client: trying to signup...");
		ret = signup("srkit@gmail.com","12345");
		System.out.println("@Client: signup returned "+ret);
		if(!ret.equals("true")) return;
		System.out.println("@Client: trying to login with wrong password...");
		ret = login("srkit@gmail.com", "srkit", "ABCD");
		System.out.println("@Client: login returned "+ret);
		if(!ret.equals("false")) return;
		System.out.println("@Client: trying to login with right password...");
		ret = login("srkit@gmail.com", "srkit", "12345");
		System.out.println("@Client: login returned "+ret);
		if(!ret.equals("true")) return;
		System.out.println("@Client: trying to signup again...");
		ret = signup("srkit@gmail.com","ABCD");
		System.out.println("@Client: signup returned "+ret);
		*/
        
	}
	
	private static void buildURL(String address, int port)
	{
		//DEBUGGING ONLY
		address = "localhost";
		port = 8000;
		urlS = "http://"+address+":"+port+"/";
	}
	
	/*
	 * @params:
	 * 		String username
	 * 		String password
	 * @return:
	 * 		"client error" -> caso haja um erro da parte do cliente
	 * 		"true" -> caso o signup tenha sido feito em condições
	 * 		"false" -> caso já exista um utilizador com esse username
	 * 		"error - ..." ->  caso haja um erro no request (vê os vários erros na Server.SignupHandler.handle)
	 */
	public static String signup(String email, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"signup?email="+email+"&?pass="+password);
		} catch (MalformedURLException e1) {
			System.out.println("@Client:error initializing url");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client:url initialized (\""+url+"\")");
		
		//CONNECT AND SEND REQUEST
		HttpURLConnection url_connect = null;
		try {
			url_connect = (HttpURLConnection) url.openConnection();
			
			url_connect.setRequestMethod("PUT");
			url_connect.setDoOutput(true);
			url_connect.setReadTimeout(60*1000);
			url_connect.connect();
						
			out = new OutputStreamWriter(url_connect.getOutputStream());
			
		} catch (IOException e1) {
			System.out.println("@Client:error connecting");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client:connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client:request sent");
		
		//READ RESPONSE
		String answer = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					url_connect.getInputStream()));
			answer = BufReaderToString();
		} catch (IOException e) {
			System.out.println("@Client:error reading response");
			e.printStackTrace();
			return "client error";
		}

		System.out.println("@Client:response received\nDATA:" + answer.trim());

		return answer;
	}

	/*
	 * @params:
	 * 		String username
	 * 		String password
	 * @return:
	 * 		"client error" -> caso haja um erro da parte do cliente
	 * 		"true" -> caso o login tenha sido feito em condições
	 * 		"false" -> caso não exista nenhum match com aquele username e password
	 * 		"error - ..." ->  caso haja um erro no request (vê os vários erros na Server.LoginHandler.handle)
	 */
	public static String login(String email, String username, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"login?email="+email+"&?user="+username+"&?pass="+password);
		} catch (MalformedURLException e1) {
			System.out.println("@Client/login:error initializing url: "+e1);
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/login:url initialized (\""+url+"\")");
		
		//CONNECT AND SEND REQUEST
		HttpURLConnection url_connect = null;
		try {
			url_connect = (HttpURLConnection) url.openConnection();
			
			url_connect.setRequestMethod("GET");
			url_connect.setReadTimeout(60*1000);
			url_connect.connect();
									
		} catch (IOException e1) {
			System.out.println("@Client/login:error connecting: "+e1);
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/login:connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client/login:request sent");
		
		//READ RESPONSE
		String answer=null;
		try {
			in  = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
			answer = BufReaderToString();
		} catch (IOException e) {
			System.out.println("@Client/login:error reading response: "+e);
			e.printStackTrace();
			return "client error";
		}
		
		System.out.println("@Client/login:response received\nDATA:"+answer.trim());
		
		return answer;
	}
	
	public static String BufReaderToString() throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		 
	      String line = null;
	      while ((line = in.readLine()) != null)
	      {
	        stringBuilder.append(line + "\n");
	      }
	      stringBuilder.deleteCharAt(stringBuilder.length()-1);
	      return stringBuilder.toString();
	}
	
}
