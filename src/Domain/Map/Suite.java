package Domain.Map;

import Controller.Admin.PopUp.SuiteEditController;
import org.controlsfx.control.PopOver;

import java.util.UUID;

/**
 * Created by Brandon on 2/13/2017.
 */
public class Suite extends MapNode {

    UUID suiteID;
    String name;
    MapNode location;

    private final String popOverEditFXML = "/Admin/Popup/SuiteEditPopup.fxml";

    public Suite() {
        super();
        this.setName("Suite");
        this.suiteID = UUID.randomUUID();

    }

    public Suite(UUID uuid, String name) {
        this.suiteID = uuid;
        this.name = name;
    }

//    public Suite(String name, MapNode location) {
//        this.suiteID = UUID.randomUUID();
//        this.name = name;
//        this.location = location;
//    }
//
//    public Suite(UUID suiteID, String name, MapNode location) {
//        this.suiteID = suiteID;
//        this.name = name;
//        this.location = location;
//    }

    @Override
    public PopOver getEditPopover()
    {
        SuiteEditController controller = new SuiteEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }

    public UUID getSuiteID() {
        return this.suiteID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MapNode getLocation() {
        return this.location;
    }

}
