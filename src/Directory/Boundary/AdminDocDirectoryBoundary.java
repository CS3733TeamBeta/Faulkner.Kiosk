package Directory.Boundary;

import Directory.Entity.Doctor;
import Map.Entity.Destination;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDocDirectoryBoundary extends AbstractDirectoryBoundary {
    private ObservableList<Doctor> doctors = h.getDoctors();

    public AdminDocDirectoryBoundary(){}

    public FilteredList<Doctor> setSearchList(String newValue) {
        return setSearchList(doctors, newValue);
    }

    public ObservableList<Doctor> getDoctors() {
        return doctors;
    }

    public void addDoctor (Doctor d) {
        doctors.add(d);
        save();
    }

    public void removeDoctor(Doctor d) {
        doctors.remove(d);
        save();
    }

    public void editDoctor(Doctor d, String name, String description, String hrs,
                           ObservableList<Destination> destinations, String phoneNum) {
        if (doctors.contains(d)) {
            Doctor newDoc = new Doctor(d.getDocID(), name, description, hrs, destinations);
            newDoc.setPhoneNum(phoneNum);
            doctors.add(newDoc);
        }

        save();
    }

    public ObservableList<Destination> getDocLoc(Doctor d) {
        ObservableList<Destination> locations;

        locations = d.getDestinations();

        return locations;
    }

}

