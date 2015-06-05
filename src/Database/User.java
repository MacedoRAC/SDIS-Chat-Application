package Database;

import java.util.ArrayList;

public class User {
	
	private String email="";
	private String username="";
	private int password;
	private ArrayList<String> friends=new ArrayList<String>();
	private ArrayList<String> friendRequests = new ArrayList<String>();
	
	public User(){}
	public User(String email, String username, int password) {
		setEmail(email);
		setUsername(username);
		setPassword(password);
	}
	public User(String email, int password) {
		setEmail(email);
		setUsername(email);
		setPassword(password);
	}
	
	
	public String getEmail() {
		return email;
	}
	public String getUsername() {
		return username;
	}
	public int getPassword() {
		return password;
	}
	public ArrayList<String> getFriends() {
		return friends;
	}
	public ArrayList<String> getFriendRequests() {
		return friendRequests;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	public void setFriends(ArrayList<String> contacts) {
		this.friends = contacts;
	}
	public boolean addFriend(String email)
	{
		if(friends.contains(email)) return false;
		friends.add(email);
		return true;
	}
	public boolean remFriend(String email)
	{
		return friends.remove(email);
	}
	public void setFriendRequests(ArrayList<String> friendRequests) {
		this.friendRequests = friendRequests;
	}
	public boolean addFriendRequest(String email)
	{
		if(friendRequests.contains(email)) return false;
		friendRequests.add(email);
		return true;
	}
	public boolean remFriendRequest(String email)
	{
		return friendRequests.remove(email);
	}
	
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User rhs = (User) obj;
        return this.getEmail().equals(rhs.getEmail());
    }


	
}
