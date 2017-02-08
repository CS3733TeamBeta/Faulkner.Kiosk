package Domain.Map;

import java.util.HashSet;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office extends Destination
{
    int id;

    HashSet<Doctor> occupants;

    public Office(int id, HashSet<Doctor> doctors) {
        this.id = id;
        this.occupants = doctors;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashSet<Doctor> getOccupants() {
        return this.occupants;
    }

    public void setOccupants(HashSet<Doctor> doctors) {
        this.occupants = doctors;
    }

}
