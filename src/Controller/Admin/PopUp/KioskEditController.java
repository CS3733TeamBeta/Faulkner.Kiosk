package Controller.Admin.PopUp;

import Domain.Map.Destination;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.PopOver;


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

    @FXML
    public void northHit(){
        System.out.println("North");
        this.nodeUnderEdit.setDirection("N");
    }

    @FXML
    public void southHit(){
        System.out.println("South");
        this.nodeUnderEdit.setDirection("S");
    }

    @FXML
    public void eastHit(){
        System.out.println("East");
        this.nodeUnderEdit.setDirection("E");
    }

    @FXML
    public void westHit(){
        System.out.println("West");
        this.nodeUnderEdit.setDirection("W");
    }

    @FXML
    public void northEastHit(){
        this.nodeUnderEdit.setDirection("NE");
        System.out.println("NE");
    }

    @FXML
    public void southEastHit(){
        this.nodeUnderEdit.setDirection("SE");
        System.out.println("SE");
    }

    @FXML
    public void northWestHit(){
        this.nodeUnderEdit.setDirection("NW");
        System.out.println("NW");
    }

    @FXML
    public void southWestHit(){
        this.nodeUnderEdit.setDirection("SW");
        System.out.println("SW");
    }
}
