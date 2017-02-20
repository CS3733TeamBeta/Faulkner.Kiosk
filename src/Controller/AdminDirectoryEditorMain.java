package Controller;

import Model.Database.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminDirectoryEditorMain extends Application {


    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        try {

            Scene scene = new Scene(root);
            SceneSwitcher.switchToScene(primaryStage, "../Admin/AdminDirectoryEditor.fxml");
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            DatabaseManager test = DatabaseManager.getInstance();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        launch(args);
    }

}
