package GUI;

import Connection.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import GUI.Main;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblSuccess;

    @FXML
    private Label lblError;

    @FXML
    private Text nowBtn;

    @FXML
    private Button loginBtn;


    @FXML
    void initialize() {
        assert nowBtn != null : "fx:id=\"nowBtn\" was not injected: check your FXML file 'login.fxml'.";
        assert loginBtn != null : "fx:id=\"loginBtn\" was not injected: check your FXML file 'login.fxml'.";
    }

    @FXML
    private void registerPanel(){

        Parent register = null;
        try {
            register = FXMLLoader.load(getClass().getResource("register.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.stage.setScene(new Scene(register, 290, 390));
        Main.stage.show();
    }

    @FXML
    private void signIn(){

        String result = Client.login(txtUsername.getText(), txtPassword.getText());


        if(result.equals("true")){
            lblError.setText("");
            lblSuccess.setText("You're ready to go!");
        }else if(result.equals("false")){
            lblSuccess.setText("");
            lblError.setText("No match with this Username and Password. Please try again!");
        }else{
            lblSuccess.setText("");
            lblError.setText("Something went wrong. Please try again!");
        }
    }

}
