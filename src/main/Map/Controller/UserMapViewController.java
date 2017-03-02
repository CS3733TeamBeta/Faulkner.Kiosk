package main.Map.Controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Application.ApplicationController;
import main.Map.Boundary.MapBoundary;
import main.Map.Boundary.UserMapBoundary;
import main.Map.Entity.*;
import main.Map.Navigation.DirectionFloorStep;
import main.Map.Navigation.DirectionStep;
import main.Map.Navigation.Guidance;
import main.Application.Exceptions.PathFindingException;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewController extends MapController
{
    Floor kioskFloor;

    Guidance newRoute;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Polygon floorUpArrow;

    @FXML
    private Polygon floorDownArrow;


    @FXML
    private JFXComboBox<Building> buildingDropdown;

    Stage primaryStage;

    UserDirectionsPanel panel = new UserDirectionsPanel(mapImage);
    UserSearchPanel searchPanel = new UserSearchPanel();

    Group zoomGroup;

    @FXML
    Label curFloorLabel;

    BiMap<MapNode, Text> nodeTextMap;

    private UserMapBoundary userMapBoundary;

    public UserMapViewController() throws Exception
    {
        super();

        userMapBoundary = new UserMapBoundary(ApplicationController.getHospital());
        boundary = userMapBoundary;

        nodeTextMap = HashBiMap.create();

        initBoundary();
    }

    @Override
    public DragIcon importMapNode(MapNode n)
    {
        DragIcon icon = super.importMapNode(n);

        Text destLabel = new Text(n.getLabel());
        destLabel.setStyle("-fx-font-weight: bold");

        destLabel.setTranslateX(n.getPosX() - (destLabel.getLayoutBounds().getWidth() / 2) - 15);
        destLabel.setTranslateY((n.getPosY() - 10));
        destLabel.setFont(Font.font(8));

        mapItems.getChildren().add(destLabel);

        destLabel.toFront();

        nodeTextMap.put(n, destLabel);

        icon.setOnMouseClicked(ev ->
        {
            if (ev.getButton() == MouseButton.PRIMARY)
            { // deal with other types of mouse clicks
                try
                {
                    findPathToNode(n);
                } catch (PathFindingException e)
                {
                }
            }
        });

        return icon;
    }

    @Override
    public void removeMapNode(MapNode n)
    {
        super.removeMapNode(n);

        mapItems.getChildren().remove(nodeTextMap.get(n));
    }

    public void zoomToExtents(Group group)
    {
        Bounds groupBounds = group.getLayoutBounds();
        final Bounds viewportBounds = scrollPane.getViewportBounds();

        while (groupBounds.getWidth() > viewportBounds.getWidth())
        {
            zoomGroup.setScaleX(.9 * zoomGroup.getScaleX());
            zoomGroup.setScaleY(.9 * zoomGroup.getScaleY());
            groupBounds = group.getLayoutBounds();
        }
    }


    @FXML
    private void floorDownResetOpacity()
    {
        floorDownArrow.setOpacity(0.4);
    }

    @FXML
    private void floorDownChangeOpacity()
    {
        floorDownArrow.setOpacity(1);
    }

    @FXML
    private void floorUpResetOpacity()
    {
        floorUpArrow.setOpacity(0.4);
    }

    @FXML
    private void floorUpChangeOpacity()
    {
        floorUpArrow.setOpacity(1);
    }

    @FXML
    private void clickedDownArrow()
    {
        int newFloorNum = boundary.changeToPreviousFloor();

        if (newFloorNum != -1)
        {
            curFloorLabel.setText("Floor " + newFloorNum);
        }

        if (newFloorNum <= 1)
        {
            floorDownArrow.setVisible(false);
        }
        else
        {
            floorDownArrow.setVisible(true);
            floorUpArrow.setVisible(true);
        }

        if (newRoute != null)
        {
            for (NodeEdge n : newRoute.getPathEdges())
            {
                // n.changeOpacity(1.0);
                // n.changeColor(Color.RED);
            }
        }
    }

    @FXML
    private void clickedUpArrow()
    {
        int newFloorNum = boundary.changeToNextFloor();

        if (newFloorNum != -1)
        {
            //   renderFloorMap();
            curFloorLabel.setText("Floor " + newFloorNum);
        }

        floorDownArrow.setVisible(true);

       /* if(newFloorNum>boundary.getKioskBuilding().getFloors().size()-1)
        {
            floorUpArrow.setVisible(false);
        }
        else
        {
            floorUpArrow.setVisible(true);
            floorDownArrow.setVisible(true);
        }*/

        if (newRoute != null)
        {
            for (NodeEdge n : newRoute.getPathEdges())
            {
                //n.changeOpacity(1.0);
                // n.changeColor(Color.RED);
            }
        }
    }

    @FXML
    private void initialize() throws Exception
    {
        mapItems.getChildren().add(mapImage);
        zoomGroup = new Group(mapItems);

        // stackpane for centering the content, in case the ScrollPane viewport
        // is larget than zoomTarget
        StackPane content = new StackPane(zoomGroup);
        //  stackPane = content;

        zoomGroup.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) ->
        {
            // keep it at least as large as the content
            content.setMinWidth(newBounds.getWidth());
            content.setMinHeight(newBounds.getHeight());
        });

        scrollPane.setContent(content);

        content.relocate(0, 0);
        mapPane.relocate(0, 0);
        //mapImage.relocate(0, 0);

        scrollPane.setPannable(true);

        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) ->
        {
            // use viewport size, if not too small for zoomTarget
            content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
        });

        content.setOnScroll(evt ->
        {
            evt.consume();

            final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;

            Bounds groupBounds = zoomGroup.getLayoutBounds();
            final Bounds viewportBounds = scrollPane.getViewportBounds();

            if (groupBounds.getWidth() > viewportBounds.getWidth() || evt.getDeltaY() > 0) //if max and trying to scroll out
            {       //DEVON  also checkout zoom to extents
                // calculate pixel offsets from [0, 1] range
                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                // convert content coordinates to zoomTarget coordinates
                Point2D posInZoomTarget = mapItems.parentToLocal(mapItems.parentToLocal(new Point2D(evt.getX(), evt.getY())));

                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = mapItems.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

                // do the resizing
                mapItems.setScaleX(zoomFactor * mapItems.getScaleX());
                mapItems.setScaleY(zoomFactor * mapItems.getScaleY());

                // refresh ScrollPane scroll positions & content bounds
                scrollPane.layout();

                // convert back to [0, 1] range
                // (too large/small values are automatically corrected by ScrollPane)
                groupBounds = zoomGroup.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });

        panel.addOnStepChangedHandler(event ->
        { //when the step is changed in the side panel, update this display!
            boundary.changeFloor(event.getSource().getFloor());
        });

        panel.mainPane.setPrefHeight(mainPane.getPrefHeight());

        mainPane.getChildren().add(searchPanel);
        searchPanel.prefWidthProperty().bind(mainPane.widthProperty());
        searchPanel.welcomeScreen();

        mainPane.getChildren().add(panel);
        panel.toFront();
        panel.relocate(mainPane.getPrefWidth() - 5, 0);

        panel.setCloseHandler(event ->
        {
            hideDirections();
            // Ben, you might want to consider reset the direction panel here
            panel.setVisible(false);
            searchPanel.welcomeScreen();
        });

        panel.setVisible(true);

        directionPaneView();

        boundary.setInitialFloor();

        curFloorLabel.setText("Floor " + boundary.getCurrentFloor().getFloorNumber());

        buildingDropdown.setItems(boundary.getHospital().getBuildings());
        buildingDropdown.toFront();

        buildingDropdown.selectionModelProperty().addListener(
                (o, oldVal, newVal)->
                {
                    boundary.changeBuilding(newVal.getSelectedItem());
                }
        );

        Kiosk kiosk = boundary.getHospital().getCurrentKiosk();

        if(kiosk!=null)
        {
            userMapBoundary.changeBuilding(kiosk.getMyFloor().getBuilding());
            buildingDropdown.getSelectionModel().select(boundary.getCurrentBuilding());
        }

        panToCenter();
    }

    private void directionPaneView()
    {

        panel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> showDirections());

        panel.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> hideDirections());
    }

    public void playDirections(Guidance g)
    {
        for (DirectionFloorStep floorStep : g.getFloorSteps())
        {
            SequentialTransition stepDrawing = new SequentialTransition();

            MapNode n = g.getPathNodes().iterator().next();

            for (DirectionStep step : floorStep.getDirectionSteps())
            {
                for (NodeEdge edge : step.getStepEdges())
                {
                    if (n.getType().equals(NodeType.Connector))
                    {
                        System.out.println("Found connector");
                    }

                    Timeline tL = new Timeline();
                    Line l = new Line();

                    l.setStrokeWidth(5);
                    l.setStroke(Color.RED);

                    mapItems.getChildren().add(l);
                    l.toBack();
                    mapImage.toBack();

                    l.setStartY(n.getPosY());
                    l.setStartX(n.getPosX());
                    l.setEndY(n.getPosY());
                    l.setEndX(n.getPosX());

                    KeyValue moveLineY = new KeyValue(l.endXProperty(), edge.getOtherNode(n).getPosX());
                    KeyValue moveLineX = new KeyValue(l.endYProperty(), edge.getOtherNode(n).getPosY());

                    KeyFrame kf = new KeyFrame((Duration.millis(500)), moveLineX, moveLineY);


                    //tL.getKeyFrames(new KeyFrame(Duration.millis(0), new KeyValue(l.visibleProperty(), true)));

                    tL.getKeyFrames().add(kf);
                    stepDrawing.getChildren().add(tL);
                }
            }

            //stepDrawing.play();
           ////switch floors
        //}

            stepDrawing.play();
        }
    }

    private void hideDirections()
    {
        Timeline slideHideDirections = new Timeline();
        KeyFrame keyFrame;
        slideHideDirections.setCycleCount(1);
        slideHideDirections.setAutoReverse(true);

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), 0);
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }

    private void showDirections()
    {
        panel.setVisible(true);
        Timeline slideHideDirections = new Timeline();
        KeyFrame keyFrame;
        slideHideDirections.setCycleCount(1);
        slideHideDirections.setAutoReverse(true);

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), -panel.getWidth() + 5);
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }

    protected void findPathToNode(MapNode endPoint) throws PathFindingException
    {
        //followPath(newRoute);
        panel.fillGuidance(newRoute);

        showDirections();
        newRoute.printTextDirections();

        playDirections(newRoute);

    }

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    private void panToCenter()
    {
        Bounds groupBounds = zoomGroup.getLayoutBounds();
        final Bounds viewportBounds = scrollPane.getViewportBounds();

        double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
        double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

        // convert content coordinates to zoomTarget coordinates
        //Point2D posInZoomTarget = mapItems.parentToLocal(mapItems.parentToLocal(new Point2D(evt.getX(), evt.getY())));

        // calculate adjustment of scroll position (pixels)

        double scaleX = (scrollPane.getHmax() - scrollPane.getHmin()) / mapItems.getBoundsInLocal().getWidth();
        double scaleY = (scrollPane.getVmax() - scrollPane.getVmin()) / mapItems.getBoundsInLocal().getHeight();

        double middleX = mapItems.getBoundsInLocal().getWidth() / 2;
        double middleY = mapItems.getBoundsInLocal().getHeight() / 2;

        System.out.println("Middle Coordinates-- X: " + middleX + "Y: " + middleY);

        System.out.println("Scaling X: " + scaleX);

        /*Point2D adjustment = new Transform() mapImage.deltaTransform(new Point2D(
                mapItems.getBoundsInParent().getWidth()/2, mapItems.getBoundsInParent().getHeight()/2));*/

        // refresh ScrollPane scroll positions & content bounds
        scrollPane.layout();

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        groupBounds = zoomGroup.getLayoutBounds();

        scrollPane.setHvalue(middleX * scaleX);
        scrollPane.setVvalue(middleY * scaleY-.1);
    }

    public void adminLogin() throws IOException
    {
        ApplicationController.getController().switchToLoginView();
    }

    public void followPath (Guidance g) {


        Circle movePath = new Circle(10);
        movePath.setFill(Color.BLACK);
        movePath.toFront();
        mapItems.getChildren().add(movePath);

        for (DirectionFloorStep fStep: g.getFloorSteps()) {
            SequentialTransition animation = new SequentialTransition();
            MapNode n = g.getPathNodes().iterator().next();
            movePath.setCenterX(n.getPosX());
            movePath.setCenterY(n.getPosY());
            for (DirectionStep step: fStep.getDirectionSteps()) {

                for (NodeEdge edge: step.getStepEdges()) {
                    if(n.getType().equals(NodeType.Connector))
                    {
                        System.out.println("Found connector");
                    }


                    Timeline tl = new Timeline();



                    double endX = edge.getOtherNode(n).getPosX();
                    double endY = edge.getOtherNode(n).getPosY();


                    KeyValue moveX = new KeyValue(movePath.centerXProperty(), endX);
                    KeyValue moveY = new KeyValue(movePath.centerYProperty(), endY);
                    KeyFrame kf = new KeyFrame(Duration.seconds(3), moveX, moveY);
                    tl.getKeyFrames().add(kf);
                    animation.getChildren().add(tl);

                    n = edge.getOtherNode(n);

                }

            }
            System.out.println("playing");
            animation.play();
        }

    }
}


     /*
    public void onEmailDirections(ActionEvent actionEvent) {
        String givenEmail = searchBar.getText().toLowerCase();
        if (givenEmail.contains("@") && (givenEmail.contains(".com") || givenEmail.contains(".org") || givenEmail.contains(".edu") || givenEmail.contains(".gov"))) {
            System.out.println("onEmailDirections called");
            emailButton.setVisible(false);
            System.out.println(searchBar.getText());
            System.out.println("end");
            newRoute.sendEmailGuidance(searchBar.getText(), mainPane);
            defaultProperty();
            searchBar.setText("Search Hospital");
            sendingEmail = false;
        } else {
            System.out.println("Not a valid address!");
            //@TODO Show in ui email was invalid
        }}
*/
