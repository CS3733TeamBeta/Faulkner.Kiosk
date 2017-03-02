package main.Application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import main.Map.Controller.VisualBuilding;

import java.io.IOError;
import java.io.IOException;


public class Main3D extends Application
{

    protected static final String View3DPath = "/application/3DMapView.fxml";

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

        Scene s = new Scene(root, 800, 600, true);
        loader.getController();
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}