package Database;

import java.util.Hashtable;

/**
 * Created by André on 28/05/2015.
 */
public class Database {

    private Hashtable<String, User> db;


    public Database(){
        db = new Hashtable<>();

        populateDatabase();
    }

    private void clearDatabase(){
        db.clear();
    }

    private void populateDatabase() {

        db.put("burn@simpsons.us", new User("burn@simpsons.us", "Burn", "1234"));
        db.put("homer@simpsons.us", new User("homer@simpsons.us","Homer", "1234"));
        db.put("louis@simpsons.us", new User("louis@simpsons.us", "Louis", "1234"));
        db.put("bart@simpsons.us", new User("bart@simpsons.us", "Bart", "1234"));
        db.put("lisa@simpsons.us", new User("lisa@simpsons.us", "Lisa", "1234"));
    }

    private void printDatabase() {

        System.out.println(db);
    }

    public Hashtable<String, User> getDb() {
        return db;
    }

    public void setDb(Hashtable<String, User> db) {
        this.db = db;
    }

    public void add(String email, User user) {
        db.put(email, user);

        printDatabase();
    }
}
