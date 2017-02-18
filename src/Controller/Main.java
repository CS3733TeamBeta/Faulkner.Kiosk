//Testing
package Controller;

import Domain.Map.*;
import Domain.ViewElements.DragIconType;
import Model.Database.DatabaseManager;
import Controller.Admin.AdminList;
import Model.DoctorProfile;
import Model.MapModel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

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

    public static MapModel mvm;

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    public static final ObservableList<Doctor> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        mvm = new MapModel();
        mvm.setCurrentFloor(new Floor(1));

        Collection<MapNode> nodes = DatabaseManager.mapNodes.values();
        for(MapNode n : nodes){
            System.out.println(n.getPosX());
            mvm.addMapNode(n);
            mvm.getCurrentFloor().addNode(n);
        }

        SceneSwitcher.switchToLoginView(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            DatabaseManager test = new DatabaseManager();
            try {
                test.loadData();
                launch(args);
                test.executeStatements(DatabaseManager.dropTables);
                System.out.println("Dropped Tables");
                test.executeStatements(DatabaseManager.createTables);
                System.out.println("Created Tables");
                test.saveData();
                test.shutdown();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }
}
