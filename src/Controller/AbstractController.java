package Controller;

import javafx.stage.Stage;

/**
 * Parent class for all controllers
 */
public class AbstractController
{
    Stage primaryScene;

    public void setStage(Stage s)
    {
        primaryScene = s;
    }
}
