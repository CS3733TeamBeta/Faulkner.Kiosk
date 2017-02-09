package Controller;

import java.awt.*;
import java.util.Iterator;

import Domain.Map.Floor;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.ViewElements.*;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.MapModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color; // for modifying the color of the lines, this is probably a hack


/**
 * Created by IanCJ on 2/8/2017.
 */
public abstract class AbsMapController extends AnchorPane {

    @FXML protected SplitPane base_pane;
    @FXML protected AnchorPane right_pane;
    @FXML protected VBox left_pane;

    protected DragIcon mDragOverIcon = null;

    protected EventHandler<DragEvent> onIconDragOverRoot = null;
    protected EventHandler<DragEvent> onIconDragDropped = null;
    protected EventHandler<DragEvent> onIconDragOverRightPane = null;
    protected EventHandler<DragEvent> onLineSyncNeeded = null;
    protected MapModel model;

    protected NodeEdge drawingEdge;

    public void onEdgeComplete()
    {
        for(EdgeCompleteEventHandler handler : model.getEdgeCompleteHandlers())
        {
            handler.handle(new EdgeCompleteEvent(drawingEdge));
        }
    }





    @FXML
    protected void initialize() {
        renderInitialMap();//bring in nodes that already exist from the floor fed in
    }

    protected void renderInitialMap(){}

    protected void setModel(MapModel importedModel){
        this.model = importedModel;
    }

    protected void setModel(Floor importedFloor){
        this.model = new MapModel();
        this.model.setCurrentFloor(importedFloor);
    }

}
