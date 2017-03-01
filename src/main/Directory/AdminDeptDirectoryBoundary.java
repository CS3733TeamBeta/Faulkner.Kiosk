package main.Directory;

import main.Map.Entity.Destination;
import main.Map.Entity.Hospital;
import main.Map.Entity.Office;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDeptDirectoryBoundary extends AbstractDirectoryBoundary {

    public AdminDeptDirectoryBoundary(Hospital h) {
        super(h);
    }

    public FilteredList<Office> setSearchList(String newValue) {
        return setSearchList(h.getOffices(), newValue);
    }

    public void addDept(Office office) {
        h.addOffice(office);
    }

    public void removeDept(Office office) {
        h.removeOffice(office);
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

    }
  }
