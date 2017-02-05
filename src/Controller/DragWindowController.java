//Testing comments, From Devon From Ian

package Controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;


public class DragWindowController
{
    public Line edgeB;
    public Line edgeA;

    public AnchorPane root;

    /**
     * The node that will be draggable.
     */
    private Group m_draggableNode;


    @FXML
    protected void initialize() {

        //DraggableNodeEdge e = new DraggableNodeEdge(); //Draggable Node Edge. Should auto connect to others
        //and form bonds

        //root.getChildren().add(e.getDrawableNode());
        //edgeA.setOnMousePressed(pressMouse());
      //  edgeA.setOnMouseDragged(dragMouse());
    }

}

