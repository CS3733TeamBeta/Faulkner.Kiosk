package Model;

import Model.RoomInfo;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;

import java.util.HashSet;

/**
 * Created by jw97 on 2/2/2017.
 */

public class DoctorProfile
{
    private String firstName;
    private String lastName;
    private RoomInfo room;
    // Should I include roomNum, phoneNum, etc.
    private HashSet<String> departments = new HashSet<String>();

    public DoctorProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.departments.clear();
    }

    // Would it be relevant to have methods that change the doctor's first and last name?

    public void addDepartment(String department) throws AddFoundException {
        if (this.departments.add(department)) {
            return;
        }

        throw new AddFoundException();
    }

    public void removeDepartment(String department) throws RemoveNotFoundException {
        if (this.departments.remove(department)) {
            return;
        }

        throw new RemoveNotFoundException();
    }

    public void assignRoom(String roomNum) {
        this.room = new RoomInfo(roomNum);

        try {
            this.room.addDoctor(this);
        } catch (AddFoundException e) {
            System.out.println("This room is already assigned to this room.");
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String fullName() {
        return this.lastName + ", " + this.firstName;
    }

    public HashSet<String> getDepartments() {
        return this.departments;
    }

    public RoomInfo getRoomNum() {
        return this.room;
    }
}
