package Controller.Admin.PopUp;

import Domain.Map.Suite;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Controlled for popup that edits an office map node
 */
public class SuiteEditController extends AbstractPopupController
{
    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField deptBox;

    @FXML
    private JFXTextField phoneBox;

    protected PopOver popOver;

    protected Suite suiteUnderEdit;

    public SuiteEditController(Suite suite)
    {
        super(suite);

        this.suiteUnderEdit = suite;
    }

    @Override
    public void saveEdits()
    {
        suiteUnderEdit.setName(nameBox.getText());
        //suiteUnderEdit.getInfo().setPhoneNumber(phoneBox.getText());
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(suiteUnderEdit.getName());
        //phoneBox.setText(suiteUnderEdit.getInfo().getPhoneNumber());
    }
}
