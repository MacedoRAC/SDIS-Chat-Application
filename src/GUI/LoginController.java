package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

}
