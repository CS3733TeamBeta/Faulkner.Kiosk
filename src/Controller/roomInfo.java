package Controller;

import Controller.Admin.doctorProfile;

import java.util.HashSet;

/**
 * Created by jw97 on 2/3/2017.
 */
public class roomInfo {
    String roomNum;
    HashSet<doctorProfile> doctors = new HashSet<doctorProfile>();

    public roomInfo(String roomNum) {
        this.roomNum = roomNum;
        this.doctors.clear();
    }

    public int changeRoomNum(String newRoomNum) {
        this.roomNum = newRoomNum;
        return 0;
    }

    public int addDoctor(doctorProfile doctor) {
        if (this.doctors.add(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public int removeDoctor(doctorProfile doctor) {
        if (this.doctors.remove(doctor)) {
            return 0;
        } else {
            return 1; // Exception
        }
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public HashSet<doctorProfile> getDoctorsList() {
        return this.doctors;
    }
}
