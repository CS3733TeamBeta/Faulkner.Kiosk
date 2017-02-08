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

    public DoctorProfile(String firstName, String lastName, String room) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.room = new RoomInfo(room);
        try {
            this.room.addDoctor(this);
        } catch (AddFoundException e) {
            System.out.println("This doctor is already assigned to this room.");
        }
        this.departments.clear();
    }

    // Would it be relevant to have methods that change the doctor's first and last name?

    public void addDepartment(StringProperty department) throws AddFoundException {
        if (this.departments.add(department)) {
            return;
        }

        throw new AddFoundException();
    }

    public void removeDepartment(StringProperty department) throws RemoveNotFoundException {
        if (this.departments.remove(department)) {
            return;
        }

        throw new RemoveNotFoundException();
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
