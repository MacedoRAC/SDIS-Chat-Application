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
<<<<<<< HEAD
        String result = "error";
        
        //System.out.println("\n\n\n\n\n\n\n\n\n" + result + "\n\n\n\n\n\n\n\n\n");
        while(new Client().getThreads().get(threadID)==null);
        result = new Client().getThreads().get(threadID);
        //TODO remove thread from hashtable
        if(!result.contains("error")) {
=======
        String result = new Client().getThreads().get(threadID);

        while(result == null){
            result = new Client().getThreads().get(threadID);
        }

        if(!result.contains("error") || !result.equals("null")) {
>>>>>>> origin/master
            lblError.setText("");
            lblSuccess.setText("Request sent.");

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
