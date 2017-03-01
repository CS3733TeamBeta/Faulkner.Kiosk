package Directory.Boundary;

import Directory.Entity.Doctor;
import Map.Entity.Hospital;
import Map.Entity.Office;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 3/1/2017.
 */
public class UserDirectoryBoundary extends AbstractDirectoryBoundary {

    public UserDirectoryBoundary(Hospital h) {
        super(h);
    }

    public FilteredList<Doctor> setSearchListForDoc(String newValue) {
        return setSearchList(h.getDoctors(), newValue);
    }

    public FilteredList<Office> setSearchListForDept(String newValue) {
        return setSearchList(h.getOffices(), newValue);
    }
}
