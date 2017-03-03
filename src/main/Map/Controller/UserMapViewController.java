package main.Map.Controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.animation.SequentialTransition;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Application.ApplicationController;
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
import java.util.HashMap;

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

    UserDirectionsPanel panel;
    UserSearchPanel searchPanel = new UserSearchPanel();

    Group zoomGroup;

    Group edgesOnFloor;

    @FXML
    Label curFloorLabel;

    BiMap<MapNode, Text> nodeTextMap;

    private UserMapBoundary userMapBoundary;

    int directionStepIndex = 0;

    DirectionFloorStep lastFloorStep  = null;
    DragIcon portal = null; //the thing a person can click on to transport them to the next step of directions

    Timeline portalTimeline;

    public UserMapViewController() throws Exception
    {
        super();

        userMapBoundary = new UserMapBoundary(ApplicationController.getHospital());
        boundary = userMapBoundary;

        nodeTextMap = HashBiMap.create();

        edgesOnFloor = new Group();

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
                if(n.equals(boundary.getHospital().getCurrentKiosk()))
                {
                    edgesOnFloor.getChildren().clear();
                    newRoute=null;
                    portal=null;

                    if(portalTimeline!=null)
                    {
                        portalTimeline.stop();
                        portalTimeline = null;
                    }

                    hideDirections();
                }
                else
                {
                    try
                    {
                        if (icon != portal)
                        {
                            findPathToNode(n);
                        }
                    } catch (PathFindingException e)
                    {
                    }
                }
            }
        });

        mapImage.toBack();
        mapItems.toFront();

        return icon;
    }

    private void makePortal(DragIcon icon)
    {
        portalTimeline = new Timeline();
        portalTimeline.setCycleCount(1000);

        final KeyValue kvA = new KeyValue(icon.scaleXProperty(), 1.4);
        final KeyFrame kfA = new KeyFrame(Duration.millis(900), kvA);

        final KeyValue kv1 = new KeyValue(icon.scaleYProperty(), 1.4);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(900), kv1);

        Glow glow = new Glow();
        glow.setLevel(0);

        icon.setEffect(glow);

        final KeyValue kv2 = new KeyValue(glow.levelProperty(), 1);
        final KeyFrame kf2 = new KeyFrame(Duration.millis(900), kv2);

        portalTimeline.getKeyFrames().add(kfA);
        portalTimeline.getKeyFrames().add(kf1);
        portalTimeline.getKeyFrames().add(kf2);

        portalTimeline.setAutoReverse(true);

        portalTimeline.play();
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

        Floor oldFloor = boundary.getCurrentFloor();

        boundary.changeToPreviousFloor();
    }

    @FXML
    private void clickedUpArrow()
    {
        Floor oldFloor = boundary.getCurrentFloor();

        boundary.changeToNextFloor();
    }

    @FXML
    private void initialize() throws Exception
    {
        mapItems.getChildren().add(mapImage);
        mapItems.getChildren().add(edgesOnFloor);


        panel= new UserDirectionsPanel(mapImage);

        panel.addOnStepChangedHandler(h->
        {
            boundary.changeFloor(h.getSource().getFloor());
        });

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

        mainPane.getChildren().add(panel);

        panel.toFront();
        panel.relocate(mainPane.getPrefWidth() - 5, 0);

        mainPane.getChildren().add(searchPanel);
        searchPanel.prefWidthProperty().bind(mainPane.widthProperty());
        searchPanel.welcomeScreen();

        panel.setCloseHandler(event ->
        {
            hideDirections();

            searchPanel.hideWelcomeScreen();
        });

        panel.setVisible(true);

        directionPaneView();

        userMapBoundary.setInitFloor();

        curFloorLabel.setText("Floor " + boundary.getCurrentFloor().getFloorNumber());

        buildingDropdown.setItems(boundary.getHospital().getBuildings());
        buildingDropdown.toFront();

        buildingDropdown.setOnAction(e->
        {
            System.out.println("Change requested");
            boundary.changeBuilding(buildingDropdown.getSelectionModel().getSelectedItem());
        });

        Kiosk kiosk = boundary.getHospital().getCurrentKiosk();

        if(kiosk!=null)
        {
            userMapBoundary.changeBuilding(kiosk.getMyFloor().getBuilding());
            buildingDropdown.getSelectionModel().select(boundary.getCurrentBuilding());
        }

        panToCenter();
        movePath.setFill(Color.BLACK);
        movePath.toFront();
        mapItems.getChildren().add(movePath);

        /**
         * Observer to watch for floor change
         */
        boundary.addObserver((o, args)->
        {
            edgesOnFloor.getChildren().clear();

            if(newRoute!=null)
            {
                for (DirectionFloorStep floorStep : newRoute.getFloorSteps())
                {
                    if (floorStep.getFloor().getFloorNumber() == boundary.getCurrentFloor().getFloorNumber())
                    {
                        playLineDirections(floorStep);
                        panel.setFloorStep(floorStep);
                    }
                }
            }

            curFloorLabel.setText("Floor " + boundary.getCurrentFloor().getFloorNumber());
        });

    }

    private void directionPaneView()
    {

       /* panel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> showDirections());

        panel.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> hideDirections());
        panel.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> followPath());*/
    }

    public void playLineDirections(DirectionFloorStep floorStep)
    {
        SequentialTransition stepDrawing = new SequentialTransition();

        MapNode n = floorStep.getNodesForThisFloor().getFirst();

        for (DirectionStep step : floorStep.getDirectionSteps())
        {
            for (NodeEdge edge : step.getStepEdges())
            {
                if(!(edge instanceof LinkEdge))
                {
                    Timeline tL = new Timeline();
                    Line l = new Line();

                    l.setStrokeWidth(5);
                    l.setStroke(Color.RED);

                    l.toBack();
                    mapImage.toBack();

                    l.setStartY(n.getPosY());
                    l.setStartX(n.getPosX());
                    l.setEndY(n.getPosY());
                    l.setEndX(n.getPosX());

                    KeyValue moveLineY = new KeyValue(l.endXProperty(), edge.getOtherNode(n).getPosX());
                    KeyValue moveLineX = new KeyValue(l.endYProperty(), edge.getOtherNode(n).getPosY());

                    edgesOnFloor.getChildren().add(l);

                    KeyFrame kf = new KeyFrame((Duration.millis(500)), moveLineX, moveLineY);

                    tL.getKeyFrames().add(kf);
                    stepDrawing.getChildren().add(tL);

                    n = edge.getOtherNode(n);
                }
            }

            lastFloorStep = floorStep;
        }

        if(!newRoute.getFloorSteps().getLast().equals(lastFloorStep)) //if its not the last floor step
        {
            stepDrawing.setOnFinished(e->
            {
                MapNode mapNode = floorStep.getNodesForThisFloor().getLast();
                DragIcon icon = iconEntityMap.inverse().get(mapNode);

                makePortal(icon);

                if (icon != null)
                {
                    portal = icon;


                    icon.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
                    {
                        System.out.println("Handler called");

                        if (newRoute != null && lastFloorStep != null)
                        {
                            int index = newRoute.getFloorSteps().indexOf(lastFloorStep);
                            DirectionFloorStep nextStep = newRoute.getFloorSteps().get(index + 1);

                            userMapBoundary.changeFloor(nextStep.getFloor());
                        }
                    });
                }
            });
        }
        else
        {
            stepDrawing.setOnFinished(e-> //just pop a bit to show you're done!
            {
                portalTimeline = new Timeline();

                MapNode mapNode = floorStep.getNodesForThisFloor().getLast();
                DragIcon icon = iconEntityMap.inverse().get(mapNode);

                final KeyValue kvA = new KeyValue(icon.scaleXProperty(), 1.4);
                final KeyFrame kfA = new KeyFrame(Duration.millis(200), kvA);

                final KeyValue kv1 = new KeyValue(icon.scaleYProperty(), 1.4);
                final KeyFrame kf1 = new KeyFrame(Duration.millis(200), kv1);

                portalTimeline.getKeyFrames().add(kfA);
                portalTimeline.getKeyFrames().add(kf1);

                portalTimeline.setAutoReverse(true);
                portalTimeline.setCycleCount(2);
                portalTimeline.play();

            });
        }


        stepDrawing.play();
    }

    private void hideDirections()
    {
        searchPanel.setVisible(true);
        edgesOnFloor.getChildren().clear();
        newRoute=null;

        if(portalTimeline!=null)
        {
            DragIcon iconFromPortal =portal;
            MapNode n = iconEntityMap.get(iconFromPortal);
            iconEntityMap.remove(iconFromPortal);

            if(n!=null) importMapNode(n);

            portalTimeline.stop();
            portalTimeline=null;
        }

        portal=null;

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
        searchPanel.setVisible(false);

        Timeline slideHideDirections = new Timeline();
        KeyFrame keyFrame;
        slideHideDirections.setCycleCount(1);
        slideHideDirections.setAutoReverse(true);

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), -panel.getWidth() + 5);
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        System.out.println("Show directions");

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }

    protected void findPathToNode(MapNode endPoint) throws PathFindingException
    {
        edgesOnFloor.getChildren().clear();
        lastFloorStep = null;

        portal = null;
        //followPath(newRoute);
        directionStepIndex = 0;
        newRoute = userMapBoundary.findPathToNode(endPoint);
        panel.fillGuidance(newRoute);

        newRoute.printTextDirections();

        if(newRoute!=null)
        {
            userMapBoundary.changeFloor(newRoute.getFloorSteps().getFirst().getFloor());
        }

        showDirections();
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

    Circle movePath = new Circle(10);

    public void followPath () {

            int i = panel.getFollowIndex();

            SequentialTransition animation = new SequentialTransition();
            MapNode n = newRoute.getPathNodes().get(i);
            NodeEdge e = newRoute.getPathEdges().get(i);
            if (newRoute.getFloorSteps().getLast().equals(n)) {
                floorUpResetOpacity();
            }
            movePath.setCenterX(n.getPosX());
            movePath.setCenterY(n.getPosY());

            Timeline tl = new Timeline();

            double endX = e.getOtherNode(n).getPosX();
            double endY = e.getOtherNode(n).getPosY();

            KeyValue moveX = new KeyValue(movePath.centerXProperty(), endX);
            KeyValue moveY = new KeyValue(movePath.centerYProperty(), endY);
            KeyFrame kf = new KeyFrame(Duration.seconds(3), moveX, moveY);
            tl.getKeyFrames().add(kf);
            animation.getChildren().add(tl);


            System.out.println("playing");
            animation.play();
    }
}
