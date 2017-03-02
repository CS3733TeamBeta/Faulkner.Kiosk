package main.Map.Entity;

import main.Application.popover.KioskEditController;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Samuel on 2/26/2017.
 */
public class Kiosk extends MapNode{
    String direction;

    String name = "Kiosk";

    UUID kioskID;

    private final String popOverEditFXML = "/Popup/KioskEditPopup.fxml";

    public Kiosk () {
        this.direction = "N";
    }

    public Kiosk (String name) {this.name = name;}

    //public Kiosk (String direction) {this.direction = direction;}

    public Kiosk (MapNode m, String name, String direction)
    {
        this.name = name;
        this.direction = direction;


        this.setPosX(m.getPosX());
        this.setPosY(m.getPosY());

        this.setType(m.getType());

        this.nodeUID = m.getNodeID();
    }

    @Override
    public PopOver getEditPopover() {
        KioskEditController controller = new KioskEditController(this);
        return getPopOver(controller, popOverEditFXML);
    }


    @Override
    public void setType(NodeType type)
    {
        super.setType(type);
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getDirection() { return this.direction; }

    @Override
    public String getLabel()
    {
        return name;
    }


}
