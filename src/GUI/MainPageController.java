package GUI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.xml.soap.Text;

/**
 * Created by André on 03/06/2015.
 */
public class MainPageController {

    @FXML
    private Label username;

    @FXML
    private Button addFriendBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private ListView listFriends = new ListView<String>();


    public void initData(String user, ObservableList<String> friends) {
        this.username.setText(user);
        listFriends.setItems(friends);
    }
}
