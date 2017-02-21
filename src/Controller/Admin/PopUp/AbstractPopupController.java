package Controller.Admin.PopUp;

import Domain.Map.Destination;
import Domain.Map.MapNode;
import Domain.ViewElements.Events.DeleteRequestedHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

import java.util.Map;

/**
 * Created by benhylak on 2/8/17.
 */
public abstract class AbstractPopupController
{
    protected PopOver popOver;

    @FXML
    protected JFXButton okayButton;

    @FXML
    protected JFXButton deleteButton;

    private MapNode nodeToDelete; //node that will be deleted, if requested

    public AbstractPopupController(MapNode nodeToDelete)
    {
        this.nodeToDelete = nodeToDelete;
    }


    @FXML
    public void initialize()
    {
        deleteButton.setOnAction(event ->
        {
            nodeToDelete.deleteFromMap();
            popOver.hide();
        });

        fillFields();
    }

    /**Called on initialize**/
    public abstract void fillFields();

    /**
     * Called when this popup is closed
     */
    public abstract void saveEdits();


    public abstract void isKiosk ();
    /**
     * When the okay button is pressed
     * @param event
     */
    @FXML
    void onConfirm(ActionEvent event) {
        saveEdits();
        popOver.hide();
    }

    public void setPopOver(PopOver popOver)
    {
        this.popOver = popOver;
    }
}

