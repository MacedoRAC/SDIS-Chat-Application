package Database;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by André on 28/05/2015.
 */
public class Database {

    private Hashtable<String, User> users;
    private Hashtable<String, Channel> channels;


    public Database(){
        users = new Hashtable<>();
        channels = new Hashtable<>();

        populateUsers();
    }

    private void clearUsers(){
        users.clear();
    }

    private void populateUsers() {
        User homer = new User("homer@simpsons.us","Homer", "1234".hashCode());
        homer.addFriend("burn@simpsons.us");
        homer.addFriend("bart@simpsons.us");

        User burn = new User("burn@simpsons.us", "Burn", "1234".hashCode());
        burn.addFriend("homer@simpson.us");

        User bart = new User("bart@simpsons.us", "Bart", "1234".hashCode());
        bart.addFriend("homer@simpsons.us");

        users.put("burn@simpsons.us", burn);
        users.put("homer@simpsons.us", homer);
        users.put("marge@simpsons.us", new User("marge@simpsons.us", "Louis", "1234".hashCode()));
        users.put("bart@simpsons.us", bart);
        users.put("lisa@simpsons.us", new User("lisa@simpsons.us", "Lisa", "1234".hashCode()));
    }

    private void printUsers() {

        System.out.println(users);
    }

    public Hashtable<String, User> getUsers() {
        return users;
    }

    public void setUsers(Hashtable<String, User> db) {
        this.users = db;
    }

    public void addUser(String email, User user) {
        users.put(email, user);

        printUsers();
    }
    
    public void addChannel(String id, Channel channel) {
    	channels.put(id, channel);
    }
    
    public ArrayList<String> getChannelIDs() {
    	
    	ArrayList<String> ret = new ArrayList<String>();
    	
    	ArrayList<Channel> arr = new ArrayList<Channel>(channels.values());
    	for(int i=0;i<arr.size();i++)
    	{
    		ret.add(arr.get(i).getId());
    	}
    	
    	return ret;
    }
    
    public Hashtable<String, Channel> getChannels() {
    	return channels;
    }
}
