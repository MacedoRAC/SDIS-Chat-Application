package GUI;

import Connection.Client;
import Database.Channel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.Collection;

/**
 * Created by André on 05/06/2015.
 */
public class RoomController {

    @FXML
    private ListView<String> peopleOnChat;

    @FXML
    private Button addPeopleBtn;

    @FXML
    private ListView<String> msgsContainer;

    @FXML
    private TextArea msgField;

    @FXML
    private Button sendBtn;

    private String channelID;

    @FXML
    private void send(){
        String threadID = "";
        if(msgField.getText() != ""){
            Channel[] channels = Client.getUser().getChannels().values().toArray(new Channel[Client.getUser().getChannels().values().size()]);
            for(int i = 0; i < channels.length; i++){
                if(channels[i].getUsers().contains(peopleOnChat.getItems().get(1)))
                    threadID = channels[i].getId();
            }

            new Client().sendMessage(msgField.getText(), threadID);
            ObservableList<String> items = msgsContainer.getItems();
            items.add(msgField.getText());
            msgField.setText("");
        }
    }

    @FXML
    private void addPeopleToRoom(){

    }

    public void initData(String myUsername, String friend) {
        ObservableList<String> people = FXCollections.observableArrayList(myUsername, friend);
        this.peopleOnChat.setItems(people);
    }
}
