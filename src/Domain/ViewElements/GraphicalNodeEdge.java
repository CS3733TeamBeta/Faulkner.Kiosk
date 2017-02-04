
package Domain.ViewElements;

import Domain.Map.NodeEdge;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by benhylak on 2/3/17.
 */
public class GraphicalNodeEdge extends AnchorPane
{
    @FXML
    Line node_link;
    NodeEdge nodeEdge;

    public GraphicalNodeEdge() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("../resources/GraphicalNodeEdge.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());
    }

    @FXML
    private void initialize() {

    }

    public NodeEdge getNodeEdge()
    {
        return nodeEdge;
    }


    public void setStart(Point2D startPoint) {

        node_link.setStartX(startPoint.getX());
        node_link.setStartY(startPoint.getY());
    }

    public void setEnd(Point2D endPoint) {

        node_link.setEndX(endPoint.getX());
        node_link.setEndY(endPoint.getY());
    }


/*public void bindEnds (DraggableNode source, DraggableNode target) {
    node_link.startXProperty().bind(
            Bindings.add(source.layoutXProperty(), (source.getWidth() / 2.0)));

    node_link.startYProperty().bind(
            Bindings.add(source.layoutYProperty(), (source.getWidth() / 2.0)));

    node_link.endXProperty().bind(
            Bindings.add(target.layoutXProperty(), (target.getWidth() / 2.0)));

    node_link.endYProperty().bind(
            Bindings.add(target.layoutYProperty(), (target.getWidth() / 2.0)));

    source.registerLink (getId());
    target.registerLink (getId());
}*/
}
