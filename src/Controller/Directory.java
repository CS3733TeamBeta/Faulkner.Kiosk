package Controller;

import Domain.Navigation.Path;

import java.util.LinkedList;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    LinkedList<doctorProfile> doctors;

    public Directory() {
        doctors = new LinkedList<doctorProfile>();
    }

    public int addToDirectory(doctorProfile doctor) {
        this.doctors.add(doctor);

        return 0;
    }

    public int removeFromDirectory(doctorProfile doctor) {
        if (this.doctors.contains(doctor)) {
            this.doctors.remove(doctor);
        } else {
            return 1; // Exception
        }

        return 0;
    }
}
