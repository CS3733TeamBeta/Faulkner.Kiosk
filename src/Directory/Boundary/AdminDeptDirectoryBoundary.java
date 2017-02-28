package Directory.Boundary;

import Map.Entity.Destination;
import Map.Entity.Hospital;
import Map.Entity.Office;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDeptDirectoryBoundary extends AbstractDirectoryBoundary {

    public AdminDeptDirectoryBoundary(Hospital h) {
        super(h);
    }

    public ObservableList<Office> getDepartments() {
        return h.getOffices();
    }

    public FilteredList<Office> setSearchList(String newValue) {
        return setSearchList(h.getOffices(), newValue);
    }

    public void addDept(Office office) {
        h.addOffice(office);
        save();
    }

    public void removeDept(Office office) {
        h.removeOffice(office);
        save();
    }

    public void editDept(Office office, String name, Destination destination) {
        if (h.containsOffice(office)) {

            if (!office.getName().equals(name)) {
                office.setName(name);
            }

            if (office.getDestination() != destination) {
                office.setDestination(destination);
            }
        }

        save();
    }
  }
