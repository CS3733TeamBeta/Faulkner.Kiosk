package main.Directory.Boundary;

import main.Application.ApplicationController;
import main.Map.Entity.Destination;
import main.Map.Entity.Hospital;
import main.Map.Entity.Office;
import javafx.collections.transformation.FilteredList;

import java.sql.SQLException;

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

        try {
            ApplicationController.getCache().getDbManager().addOfficeToDB(office);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDept(Office office) {
        h.removeOffice(office);

        try {
            ApplicationController.getCache().getDbManager().delOfficeFromDB(office);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  }
