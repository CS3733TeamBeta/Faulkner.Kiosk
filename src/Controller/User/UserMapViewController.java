package Controller.User;

import Controller.AbstractController;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.Floor;
import Domain.Map.LinkEdge;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.Navigation.Guidance;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Exceptions.PathFindingException;
import Model.Database.DatabaseManager;
import Model.MapModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashSet;


/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewController extends AbstractController {

    Boolean downArrow = true; // By default, the navigation arrow is to minimize the welcome page
    ColorAdjust colorAdjust = new ColorAdjust();
    int numClickDr = 0;
    int numClickFood = 0;
    int numClickBath = 0;
    int numClickHelp = 0;

    double xNodeScale = 1200/941;
    double yNodeScale = 700/546;

    Floor kioskFloor;

    Guidance newRoute;

    @FXML
    AnchorPane mapPane;

    @FXML
    AnchorPane mainPane;

    @FXML
    AnchorPane searchMenu;

    @FXML
    ImageView doctorIcon;

    @FXML
    ImageView bathroomIcon;

    @FXML
    ImageView foodIcon;

    @FXML
    ImageView helpIcon;

    @FXML
    ImageView navigateArrow;

    @FXML
    TextField searchBar;

    @FXML
    Text welcomeGreeting;

    @FXML
    TreeTableView deptTable;

    @FXML
    TreeTableView doctorTable;

    @FXML
    ImageView mapImage;

    @FXML
    ScrollPane scrollPane;

    Stage primaryStage;

    MapModel model;

    UserDirectionsPanel panel = new UserDirectionsPanel(mapImage);

    Group mapItems;

    Group zoomTarget;

    public UserMapViewController() throws Exception
    {

    }

    protected void renderFloorMap()
    {
        mapItems = new Group();
        mapItems.getChildren().add(mapImage);

        mapImage.setImage(model.getCurrentFloor().getImageInfo().getFXImage());

        //and then set all the existing nodes up
        HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

        for(MapNode n : model.getCurrentFloor().getFloorNodes())
        {
            System.out.println("Adding node");

            if(Main.fixOffsetsFromEditor){
                System.out.println("Fixing node positons");
                n.setPosX(n.getPosX() + 7.5);
                n.setPosY(n.getPosY() + 7.5);
            }

            n.getNodeToDisplay().setOnMouseClicked(null);
            n.getNodeToDisplay().setOnDragDetected(null);
            n.getNodeToDisplay().setOnMouseDragged(null);
            n.getNodeToDisplay().setOnMouseEntered(null);

            addToMap(n);

            for(NodeEdge edge: n.getEdges())
            {
                if(!collectedEdges.contains(edge) && !(edge instanceof LinkEdge)) collectedEdges.add(edge);
            }
        }

        if(Main.fixOffsetsFromEditor){
            Main.fixOffsetsFromEditor = false;
        }

        for(NodeEdge edge : collectedEdges)
        {
            edge.getEdgeLine().setOnMouseClicked(null);
            edge.getEdgeLine().setOnMouseEntered(null);
            edge.getEdgeLine().setOnMouseExited(null);

            if(!mapItems.getChildren().contains(edge.getNodeToDisplay()))
            {
                mapItems.getChildren().add(edge.getNodeToDisplay());
            }

            MapNode source = edge.getSource();
            MapNode target = edge.getTarget();

            //@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

            if(!mapItems.getChildren().contains(source.getNodeToDisplay()))
            {
                addToMap(source);
            }

            if(!mapItems.getChildren().contains(target.getNodeToDisplay()))
            {
                addToMap(target);
            }

            edge.updatePosViaNode(source);
            edge.updatePosViaNode(target);

            edge.toBack();
            edge.changeOpacity(0.0);
            source.toFront();
            target.toFront();
        }

        mapImage.toBack();
    }

    public void addToMap(MapNode n)
    {
        if(!mapItems.getChildren().contains(n.getNodeToDisplay()))
        {
            mapItems.getChildren().add(n.getNodeToDisplay()); //add to right panes children
        }

        setupImportedNode(n);

        if(n.getIconType().equals(DragIconType.connector))
        {
           // n.getNodeToDisplay().setVisible(false);
        }

        ((DragIcon) n.getNodeToDisplay()).relocateToPoint(new Point2D(n.getPosX(),
                n.getPosY()));

    }

    public void zoomToExtents(Group group)
    {
        Bounds groupBounds = group.getLayoutBounds();
        final Bounds viewportBounds = scrollPane.getViewportBounds();

        while (groupBounds.getWidth() > viewportBounds.getWidth())
        {
            zoomTarget.setScaleX(.9 * zoomTarget.getScaleX());
            zoomTarget.setScaleY(.9 * zoomTarget.getScaleY());
            groupBounds = group.getLayoutBounds();
        }
    }

    @FXML
    private void initialize() throws Exception
    {
        model = new MapModel();

        renderFloorMap();

        mapItems.relocate(0, 0);

        zoomTarget = mapItems;

        Group group = new Group(zoomTarget);

        // stackpane for centering the content, in case the ScrollPane viewport
        // is larget than zoomTarget
        StackPane content = new StackPane(group);
      //  stackPane = content;

        group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
        // keep it at least as large as the content
            content.setMinWidth(newBounds.getWidth());
            content.setMinHeight(newBounds.getHeight());
        });

        scrollPane.setContent(content);
        content.relocate(0, 0);
        mapPane.relocate(0, 0);

        scrollPane.setPannable(true);

        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            // use viewport size, if not too small for zoomTarget
            content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
        });

        content.setOnScroll(evt ->
        {
            evt.consume();

            final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;

            Bounds groupBounds = group.getLayoutBounds();
            final Bounds viewportBounds = scrollPane.getViewportBounds();

            if(groupBounds.getWidth()>viewportBounds.getWidth() || evt.getDeltaY()>0) //if max and trying to scroll out
            {       //DEVON  also checkout zoom to extents
                // calculate pixel offsets from [0, 1] range
                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                // convert content coordinates to zoomTarget coordinates
                Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));

                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = zoomTarget.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

                // do the resizing
                zoomTarget.setScaleX(zoomFactor * zoomTarget.getScaleX());
                zoomTarget.setScaleY(zoomFactor * zoomTarget.getScaleY());

                // refresh ScrollPane scroll positions & content bounds
                scrollPane.layout();

                // convert back to [0, 1] range
                // (too large/small values are automatically corrected by ScrollPane)
                groupBounds = group.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
    });

        panel.addOnStepChangedHandler(event -> { //when the step is changed in the side panel, update this display!
            model.setCurrentFloor(event.getSource().getFloor());
        });

        //kioskFloor = DatabaseManager.Faulkner.getBuildings().iterator().next().getFloor(1);

        panel.mainPane.setPrefHeight(mainPane.getPrefHeight());

        mainPane.getChildren().add(panel);
        panel.toFront();
        panel.relocate(mainPane.getPrefWidth()-5, 0);


        panel.setCloseHandler(event->
        {
            ///DEVONNNN
            hideDirections();
            // Ben, you might want to consider reset the direction panel here
            panel.setVisible(false);
            searchMenuUp();

            zoomToExtents(group); // TESTING PROGRAMATIC ZOOMING

            Bounds groupBounds = group.getLayoutBounds();

            final Bounds viewportBounds = scrollPane.getViewportBounds();


              //calculate pixel offsets from [0, 1] range
                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                // convert content coordinates to zoomTarget coordinates
               /* Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(new Point2D(viewportBounds.getWidth()/2,
                        viewportBounds.getHeight()/2)));*/

                Point2D zoomTargetCenter = zoomTarget.parentToLocal(group.parentToLocal(content.getWidth()/2, content.getHeight()/2));

                Point2D posInZoomTarget = zoomTargetCenter;

                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = zoomTarget.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(viewportBounds.getWidth()/400- 1));

                // do the resizing
                zoomTarget.setScaleX(viewportBounds.getWidth()/400 * zoomTarget.getScaleX());
                zoomTarget.setScaleY(viewportBounds.getWidth()/400 * zoomTarget.getScaleY());

                // refresh ScrollPane scroll positions & content bounds
                scrollPane.layout();

                // convert back to [0, 1] range
                // (too large/small values are automatically corrected by ScrollPane)
                groupBounds = group.getLayoutBounds();

                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
        });

        panel.setVisible(false);
        directionPaneView();
    }

    private void directionPaneView() {

        panel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        showDirections();
                    }
                });

        panel.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        hideDirections();
                    }
                });
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

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), -panel.getWidth()+5);
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }


    private void setupImportedNode(MapNode nodeToSetup){

        //droppedNode.setType(droppedNode.getIconType()); //set the type

        nodeToSetup.getNodeToDisplay().setOnMouseClicked(null);
        nodeToSetup.getNodeToDisplay().setOnDragDetected(null);
        nodeToSetup.getNodeToDisplay().setOnMouseDragged(null);
        nodeToSetup.getNodeToDisplay().setOnMouseEntered(null);
        nodeToSetup.getNodeToDisplay().setOnMouseExited(null);

        nodeToSetup.getNodeToDisplay().setOnMouseClicked(ev -> {
            if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
                try{
                    findPathToNode(nodeToSetup);
                }catch(PathFindingException e){

                }
            }
        });

        /*nodeToSetup.getNodeToDisplay().setOnMouseEntered(ev->
        {
            nodeToSetup.getNodeToDisplay().setOpacity(.65);
        });

        nodeToSetup.getNodeToDisplay().setOnMouseExited(ev->
        {
            nodeToSetup.getNodeToDisplay().setOpacity(1);
        });*/
    }

    protected void findPathToNode(MapNode endPoint) throws PathFindingException {
        System.out.println("In path finding");

        MapNode startPoint = model.getCurrentFloor().getKioskNode();
        if(startPoint == null){
            System.out.println("ERROR: NO KIOSK NODE SET ON USERSIDE. SETTING ONE RANDOMLY.");
            startPoint = model.getCurrentFloor().getFloorNodes().getFirst();
        }
        if (endPoint == startPoint) {
            System.out.println("ERROR; CANNOT FIND PATH BETWEEN SAME NODES");
            return;//TODO add error message of some kind
        }
        try {
            newRoute = new Guidance(startPoint, endPoint, false);
        } catch (PathFindingException e) {
            return;//TODO add error message throw
        }

        for (NodeEdge edge : model.getCurrentFloor().getFloorEdges()) {
            if(newRoute.getPathEdges().contains(edge)) {
                edge.changeOpacity(1.0);
                edge.changeColor(Color.RED);
            }
            else{
                edge.changeOpacity(0.0);
                edge.changeColor(Color.BLACK);
            }
        }

        panel.fillDirectionsList(newRoute.getSteps().getFirst());

        showDirections();
        newRoute.printTextDirections();
    }

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public void defaultProperty() {
        searchMenu.setStyle("-fx-background-color:  #f2f2f2;");

        // Sets the color of the icons to black
        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);

        // By default, only the departments table is shown
        deptTable.setVisible(true);
        // Set all other tables false
        doctorTable.setVisible(false);
        searchBar.setPromptText("Search for Departments");

        // Title shown
        welcomeGreeting.setVisible(true);
        panel.setVisible(false);
    }

    public void searchMenuUp() {
            Timeline menuSlideDown = new Timeline();
            KeyFrame keyFrame;
            menuSlideDown.setCycleCount(1);
            menuSlideDown.setAutoReverse(true);

            if (downArrow)
            {
                // Navigate down icon -> welcome page down (left with search bar)
                KeyValue welcomeDown = new KeyValue(searchMenu.translateYProperty(), 180);
                keyFrame = new KeyFrame(Duration.millis(600), welcomeDown);
                welcomeGreeting.setVisible(false);
                downArrow = false; // Changes to up icon
                searchMenu.setStyle("-fx-background-color: transparent;");
                panel.setVisible(true);
            }
            else
            { // Navigate up icon -> show welcome page
                panel.setVisible(false);
                KeyValue welcomeUp = new KeyValue(searchMenu.translateYProperty(), 0);
                keyFrame = new KeyFrame(Duration.millis(600), welcomeUp);

                // Reset to default
                defaultProperty();

                downArrow = true;
                numClickDr = 0;
                numClickFood = 0;
                numClickBath = 0;
                numClickHelp = 0;

                searchBar.clear();

                navigateArrow.setRotate(navigateArrow.getRotate() + 180); // Changes to direction of arrow icon
            }

        menuSlideDown.getKeyFrames().add(keyFrame);
        menuSlideDown.play();

    }

    public void loadMenu() {
        defaultProperty();

        Timeline menuSlideUp = new Timeline();
        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        KeyValue menuUp = new KeyValue(searchMenu.translateYProperty(), -(mainPane.getHeight() - 350));
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), menuUp);

        menuSlideUp.getKeyFrames().add(keyFrame);

        menuSlideUp.play();
    }

    public void doctorSelected()
    {
        loadMenu();
        numClickDr = numClickDr + 1;
        numClickHelp = 0;
        numClickBath = 0;
        numClickFood = 0;

        if (numClickDr == 2)
        {
            defaultProperty();
            numClickDr = 0;
        }
        else
        {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            doctorIcon.setEffect(clicked);

            searchBar.setPromptText("Search for doctors");

            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }

    }

    public void bathroomSelected() {
        loadMenu();
        numClickBath = numClickBath + 1;
        numClickHelp = 0;
        numClickDr = 0;
        numClickFood = 0;

        if (numClickBath == 2) {
            defaultProperty();
            numClickBath = 0;
        } else {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);

            bathroomIcon.setEffect(clicked);

            searchBar.setPromptText("Search for bathrooms");
        }
    }

    public void foodSelected() {
        loadMenu();
        numClickFood = numClickFood + 1;
        numClickHelp = 0;
        numClickBath = 0;
        numClickDr = 0;

        if (numClickFood == 2) {
            defaultProperty();
            numClickFood = 0;
        } else {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);
            foodIcon.setEffect(clicked);

            searchBar.setPromptText("Search for food");
        }
    }

    public void helpSelected() {
        loadMenu();
        numClickHelp = numClickHelp + 1;
        numClickDr = 0;
        numClickBath = 0;
        numClickFood = 0;

        if (numClickHelp == 2) {
            defaultProperty();
            numClickHelp = 0;
        } else {
            defaultProperty();
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);
            helpIcon.setEffect(clicked);

            searchBar.setPromptText("Search for help");
        }
    }

    public void adminLogin() throws IOException {
        SceneSwitcher.switchToLoginView(primaryStage);
    }
}
