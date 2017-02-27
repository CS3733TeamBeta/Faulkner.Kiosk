package Application;

import javafx.stage.Stage;

import javax.swing.*;

/**
 * Parent class for all controllers
 */
public class AbstractController extends JFrame {
    protected Stage primaryScene;

    public void setStage(Stage s)
    {
        primaryScene = s;
    }

    public Stage getStage(){
        return this.primaryScene;
    }
}
