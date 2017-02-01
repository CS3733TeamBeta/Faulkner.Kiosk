package Controller;
	
import Domain.ViewElements.Draggables.RootLayout;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class DragTest extends Application {
	
	@Override
	public void start(Stage primaryStage)
	{
		BorderPane root = new BorderPane();
		
		try
		{
			Scene scene = new Scene(root,1200,700);
			scene.getStylesheets().add(getClass().getResource("../view_resources/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		root.setCenter(new RootLayout());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
