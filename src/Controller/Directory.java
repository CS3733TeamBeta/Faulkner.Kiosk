package Controller;

import Domain.Map.*;

import java.util.HashMap;
import Controller.Admin.*;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashMap<Doctor, MapEditorController> doctors = new HashMap<Doctor, MapEditorController>();

    public Directory() {
        doctors.clear();
    }

    public int addToDirectory(Doctor doctor, MapEditorController room) {
        if (this.doctors.containsKey(doctor)){
            return 1; // Exception
        }

        this.doctors.put(doctor, room);
        return 0;
    }

    public int removeFromDirectory(Doctor doctor) {
        if (this.doctors.containsKey(doctor)) {
            this.doctors.remove(doctor);
            return 0;
        }

        return 1; // Exception
    }
}
