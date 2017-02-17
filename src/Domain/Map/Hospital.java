package Domain.Map;

import java.util.HashSet;
import java.util.HashMap;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;

    private HashMap<Integer, MapNode> mapNodes;
    private HashMap<Integer, NodeEdge> edges;
    private HashMap<String, Doctor> doctors;
    private HashMap<Integer, Floor> floors;
    private HashMap<String, Suite> suites;

    public Hospital() {
        buildings = new HashSet<Building>();

        mapNodes = new HashMap<>();
        edges = new HashMap<>();
        doctors = new HashMap<>();
        floors = new HashMap<>();
        suites = new HashMap<>();
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    //Alter MapNode HashMap: mapNodes
    public HashMap<Integer, MapNode> getMapNodes(){
        return mapNodes;
    }
    public void addMapNodes(Integer k, MapNode node) {
        mapNodes.put(k, node);
    }
    public void setMapNodes(HashMap<Integer, MapNode> mapNodes) {
        this.mapNodes = mapNodes;
    }
    public void removeMapNodes(Integer k){
        mapNodes.remove(k);
    }

    //Alter NodeEdge HashMap: edges
    public HashMap<Integer, NodeEdge> getEdges() {
        return edges;
    }
    public void addEdges(Integer k, NodeEdge edge) {
        edges.put(k, edge);
    }
    public void setEdges(HashMap<Integer, NodeEdge> edges) {
        this.edges = edges;
    }
    public void removeNodeEdge(Integer k){
        edges.remove(k);
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


    //Alter Floor HashMap: floors
    public HashMap<Integer, Floor> getFloors() {
        return floors;
    }
    public void addFloors(Integer k, Floor floor){
        floors.put(k, floor);
    }
    public void setFloors(HashMap<Integer, Floor> floors) {
        this.floors = floors;
    }
    public void removeFloors(Integer k){
        floors.remove(k);
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
}
