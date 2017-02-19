package Domain.Map;

import Controller.Admin.PopUp.OfficeEditController;
import org.controlsfx.control.PopOver;

import java.util.HashSet;
import java.util.UUID;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office extends Destination
{
    UUID id;
    String name;
    Suite suite;

    HashSet<Doctor> occupants;
    protected String department;
    private final String popOverEditFXML = "/Admin/Popup/OfficeEditPopup.fxml";

    public Office(String name, Suite suite) {
        this.name = name;
        this.suite = suite;
    }

    public Office(UUID id, String name, Suite suite) {
        this.id = id;
        this.name = name;
        this.suite = suite;
    }

    public Office()
    {
        super();
        this.myInfo.setName("Office");
    }

    public String getName() {
        return this.name;
    }

    public Suite getSuite() {
        return this.suite;
    }

    @Override
    public PopOver getEditPopover()
    {
        OfficeEditController controller = new OfficeEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }
    public UUID getId() {
        return this.id;
    }

    public HashSet<Doctor> getOccupants() {
        return this.occupants;
    }

    public void setOccupants(HashSet<Doctor> doctors) {
        this.occupants = doctors;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDepartment()
    {
        return this.department;
    }
}
