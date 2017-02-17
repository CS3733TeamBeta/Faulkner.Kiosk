package Domain.Map;

import Controller.Admin.PopUp.OfficeEditController;
import org.controlsfx.control.PopOver;

import java.util.HashSet;

/**
 *  Office is a particular type of destination that is a doctor's office
 */

public class Office extends Destination {
    int id;

    HashSet<Doctor> occupants;
    protected String department;
    private final String popOverEditFXML = "/Admin/Popup/OfficeEditPopup.fxml";

    /**
     * Creates a new Office with the given ID and the given doctors
     * @param id as an integer
     * @param doctors as a HashSet<Doctor>
     */
    public Office(int id, HashSet<Doctor> doctors) {
        super();

        this.id = id;
        this.occupants = doctors;
    }

    /**
     * Create an empty office
     */
    public Office()
    {
        super();
        this.myInfo.setName("Office");
    }

    /**
     * Retrieves the popover contained within this?
     * @TODO Make this comment
     * @return
     */
    @Override
    public PopOver getEditPopover() {
        OfficeEditController controller = new OfficeEditController(this);
        return getPopOver(controller, popOverEditFXML);
    }

    /**
     * Retrieves this office's ID
     * @return this office's ID, as an int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets this office's ID to the given ID
     * @param id as an int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the occupants of this office as a collection of doctors
     * @return the doctors in this office, as a HashSet<Doctor>
     */
    public HashSet<Doctor> getOccupants() {
        return this.occupants;
    }

    /**
     * Sets this Office's HashSet of doctors to the given HashSet
     * @param doctors the desired HashSet of docotrs
     */
    public void setOccupants(HashSet<Doctor> doctors) {
        this.occupants = doctors;
    }

    /**
     * Sets the department of this office to the given department
     * @param department as a String
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Retrieves the department of this office
     * @return the department of this office, as a String
     */
    public String getDepartment() {
        return this.department;
    }
}
