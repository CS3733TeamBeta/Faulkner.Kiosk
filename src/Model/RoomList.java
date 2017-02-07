package Model;

import java.util.HashMap;
import javafx.beans.property.StringProperty;
import Exceptions.AddFoundException;
import Exceptions.RemoveNotFoundException;

/**
 * Created by jw97 on 2/5/2017.
 */
public class RoomList {
    HashMap<StringProperty, RoomInfo> hospitalRooms = new HashMap<StringProperty, RoomInfo>();

    public RoomList() {
        hospitalRooms.clear();
    }

    public void addRoom(StringProperty roomNum, RoomInfo info) throws AddFoundException {
        if (this.hospitalRooms.containsKey(roomNum)) {
            throw new AddFoundException();
        }

        this.hospitalRooms.put(roomNum, info);
    }

    public void removeRoom(StringProperty roomNum) throws RemoveNotFoundException {
        if (this.hospitalRooms.containsKey(roomNum)) {
            this.hospitalRooms.remove(roomNum);
        }

        throw new RemoveNotFoundException();
    }

    public Boolean hospitalHasRoom(StringProperty roomNum) {
        return this.hospitalRooms.containsKey(roomNum);
    }
}
