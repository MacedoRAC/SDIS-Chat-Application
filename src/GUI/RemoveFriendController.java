package GUI;

import Connection.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Andr� on 04/06/2015.
 */
public class RemoveFriendController {

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblSuccess;

    @FXML
    private Label lblError;

    @FXML
    private Button removeFriendBtn;

    @FXML
    private Button cancelBtn;


    @FXML
    private void removeFriend(){
        long threadID = new Client().removeFriend(txtEmail.getText());
        String result = "error";
        
        while(new Client().getThreads().get(threadID)==null);
        result = new Client().getThreads().get(threadID);
        //TODO remove thread from hashtable
        if(!result.contains("error")) {
            lblError.setText("");
            lblSuccess.setText("Friend removed!");


            // get a handle to the stage
            Stage stage = (Stage) removeFriendBtn.getScene().getWindow();
            stage.close();
        }else {
            lblSuccess.setText("");
            lblError.setText("Friend not removed! Try again.");
        }
    }

    @FXML
    private void cancel() {
        // get a handle to the stage
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
