package Map.Entity;

import Directory.Entity.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.UUID;

import static javafx.collections.FXCollections.observableArrayList;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    ObservableList<Building> buildings;

    private ObservableList<Doctor> doctors;
    private ObservableList<Destination> destinations;
    private ObservableList<Office> offices;

    CampusFloor CampusFloor;

    private Kiosk currentKiosk = null;
    protected ObservableList<Kiosk> kiosks;

    public Hospital() {
        buildings = FXCollections.observableArrayList();
        destinations = FXCollections.observableArrayList();
        offices = FXCollections.observableArrayList();
        doctors = FXCollections.observableArrayList();

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
    public ObservableList<Doctor> getDoctors() {
        return doctors;
    }
    public void addDoctors(Doctor doc) {
        doctors.add(doc);
    }
    public void setDoctors(ObservableList<Doctor> doctors) {
        this.doctors = doctors;
    }

    //Alter Suite HashMap: destinations
    public ObservableList<Destination> getDestinations() {
        return destinations;
    }
    public void addDestinations(Destination destination) {
        destinations.add(destination);
    }

    //Alter Office HashMap: offices
    public void addOffices(Office off) {
        offices.add(off);
    }
    public ObservableList<Office> getOffices() {
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
    }

}
