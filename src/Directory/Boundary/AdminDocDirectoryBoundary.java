package Directory.Boundary;

import Directory.Entity.Doctor;
import Map.Entity.Destination;
import Map.Entity.Hospital;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDocDirectoryBoundary extends AbstractDirectoryBoundary {
    public AdminDocDirectoryBoundary(Hospital h) {
        super(h);
    }

    public FilteredList<Doctor> setSearchList(String newValue) {
        return setSearchList(h.getDoctors(), newValue);
    }

    public ObservableList<Doctor> getDoctors() {
        return h.getDoctors();
    }

    public void addDoctor (Doctor d) {
        h.addDoctor(d);
        save();
    }

    public void removeDoctor(Doctor d) {
        h.removeDoctor(d);
        save();
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

        save();
    }

    public ObservableList<Destination> getDocLoc(Doctor d) {
        ObservableList<Destination> locations;

        locations = d.getDestinations();

        return locations;
    }

}

