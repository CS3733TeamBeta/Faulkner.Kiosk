package Domain.Map;


import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;

    private HashMap<String, Doctor> doctors;
    private HashMap<String, Suite> suites;
    private HashMap<String, Office> offices;

    public Hospital() {
        buildings = new HashSet<Building>();

        doctors = new HashMap<>();
        suites = new HashMap<>();
        offices = new HashMap<>();
    }

    public void addBuilding(Building b) {
        buildings.add(b);
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
    public void removeDoctors(String s){
        doctors.remove(s);
    }


    //Alter Suite HashMap: suites
    public HashMap<String, Suite> getSuites() {
        return suites;
    }
    public void addSuites(String s, Suite suite) {
        suites.put(s, suite);
    }
    public void setSuites(HashMap<String, Suite> suites) {
        this.suites = suites;
    }
    public void removeSuites(String s){
        suites.remove(s);
    }
    public boolean containsBuilding(Building b)
    {
        return buildings.contains(b);
    }

    //Alter Office HashMap: offices
    public void addOffices(String s, Office off) {
        offices.put(s, off);
    }
    public HashMap<String, Office> getOffices() {
        return this.offices;
    }

    public int buildingCount()
    {
        return buildings.size();
    }

    public Collection<Building> getBuildings()
    {
        return buildings;
    }

}
