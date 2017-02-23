package Domain.Map;

import Controller.Admin.PopUp.DestinationEditController;
import Controller.Admin.PopUp.KioskEditController;
import Domain.ViewElements.DragIconType;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;

import java.util.HashSet;
import java.util.UUID;

/**
 * Destination is a type of node that you'd want to navigate to
 */

public class Kiosk extends MapNode {

    UUID destUID; //Thing specific for the destination - brandon

    protected String description;
    protected String hours;

    String name;

    Image icon;
    Image destinationView;

    String floorID;

    private final String popOverEditFXML = "/Admin/Popup/KioskEditPopup.fxml";

    /**
     *  Creates a new Destination with an new empty info
     */
    public Kiosk() {
        System.out.println("init kiosk");
        this.destUID = UUID.randomUUID();
    }

    public Kiosk(String name) {
        this.name = name;
    }

    //Creates a destination from a map node
    public Kiosk(UUID uuid, Kiosk m, String name, String floor)
    {
        this.destUID = uuid;
        this.name = name;
        this.floorID = floor;

        this.setPosX(m.getPosX());
        this.setPosY(m.getPosY());

        this.setType(m.getIconType());

        this.nodeUID = m.getNodeID();
    }


    //Returns a pop over window to edit this node
    /**
     * God knows what this does. @TODO Ben add what this does.
     */
    @Override
    public PopOver getEditPopover() {
        System.out.println("GETTING POPOVER");
        KioskEditController controller = new KioskEditController(this);
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


    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getFloorID() {
        return this.floorID;
    }

}
