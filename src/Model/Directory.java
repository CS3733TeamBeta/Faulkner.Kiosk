package Model;

import java.util.HashMap;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashMap<DoctorProfile, RoomInfo> doctors = new HashMap<DoctorProfile, RoomInfo>();

    public Directory() {
        doctors.clear();
    }

    public void addToDirectory(DoctorProfile doctor, RoomInfo room) throws AddFoundException {
        if (this.doctors.containsKey(doctor)){
            throw new AddFoundException(); // Exception
        }

        this.doctors.put(doctor, room);
    }

    public void removeFromDirectory(DoctorProfile doctor) throws RemoveNotFoundException {
        if (this.doctors.containsKey(doctor)) {
            this.doctors.remove(doctor);
        }

        throw new RemoveNotFoundException();
    }
}
