package Controller.User;

import Controller.AbstractController;
import Controller.DragDropMain;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.Navigation.Guidance;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Exceptions.PathFindingException;
import Model.MapEditorModel;
import Model.MapModel;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;


/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewController extends AbstractController {
    public JFXButton emailButton;
    Boolean downArrow = true; // By default, the navigation arrow is to minimize the welcome page
    ColorAdjust colorAdjust = new ColorAdjust();
    int numClickDr = 0;
    int numClickFood = 0;
    int numClickBath = 0;
    int numClickHelp = 0;

    double xNodeScale = 1200/941;
    double yNodeScale = 700/546;
    boolean sendingEmail = false;
    Guidance newRoute;

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

    Stage primaryStage;

    MapModel model;

    protected void renderInitialMap()
    {

        //and then set all the existing nodes up
        HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

        for(MapNode n : model.getCurrentFloor().getFloorNodes())
        {
            for(NodeEdge edge: n.getEdges())
            {
                if(!collectedEdges.contains(edge)) collectedEdges.add(edge);
            }

            if(!mainPane.getChildren().contains(n.getNodeToDisplay()))
            {
                mainPane.getChildren().add(n.getNodeToDisplay());
            }


            System.out.println("Adding node at X:" + n.getPosX() + "Y: " + n.getPosY());

            n.getNodeToDisplay().relocate(n.getPosX()*xNodeScale*1.27, 1.27*n.getPosY()*yNodeScale);
            n.getNodeToDisplay().setOnMouseClicked(null);
            n.getNodeToDisplay().setOnMouseEntered(null);
            n.getNodeToDisplay().setOnMouseDragged(null);

            setupImportedNode(n);
        }


        for(NodeEdge edge : collectedEdges)
        {

            if(!mainPane.getChildren().contains(edge.getEdgeLine()))
            {
                mainPane.getChildren().add(edge.getEdgeLine());
            }

            MapNode source = edge.getSource();
            MapNode target = edge.getTarget();

            //@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

            if(!mainPane.getChildren().contains(source.getNodeToDisplay()))
            {

                mainPane.getChildren().add(source.getNodeToDisplay());

                source.getNodeToDisplay().relocate(source.getPosX() * 2*xNodeScale, source.getPosY() * 2* yNodeScale);
            }

            if(!mainPane.getChildren().contains(target.getNodeToDisplay()))
            {
                mainPane.getChildren().add(target.getNodeToDisplay());
                target.getNodeToDisplay().relocate(target.getPosX() * 2*xNodeScale, target.getPosY() * 2*yNodeScale);
            }

            edge.updatePosViaNode(source);
            edge.updatePosViaNode(target);

            edge.setSource(source);
            edge.setTarget(target);

            source.toFront();
            target.toFront();

            edge.getEdgeLine().setOnMouseEntered(null);
            edge.getEdgeLine().setOnMouseClicked(null);

            mainPane.toBack();
        }

        searchMenu.toFront();
    }

    @FXML
    private void initialize()
    {
        model = new MapModel();
        renderInitialMap();

        emailButton.setVisible(false);
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

        droppedNode.getNodeToDisplay().setOnMouseEntered(ev->
        {
            droppedNode.getNodeToDisplay().setOpacity(.65);
        });

        droppedNode.getNodeToDisplay().setOnMouseExited(ev->
        {
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
        } catch (PathFindingException e) {
            return;//TODO add error message throw
        }

        for (NodeEdge edge : model.getCurrentFloor().getFloorEdges()) {
            if(newRoute.getPathEdges().contains(edge)) {
                edge.changeOpacity(1.0);
                edge.changeColor(Color.RED);
            }
            else{
                edge.changeOpacity(0.8);
                edge.changeColor(Color.BLACK);
            }
        }

        newRoute.printTextDirections();
        emailButton.setVisible(true);
        searchBar.setPromptText("Your Email");
        sendingEmail = true;
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
    }

    public void searchMenuUp() {
        if(!sendingEmail)
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
            }
            else
            { // Navigate up icon -> show welcome page
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
            }

            navigateArrow.setRotate(navigateArrow.getRotate() + 180); // Changes to direction of arrow icon

            menuSlideDown.getKeyFrames().add(keyFrame);
            menuSlideDown.play();
        }
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
        }
    }
}
