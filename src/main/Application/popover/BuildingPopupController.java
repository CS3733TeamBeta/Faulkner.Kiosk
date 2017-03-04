package main.Application.popover;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import main.Application.ApplicationController;
import main.Map.Entity.Building;
import org.controlsfx.control.PopOver;

import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by benhylak on 3/2/17.
 */
public class BuildingPopupController
{
    Building b;

    PopOver popOver;

    @FXML
    JFXTextField buildingNameBox;

    @FXML
    JFXButton editFloors;

    public BuildingPopupController(Building b)
    {
        this.b = b;
    }

    @FXML
    public void initialize()
    {
        buildingNameBox.setText(b.getName());

        editFloors.setOnAction(e->
        {
            try
            {
                ApplicationController.getController().switchToMapEditorView(b);
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        });
    }

    @FXML
    public void close()
    {
        b.setName(buildingNameBox.getText());
        popOver.hide();
    }

    public void setPopover(PopOver p)
    {
        popOver = p;
    }
}
