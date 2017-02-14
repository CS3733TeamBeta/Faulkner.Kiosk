package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChooser_Controller
{

    @FXML
    ImageView imgFileShow;
    @FXML
    Button btnChooseFile;
    @FXML
    TextArea txtText;


    // This is all you need to bring up the fileChooser dialogue:

    Stage newStage = new Stage();
    FileChooser newFileChooser = new FileChooser();
    String filePath = new String();


    @FXML
    private void clickChooseFile()
    {
        File selectedFile = newFileChooser.showOpenDialog(newStage);
        filePath = selectedFile.getPath();
        txtText.setText(filePath);
        //Image img = new Image(filePath);
        //imgFileShow.setImage(img);
    }

    // I also included a way to get the path to the file
    // Perhaps a conditional statement could be written to make sure that fileSelected is an image format
}
