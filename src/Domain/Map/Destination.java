package Domain.Map;

import Controller.Admin.PopUp.DestinationEditController;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Destination is a type of node that you'd want to navigate to
 */

public class Destination extends MapNode {
    UUID destUID;
    protected Info myInfo;
    Image icon;
    Image destinationView;
    ArrayList<Object> occupants;
    private final String popOverEditFXML = "/Admin/Popup/DestinationEditPopup.fxml";

    /**
     *  Creates a new Destination with an new empty info
     */
    public Destination() {
        this.destUID = UUID.randomUUID();
        myInfo = new Info();
        myInfo.setName("Node");
    }
    public Destination(String name) {
        myInfo = new Info();
        myInfo.setName(name);
    }


    /**
     * Returns the info of this Destination
     * @return Info of this Destination
     */
    public Info getInfo()
    {
        return myInfo;
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
    public String toString()
    {
        return myInfo.getName();
    }
}
