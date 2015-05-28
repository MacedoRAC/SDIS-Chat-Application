package GUI;

import Connection.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Created by André on 23/05/2015.
 */
public class RegisterController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblSuccess;

    @FXML
    private Label lblError;

    @FXML
    private void loginPage(){
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
    private void register(){
        String result;

        if(!txtEmail.getText().equals("") && !txtPassword.getText().equals(""))
            result = Client.signup(txtEmail.getText(), txtPassword.getText());
        else
            result = "required";

        if(result.equals("true")){
            lblError.setText("");
            lblSuccess.setText("You're ready to go!");
            loginPage();
        }else if(result.equals("false")){
            lblSuccess.setText("");
            lblError.setText("Username already exists.");
            txtPassword.clear();
            txtEmail.clear();
        }else if(result.equals("required")){
            lblSuccess.setText("");
            lblError.setText("Both fields are required!");
            txtPassword.clear();
            txtEmail.clear();
        }else{
            lblSuccess.setText("");
            lblError.setText("Something went wrong. Please try again!");
            txtPassword.clear();
            txtEmail.clear();
        }
    }
}
