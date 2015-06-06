package GUI;

import Connection.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Andr� on 04/06/2015.
 */
public class AddFriendController {

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblSuccess;

    @FXML
    private Label lblError;

    @FXML
    private Button addFriendBtn;

    @FXML
    private Button cancelBtn;


    @FXML
    private void addFriend(){
        long threadID = new Client().sendFriend(txtEmail.getText());
        String result = "error";
        
        while(new Client().getThreads().get(threadID)==null);
        result = new Client().getThreads().get(threadID);
        //TODO remove thread from hashtable
        if(!result.contains("error")) {
            lblError.setText("");
            lblSuccess.setText("Request sent.");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
            MainPageController controller = loader.<MainPageController>getController();
            controller.setFriendsList(FXCollections.observableArrayList(Client.getUser().getFriends()));

            // get a handle to the stage
            Stage stage = (Stage) addFriendBtn.getScene().getWindow();
            stage.close();
        }else {
            lblSuccess.setText("");
            lblError.setText("Friend not added! Try again.");
        }
    }

    @FXML
    private void cancel() {
        // get a handle to the stage
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
