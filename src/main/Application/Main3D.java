package main.Application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;


public class Main3D extends Application
{

    public static final String View3DPath = "/map/3DMapView.fxml";

    @Override
    public void start(Stage primaryStage)
    {
        Parent root = null;

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(View3DPath));

        try {
            root = loader.load();
        } catch (IOException e){
            System.out.println(e);
        }

        Scene s = new Scene(root);

        primaryStage.setScene(s);

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}