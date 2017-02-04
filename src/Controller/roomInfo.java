package Controller;

import java.util.LinkedList;

/**
 * Created by jw97 on 2/3/2017.
 */
public class roomInfo {
    String roomNum;
    LinkedList<doctorProfile> doctors;

    public roomInfo(String roomNum) {
        this.roomNum = roomNum;
        doctors = new LinkedList<doctorProfile>();
    }

    public int changeRoomNum(String newRoomNum) {
        this.roomNum = newRoomNum;
        return 0;
    }

    public int addDoctor(doctorProfile doctor) {
        // Checks if the doctor is currently in the room
        if (this.doctors.contains(doctor)) {
            return 1; // Exception
        }

        this.doctors.add(doctor);
        return 0;
    }

    public int removeDoctor(doctorProfile doctor) {
        // Checks if the doctor is currently in the room
        if (this.doctors.contains(doctor)) {
            this.doctors.remove(doctor);
        } else {
            return 1; // Exception
        }

        return 0;
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public LinkedList<doctorProfile> getDoctorsList() {
        return this.doctors;
    }
}
