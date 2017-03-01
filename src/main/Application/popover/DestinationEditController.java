package main.Application.popover;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import main.Application.ApplicationController;
import main.Map.Entity.Destination;
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
            ApplicationController.getCache().getDbManager().updateDestination(destinationUnderEdit);
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
