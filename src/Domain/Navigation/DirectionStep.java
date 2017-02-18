package Domain.Navigation;

import Domain.Map.Floor;

import java.util.LinkedList;

/**
 * Created by Pattop on 2/16/2017.
 */
public class DirectionStep {
    Floor floorOfTheseDirections;
    LinkedList<String> directionsForThisFloor;

    public DirectionStep(Floor floor){
        floorOfTheseDirections = floor;
        directionsForThisFloor = new LinkedList<String>();
    }

    public void addDirections(String newDirection){
        this.directionsForThisFloor.add(newDirection);
    }

    public void setDirections(LinkedList<String> directions){
        this.directionsForThisFloor.clear();
        for(String s : directions){
            this.directionsForThisFloor.add(s);
        }
    }

    public LinkedList<String> getDirections(){
        return directionsForThisFloor;
    }

}
