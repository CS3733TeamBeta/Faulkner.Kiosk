package Map.Entity;


import Application.Database.DatabaseManager;
import Directory.Entity.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;


/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital extends Observable {
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
        kiosks = FXCollections.observableArrayList(new ArrayList<Kiosk>());
    }

    public void addBuilding(Building b) {

        buildings.add(b);
        b.setHospital(this);
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

    public ObservableList<Doctor> getDoctors() {
        return doctors;
    }

    public void addDoctor(Doctor doc) {
        doctors.add(doc);

        doc.addObserver((observer, args)->
        {
            int i = doctors.indexOf(doc);

            doctors.remove(doc);
            doctors.add(i, doc);
        });
    }

    //Alter Doctor HashMap: doctors
    public void removeDoctor(Doctor doc) { doctors.remove(doc); }
    public boolean containsDoctor(Doctor doc) { return doctors.contains(doc); }
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
    public void addOffice(Office off) {
        offices.add(off);

        off.addObserver((observer, args)->
        {
            int i = offices.indexOf(off);

            offices.remove(off);
            offices.add(i, off);
        });
    }
    public void removeOffice(Office off) {
        offices.remove(off);
    }
    public ObservableList<Office> getOffices() {
        return this.offices;
    }

    public Boolean containsOffice(Office off) {
        return offices.contains(off);
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
        if(currentKiosk!=null)
        {
            currentKiosk.setType(NodeType.Kiosk);
        }

        kiosk.setType(NodeType.CurrentKiosk);

        this.currentKiosk = kiosk;

        try {
            new DatabaseManager().updateCurKiosk(kiosk);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
