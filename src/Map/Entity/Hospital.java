package Map.Entity;


import Application.Database.DatabaseManager;
import Directory.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;

    private HashMap<String, Doctor> doctors;
    private HashMap<UUID, Destination> destinations;
    private HashMap<String, Office> offices;

    CampusFloor CampusFloor;

    private Kiosk currentKiosk = null;
    protected ObservableList<Kiosk> kiosks;

    public Hospital() {
        buildings = new HashSet<Building>();
        destinations = new HashMap<>();
        offices = new HashMap<>();
        doctors = new HashMap<>();

        CampusFloor  = new CampusFloor();
        kiosks = FXCollections.observableArrayList();

    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    public CampusFloor getCampusFloor()
    {
        CampusFloor.clearBuildings();

        for(Building b: getBuildings())
        {
            CampusFloor.importBuilding(b);
        }

        return CampusFloor;
    }

    //Alter Doctor HashMap: doctors
    public HashMap<String, Doctor> getDoctors() {
        return doctors;
    }
    public void addDoctors(String s, Doctor doc) {
        doctors.put(s, doc);
    }
    public void setDoctors(HashMap<String, Doctor> doctors) {
        this.doctors = doctors;
    }

    //Alter Suite HashMap: destinations
    public HashMap<UUID, Destination> getDestinations() {
        return destinations;
    }
    public void addDestinations(UUID uuid, Destination suite) {
        destinations.put(uuid, suite);
    }

    //Alter Office HashMap: offices
    public void addOffices(String s, Office off) {
        offices.put(s, off);
    }
    public HashMap<String, Office> getOffices() {
        return this.offices;
    }

    public Collection<Building> getBuildings()
    {
        return buildings;
    }

    public ObservableList<Kiosk> getKiosks () { return kiosks; }
    public Kiosk getCurrentKiosk() { return  currentKiosk; }

    public void addKiosk (Kiosk newKiosk) {
        kiosks.add(newKiosk);
    }

    public  void setCurrentKiosk (Kiosk kiosk) {
        this.currentKiosk = kiosk;
        try {
            new DatabaseManager().updateCurKiosk(kiosk);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
