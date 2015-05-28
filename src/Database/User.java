package Database;

public class User {
	
	private String email="";
	private String username="";
	private String password="";
	
	public User(String email, String username, String password)
	{
		setEmail(email);
		setUsername(username);
		setPassword(password);
	}
	public User(String email, String password)
	{
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
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
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
