package Controller.Admin.PopUp;

import Controller.AbstractController;
import Domain.Map.Office;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Controlled for popup that edits an office map node
 */
public class OfficeEditController extends AbstractPopupController
{
    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField deptBox;

    @FXML
    private JFXTextField phoneBox;

    protected PopOver popOver;

    protected Office officeUnderEdit;

    public OfficeEditController(PopOver popOver)
    {
        super(popOver);
    }

    @FXML
    public void initialize()
    {
        fillFields();
    }

    public OfficeEditController(Office office, PopOver popOver)
    {
        this(popOver);

        this.officeUnderEdit = office;
    }

    public void onConfirm()
    {
        officeUnderEdit.setDepartment(deptBox.getText());
        officeUnderEdit.getInfo().setName(nameBox.getText());
        officeUnderEdit.getInfo().setPhoneNumber(phoneBox.getText());
        popOver.hide();
    }

    public void fillFields()
    {
        nameBox.setText(officeUnderEdit.getInfo().getName());
        deptBox.setText(officeUnderEdit.getDepartment());
        phoneBox.setText(officeUnderEdit.getInfo().getPhoneNumber());
    }
}
