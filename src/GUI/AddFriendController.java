package GUI;

import Connection.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by André on 04/06/2015.
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
       new Client().sendFriend(txtEmail.getText());

        if(result) {
            lblError.setText("");
            lblSuccess.setText("Request sended.");

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
