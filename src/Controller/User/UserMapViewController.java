package Controller.User;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.Floor;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.Navigation.Guidance;
import Domain.ViewElements.DragIcon;
import Exceptions.PathFindingException;
import Model.Database.DatabaseManager;
import Model.MapModel;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;
import static Model.Database.DatabaseManager.Faulkner;
import static Model.Database.DatabaseManager.suites;


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

    Stage primaryStage;

    MapModel model;

    UserDirectionsPanel panel = new UserDirectionsPanel(mapImage);

    public UserMapViewController() throws Exception
    {
    }

    protected void renderFloorMap()
    {
        mapPane.getChildren().clear();
        mapPane.getChildren().add(mapImage);

        mapImage.setImage(model.getCurrentFloor().getImageInfo().getFXImage());

        //and then set all the existing nodes up
        HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

        for(MapNode n : model.getCurrentFloor().getFloorNodes())
        {
            System.out.println("Adding node");

            addToMap(n);

            for(NodeEdge edge: n.getEdges())
            {
                if(!collectedEdges.contains(edge)) collectedEdges.add(edge);
            }
            if(!mainPane.getChildren().contains(n.getNodeToDisplay())) {
                mainPane.getChildren().add(n.getNodeToDisplay());
            }
            System.out.println("Adding node at X:" + n.getPosX() + "Y: " + n.getPosY());
            n.getNodeToDisplay().relocate(n.getPosX()*xNodeScale*1.27, 1.27*n.getPosY()*yNodeScale);
            n.getNodeToDisplay().setOnMouseClicked(null);
            n.getNodeToDisplay().setOnDragDetected(null);
        }

        for(NodeEdge edge : collectedEdges)
        {
            edge.getEdgeLine().setOnMouseClicked(null);
            edge.getEdgeLine().setOnMouseEntered(null);
            edge.getEdgeLine().setOnMouseExited(null);

            if(!mapPane.getChildren().contains(edge.getNodeToDisplay()))
            {
                mapPane.getChildren().add(edge.getNodeToDisplay());
            }
            MapNode source = edge.getSource();
            MapNode target = edge.getTarget();
            //@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

            if(!mapPane.getChildren().contains(source.getNodeToDisplay()))
            {
                addToMap(source);
            }

            if(!mapPane.getChildren().contains(target.getNodeToDisplay()))
            {
                addToMap(target);
            }
            edge.updatePosViaNode(source);
            edge.updatePosViaNode(target);

            edge.toBack();
            source.toFront();
            target.toFront();
        }

        mapImage.toBack();
    }

    public void addToMap(MapNode n)
    {
        if(!mapPane.getChildren().contains(n.getNodeToDisplay()))
        {
            mapPane.getChildren().add(n.getNodeToDisplay()); //add to right panes children
        }

        ((DragIcon) n.getNodeToDisplay()).relocateToPoint(new Point2D(n.getPosX(),
                n.getPosY()));
    }


    private void initialize() throws Exception {
        model = new MapModel();


        renderFloorMap();

        panel.addOnStepChangedHandler(event -> { //when the step is changed in the side panel, update this display!
            model.setCurrentFloor(event.getSource().getFloor());
        });

        //kioskFloor = DatabaseManager.Faulkner.getBuildings().iterator().next().getFloor(1);

        panel.mainPane.setPrefHeight(mainPane.getPrefHeight());

        mainPane.getChildren().add(panel);
        panel.toFront();
        panel.relocate(mainPane.getPrefWidth()-panel.getPrefWidth(),0);

        panel.setCloseHandler(event->
        {
            hideDirections();
            loadMenu();
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

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), panel.getPrefWidth());
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }

    private void showDirections()
    {
        Timeline slideHideDirections = new Timeline();
        KeyFrame keyFrame;
        slideHideDirections.setCycleCount(1);
        slideHideDirections.setAutoReverse(true);

        KeyValue hideDirections = new KeyValue(panel.translateXProperty(), mainPane.getWidth()-panel.getPrefWidth());
        keyFrame = new KeyFrame(Duration.millis(600), hideDirections);

        slideHideDirections.getKeyFrames().add(keyFrame);
        slideHideDirections.play();
    }


    private void setupImportedNode(MapNode droppedNode){
        //droppedNode.setType(droppedNode.getIconType()); //set the type
        droppedNode.getNodeToDisplay().setOnMouseClicked(ev -> {
            if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
                try{
                    findPathToNode(droppedNode);
                }catch(PathFindingException e){

                }
            }
        });
        droppedNode.getNodeToDisplay().setOnMouseEntered(ev-> {
            droppedNode.getNodeToDisplay().setOpacity(.65);
        });
        droppedNode.getNodeToDisplay().setOnMouseExited(ev-> {
            droppedNode.getNodeToDisplay().setOpacity(1);
        });
    }

    protected void findPathToNode(MapNode endPoint) throws PathFindingException {
        System.out.println("In path finding");
        MapNode startPoint = model.getCurrentFloor().getKioskNode();
        if (endPoint == startPoint) {
            System.out.println("ERROR; CANNOT FIND PATH BETWEEN SAME NODES");
            return;//TODO add error message of some kind
        }
        try {
            newRoute = new Guidance(startPoint, endPoint, false);
        }
        catch (PathFindingException e) {
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

        panel.fillDirectionsList(newRoute.getSteps().getFirst());

        showDirections();
        newRoute.printTextDirections();
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
            } else { // Navigate up icon -> show welcome page
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
        }}

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

    public void adminLogin() throws IOException {
        SceneSwitcher.switchToLoginView(primaryStage);
    }

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

    private void LoadTableData() {
        docName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        jobTitle.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        docDepts.setCellValueFactory(new PropertyValueFactory<Doctor, String>("suites"));
        Collection<Doctor> doctrine = Faulkner.getDoctors().values();
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
        Collection<Suite> suiteVal = Faulkner.getSuites().values();
        ObservableList<Suite> suites = FXCollections.observableArrayList(suiteVal);
        FilteredList<Suite> filteredSuite = new FilteredList<>(suites);
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredSuite.setPredicate((Predicate<? super Suite>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) { return true; }
                // Compare the name of the doctor with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter)) { return true; }
                // Filter does not match
                return false;
            });});
        SortedList<Suite> sortedSuite = new SortedList<Suite>(filteredSuite);
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
