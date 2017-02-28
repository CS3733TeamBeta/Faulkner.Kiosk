package Directory.Boundary;

import Map.Entity.Destination;
import Map.Entity.Office;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDeptDirectoryBoundary extends AbstractDirectoryBoundary {
    protected ObservableList<Office> departments = h.getOffices();

    public AdminDeptDirectoryBoundary() {}

    public ObservableList<Office> getDepartments() {
        return this.departments;
    }

    public FilteredList<Office> setSearchList(String newValue) {
        return setSearchList(departments, newValue);
    }

    public void addDept(Office office) {
        this.departments.add(office);
        save();
    }

    public void removeDept(Office office) {
        this.departments.remove(office);
        save();
    }

    public void editDept(Office office, String name, Destination destination) {
        if (this.departments.contains(office)) {

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
