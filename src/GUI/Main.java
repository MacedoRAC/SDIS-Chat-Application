package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        this.stage = primaryStage;
        stage.getIcons().add(new Image("/GUI/img/icon.png"));
        stage.setTitle("Chat_me");
        stage.setScene(new Scene(root, 290, 390));
        stage.setResizable(false);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
