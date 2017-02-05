
package Domain.ViewElements;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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
    Line edgeLine;

    protected GraphicalMapNode source = null;
    protected GraphicalMapNode target = null;

    public GraphicalNodeEdge()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Admin/MapBuilder/NodeLink.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

      //  edgeLine = new Line();
       // this.getChildren().add(edgeLine);

        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());
    }

    public GraphicalNodeEdge(GraphicalMapNode source, GraphicalMapNode target)
    {
       this.source = source;
       this.target = target;
       // node_link.setStart(a.getLocation);
    }

    @FXML
    private void initialize() {

    }

    /**
     *
     * @return source of edge
     */
    public GraphicalMapNode getSource()
    {
        return source;
    }

    /**
     *
     * @return target of edge
     */
    public GraphicalMapNode getTarget()
    {
        return target;
    }

    /**
     * Set source node and update start/end point
     * @param source
     */
    public void setSource(GraphicalMapNode source)
    {
        this.source = source;

        Bounds boundsInScene = source.getBoundsInLocal();

        Point2D startPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        setStart(source.localToParent(startPoint));
        setEnd(source.localToParent(startPoint));
    }

    /**
     * Set target node and update start point
     * @param target
     */
    public void setTarget(GraphicalMapNode target)
    {
        this.target = target;

        Bounds boundsInScene = target.getBoundsInLocal();

        Point2D endPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        setEnd(target.localToParent(endPoint));
    }

    /**
     * Set start point
     * @param startPoint
     */
    public void setStart(Point2D startPoint) {

        edgeLine.setStartX(startPoint.getX());
        edgeLine.setStartY(startPoint.getY());
    }

    //Set end point
    public void setEnd(Point2D endPoint) {

        edgeLine.setEndX(endPoint.getX());
        edgeLine.setEndY(endPoint.getY());
    }

    public Line getEdgeLine()
    {
        return edgeLine;
    }

}
