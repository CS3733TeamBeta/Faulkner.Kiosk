package main.Directory.Boundary;

import main.Directory.Boundary.AbstractDirectoryBoundary;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Hospital;
import main.Map.Entity.Office;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 3/1/2017.
 */
public class UserDirectoryBoundary extends AbstractDirectoryBoundary
{

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
