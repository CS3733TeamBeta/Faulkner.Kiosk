package main.Directory.Boundary;

import main.Directory.Boundary.AbstractDirectoryBoundary;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Destination;
import main.Map.Entity.Hospital;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDocDirectoryBoundary extends AbstractDirectoryBoundary
{
    public AdminDocDirectoryBoundary(Hospital h) {
        super(h);
    }

    public FilteredList<Doctor> setSearchList(String newValue) {
        return setSearchList(h.getDoctors(), newValue);
    }

    public void addDoctor (Doctor d) {
        h.addDoctor(d);
    }

    public void removeDoctor(Doctor d) {
        h.removeDoctor(d);
    }

    public void editDoctor(Doctor d, String name, String description, String hrs,
                           ObservableList<Destination> destinations, String phoneNum) {
        if (h.containsDoctor(d)) {
            d.setName(name);
            d.setDescription(description);
            d.setHours(hrs);
            d.setDestinations(destinations);
            d.setPhoneNum(phoneNum);
        }

    }

    public ObservableList<Destination> getDocLoc(Doctor d) {
        ObservableList<Destination> locations;

        locations = d.getDestinations();

        return locations;
    }

}

