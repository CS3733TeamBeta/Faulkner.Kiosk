package main.Application;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import main.Map.Controller.VisualBuilding;

public class DragDropMain extends Application
{
	int offset = 200;
	//@TODO  Add code to calculate offset

	@Override
	public void start(Stage primaryStage)
	{
		final PerspectiveCamera cam = new PerspectiveCamera();
		cam.setFieldOfView(50);
		cam.setFarClip(10000);
		cam.setNearClip(0.01);
		cam.getTransforms().addAll(new Rotate(75,Rotate.X_AXIS),new Translate(-200,-200,-60));

		final Group root = new Group();

		PointLight greenLight = new PointLight();
		greenLight.setColor(Color.WHITE);
		greenLight.setTranslateX(200);
		greenLight.setTranslateY(200);
		greenLight.setTranslateZ(-200);

		PointLight whiteLight = new PointLight();
		greenLight.setColor(Color.WHITE);
		greenLight.setTranslateX(300);
		greenLight.setTranslateY(700);
		greenLight.setTranslateZ(-400);

		final Box floor = new Box(500, 360, 1);
		floor.setTranslateX(200);
		floor.setTranslateY(200);
		floor.setTranslateZ(50);

		Image img = new Image(this.getClass().getResourceAsStream("/map/FloorMaps/1_thefirstfloor.png"));

		PhongMaterial material = new PhongMaterial(Color.WHITE);
		material.setSpecularPower(10000);
		material.setDiffuseMap(img);

		floor.setMaterial(material);
		root.getChildren().add(floor);
		root.getChildren().add(greenLight);
		root.getChildren().add(whiteLight);

		// THIS IS THE BUILDING STUFF
		int boxHeight = 10;
		VisualBuilding visualbuilding = new VisualBuilding(80, 80, 10, 200, 200, (floor.getTranslateZ()-boxHeight));
		root.getChildren().add(visualbuilding.getGroup());


		VisualBuilding visualbuilding2 = new VisualBuilding(80, 80, 10, 100, 100, (floor.getTranslateZ()-boxHeight));
		root.getChildren().add(visualbuilding2.getGroup());


		// THIS IS THE BUILDING STUFF

		final Rectangle rectangle = new Rectangle(400, 400, Color.TRANSPARENT);
		rectangle.setMouseTransparent(true);
		rectangle.setDepthTest(DepthTest.DISABLE);
		root.getChildren().add(rectangle);

		final Scene scene = new Scene(root, 800, 600, true);
		scene.setCamera(cam);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Drag&DropTest");
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}


}