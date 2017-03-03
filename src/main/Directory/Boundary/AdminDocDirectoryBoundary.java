package main.Directory.Boundary;

import javafx.collections.transformation.FilteredList;
import main.Application.ApplicationController;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Hospital;

import java.sql.SQLException;

/**
 * Created by jw97 on 2/25/2017.
 */
public class AdminDocDirectoryBoundary extends AbstractDirectoryBoundary
{
    public AdminDocDirectoryBoundary(Hospital h) {
        super(h);
    }

    public FilteredList<Doctor> setSearchList(String newValue) {
        return setSearchList(h.getDoctors(), newValue);
    }

    public void addDoc (Doctor d) {
        h.addDoctor(d);
    }

    public void removeDoctor(Doctor d) {
        h.removeDoctor(d);

        try {
            ApplicationController.getCache().getDbManager().delDocFromDB(d);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

