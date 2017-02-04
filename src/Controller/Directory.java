package Controller;

import java.util.HashSet;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashSet<doctorProfile> doctors;

    public Directory() {
        doctors.clear();
    }

    public int addToDirectory(doctorProfile doctor) {
        if (this.doctors.add(doctor)) {
            return 0; // Adds the doctor to the directory
        } else {
            return 1; // Exception
        }
    }

    public int removeFromDirectory(doctorProfile doctor) {
        if (this.doctors.remove(doctor)) {
            return 0; // Removes the doctor from the director if it is present
        } else {
            return 1; // Exception
        }
    }
}
