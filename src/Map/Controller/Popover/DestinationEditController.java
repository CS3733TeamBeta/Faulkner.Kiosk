package Map.Controller.Popover;

import Application.Database.DatabaseManager;
import Map.Entity.Destination;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

import java.sql.SQLException;

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
        try {
            new DatabaseManager().updateDestination(destinationUnderEdit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(destinationUnderEdit.getName());
    }
}
