package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Created by André on 23/05/2015.
 */
public class RegisterController {

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
}
