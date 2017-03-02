package main.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ScrollBar;

import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;

import static javafx.application.ConditionalFeature.FXML;

/**
 * Created by Devon on 3/1/2017.
 */
public class View3DController {

    @FXML
    private Button newBuildingButton;

    @FXML
    private Button resetCameraButton;

    @FXML
    private AnchorPane pane3D;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollBar verticalScroll;

    @FXML
    private ScrollBar horizontalScroll;


    private Group objects3D;

    private double mouseOldX, mouseOldY = 0;
    private Rotate rotateX = new Rotate(90, 200, 200, 0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, 200, 200, -200, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, 200, 200, 0, Rotate.Z_AXIS);


    public View3DController()
    {

    }

    PerspectiveCamera cam;

    @FXML
    void newBuildingButtonPress() {

    }

    @FXML
    void resetCameraButtonPress() {
        rotateX.setAngle(30);
        rotateY.setAngle(0);
        rotateZ.setAngle(0);
        verticalScroll.setValue(30);
        horizontalScroll.setValue(0);
    }

    @FXML
    public void initialize(){

        verticalScroll.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                rotateX.setAngle(verticalScroll.getValue());
            }
        });


        horizontalScroll.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val){
                rotateZ.setAngle(horizontalScroll.getValue());
            }
        });

        objects3D = new Group();

        objects3D.getTransforms().addAll(rotateX, rotateY, rotateZ);

        System.out.println("Adding Building");
        int boxHeight = 10;

        VisualBuilding faulkner = new VisualBuilding(110, 110, 10, 200, 200, (50 - boxHeight));
        objects3D.getChildren().add(faulkner.getGroup());

        VisualBuilding house = new VisualBuilding(40, 50, 10, 100, 70, (50 - boxHeight));
        objects3D.getChildren().add(house.getGroup());

        VisualBuilding lot = new VisualBuilding(50, 80, 10, 90, 150, (50 - boxHeight));
        objects3D.getChildren().add(lot.getGroup());


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

        resetCameraButton.toFront();
        newBuildingButton.toFront();



    }
}
