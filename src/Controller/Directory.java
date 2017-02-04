package Controller;

import java.util.HashMap;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashMap<doctorProfile, roomInfo> doctors = new HashMap<doctorProfile, roomInfo>();

    public Directory() {
        doctors.clear();
    }

    public int addToDirectory(doctorProfile doctor, roomInfo room) {
        if (this.doctors.containsKey(doctor)){
            return 1; // Exception
        }

        this.doctors.put(doctor, room);
        return 0;
    }

    public int removeFromDirectory(doctorProfile doctor) {
        if (this.doctors.containsKey(doctor)) {
            this.doctors.remove(doctor);
            return 0;
        }

        return 1; // Exception
    }
}
