package main.Application;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import main.Map.Controller.VisualBuilding;

import java.awt.event.ActionEvent;

import static javafx.application.ConditionalFeature.FXML;

/**
 * Created by Devon on 3/1/2017.
 */
public class View3DController {

    @FXML
    private Button button;

    @FXML
    private AnchorPane Pane3D;

    @FXML
    void buttonPush() {
        System.out.println("Button");
    }

    @FXML
    protected void initialize()   {

        final PerspectiveCamera cam = new PerspectiveCamera();
        cam.setFieldOfView(50);
        cam.setFarClip(10000);
        cam.setNearClip(0.01);
        cam.getTransforms().addAll(new Rotate(75,Rotate.X_AXIS),new Translate(-200,-200,-60));

        PointLight greenLight = new PointLight();
        greenLight.setColor(Color.WHITE);
        greenLight.setTranslateX(200);
        greenLight.setTranslateY(200);
        greenLight.setTranslateZ(-200);

        PointLight whiteLight = new PointLight();
        greenLight.setColor(Color.WHITE);
        greenLight.setTranslateX(300);
        greenLight.setTranslateY(700);
        greenLight.setTranslateZ(-400);

        final Box floor = new Box(500, 360, 1);
        floor.setTranslateX(200);
        floor.setTranslateY(200);
        floor.setTranslateZ(50);

        //Image img = new Image(this.getClass().getResourceAsStream("C:\\Users\\Devon\\Dropbox\\School\\college\\WPI - Senior Year\\C Term\\CS 3733\\git folder\\Faulkner.Kiosk\\src\\main\\resources\\map\\FloorMaps\\1_thefirstfloor.png"));

        PhongMaterial material = new PhongMaterial(Color.WHITE);
        material.setSpecularPower(10000);
        //material.setDiffuseMap(img);

        floor.setMaterial(material);
        Pane3D.getChildren().add(floor);
        Pane3D.getChildren().add(greenLight);
        Pane3D.getChildren().add(whiteLight);

        // THIS IS THE BUILDING STUFF
        int boxHeight = 10;
        VisualBuilding visualbuilding = new VisualBuilding(80, 80, 10, 200, 200, (floor.getTranslateZ()-boxHeight));
        Pane3D.getChildren().add(visualbuilding.getGroup());


        // THIS IS THE BUILDING STUFF

        final Rectangle rectangle = new Rectangle(400, 400, Color.TRANSPARENT);
        rectangle.setMouseTransparent(true);
        rectangle.setDepthTest(DepthTest.DISABLE);
        Pane3D.getChildren().add(rectangle);

    }


}
