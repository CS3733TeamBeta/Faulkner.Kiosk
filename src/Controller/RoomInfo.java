package Controller;

import Domain.Map.Doctor;

import java.util.HashSet;

/**
 * Created by jw97 on 2/3/2017.
 */
public class RoomInfo
{
    String roomNum;
    HashSet<Doctor> doctors = new HashSet<Doctor>();

    public RoomInfo(String roomNum) {
        this.roomNum = roomNum;
        this.doctors.clear();
    }

    public int changeRoomNum(String newRoomNum) {
        this.roomNum = newRoomNum;
        return 0;
    }

    public int addDoctor(Doctor doctor) {
        if (this.doctors.add(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public int removeDoctor(Doctor doctor) {
        if (this.doctors.remove(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public HashSet<Doctor> getDoctorsList() {
        return this.doctors;
    }
}
