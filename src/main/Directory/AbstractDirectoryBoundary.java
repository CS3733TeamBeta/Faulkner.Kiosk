package main.Directory;

import main.Application.Database.DataCache;
import main.Directory.Doctor;
import main.Map.Entity.Hospital;
import main.Map.Entity.Office;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.Predicate;

/**
 * Created by jw97 on 2/26/2017.
 */
public class AbstractDirectoryBoundary {
    protected Hospital h;

    AbstractDirectoryBoundary(Hospital h) {
        this.h = h;
    }

    public FilteredList setSearchList(ObservableList<?> list, String newValue) {
        FilteredList filteredList = new FilteredList<>(list);

        if (filteredList.get(0) instanceof Doctor) {
            filteredList.setPredicate((Predicate<? super Doctor>) d -> filterMatches(d.getName(), newValue));
        } else if (filteredList.get(0) instanceof Office){
            filteredList.setPredicate((Predicate<? super Office>) o -> filterMatches(o.getName(), newValue));
        }

        return filteredList;
    }

    public boolean filterMatches(String name, String newValue) {
        // By default, the entire directory is displayed
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }

        String lowerCaseFilter = newValue.toLowerCase();

        // Checks if filter matches
        if (name.toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }

        // Filter does not match
        return false;
    }

    public ObservableList<Doctor> getDoctors() {
        return h.getDoctors();
    }

    public ObservableList<Office> getDepartments() {
        return h.getOffices();
    }

    // Save to database
    public void save() {
        DataCache.getInstance().save();
    }
}
