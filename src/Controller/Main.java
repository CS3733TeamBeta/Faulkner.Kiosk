package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static Stage thisStage;
    public static Scene scene1, scene2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        thisStage = primaryStage;
        Parent scene1root = FXMLLoader.load(getClass().getResource("../AdminLoginView.fxml"));
        Parent scene2root = FXMLLoader.load(getClass().getResource("../AdminWelcomeView.fxml"));
        thisStage.setTitle("Hello World");
        scene1 = new Scene(scene1root);
        scene2 = new Scene(scene2root);
        thisStage.setScene(scene1);
        thisStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
