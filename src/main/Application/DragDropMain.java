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

	private double mouseOldX, mouseOldY = 0;
	private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
	private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

	@Override
	public void start(Stage stage)
	{
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setSpecularColor(Color.ORANGE);
		redMaterial.setDiffuseColor(Color.RED);

		Box myBox = new Box(100, 100, 100);
		myBox.setTranslateX(400);
		myBox.setTranslateY(300);
		myBox.setTranslateZ(400);
		myBox.setMaterial(redMaterial);

		Rectangle rectangle = new Rectangle();
		rectangle.setX(200);
		rectangle.setY(600);
		rectangle.setWidth(200);
		rectangle.setHeight(100);
		rectangle.setFill(Color.GREY);

		// to Set pivot points
		rotateX.setPivotX(400);
		rotateX.setPivotY(300);
		rotateX.setPivotZ(400);

		rotateY.setPivotX(400);
		rotateY.setPivotY(300);
		rotateY.setPivotZ(400);

		rotateZ.setPivotX(400);
		rotateZ.setPivotY(300);
		rotateZ.setPivotZ(400);


		// initialize the camera
		PerspectiveCamera camera = new PerspectiveCamera(false);
		camera.getTransforms().addAll (rotateX, rotateY, new Translate(0, 0, 0));

		Group root = new Group();
		Group subRoot = new Group();

		root.getChildren().add(rectangle);

		Scene scene = new Scene(root, 1000, 1000, true);
		SubScene subScene = new SubScene(subRoot, 800, 800, true, SceneAntialiasing.BALANCED);

		subScene.setCamera(camera);
		subRoot.getChildren().add(myBox);
		root.getChildren().add(subScene);

		// events for rotation
		rectangle.setOnMousePressed(event -> {
			mouseOldX = event.getSceneX();
			mouseOldY = event.getSceneY();
		});

		rectangle.setOnMouseDragged(event -> {
			if(event.isPrimaryButtonDown())
			{
				rotateX.setAngle(rotateX.getAngle() - (event.getSceneY() - mouseOldY));
				rotateY.setAngle(rotateY.getAngle() + (event.getSceneX() - mouseOldX));
				mouseOldX = event.getSceneX();
				mouseOldY = event.getSceneY();
			}
		});

		stage.setTitle("JavaFX 3D Object");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}


}