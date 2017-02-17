package Domain.Map;

import Controller.Admin.PopUp.OfficeEditController;
import org.controlsfx.control.PopOver;

import java.util.HashSet;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office extends Destination
{
    int id;

    HashSet<Doctor> occupants;
    protected String department;
    private final String popOverEditFXML = "/Admin/Popup/OfficeEditPopup.fxml";

    public Office(int id, HashSet<Doctor> doctors) {
        super();

        this.id = id;
        this.occupants = doctors;
    }

    public Office()
    {
        super();
        this.myInfo.setName("Office");
    }

    @Override
    public PopOver getEditPopover()
    {
        OfficeEditController controller = new OfficeEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
