package main.Map.Controller;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import main.Application.ApplicationController;
import main.Map.Controller.VisualBuilding;
import javafx.scene.control.ScrollBar;
import main.Map.Entity.Building;
import main.Map.Entity.Kiosk;

import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static javafx.application.ConditionalFeature.FXML;
import static main.Application.ApplicationController.getHospital;

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
    private ScrollBar verticalScroll;

    @FXML
    private ScrollBar horizontalScroll;

    @FXML
    private JFXComboBox<Kiosk> kioskSelection;

    @FXML
    private JFXComboBox selectTimeout;



    private Group objects3D;

    private Rotate rotateX = new Rotate(30, 200, 200, 0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, 200, 200, -200, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, 200, 200, 0, Rotate.Z_AXIS);

    PhongMaterial defaultMaterial = new PhongMaterial(Color.rgb(255, 255, 255, .95));
    PhongMaterial hoverMaterial = new PhongMaterial(Color.rgb(255, 255, 255, 1));

    public View3DController()
    {
        Image img = new Image(this.getClass().getResourceAsStream("/map/FloorMaps/1_thefirstfloor.png"));

        defaultMaterial.setSpecularPower(10000);
        defaultMaterial.setDiffuseMap(img);

        hoverMaterial.setSpecularPower(10000);
        hoverMaterial.setDiffuseMap(img);
    }

    PerspectiveCamera cam;

    @FXML
    void newBuildingButtonPress() {
        System.out.println("Adding Building");
        int boxHeight = 10;
        VisualBuilding visualbuilding = new VisualBuilding(80, 80, 10, 200, 300, (50 - boxHeight));
        visualbuilding.createNewFloor();

        visualbuilding.getGroup().addEventHandler(MouseEvent.MOUSE_CLICKED,
                e->
                {
                    if(e.getButton() == MouseButton.SECONDARY)
                    {
                        objects3D.getChildren().remove(visualbuilding.getGroup());
                    }
                });

        objects3D.getChildren().add(visualbuilding.getGroup());
    }

    @FXML
    void resetCameraButtonPress() {
        rotateX.setAngle(30);
        rotateY.setAngle(0);
        rotateZ.setAngle(0);
        verticalScroll.setValue(40);
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


        for(Building b: getHospital().getBuildings())
        {
            System.out.println("Parsing " + b.getName());
            objects3D.getChildren().add(VisualBuilding.BuildingFactory(b).getGroup());
        }

       /* objects3D.getChildren().add(faulkner.getGroup());

        VisualBuilding house = new VisualBuilding(40, 50, 10, 100, 70, 50-(boxHeight/2));
        objects3D.getChildren().add(house.getGroup());

        VisualBuilding lot = new VisualBuilding(50, 80, 10, 90, 150, 50-(boxHeight/2));
        objects3D.getChildren().add(lot.getGroup());*/


        cam = new PerspectiveCamera();
        cam.setFieldOfView(50);
        cam.setFarClip(10000);
        cam.setNearClip(0.1);


        cam.getTransforms().addAll(new Rotate(75,Rotate.X_AXIS),new Translate(-300,-300,100));

        PointLight greenLight = new PointLight();
        greenLight.setColor(Color.WHITE);
        greenLight.setTranslateX(200);
        greenLight.setTranslateY(200);
        greenLight.setTranslateZ(-400);

        PointLight whiteLight = new PointLight();
        greenLight.setColor(Color.WHITE);
        greenLight.setTranslateX(400);
        greenLight.setTranslateY(200);
        greenLight.setTranslateZ(-600);

        final Box floor = new Box(500, 360, 1);
        floor.setTranslateX(200);
        floor.setTranslateY(200);
        floor.setTranslateZ(50);

        floor.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e->
                {
                    if(e.getClickCount()==2)
                    {
                        try
                        {
                            ApplicationController.getController().switchToMapEditorView(getHospital().getCampusFloor()
                            .getBuilding());
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                });

        floor.setOnMouseEntered(e-> floor.setMaterial(hoverMaterial));
        floor.setOnMouseExited(e-> floor.setMaterial(defaultMaterial));

        floor.setMaterial(defaultMaterial);

        objects3D.getChildren().add(floor);
        objects3D.getChildren().add(greenLight);
        objects3D.getChildren().add(whiteLight);

        // THIS IS THE BUILDING STUFF

        // THIS IS THE BUILDING STUFF

        SubScene subScene = new SubScene(objects3D, pane3D.prefWidthProperty().doubleValue(), pane3D.prefHeightProperty().doubleValue(), true, SceneAntialiasing.BALANCED);
        subScene.widthProperty().bind(pane3D.widthProperty());
        subScene.heightProperty().bind(pane3D.heightProperty());

        pane3D.getChildren().add(subScene);

        subScene.setCamera(cam);

        resetCameraButton.toFront();
        newBuildingButton.toFront();

        ObservableList<String> timeOuts = FXCollections.observableArrayList();
        timeOuts.addAll("10 seconds", "30 seconds", "1 minute", "2 minutes", "5 minutes");
        selectTimeout.setItems(timeOuts);
        if (ApplicationController.getController().getTimeout() == 10) {
            selectTimeout.setPromptText("10 seconds");
        }
        if (ApplicationController.getController().getTimeout() == 30) {
            selectTimeout.setPromptText("30 seconds");
        }
        if (ApplicationController.getController().getTimeout() == 60) {
            selectTimeout.setPromptText("1 minute");
        }
        if (ApplicationController.getController().getTimeout() == 120) {
            selectTimeout.setPromptText("2 minutes");
        }
        if (ApplicationController.getController().getTimeout() == 300) {
            selectTimeout.setPromptText("5 minutes");
        }

        kioskSelection.setItems(getHospital().getKiosks());

        if(getHospital().getCurrentKiosk()!=null)
        {
            kioskSelection.getSelectionModel().select(getHospital().getCurrentKiosk());
        }

        kioskSelection.setOnAction(e->
        {
            ApplicationController.getHospital().setCurrentKiosk(kioskSelection.getValue());
        });
    }

    @FXML
    public void timeOutSelection () {
        if (selectTimeout.getValue().equals("10 seconds")) {
            ApplicationController.getController().setTimeout(10);
        }
        if (selectTimeout.getValue().equals("30 seconds")) {
            ApplicationController.getController().setTimeout(30);
        }
        if (selectTimeout.getValue().equals("1 minute")) {
            ApplicationController.getController().setTimeout(60);
        }
        if (selectTimeout.getValue().equals("2 minutes")) {
            ApplicationController.getController().setTimeout(120);
        }
        if (selectTimeout.getValue().equals("5 minutes")) {
            ApplicationController.getController().setTimeout(300);
        }
    }

    @FXML
    public void onDirectoryEditorSwitch() throws IOException
    {
        ApplicationController.getController().switchToModifyDirectoryView();
    }

    @FXML
    public void logOut () throws IOException {
        ApplicationController.getController().switchToUserMapView();
    }
}
