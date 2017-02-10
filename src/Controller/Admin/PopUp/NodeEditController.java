package Controller.Admin.PopUp;

import Domain.Map.MapNode;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Controlled for popup that edits an office map node
 */
public class NodeEditController extends AbstractPopupController
{
    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField deptBox;

    @FXML
    private JFXTextField phoneBox;

    protected PopOver popOver;

    protected MapNode nodeUnderEdit;

    public NodeEditController(MapNode destination)
    {
        super(destination);

        this.nodeUnderEdit = destination;
    }

    @Override
    public void saveEdits()
    {
        //nothing to save
    }

    @Override
    public void fillFields()
    {
        //nothing to fill
    }
}
