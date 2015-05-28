package Connection;

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//DEBUGGIN ONLY - WHOLE MAIN METHOD TO BE REMOVED
		if(args.length<2)
		{
			System.out.println("Usage: java Client <address> <port>");
			//										0         1
			return;
		}

		buildURL(args[0],Integer.parseInt(args[1]));
		String ret;
		System.out.println("@Client: trying to signup...");
		ret = signup("Serkite","12345");
		System.out.println("@Client: signup returned "+ret);
		if(!ret.equals("true")) return;
		System.out.println("@Client: trying to login...");
		ret = login("Serkite","12345");
		System.out.println("@Server: login returned "+ret);
		System.out.println("@Client: trying to signup again...");
		ret = signup("Serkite","ABCD");
		System.out.println("@Client: signup returned "+ret);
        
	}
	
	private static void buildURL(String address, int port)
	{
		urlS = "http://"+address+":"+port+"/";
	}
	
	/*
	 * @params:
	 * 		String username
	 * 		String password
	 * @return:
	 * 		"client error" -> caso haja um erro da parte do cliente
	 * 		"true" -> caso o signup tenha sido feito em condi��es
	 * 		"false" -> caso j� exista um utilizador com esse username
	 * 		"error - ..." ->  caso haja um erro no request (v� os v�rios erros na Server.SignupHandler.handle)
	 */
	public static String signup(String username, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"signup?user="+username+"&?pass="+password);
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
	 * 		"true" -> caso o login tenha sido feito em condi��es
	 * 		"false" -> caso n�o exista nenhum match com aquele username e password
	 * 		"error - ..." ->  caso haja um erro no request (v� os v�rios erros na Server.LoginHandler.handle)
	 */
	public static String login(String username, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"login?user="+username+"&?pass="+password);
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
			
			url_connect.setRequestMethod("GET");
			url_connect.setReadTimeout(60*1000);
			url_connect.connect();
									
		} catch (IOException e1) {
			System.out.println("@Client:error connecting");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client:connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client:request sent");
		
		//READ RESPONSE
		String answer=null;
		try {
			in  = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
			answer = BufReaderToString();
		} catch (IOException e) {
			System.out.print("@Client:error reading response\n");
			e.printStackTrace();
			return "client error";
		}
		
		System.out.print("@Client:response received\nDATA:"+answer.trim()+"\n");
		
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
