//Testing
package Controller;

import Controller.Admin.AdminList;
import Domain.Map.Doctor;
import Model.Database.DatabaseManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Main extends Application {

//    MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ,
//            nodeK, nodeL, nodeM;
//    NodeEdge edgeA, edgeB, edgeC, edgeD, edgeE, edgeF, edgeG, edgeH, edgeI, edgeJ,
//            edgeK, edgeL, edgeM;
//
//    Hospital myHospital;
//    Building myBuilding;
//    Floor floor1;
//    static final int xoffset = -60;
//    static final int yoffset = -80;
//    static final int BATHROOM = 0;
//    static final int DOCTOR = 1;
//    static final int ELEVATOR = 2;
//    static final int FOOD = 3;
//    static final int INFO = 4;
//    static final int HELP = 5;
//    static final int CONNECTOR = 6;


    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments = FXCollections.observableArrayList();

    public static final ObservableList<Doctor> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        SceneSwitcher.switchToUserMapView(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            DatabaseManager test = DatabaseManager.getInstance();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        launch(args);
    }
}
