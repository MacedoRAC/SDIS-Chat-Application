package GUI;

import Connection.Client;
import Connection.Server;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    private ListView<String> listFriends = new ListView<>();

    @FXML
    private Button removeFriendBtn;


    public void initData(String user, ObservableList<String> friends) {

        this.username.setText(user);

        if (friends != null)
            listFriends.setItems(friends);
    }

    @FXML
    private void openAddFriendPage() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("addFriend.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/GUI/img/icon.png"));
        stage.setTitle("Add a new friend");
        stage.setScene(new Scene(root, 330, 180));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void openRemoveFriendPage() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("removeFriend.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/GUI/img/icon.png"));
        stage.setTitle("Remove a friend");
        stage.setScene(new Scene(root, 330, 180));
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

    @FXML
    private void openRoom(MouseEvent event){
        String friend = listFriends.getSelectionModel().getSelectedItem();

        /* open communication channel */
        new Client().createChannel("", friend);

        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("room.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.getIcons().add(new Image("/GUI/img/icon.png"));
        stage.setTitle("Chating...");
        stage.setScene(new Scene(root, 590, 390));
        stage.setResizable(false);

        RoomController controller = loader.<RoomController>getController();

        controller.initData(Client.getUser().getUsername(), friend, Client.getUser().getFriends());

        stage.show();
    }

    public void setFriendsList(ObservableList<String> friendsList) {
        this.listFriends.setItems(friendsList);
    }
}
