package main.Application.popover;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import main.Application.ApplicationController;
import main.Map.Entity.Kiosk;
import org.controlsfx.control.PopOver;

import java.sql.SQLException;

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
        try {
            ApplicationController.getCache().getDbManager().updateKiosk(kioskUnderEdit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fillFields()
    {
        nameBox.setText(kioskUnderEdit.getName());
    }
}
