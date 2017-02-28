package Directory.Boundary;

import Application.Database.DataCache;
import Directory.Entity.Doctor;
import Map.Entity.Hospital;
import Map.Entity.Office;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.Predicate;

/**
 * Created by jw97 on 2/26/2017.
 */
public class AbstractDirectoryBoundary {
    protected Hospital h = DataCache.getInstance().getHospital();

    AbstractDirectoryBoundary() {
    }

    public FilteredList setSearchList(ObservableList<?> list, String newValue) {
        FilteredList filteredList = new FilteredList<>(list);

        if (filteredList.getClass().equals(Doctor.class)) {
            filteredList.setPredicate((Predicate<? super Doctor>) d -> filterMatches(this, newValue));
        } else {
            filteredList.setPredicate((Predicate<? super Office>) o -> filterMatches(this, newValue));
        }

        return filteredList;
    }

    public boolean filterMatches(Object obj, String newValue) {
        // By default, the entire directory is displayed
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }

        String lowerCaseFilter = newValue.toLowerCase();

        // Checks if filter matches
        if (obj.getClass().getName().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }

        // Filter does not match
        return false;
    }

    // Save to database
    public void save() {
        DataCache.getInstance().save();
    }
}
