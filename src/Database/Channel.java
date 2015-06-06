package Database;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by André on 01/06/2015.
 */
public class Channel {

    private String id;
    private String name;
    private ArrayList<String> users;
    private ArrayList<Message> msgs;

   public Channel(ArrayList<String> ids){
	   String id = "";
	   do{
		   id = generateRandomId();
	   }while(ids.contains(id));
	   this.id = id;
	   this.name = "";
	   this.users = new ArrayList<>();
	   this.msgs = new ArrayList<>();
    }
   
   public Channel(String name, ArrayList<String> ids){
	   String id = "";
	   do{
		   id = generateRandomId();
	   }while(ids.contains(id));
	   this.id = id;
       setName(name);
       this.users = new ArrayList<>();
       this.msgs = new ArrayList<>();
   }

    public String getId() {
        return id;
    }
    public String getName()
    {
    	return name;
    }
    public ArrayList<String> getUsers() {
    	return users;
    }
    public ArrayList<Message> getMessages() {
    	return msgs;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name)
    {
    	this.name=name;
    }
    public void addUser(String email){

        if(!users.contains(email)) {
            users.add(email);
        }else{
            System.out.println("User " + email + " is already in this channel");
        }
    }
    public void removeUser(User new_user){

        if(users.contains(new_user)) {
            users.remove(new_user);
        }else{
            System.out.println("User " + new_user.getUsername() + " isn't in this channel");
        }
    }
    public void addMessage(Message msg)
    {
    	msgs.add(msg);
    }
    
    
    
    private String generateRandomId(){

        char[] chars = "abcdefghijklmnopqrstuvwxyz12345967890ABCDEFGHIJKMLMNOPQRSTWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 32; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
    
}
