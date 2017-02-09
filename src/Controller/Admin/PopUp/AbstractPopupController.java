package Controller.Admin.PopUp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;

/**
 * Created by benhylak on 2/8/17.
 */
public class AbstractPopupController
{
    protected PopOver popOver;

    @FXML
    private JFXButton okayButton;

    @FXML
    private JFXButton deleteButton;


    public AbstractPopupController(PopOver popOver)
    {
        this.popOver = popOver;
    }

    @FXML
    void onConfirm(ActionEvent event) {
        popOver.hide();
    }
}
