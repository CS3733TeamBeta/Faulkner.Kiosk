package Domain.Map;

import java.util.HashSet;

/**
 *  Office is a particular type of destination that is a doctor's office
 */
public class Office extends Destination
{
    int id;
    HashSet<Doctor> occupants;

}
