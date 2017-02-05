package Model;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 * Created by Lukezebrowski on 2/4/17.
 */
public class AdminWelcomeModel
{
    private Line edgeA;
    private Line edgeB;
    private AnchorPane root;
    private Group m_draggableNode;

    public AdminWelcomeModel(Line A,Line B,AnchorPane r,Group m)
    {
        edgeA = A;
        edgeB = B;
        root = r;
        m_draggableNode = m;
    }


}
