package Controller.User;

import Controller.AbstractController;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.*;
import Domain.Navigation.DirectionStep;
import Domain.Navigation.Guidance;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Exceptions.PathFindingException;
import Model.Database.DatabaseManager;
import Model.MapModel;
import javafx.event.EventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;
import static Model.Database.DatabaseManager.destinations;


/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewController extends AbstractController {

    Boolean downArrow = true; // By default, the navigation arrow is to minimize the welcome page
    ColorAdjust colorAdjust = new ColorAdjust();
    int numClickDr = -1;
    int numClickFood = -1;
    int numClickBath = -1;
    int numClickHelp = -1;

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
    ImageView mapImage;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Polygon floorUpArrow;

    @FXML
    Polygon floorDownArrow;

    @FXML private ChoiceBox<String> dropDown;

    Stage primaryStage;

    MapModel model;

    UserDirectionsPanel panel = new UserDirectionsPanel(mapImage, this);

    Group mapItems;

    Group zoomGroup;

    public UserMapViewController() throws Exception
    {

    }

    protected void renderFloorMap()
    {
        mapItems.getChildren().clear();
        mapImage.setImage(model.getCurrentFloor().getImageInfo().getFXImage());
        mapItems.getChildren().add(mapImage);

        for(MapNode n: model.getCurrentFloor().getFloorNodes())
        {
            addToMap(n);

            for(NodeEdge e : n.getEdges())
            {
                if(!mapItems.getChildren().contains(e.getEdgeLine()))
                {
                    mapItems.getChildren().add(e.getEdgeLine());
                }

                e.updatePosViaNode(n);
            }

            n.getNodeToDisplay().toFront();
        }

        mapImage.toBack();
    }

    protected void importNode(MapNode mapNode)
    {
        if(!mapItems.getChildren().contains(mapNode.getNodeToDisplay()))
        {
            mapItems.getChildren().add(mapNode.getNodeToDisplay()); //add to right panes children
        }

        mapNode.setPos(mapNode.getPosX(), mapNode.getPosY());


        if(!mapNode.getIconType().equals(DragIconType.connector)) //treeview checks that floor actually contains
        {
            //addToTreeView(mapNode); disabled for now.
        }

        mapNode.toFront(); //send the node to the front
    }

    public void addToMap(MapNode n)
    {
        Text destLabel = new Text(n.getLabel());
        destLabel.setStyle("-fx-font-weight: bold");
        destLabel.setTranslateX(n.getPosX() - (destLabel.getLayoutBounds().getWidth() / 2) + 12);
        destLabel.setTranslateY((n.getPosY()-5));


        if(!mapItems.getChildren().contains(n.getNodeToDisplay()))
        {
            mapItems.getChildren().add(n.getNodeToDisplay()); //add to right panes children
            mapItems.getChildren().add(destLabel);
        }

        setupImportedNode(n);

        if(n.getIconType().equals(DragIconType.connector))
        {
           // n.getNodeToDisplay().setVisible(false);
        }

        ((DragIcon) n.getNodeToDisplay()).relocateToPoint(new Point2D(n.getPosX(),
                n.getPosY()));

    }

    /*public void zoomToExtents(Group group)
    {
        Bounds groupBounds = group.getLayoutBounds();
        final Bounds viewportBounds = scrollPane.getViewportBounds();

        while (groupBounds.getWidth() > viewportBounds.getWidth())
        {
            zoomTarget.setScaleX(.9 * zoomTarget.getScaleX());
            zoomTarget.setScaleY(.9 * zoomTarget.getScaleY());
            groupBounds = group.getLayoutBounds();
        }
    }*/

    @FXML
    private void dropDownResetOpacity(){
        dropDown.setOpacity(0.85);
    }
    @FXML
    private void dropDownChangeOpacity(){
        dropDown.setOpacity(1.0);
    }

    @FXML
    private void floorDownResetOpacity(){
        floorDownArrow.setOpacity(0.7);
    }
    @FXML
    private void floorDownChangeOpacity(){
        floorDownArrow.setOpacity(0.8);
    }

    @FXML
    private void floorUpResetOpacity(){
        floorUpArrow.setOpacity(0.7);
    }
    @FXML
    private void floorUpChangeOpacity(){
        floorUpArrow.setOpacity(0.8);
    }

    @FXML
    private void clickedDownArrow(){
        int desiredFloor = model.getCurrentFloor().getFloorNumber() - 1;
        boolean foundFloor = false;
        for(Building b: model.getHospital().getBuildings()) {
            try {
                Floor tempFloor = b.getFloor(desiredFloor);
                model.setCurrentFloor(tempFloor);
                foundFloor = true;
            } catch (Exception e) {
                System.out.println("Threw funky exception");
                e.printStackTrace();

            }
        }
        if(foundFloor) {
            System.out.println("Changing floor");
            renderFloorMap();
        }else{
            System.out.println("Error in changing floor");
        }
    }

    @FXML
    private void clickedUpArrow(){
        int desiredFloor = model.getCurrentFloor().getFloorNumber() + 1;
        boolean foundFloor = false;
        for(Building b: model.getHospital().getBuildings()) {
            try {
                Floor tempFloor = b.getFloor(desiredFloor);
                model.setCurrentFloor(tempFloor);
                foundFloor = true;
            } catch (Exception e) {
                System.out.println("Threw funky exception");
                e.printStackTrace();

            }
        }
        if(foundFloor) {
            System.out.println("Changing floor");
            renderFloorMap();
        }else{
            System.out.println("Error in changing floor");
        }
    }
    @FXML
    private void dropDownHit(){
        if(Integer.parseInt(dropDown.getValue()) != model.getCurrentFloor().getFloorNumber()){
            int desiredFloor = Integer.parseInt(dropDown.getValue());
            boolean foundFloor = false;
            for(Building b : model.getHospital().getBuildings()) {
                if(b.getFloors().contains(model.getCurrentFloor())) {
                    for(Floor f : b.getFloors()) {
                        if(f.getFloorNumber() == desiredFloor){
                            model.setCurrentFloor(f);
                            dropDown.setValue(Integer.toString(desiredFloor));
                            foundFloor = true;
                        }
                    }
                }
            }
            if(foundFloor) {
                System.out.println("Changing floor");
                renderFloorMap();
            }else{
                System.out.println("Error in changing floor");
            }
        }
    }

    @FXML
    private void initialize() throws Exception {
        model = new MapModel();
        mapItems = new Group();

        renderFloorMap();

        zoomGroup = new Group(mapItems);

        // stackpane for centering the content, in case the ScrollPane viewport
        // is larget than zoomTarget
        StackPane content = new StackPane(zoomGroup);
      //  stackPane = content;

        zoomGroup.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
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

            Bounds groupBounds = zoomGroup.getLayoutBounds();
            final Bounds viewportBounds = scrollPane.getViewportBounds();

            if(groupBounds.getWidth()>viewportBounds.getWidth() || evt.getDeltaY()>0) //if max and trying to scroll out
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

            //panToCenter();
        });

        panel.setVisible(false);
        directionPaneView();

        for( Building b : model.getHospital().getBuildings()) {
            if(b.getFloors().contains(model.getCurrentFloor())) {
                for(Floor f : b.getFloors()) {
                    dropDown.getItems().add(Integer.toString(f.getFloorNumber()));
                }
            }
        }
        dropDown.setValue(Integer.toString(model.getCurrentFloor().getFloorNumber()));
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

        double scaleX = (scrollPane.getHmax()-scrollPane.getHmin())/mapItems.getBoundsInLocal().getWidth();
        double scaleY = (scrollPane.getVmax()-scrollPane.getVmin())/mapItems.getBoundsInLocal().getHeight();

        double middleX = mapItems.getBoundsInLocal().getWidth()/2;
        double middleY = mapItems.getBoundsInLocal().getHeight()/2;

        System.out.println("Middle Coordinates-- X: " + middleX + "Y: " + middleY);

        System.out.println("Scaling X: " + scaleX);

        /*Point2D adjustment = new Transform() mapImage.deltaTransform(new Point2D(
                mapItems.getBoundsInParent().getWidth()/2, mapItems.getBoundsInParent().getHeight()/2));*/

        // refresh ScrollPane scroll positions & content bounds
        scrollPane.layout();

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        groupBounds = zoomGroup.getLayoutBounds();

        scrollPane.setHvalue(middleX*scaleX);
        scrollPane.setVvalue(middleY*scaleY);
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
        numClickDr = -1;
        numClickFood = -1;
        numClickBath = -1;
        numClickHelp = -1;
        LoadTableData();
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
            newRoute = new Guidance(startPoint, endPoint, "North");
        } catch (PathFindingException e) {
            return;//TODO add error message throw
        }
        for (NodeEdge edge : model.getCurrentFloor().getFloorEdges()) {
            if(newRoute.getPathEdges().contains(edge)) {
                edge.changeOpacity(1.0);
                edge.changeColor(Color.RED);
            } else {
                edge.changeOpacity(0.8);
                edge.changeColor(Color.BLACK);
            }
        }

        panel.fillGuidance(newRoute);

        showDirections();
        newRoute.printTextDirections();
    }

    //@TODO @blhylak make this pretty, however you want to

    public void highlightStep(DirectionStep step) {
        for (NodeEdge edgeInThis : model.getCurrentFloor().getFloorEdges()) {
            edgeInThis.changeColor(Color.RED);
            edgeInThis.changeOpacity(0.1);
        }
        for (NodeEdge edgeInStep : step.getStepEdges()) {
            for (NodeEdge edgeInThis : model.getCurrentFloor().getFloorEdges()) {
                if (edgeInStep.equals(edgeInThis)) {
                    edgeInThis.changeColor(Color.PURPLE);
                    edgeInThis.changeOpacity(1.0);
                }
            }
        }
    }
    public void setStage(Stage s) {
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
            if (downArrow) { // Navigate down icon -> welcome page down (left with search bar)
                KeyValue welcomeDown = new KeyValue(searchMenu.translateYProperty(), 180);
                keyFrame = new KeyFrame(Duration.millis(600), welcomeDown);
                welcomeGreeting.setVisible(false);
                downArrow = false; // Changes to up icon
                searchMenu.setStyle("-fx-background-color: transparent;");
                panel.setVisible(true);
            } else { // Navigate up icon -> show welcome page
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

    public void loadMenu() {
        //defaultProperty();
        Timeline menuSlideUp = new Timeline();
        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);
        KeyValue menuUp = new KeyValue(searchMenu.translateYProperty(), -(mainPane.getHeight() - 350));
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), menuUp);
        menuSlideUp.getKeyFrames().add(keyFrame);
        menuSlideUp.play();
    }

    public void doctorSelected() {
        loadMenu();
        numClickDr = numClickDr*(-1);
        numClickHelp = -1;
        numClickBath = -1;
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void bathroomSelected() {
        loadMenu();
        numClickDr = -1;
        numClickHelp = -1;
        numClickBath = numClickBath*(-1);
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void foodSelected() {
        loadMenu();
        numClickDr = -1;
        numClickHelp = -1;
        numClickBath = -1;
        numClickFood = numClickFood*(-1);
        DisplayCorrectTable();
    }

    public void helpSelected() {
        loadMenu();
        numClickDr = -1;
        numClickHelp = numClickHelp*(-1);
        numClickBath = -1;
        numClickFood = -1;
        DisplayCorrectTable();
    }

    public void adminLogin() throws IOException
    {

        SceneSwitcher.switchToLoginView(primaryStage);
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
    private void LoadTableData() {
        docName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        jobTitle.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        docDepts.setCellValueFactory(new PropertyValueFactory<Doctor, String>("suites"));
        Collection<Doctor> doctrine = model.getHospital().getDoctors().values();
        ObservableList<Doctor> doctors = FXCollections.observableArrayList(doctrine);
        FilteredList<Doctor> filteredDoctor = new FilteredList<>(doctors);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredDoctor.setPredicate((Predicate<? super Doctor>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) { return true; }
                // Compare the name of the doctor with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter)) { return true; }
                // Filter does not match
                return false;
            });});
        SortedList<Doctor> sortedDoctor = new SortedList<Doctor>(filteredDoctor);
        sortedDoctor.comparatorProperty().bind(deptTable.comparatorProperty());
        doctorTable.setItems(sortedDoctor);


        deptName.setCellValueFactory(new PropertyValueFactory<Destination, String>("name"));
        deptPhoneNum.setCellValueFactory(new PropertyValueFactory<Destination, String>("phoneNum"));
        deptLocation.setCellValueFactory(new PropertyValueFactory<Destination, String>("location"));
        Collection<Destination> suiteVal = model.getHospital().getDestinations().values();
        ObservableList<Destination> suites = FXCollections.observableArrayList(suiteVal);
        FilteredList<Destination> filteredSuite = new FilteredList<>(suites);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredSuite.setPredicate((Predicate<? super Destination>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) { return true; }
                // Compare the name of the doctor with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter)) { return true; }
                // Filter does not match
                return false;
            });});
        SortedList<Destination> sortedSuite = new SortedList<Destination>(filteredSuite);
        sortedSuite.comparatorProperty().bind(deptTable.comparatorProperty());
        deptTable.setItems(sortedSuite);
    }

    public void DisplayCorrectTable() {
        defaultProperty();
        if (numClickDr == 1) {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            doctorIcon.setEffect(clicked);
            searchBar.setPromptText("Search for doctors");
            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }
        if (numClickBath == 1) {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            bathroomIcon.setEffect(clicked);
            searchBar.setPromptText("Search for bathrooms");
        }
        if (numClickFood == 1) {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            foodIcon.setEffect(clicked);
            searchBar.setPromptText("Search for food");
        }
        if (numClickHelp == 1) {
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);
            helpIcon.setEffect(clicked);
            searchBar.setPromptText("Search for help");
        }
        if((numClickDr == -1)&&(numClickBath == -1)&&(numClickFood == -1)&&(numClickHelp == -1)) {
            defaultProperty();
        }}
}
