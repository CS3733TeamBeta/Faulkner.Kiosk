package Controller.Admin.PopUp;

import Controller.AbstractController;
import Domain.Map.Office;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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

    @FXML
    public void initialize()
    {
        fillFields();
    }

    public OfficeEditController(Office office, PopOver popOver)
    {
        super(popOver);
        this.officeUnderEdit = office;
    }

    public void saveEdits()
    {
        officeUnderEdit.setDepartment(deptBox.getText());
        officeUnderEdit.getInfo().setName(nameBox.getText());
        officeUnderEdit.getInfo().setPhoneNumber(phoneBox.getText());
    }

    public void fillFields()
    {
        if(nameBox!=null && deptBox != null && phoneBox!=null)
        {
            nameBox.setText(officeUnderEdit.getInfo().getName());
            deptBox.setText(officeUnderEdit.getDepartment());
            phoneBox.setText(officeUnderEdit.getInfo().getPhoneNumber());
        }
    }
}
