package main.Directory;

import main.Application.Exceptions.AddFoundException;
import main.Application.Exceptions.RemoveNotFoundException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;

/**
 * Created by jw97 on 2/3/2017.
 */
public class RoomInfo
{
    StringProperty roomNum;
    HashSet<DoctorProfile> doctors = new HashSet<DoctorProfile>();

    public RoomInfo(String roomNum) {
        this.roomNum = new SimpleStringProperty(roomNum);;
        this.doctors.clear();
    }

    public void changeRoomNum(String newRoomNum) {
        this.roomNum.set(newRoomNum);
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
        return this.roomNum.get();
    }

    public HashSet<DoctorProfile> getDoctorsList() {
        return this.doctors;
    }
}
