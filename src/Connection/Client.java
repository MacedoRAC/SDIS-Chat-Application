package Connection;

import Database.User;
import GUI.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;


public class Client {

	private static String urlS;
	//private static OutputStreamWriter out;
	//private static BufferedReader in;
	private Hashtable<Long,String> threads = new Hashtable<Long,String>();
	
	private static User user = new User();
		
	private static Main gui;
	
	public static void main(String[] args) {
		
		Client client = new Client();
		
		client.buildURL("localhost",8000);
		
		gui = new Main();
		String[]argsFX = new String[0];
		gui.run(argsFX);
		
		/*client.login("homer@simpsons.us", "homer", "1234");
		client.askFriends();
		
		Scanner scanner = new Scanner(System.in);
		scanner.next();
		
		System.out.println("friends["+user.getFriends().size()+"]:");
		for(int i=0;i<user.getFriends().size();i++)
		{
			System.out.println("\t"+user.getFriends().get(i));
		}*/
		
	}
	
	private void buildURL(String address, int port)
	{
		urlS = "http://"+address+":"+port+"/";
	}
	
	public static String signup(String email, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"signup?email="+email+"&?pass="+password.hashCode());
		} catch (MalformedURLException e1) {
			System.out.println("@Client/signup:error initializing url");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/signup:url initialized (\""+url+"\")");
		
		//CONNECT AND SEND REQUEST
		HttpURLConnection url_connect = null;
		try {
			url_connect = (HttpURLConnection) url.openConnection();
			
			url_connect.setRequestMethod("PUT");
			url_connect.setDoOutput(true);
			url_connect.setReadTimeout(60*1000);
			url_connect.connect();
						
			//out = new OutputStreamWriter(url_connect.getOutputStream());
			
		} catch (IOException e1) {
			System.out.println("@Client/signup:error connecting");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/signup:connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client/signup:request sent");
		
		//READ RESPONSE
		String answer = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url_connect.getInputStream()));
			answer = BufReaderToString(in);
		} catch (IOException e) {
			System.out.println("@Client/signup:error reading response");
			e.printStackTrace();
			return "client error";
		}

		System.out.println("@Client/signup:response received\nDATA:" + answer.trim());

		return answer;
	}
	public static String login(String email, String username, String password)
	{
		//BUILD URL
		URL url = null;
		try {
			url = new URL(urlS+"login?email="+email+"&?user="+username+"&?pass="+password.hashCode());
		} catch (MalformedURLException e1) {
			System.out.println("@Client/login:error initializing url");
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
			System.out.println("@Client/login:error connecting");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/login:connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client/login:request sent");
		
		//READ RESPONSE
		String answer=null;
		try {
			BufferedReader in  = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
			answer = BufReaderToString(in);
		} catch (IOException e) {
			System.out.println("@Client/login:error reading response");
			e.printStackTrace();
			return "client error";
		}
		
		System.out.println("@Client/login:response received\nDATA:"+answer.trim());
		if(answer.startsWith("true"))
		{
			user.setEmail(email);
			if(username.equals(""))
			{
				int indUB = answer.indexOf("<username>");
				int indUE = answer.indexOf("</username>");
				if(indUB==-1 || indUE==-1)
				{
					System.out.println("@Client/login:bad response received - can't find \"<username>\" or \"</username>\" tags in body");
					return answer;
				}
				else
				{
					username = answer.substring(indUB+"<username>".length(),indUE);
					
				}
			}
			user.setUsername(username);
		}
		
		return answer;			
		
	}	
	public long checkFriends()
	{
		Runnable r = new CheckFriendsThread();
		Thread t = new Thread(r);
		t.setName("check_friends");
		t.start();
		
		return t.getId();
	}	
	public long sendFriend(String email)
	{
		Runnable r = new SendFriendThread(email);
		Thread t = new Thread(r);
		t.setName("sendFriend");
		t.start();
		
		return t.getId();
	}
	public long acceptFriend(String email)
	{
		Runnable r = new AcceptFriendThread(email);
		Thread t = new Thread(r);
		t.setName("acceptFriend");
		t.start();
		
		return t.getId();
	}
	public long removeFriend(String email)
	{
		Runnable r = new RemoveFriendThread(email);
		Thread t = new Thread(r);
		t.setName("removeFriend");
		t.start();
		
		return t.getId();
	}
	public long refuseFriend(String email) {
		Runnable r = new RefuseFriendThread(email);
		Thread t = new Thread(r);
		t.setName("refuseFriend");
		t.start();
		
		return t.getId();
	}
	public static String askFriends() {
		// BUILD URL
		URL url = null;
		try {
			url = new URL(urlS + "friend?type=ask&?email=" + user.getEmail());
		} catch (MalformedURLException e1) {
			System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":error initializing url");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":url initialized (\"" + url + "\")");
		
		//CONNECT AND SEND REQUEST
		HttpURLConnection url_connect = null;
		try {
			url_connect = (HttpURLConnection) url.openConnection();
			
			url_connect.setRequestMethod("GET");
			url_connect.setReadTimeout(60*2*1000);
			url_connect.connect();
									
		} catch (IOException e1) {
			System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":error connecting");
			e1.printStackTrace();
			return "client error";
		}
		System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
		System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":request sent");
		
		//READ RESPONSE
		String answer=null;
		try {
			BufferedReader in  = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
			answer = BufReaderToString(in);
		} catch (IOException e) {
			System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":error reading response");
			e.printStackTrace();
			return "client error";
		}
		
		System.out.println("@Client/ask_friends/#+"+Thread.currentThread().getId()+":response received\nDATA:"+answer.trim());
		
		int indEB = answer.indexOf("<email>");
		int indEE = answer.indexOf("</email>");
		
		while(indEB!=-1 && indEE!=-1)
		{
			String email = answer.substring(indEB+"<email>".length(),indEE);
			user.addFriend(email);
			
			indEB = answer.indexOf("<email>", indEB+1);
			indEE = answer.indexOf("</email>", indEE+1);
		}
		
		return "true";
	}
	
	private class CheckFriendsThread implements Runnable {

		@Override
		public void run() {
			
			threads.put(Thread.currentThread().getId(), "");
			
			// BUILD URL
			URL url = null;
			try {
				url = new URL(urlS + "friend?type=check&?email=" + user.getEmail());
			} catch (MalformedURLException e1) {
				System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":error initializing url");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/check_friend:url initialized (\"" + url + "\")");		
			
			//CONNECT AND SEND REQUEST
			HttpURLConnection url_connect = null;
			try {
				url_connect = (HttpURLConnection) url.openConnection();
				
				url_connect.setRequestMethod("GET");
				url_connect.setReadTimeout(0);
				url_connect.connect();
										
			} catch (IOException e1) {
				System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":error connecting");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
			System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":request sent");
			
			//READ RESPONSE
			String answer=null;
			try {
				BufferedReader in  = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
				answer = BufReaderToString(in);
			} catch (IOException e) {
				System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":error reading response");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			
			System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":response received\nDATA:"+answer.trim());
			
			int indEB = answer.indexOf("<email>");
			int indEE = answer.indexOf("</email>");
			if(indEB==-1 || indEE==-1)
			{
				System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":bad response received - can't find \"<email>\" or \"</email>\" tags in body");
				threads.put(Thread.currentThread().getId(), answer);
				return;
			}
			
			String email = answer.substring(indEB+"<email>".length(),indEE);
			
			if(answer.startsWith("<request>") && answer.endsWith("</request>"))
			{
				user.addFriendRequest(email);
				threads.put(Thread.currentThread().getId(), "request received: "+email);
			}
			else if(answer.startsWith("<accept>") && answer.endsWith("</accept>"))
			{
				user.addFriend(email);
				user.remFriendRequest(email);
				threads.put(Thread.currentThread().getId(), "friend added: "+email);
			}
			else if(answer.startsWith("<remove>") && answer.endsWith("</remove>"))
			{
				user.remFriend(email);
				threads.put(Thread.currentThread().getId(), "friend removed: "+email);
			}
			else if(answer.startsWith("<refuse>") && answer.endsWith("</refuse>"))
			{
				user.remFriend(email);
				threads.put(Thread.currentThread().getId(), "friend refused: "+email);
			}
			else
			{
				System.out.println("@Client/check_friend/#+"+Thread.currentThread().getId()+":bad response received - can't find \"<request>\", \"</request>\" or \"<accept>\", \"</accept>\" tags in body");
				threads.put(Thread.currentThread().getId(), "server error");
				return;
			}
			checkFriends();
		}	
	}
	private class SendFriendThread implements Runnable {
		
		private String email;
		
		public SendFriendThread(String email)
		{
			this.email=email;
		}

		@Override
		public void run() {
			
			threads.put(Thread.currentThread().getId(), "");
			
			if(user.getFriends().contains(email))
			{
				threads.put(Thread.currentThread().getId(), "client error - friend already exists");
				return;
			}
			
			// BUILD URL
			URL url = null;
			try {
				url = new URL(urlS + "friend?type=request&?email=" + user.getEmail() + "&friend="+email);
			} catch (MalformedURLException e1) {
				System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":error initializing url");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":url initialized (\"" + url + "\")");
			
			//CONNECT AND SEND REQUEST
			HttpURLConnection url_connect = null;
			try {
				url_connect = (HttpURLConnection) url.openConnection();
				
				url_connect.setRequestMethod("PUT");
				url_connect.setReadTimeout(60*1000);
				url_connect.connect();
										
			} catch (IOException e1) {
				System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":error connecting");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
			System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":request sent");
				
			//READ RESPONSE
			String answer = null;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url_connect.getInputStream()));
				answer = BufReaderToString(in);
			} catch (IOException e) {
				System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":error reading response");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}

			System.out.println("@Client/send_friend/#+"+Thread.currentThread().getId()+":response received\nDATA:"
					+ answer.trim());
			
			threads.put(Thread.currentThread().getId(), answer);
		}
	}
	private class AcceptFriendThread implements Runnable {
		
		private String email;
		
		public AcceptFriendThread(String email)
		{
			this.email=email;
		}

		
		@Override
		public void run() {
			
			threads.put(Thread.currentThread().getId(), "");
			
			if(!user.getFriendRequests().contains(email))
			{
				System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":error - can't find this friend request");
				threads.put(Thread.currentThread().getId(), "client error - no friend request");
				return;
			}
			
			// BUILD URL
			URL url = null;
			try {
				url = new URL(urlS + "friend?type=accept&?email=" + user.getEmail() + "&friend="+email);
			} catch (MalformedURLException e1) {
				System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":error initializing url");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":url initialized (\"" + url + "\")");
			
			//CONNECT AND SEND REQUEST
			HttpURLConnection url_connect = null;
			try {
				url_connect = (HttpURLConnection) url.openConnection();
				
				url_connect.setRequestMethod("PUT");
				url_connect.setReadTimeout(60*1000);
				url_connect.connect();
										
			} catch (IOException e1) {
				System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":error connecting");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
			System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":request sent");
			
			//READ RESPONSE
			String answer = null;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url_connect.getInputStream()));
				answer = BufReaderToString(in);
			} catch (IOException e) {
				System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":error reading response");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}

			System.out.println("@Client/accept_friend/#+"+Thread.currentThread().getId()+":response received\nDATA:"
					+ answer.trim());
			
			user.remFriendRequest(email);
			user.addFriend(email);
			
			threads.put(Thread.currentThread().getId(), answer);
		}
	}
	private class RemoveFriendThread implements Runnable {
		
		private String email;
		
		public RemoveFriendThread(String email)
		{
			this.email=email;
		}

		@Override
		public void run() {
			
			threads.put(Thread.currentThread().getId(), "");
			
			if(!user.getFriends().contains(email))
			{
				System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":error - can't find this friend request");
				threads.put(Thread.currentThread().getId(), "client error - no friend request");
				return;
			}
			
			// BUILD URL
			URL url = null;
			try {
				url = new URL(urlS + "friend?type=remove&?email=" + user.getEmail() + "&friend="+email);
			} catch (MalformedURLException e1) {
				System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":error initializing url");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":url initialized (\"" + url + "\")");
			
			//CONNECT AND SEND REQUEST
			HttpURLConnection url_connect = null;
			try {
				url_connect = (HttpURLConnection) url.openConnection();
				
				url_connect.setRequestMethod("DELETE");
				url_connect.setReadTimeout(60*1000);
				url_connect.connect();
										
			} catch (IOException e1) {
				System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":error connecting");
				e1.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
			System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":request sent");
			
			//READ RESPONSE
			String answer = null;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url_connect.getInputStream()));
				answer = BufReaderToString(in);
			} catch (IOException e) {
				System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":error reading response");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}

			System.out.println("@Client/remove_friend/#+"+Thread.currentThread().getId()+":response received\nDATA:"
					+ answer.trim());
			
			if(answer.equals("true")) user.remFriend(email);
			
			threads.put(Thread.currentThread().getId(), answer);
			
		}		
	}
	private class RefuseFriendThread implements Runnable {
		
		private String email;
		
		public RefuseFriendThread(String email) {
			this.email=email;
		}
		
		
		@Override
 		public void run() {
			
			threads.put(Thread.currentThread().getId(), "");
			
			if(!user.getFriendRequests().contains(email)) {
				//ERRO
				System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":error - can't find this friend request");
				threads.put(Thread.currentThread().getId(), "client error - no friend request found");
				return;
			}
			
			//URL
			URL url = null;
			try {
				url = new URL(urlS + "friend?type=refuse&?email=" + user.getEmail() + "&friend="+email);
			} catch (MalformedURLException e) {
				System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":error initializing url");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":url initialized (\"" + url + "\")");
			
			//CONNECTION 
			HttpURLConnection url_connect = null;
			
			try {
				url_connect = (HttpURLConnection) url.openConnection();
				
				url_connect.setRequestMethod("PUT");
				url_connect.setReadTimeout(60*1000);
				url_connect.connect();
			} catch (IOException e) {
				System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":error connecting");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			
			System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":connected with request method \""+url_connect.getRequestMethod()+"\"");
			System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":request sent");
			
			//RESPONSE
			String answer=null;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url_connect.getInputStream()));
				answer = BufReaderToString(in);
			} catch (IOException e) {
				System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":error reading response");
				e.printStackTrace();
				threads.put(Thread.currentThread().getId(), "client error");
				return;
			}
			
			System.out.println("@Client/refuse_friend/#+"+Thread.currentThread().getId()+":response received\nDATA:"
					+ answer.trim());
			
			user.remFriendRequest(email);
			
			threads.put(Thread.currentThread().getId(), answer);
		}
		
	}

	private class CreateChannelThread implements Runnable {
		
		private String name;
		
		public CreateChannelThread(String name)
		{
			this.name=name;
		}

		@Override
		public void run() {
						
		}
		
	}
	
 	public static String BufReaderToString(BufferedReader in) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		 
	      String line = null;
	      while ((line = in.readLine()) != null)
	      {
	        stringBuilder.append(line + "\n");
	      }
	      stringBuilder.deleteCharAt(stringBuilder.length()-1);
	      return stringBuilder.toString();
	}

	public Hashtable<Long, String> getThreads() {
		return threads;
	}

	public static User getUser() {
		return user;
	}
}
