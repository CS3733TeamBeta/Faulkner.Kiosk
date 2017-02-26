package Entity.Map;

import Controller.Map.Admin.PopUp.DestinationEditController;
import Controller.Map.ViewElements.DragIconType;
import Entity.Doctor;
import org.controlsfx.control.PopOver;

import java.util.HashSet;
import java.util.UUID;

/**
 * Destination is a type of node that you'd want to navigate to
 */

public class Destination extends MapNode {

    UUID destUID; //Thing specific for the destination - brandon

    String name;

    UUID floorID;

    HashSet<Doctor> doctors;
    HashSet<Office> offices;

    private final String popOverEditFXML = "/Admin/Popup/DestinationEditPopup.fxml";

    /**
     *  Creates a new Destination with an new empty info
     */
    public Destination() {
        this.destUID = UUID.randomUUID();
    }

    public Destination(String name) {
        this.name = name;
    }

    //Creates a destination from a map node
    public Destination(UUID uuid, MapNode m, String name, UUID floor)
    {
        this.destUID = uuid;
        this.name = name;
        this.floorID = floor;

        this.setPosX(m.getPosX());
        this.setPosY(m.getPosY());

        this.setType(m.getType());

        this.nodeUID = m.getNodeID();
        this.doctors = new HashSet<>();
        this.offices = new HashSet<>();
    }


    //Returns a pop over window to edit this node
    /**
     * God knows what this does. @TODO Ben add what this does.
     */
    @Override
    public PopOver getEditPopover() {
        DestinationEditController controller = new DestinationEditController(this);
        return getPopOver(controller, popOverEditFXML);
    }

    @Override
    public void setType(DragIconType type)
    {
        super.setType(type);
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public UUID getDestUID() {
        return this.destUID;
    }

    public void addOffice(Office o) {
        offices.add(o);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public UUID getFloorID() {
        return this.floorID;
    }

    public void addDoctor(Doctor d) {
        doctors.add(d);
    }

    @Override
    public String getLabel()
    {
        return name;
    }
}
