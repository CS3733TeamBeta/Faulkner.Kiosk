package Map.Controller;

import Application.ApplicationController;
import Directory.Doctor;
import Map.Boundary.MapBoundary;
import Map.Navigation.Guidance;
import Application.Exceptions.PathFindingException;
import Map.Entity.Destination;
import Map.Entity.Floor;
import Map.Entity.MapNode;
import Map.Entity.NodeEdge;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewController extends MapController
{

    Boolean downArrow = true; // By default, the navigation arrow is to minimize the welcome page
    ColorAdjust colorAdjust = new ColorAdjust();
    int numClickDr = -1;
    int numClickFood = -1;
    int numClickBath = -1;
    int numClickHelp = -1;

    Floor kioskFloor;

    Guidance newRoute;

    BiMap<Line, NodeEdge>  edgeEntityMap;

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
    TableView deptTable;

    @FXML
    TableColumn deptName;

    @FXML
    TableColumn deptPhoneNum;

    @FXML
    TableColumn deptLocation;

    @FXML
    TableView doctorTable;

    @FXML
    TableColumn docName;

    @FXML
    TableColumn jobTitle;

    @FXML
    TableColumn docDepts;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Polygon floorUpArrow;

    @FXML
    Polygon floorDownArrow;

    Stage primaryStage;

    UserDirectionsPanel panel = new UserDirectionsPanel(mapImage);

    Group zoomGroup;

    @FXML
    Label curFloorLabel;

    BiMap<MapNode, Text> nodeTextMap;

    public UserMapViewController() throws Exception
    {
        super();
        boundary = new MapBoundary(ApplicationController.getHospital());
        nodeTextMap = HashBiMap.create();
        edgeEntityMap = HashBiMap.create();

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

        //kioskFloor = DatabaseManager.Faulkner.getBuildings().iterator().next().getFloor(1);

        panel.mainPane.setPrefHeight(mainPane.getPrefHeight());

        mainPane.getChildren().add(panel);
        panel.toFront();
        panel.relocate(mainPane.getPrefWidth() - 5, 0);

        panel.setCloseHandler(event ->
        {
            hideDirections();
            // Ben, you might want to consider reset the direction panel here
            panel.setVisible(false);
            searchMenuUp();
        });

        panel.setVisible(false);
        directionPaneView();

        boundary.setInitialFloor();

        curFloorLabel.setText("Floor " + boundary.getCurrentFloor().getFloorNumber());

        panToCenter();

        addEdgesToCurrentFloor();
    }

    private void addEdgesToCurrentFloor(){
        for(MapNode n : boundary.getCurrentFloor().getFloorNodes()){
            for(NodeEdge e : n.getEdges()){
                if(!edgeEntityMap.values().contains(e)){
                    addEdge(e);
                    updateEdgeLine(e, Color.BLACK, 0.2);
                }
            }
        }
    }

    private void removeEdgesFromFloor(){
        for(MapNode n : boundary.getCurrentFloor().getFloorNodes()){
            for(NodeEdge e : n.getEdges()){
                if(edgeEntityMap.values().contains(e)){
                    Line line = edgeEntityMap.inverse().get(e);
                    mapItems.getChildren().remove(line);
                    edgeEntityMap.remove(line, e);
                }
            }
        }
    }

    private void removeNodesFromFloor(){
        for(MapNode n : boundary.getCurrentFloor().getFloorNodes()){
            removeMapNode(n);
        }
    }

    private void directionPaneView()
    {

        panel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> showDirections());

        panel.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> hideDirections());

        numClickDr = -1;
        numClickFood = -1;
        numClickBath = -1;
        numClickHelp = -1;
        LoadTableData();
    }

    public void playDirections(Guidance g)
    {
        //for(FloorStep floorStep: g.getFloorSteps()
        //{
       /* for(DirectionStep step : g.getSteps())
        {
            SequentialTransition stepDrawing = new SequentialTransition();

            //for(edge in step)//
            /*{
                Timeline tL = new Timeline();
                Line l = new Line();
                KeyValue moveLineY = new KeyValue(l.endXProperty(), end_x_of_edge);
                KeyValue moveLineX = new KeyValue(l.endYProperty(), end_y_of_edge);

                KeyFrame kf = new KeyFrame((Duration.millis(500)), moveLineX, moveLineY);

                tL.getKeyFrames().add(kf);
                stepDrawing.getChildren().add(tL);
             }*/

            //stepDrawing.play();
           ////switch floors
        //}
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
        if (newRoute != null) //hide stale path
        {
            for (NodeEdge n : newRoute.getPathEdges())
            {
                //   n.changeOpacity(0.0);
                //  n.changeColor(Color.BLACK);
            }
        }

        System.out.println("In path finding");
        MapNode startPoint = boundary.getHospital().getCampusFloor().getKioskNode();

        if (startPoint == null)
        {
            System.out.println("ERROR: NO KIOSK NODE SET ON USERSIDE. SETTING ONE RANDOMLY.");
            startPoint = boundary.getHospital().getCampusFloor().getFloorNodes().iterator().next();
        }

        if (endPoint == startPoint)
        {
            System.out.println("ERROR; CANNOT FIND PATH BETWEEN SAME NODES");
            return;//TODO add error message of some kind
        }

        try
        {
            newRoute = new Guidance(startPoint, endPoint, "North");
        } catch (PathFindingException e)
        {
            return;//TODO add error message throw
        }

        for (NodeEdge edge : edgeEntityMap.values()) {
            if (newRoute.getPathEdges().contains(edge)) {
                updateEdgeLine(edge, Color.RED, 1.0);
            } else {
                updateEdgeLine(edge, Color.BLACK, 0.2);
            }
        }
        /*
        for(Building b : model.getHospital().getBuildings()) {
            for(Floor f : b.getFloors()) {
                for (NodeEdge edge : f.getFloorEdges()) {
                    if (newRoute.getPathEdges().contains(edge)) {
                        edge.changeOpacity(1.0);
                        edge.changeColor(Color.RED);
                    } else {
                        edge.changeOpacity(0.0);
                        edge.changeColor(Color.BLACK);
                    }
                }
            }
        }*/
        panel.fillGuidance(newRoute);

        showDirections();
        newRoute.printTextDirections();
    }

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    /**
     * Called when a NodeEdge is added to the boundary
     * @param edge the edge that was added
     */
    private void addEdge(NodeEdge edge)
    {
        Line line = new Line();
        line.setStrokeWidth(5);

        mapItems.getChildren().add(line);
        line.toBack();
        mapImage.toBack();

        edgeEntityMap.put(line, edge);

        line.setStroke(Color.RED);
        MapNode source = edge.getSource();
        MapNode target = edge.getTarget();

        line.setStartX(source.getPosX());
        line.setStartY(source.getPosY());

        line.setEndX(target.getPosX());
        line.setEndY(target.getPosY());
    }

    private void updateEdgeLine(NodeEdge edge, Color color, double opacity)
    {
        Line l = edgeEntityMap.inverse().get(edge);

        l.setStroke(color);
        l.setOpacity(opacity);
    }

    public void defaultProperty()
    {
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

    public void searchMenuUp()
    {
        Timeline menuSlideDown = new Timeline();
        KeyFrame keyFrame;
        menuSlideDown.setCycleCount(1);
        menuSlideDown.setAutoReverse(true);
        if (downArrow)
        { // Navigate down icon -> welcome page down (left with search bar)
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
            //defaultProperty();
            downArrow = true;
            numClickDr = -1;
            numClickFood = -1;
            numClickBath = -1;
            numClickHelp = -1;
            searchBar.clear();
        }
        navigateArrow.setRotate(navigateArrow.getRotate() + 180); // Changes to direction of arrow icon
        menuSlideDown.getKeyFrames().add(keyFrame);
        menuSlideDown.play();
    }

    public void loadMenu()
    {
        //defaultProperty();
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
        numClickDr = numClickDr * (-1);
        numClickHelp = -1;
        numClickBath = -1;
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void bathroomSelected()
    {
        loadMenu();
        numClickDr = -1;
        numClickHelp = -1;
        numClickBath = numClickBath * (-1);
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void foodSelected()
    {
        loadMenu();
        numClickDr = -1;
        numClickHelp = -1;
        numClickBath = -1;
        numClickFood = numClickFood * (-1);
        DisplayCorrectTable();
    }

    public void helpSelected()
    {
        loadMenu();
        numClickDr = -1;
        numClickHelp = numClickHelp * (-1);
        numClickBath = -1;
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void adminLogin() throws IOException
    {
        ApplicationController.getController().switchToLoginView();
    }

    private void LoadTableData()
    {
        docName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        jobTitle.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        docDepts.setCellValueFactory(new PropertyValueFactory<Doctor, String>("suites"));

        Collection<Doctor> doctrine = boundary.getHospital().getDoctors().values();
        ObservableList<Doctor> doctors = FXCollections.observableArrayList(doctrine);
        FilteredList<Doctor> filteredDoctor = new FilteredList<>(doctors);

        searchBar.textProperty().addListener((observableValue, oldValue, newValue) ->
        {
            filteredDoctor.setPredicate((Predicate<? super Doctor>) profile ->
            {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                // Compare the name of the doctor with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                // Filter does not match
                return false;
            });
        });

        SortedList<Doctor> sortedDoctor = new SortedList<Doctor>(filteredDoctor);
        sortedDoctor.comparatorProperty().bind(deptTable.comparatorProperty());
        doctorTable.setItems(sortedDoctor);


        deptName.setCellValueFactory(new PropertyValueFactory<Destination, String>("name"));
        deptPhoneNum.setCellValueFactory(new PropertyValueFactory<Destination, String>("phoneNum"));
        deptLocation.setCellValueFactory(new PropertyValueFactory<Destination, String>("location"));
        Collection<Destination> suiteVal = boundary.getHospital().getDestinations().values();
        ObservableList<Destination> suites = FXCollections.observableArrayList(suiteVal);
        FilteredList<Destination> filteredSuite = new FilteredList<>(suites);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) ->
        {
            filteredSuite.setPredicate((Predicate<? super Destination>) profile ->
            {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }
                // Compare the name of the doctor with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                // Filter does not match
                return false;
            });
        });
        SortedList<Destination> sortedSuite = new SortedList<Destination>(filteredSuite);
        sortedSuite.comparatorProperty().bind(deptTable.comparatorProperty());
        deptTable.setItems(sortedSuite);
    }

    public void DisplayCorrectTable()
    {
        defaultProperty();
        if (numClickDr == 1)
        {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            doctorIcon.setEffect(clicked);
            searchBar.setPromptText("Search for doctors");
            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }
        if (numClickBath == 1)
        {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            bathroomIcon.setEffect(clicked);
            searchBar.setPromptText("Search for bathrooms");
        }
        if (numClickFood == 1)
        {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            foodIcon.setEffect(clicked);
            searchBar.setPromptText("Search for food");
        }
        if (numClickHelp == 1)
        {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            helpIcon.setEffect(clicked);
            searchBar.setPromptText("Search for help");
        }
        if ((numClickDr == -1) && (numClickBath == -1) && (numClickFood == -1) && (numClickHelp == -1))
        {
            defaultProperty();
        }
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