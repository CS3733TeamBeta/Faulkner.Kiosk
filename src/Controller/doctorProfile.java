package Controller;

import java.util.HashSet;

/**
 * Created by jw97 on 2/2/2017.
 */

public class doctorProfile {
    private String firstName;
    private String lastName;
    private roomInfo room;
    // Should I include roomNum, phoneNum, etc.
    private HashSet<String> departments = new HashSet<String>();

    public doctorProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        room = new roomInfo("00");
        this.departments.clear();
    }

    // Would it be relevant to have methods that change the doctor's first and last name?

    public int addDepartment(String department) {
        if (this.departments.add(department)) {
            return 0;
        } else {
            return 1;
        }
    }

    public int removeDepartment(String department) {
        if (this.departments.remove(department)) {
            return 0;
        } else {
            return 1;
        }
    }

    public int assignRoom(String roomNum) {
        return this.room.changeRoomNum(roomNum);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public HashSet<String> getDepartments() {
        return this.departments;
    }

    public roomInfo getRoomNum() {
        return this.room;
    }
}
