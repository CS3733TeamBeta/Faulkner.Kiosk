package Controller.Map.Admin.PopUp;

import Domain.Map.Destination;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Controlled for popup that edits an office map node
 */
public class DestinationEditController extends AbstractPopupController
{
    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField deptBox;

    @FXML
    private JFXTextField phoneBox;

    protected PopOver popOver;

    protected Destination destinationUnderEdit;

    public DestinationEditController(Destination destination)
    {
        super(destination);

        this.destinationUnderEdit = destination;
    }

    @Override
    public void saveEdits()
    {
        destinationUnderEdit.setName(nameBox.getText());
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(destinationUnderEdit.getName());
    }
}
