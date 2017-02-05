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

       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("MyGui.fxml"));
        Parent root = (Parent)loader.load();
        AdminLoginController controller = (AdminLoginController) loader.getController();
        controller.setStage(primaryStage); // or what you want to do*/

       // Stage stage;

        FXMLLoader loader = new FXMLLoader((getClass().getResource("../AdminLoginView.fxml")));

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
