package Controller;

import Controller.Admin.MapEditorController;
import Domain.Map.*;
import Model.MapModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DragDropMain extends Application
{


	int offset = 200;
	//@TODO  Add code to calculate offset

	public static MapModel mvm = null;

	@Override
	public void start(Stage primaryStage)
	{
		BorderPane root = new BorderPane();

		try
		{

			Scene scene = new Scene(root, 640, 480);
			//scene.getStylesheets().add(getClass().getResource("../Admin/MapBuilder/application.css").toExternalForm());
			SceneSwitcher.switchToScene(primaryStage, "../Admin/MapBuilder/MapEditorView.fxml");
			//primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		//root.setCenter(new MapEditorController());
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}