package Controller;

import Model.DoctorProfile;

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

    public int changeRoomNum(String newRoomNum) {
        this.roomNum = newRoomNum;
        return 0;
    }

    public int addDoctor(DoctorProfile doctor) {
        if (this.doctors.add(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public int removeDoctor(DoctorProfile doctor) {
        if (this.doctors.remove(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public HashSet<DoctorProfile> getDoctorsList() {
        return this.doctors;
    }
}
