package Controller.Admin.PopUp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

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


    public AbstractPopupController(PopOver popOver)
    {
        this.popOver = popOver;
    }

    public abstract void saveEdits();

    @FXML
    void onConfirm(ActionEvent event) {
        saveEdits();
        popOver.hide();
    }
}
