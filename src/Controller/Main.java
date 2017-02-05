//Testing
package Controller;

import Controller.Admin.AdminList;
import Controller.Admin.AdminLoginController;
import Controller.Admin.ModifyLocations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Main extends Application {

    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

       // Stage stage;


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Admin/AdminLoginView.fxml"));

        Parent root = (Parent)loader.load();


        AdminLoginController controller = loader.getController();
        controller.setStage(primaryStage);

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
