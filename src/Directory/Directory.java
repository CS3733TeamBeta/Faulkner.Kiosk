package Directory;

import java.util.HashSet;
import Application.Exceptions.AddFoundException;
import Application.Exceptions.RemoveNotFoundException;

/**
 * Created by jw97 on 2/3/2017.
 */
public class Directory {
    HashSet<DoctorProfile> doctors = new HashSet<DoctorProfile>();

    public Directory() {
        doctors.clear();
    }

    public void addToDirectory(DoctorProfile doctor) throws AddFoundException {
        if (this.doctors.add(doctor)){
            return;
        }

        throw new AddFoundException(); // Exception
    }

    public void removeFromDirectory(DoctorProfile doctor) throws RemoveNotFoundException {
        if (this.doctors.remove(doctor)) {
            return;
        }

        throw new RemoveNotFoundException();
    }
}
