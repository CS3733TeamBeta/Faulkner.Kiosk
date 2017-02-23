package Controller.Admin.PopUp;

import Domain.Map.Kiosk;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

import static Domain.ViewElements.DragIconType.kiosk;

/**
 * Controlled for popup that edits an office map node
 */
public class KioskEditController extends AbstractPopupController
{
    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField deptBox;

    @FXML
    private JFXTextField phoneBox;

    protected PopOver popOver;

    protected Kiosk nodeUnderEdit;


    public KioskEditController(Kiosk kiosk)
    {
        super(kiosk);

        this.nodeUnderEdit = kiosk;
    }



    @Override
    public void saveEdits()
    {
        nodeUnderEdit.setName(nameBox.getText());
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(nodeUnderEdit.getName());
    }
}
