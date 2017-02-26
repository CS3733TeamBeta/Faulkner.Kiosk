package Controller.Map.Admin.PopUp;

import Entity.Map.Destination;
import Entity.Map.Kiosk;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Created by Samuel on 2/26/2017.
 */
public class KioskEditController extends AbstractPopupController{

    @FXML
    private JFXTextField nameBox;

    @FXML
    private JFXTextField directionBox;

    protected PopOver popOver;

    protected Kiosk kioskUnderEdit;

    public KioskEditController(Kiosk kiosk)
    {
        super(kiosk);

        this.kioskUnderEdit = kiosk;
    }

    @Override
    public void saveEdits()
    {
        kioskUnderEdit.setName(nameBox.getText());
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(kioskUnderEdit.getName());
    }
}
