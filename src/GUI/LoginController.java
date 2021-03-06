package GUI;

import Connection.Client;
import Database.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import GUI.Main;
import javafx.stage.Stage;

import javax.xml.soap.Node;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

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

        String result = Client.login(txtEmail.getText(), txtUsername.getText(), txtPassword.getText());


        if(result.startsWith("true")){
            lblError.setText("");
            lblSuccess.setText("You're ready to go!");
            
            if(result.length()>"true".length())
            {
            	int indUB = result.indexOf("<username>");
				int indUE = result.indexOf("</username>");
				if(indUB==-1 || indUE==-1)
				{
					System.out.println("@ClientGUI/login:bad response received - can't find \"<username>\" or \"</username>\" tags in body");
				}
				else txtUsername.setText(result.substring(indUB+"<username>".length(),indUE));
            }

            try {
                loadMainPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(result.equals("false")){
            lblSuccess.setText("");
            lblError.setText("Email and Password don't match!");
            txtUsername.clear();
            txtPassword.clear();
            txtEmail.clear();
        }else{
            lblSuccess.setText("");
            lblError.setText("Something went wrong. Please try again!");
            txtUsername.clear();
            txtPassword.clear();
            txtEmail.clear();
        }
    }

    private void loadMainPage() throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root, 342, 555));

        MainPageController controller = loader.<MainPageController>getController();

        Client.askFriends();
        new Client().checkFriends();

        ObservableList<String> friends = FXCollections.observableArrayList(Client.getUser().getFriends());

        controller.initData(txtUsername.getText(), friends);

        Main.stage.show();
    }

}
