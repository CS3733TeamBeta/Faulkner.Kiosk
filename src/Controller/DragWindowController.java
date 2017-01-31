//Testing comments, From Devon From Ian

package Controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.util.event.MouseControlUtil;
import Domain.ViewElements.GraphicalNodeEdge;


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

        GraphicalNodeEdge e = new GraphicalNodeEdge(); //Graphical node edge

        root.getChildren().add(e.getViewNode());

        //edgeA.setOnMousePressed(pressMouse());
      //  edgeA.setOnMouseDragged(dragMouse());
    }

}

