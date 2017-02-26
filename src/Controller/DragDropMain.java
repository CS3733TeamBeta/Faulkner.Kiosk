package Controller;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import jfxtras.labs.util.event.MouseControlUtil;

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
		cam.getTransforms().addAll(new Rotate(40,Rotate.X_AXIS),new Translate(-200,-200,-60));

		final Group root = new Group();

		//TextureCubeMap cubeMap = new TextureCubeMap();
		//ImageComponent2D ic = new ImageComponent2D(1, img);

		//cubeMap.setImage(1, ic);
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

		Image img = new Image(this.getClass().getResourceAsStream("../FloorMaps/1_thefirstfloor.png"));

		PhongMaterial material = new PhongMaterial(Color.WHITE);
		material.setSpecularPower(10000);
		material.setDiffuseMap(img);

		floor.setMaterial(material);
		root.getChildren().add(floor);
		root.getChildren().add(greenLight);
		root.getChildren().add(whiteLight);

		MouseControlUtil.makeDraggable(floor);


		final Box box = new Box(80, 80, 10);
		box.setMaterial(new PhongMaterial(Color.RED));
		root.getChildren().add(box);

		box.setTranslateY(200);
		box.setTranslateX(200);

		final Box box1 = new Box(80, 80, 10);
		box1.setTranslateZ(10);

		box1.setTranslateY(200);
		box1.setTranslateX(200);

		box1.setMaterial(new PhongMaterial(Color.RED));
		root.getChildren().add(box1);


		//MouseControlUtil.makeDraggable(box);

		final Rectangle rectangle = new Rectangle(400, 400, Color.TRANSPARENT);
		rectangle.setMouseTransparent(true);
		rectangle.setDepthTest(DepthTest.DISABLE);
		root.getChildren().add(rectangle);

		// D&D starts
		box.setOnDragDetected((MouseEvent event)-> {
			box.setMouseTransparent(true);
			rectangle.setMouseTransparent(false);
			box.setCursor(Cursor.MOVE);
			box.startFullDrag();
		});

		// D&D ends
		box.setOnMouseReleased((MouseEvent event)-> {
			box.setMouseTransparent(false);
			rectangle.setMouseTransparent(true);
			box.setCursor(Cursor.DEFAULT);
		});

		// While D&D, only confined to the rectangle
		rectangle.setOnMouseDragOver((MouseDragEvent event)-> {
			Point3D coords = event.getPickResult().getIntersectedPoint();
			coords = rectangle.localToParent(coords);
			box.setTranslateX(coords.getX());
			box.setTranslateY(coords.getY());
			box.setTranslateZ(coords.getZ());
		});

		box.setOnMouseEntered(event->
		{
			box.setMaterial(new PhongMaterial(Color.YELLOW));
		});


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