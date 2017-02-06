package Model;

import Model.RoomInfo;
import Model.RoomList;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;
import Exceptions.RoomNotFoundException;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.HashSet;

/**
 * Created by jw97 on 2/2/2017.
 */

public class DoctorProfile
{
    private StringProperty firstName;
    private StringProperty lastName;
    private RoomInfo room;
    // Should I include roomNum, phoneNum, etc.
    private HashSet<StringProperty> departments = new HashSet<StringProperty>();

    public DoctorProfile(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);;
        this.departments.clear();
    }

    // Would it be relevant to have methods that change the doctor's first and last name?

    public void addDepartment(String department) throws AddFoundException {
        StringProperty dept = new SimpleStringProperty(department);

        if (this.departments.add(dept)) {
            return;
        }

        throw new AddFoundException();
    }

    public void removeDepartment(String department) throws RemoveNotFoundException {
        StringProperty dept = new SimpleStringProperty(department);

        if (this.departments.remove(dept)) {
            return;
        }

        throw new RemoveNotFoundException();
    }

    public void assignRoom(String roomNum) {
        try {
            this.room.addDoctor(this);
        } catch (AddFoundException e) {
            System.out.println("This room is already assigned to this room.");
        }
    }

    public String getFirstName() {
        return this.firstName.get();
    }

    public String getLastName() {
        return this.lastName.get();
    }

    public HashSet<StringProperty> getDepartments() {
        return this.departments;
    }

    public RoomInfo getRoomNum() {
        return this.room;
    }
}
