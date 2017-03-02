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
    private AnchorPane pane3D;

    @FXML
    private AnchorPane rootPane;

    private Group objects3D;

    private double mouseOldX, mouseOldY = 0;
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

    PerspectiveCamera cam;
    @FXML
    void buttonPush() {
        System.out.println("Adding Building");
        int boxHeight = 10;
        VisualBuilding visualbuilding = new VisualBuilding(80, 80, 10, 200, 200, (50 - boxHeight));
        objects3D.getChildren().add(visualbuilding.getGroup());
    }

    @FXML
    public void initialize()   {
        
        objects3D = new Group();

        cam = new PerspectiveCamera();
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

        Image img = new Image(this.getClass().getResourceAsStream("/map/FloorMaps/1_thefirstfloor.png"));

        PhongMaterial material = new PhongMaterial(Color.WHITE);
        material.setSpecularPower(10000);
        material.setDiffuseMap(img);

        floor.setMaterial(material);
        objects3D.getChildren().add(floor);
        objects3D.getChildren().add(greenLight);
        objects3D.getChildren().add(whiteLight);

        // THIS IS THE BUILDING STUFF

        // THIS IS THE BUILDING STUFF

        SubScene subScene = new SubScene(objects3D, 800, 800, true, SceneAntialiasing.BALANCED);

        pane3D.getChildren().add(subScene);

        subScene.setCamera(cam);

        button.toFront();
    }
}
