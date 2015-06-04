package GUI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;

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

    @FXML
    private void openAddFriendPage() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("addFriend.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/GUI/img/icon.png"));
        stage.setTitle("Add a new friend");
        stage.setScene(new Scene(root, 342, 192));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void logout(){
        Parent login = null;
        try {
            login = FXMLLoader.load(getClass().getResource("login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.stage.setScene(new Scene(login, 290, 390));
        Main.stage.show();
    }
}
