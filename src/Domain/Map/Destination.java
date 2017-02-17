package Domain.Map;

import Controller.Admin.PopUp.AbstractPopupController;
import Controller.Admin.PopUp.DestinationEditController;
import Domain.ViewElements.Events.DeleteRequestedEvent;
import Domain.ViewElements.Events.DeleteRequestedHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Destination is a type of node that you'd want to navigate to
 */

public class Destination extends MapNode {
    protected Info myInfo;
    Image icon;
    Image destinationView;
    private final String popOverEditFXML = "/Admin/Popup/DestinationEditPopup.fxml";

    public Destination() {
        myInfo = new Info();
        myInfo.setName("Node");
    }

    public Info getInfo()
    {
        return myInfo;
    }

    /**Returns a pop over window to edit this node**/
    @Override
    public PopOver getEditPopover()
    {
        DestinationEditController controller = new DestinationEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }

    @Override
    public String toString()
    {
        return myInfo.getName();
    }
}
