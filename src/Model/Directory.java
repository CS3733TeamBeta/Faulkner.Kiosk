package Model;

import Model.DoctorProfile;
import Model.RoomInfo;

import java.util.HashMap;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashMap<DoctorProfile, RoomInfo> doctors = new HashMap<DoctorProfile, RoomInfo>();

    public Directory() {
        doctors.clear();
    }

    public int addToDirectory(DoctorProfile doctor, RoomInfo room) {
        if (this.doctors.containsKey(doctor)){
            return 1; // Exception
        }

        this.doctors.put(doctor, room);
        return 0;
    }

    public int removeFromDirectory(DoctorProfile doctor) {
        if (this.doctors.containsKey(doctor)) {
            this.doctors.remove(doctor);
            return 0;
        }

        return 1; // Exception
    }
}
