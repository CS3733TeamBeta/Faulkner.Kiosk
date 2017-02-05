package Model;

import Model.DoctorProfile;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;

import java.util.HashSet;

/**
 * Created by jw97 on 2/3/2017.
 */
public class RoomInfo
{
    String roomNum;
    HashSet<DoctorProfile> doctors = new HashSet<DoctorProfile>();

    public RoomInfo(String roomNum) {
        this.roomNum = roomNum;
        this.doctors.clear();
    }

    public void changeRoomNum(String newRoomNum) {
        this.roomNum = newRoomNum;
    }

    public void addDoctor(DoctorProfile doctor) throws AddFoundException {
        if (this.doctors.add(doctor)) {
            return;
        }

        throw new AddFoundException();
    }

    public void removeDoctor(DoctorProfile doctor) throws RemoveNotFoundException {
        if (this.doctors.remove(doctor)) {
            return;
        }

        throw new RemoveNotFoundException();
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public HashSet<DoctorProfile> getDoctorsList() {
        return this.doctors;
    }
}
