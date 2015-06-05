package Database;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by André on 01/06/2015.
 */
public class Channel {

    private String id;
    private String name;
    private ArrayList<User> users;
    private ArrayList<Message> msgs;

   public Channel(){
        this.id = generateRandomId();
        this.name = "";
        this.users = new ArrayList<>();
        this.msgs = new ArrayList<>();
    }
   
   public Channel(String name){
       this.id = generateRandomId();
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
    
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name)
    {
    	this.name=name;
    }
    public void addUser(User new_user){

        if(!users.contains(new_user)) {
            users.add(new_user);
        }else{
            System.out.println("User " + new_user.getUsername() + " is already in this channel");
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
