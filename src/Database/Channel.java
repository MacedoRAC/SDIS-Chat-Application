package Database;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by André on 01/06/2015.
 */
public class Channel {

    private String id;
    private String name;
    private String password;
    private ArrayList<User> users;
    private ArrayList<Message> msgs;
    private Boolean hasPassword;


    void Channel(){
        this.id = generateRandomId();
        this.name = "";
        this.password = "";
        this.users = new ArrayList<>();
        this.msgs = new ArrayList<>();
        this.hasPassword = false;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
