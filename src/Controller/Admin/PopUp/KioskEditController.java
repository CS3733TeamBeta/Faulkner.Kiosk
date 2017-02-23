package Controller.Admin.PopUp;

import Domain.Map.Destination;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

import static Domain.ViewElements.DragIconType.kiosk;

/**
 * Controlled for popup that edits an office map node
 */
public class KioskEditController extends AbstractPopupController
{

    protected PopOver popOver;

    protected Destination nodeUnderEdit;


    public KioskEditController(Destination destination)
    {
        super(destination);

        this.nodeUnderEdit = destination;
    }

    @FXML
    public void setPrimaryKiosk(){
        this.nodeUnderEdit.setKioskNode(true);
        System.out.println("HERE");
    }

    @Override
    public void saveEdits()
    {
        //nodeUnderEdit.setName(nameBox.getText());
    }

    @Override
    public void fillFields()
    {
        //nameBox.setText(nodeUnderEdit.getName());
    }
}
