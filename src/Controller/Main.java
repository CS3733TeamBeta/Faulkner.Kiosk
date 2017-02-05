//Testing
package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Main extends Application {

    public static Parent adminLoginRoot;

    public static Scene testScene;

    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        //adminLoginRoot = FXMLLoader.load(getClass().getResource("../AdminLoginView.fxml"));        //Scene adminLogin = new Scene(adminLoginRoot);
        //testScene = new Scene(adminLoginRoot);
        //thisStage.setTitle("Hello World");
        //thisStage.setScene(adminLogin);
        //thisStage.show();
        new AdminLoginController();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
