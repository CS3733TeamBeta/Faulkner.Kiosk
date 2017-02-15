package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
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


    Stage newStage = new Stage();
    FileChooser newFileChooser = new FileChooser();
    String strFilePath = new String();
    Alert alert = new Alert(Alert.AlertType.WARNING);


    @FXML
    private void clickChooseFile()
    {
        File selectedFile = newFileChooser.showOpenDialog(newStage);
        strFilePath = selectedFile.getPath();
        txtText.setText(strFilePath);
        String strExtension = getFileExtension(selectedFile);
        txtText.setText(strExtension);

        // Adding "file:" to the front of strFilePath is necessary
        if (strExtension.equals("png")){
            Image img = new Image("file:" + strFilePath);
            imgFileShow.setImage(img);
        }
        else if (strExtension.equals("jpg")){
            Image img = new Image("file:" + strFilePath);
            imgFileShow.setImage(img);
        }
        else if (strExtension.equals("gif")){
            Image img = new Image("file:" + strFilePath);
            imgFileShow.setImage(img);
        }
        else if (strExtension.equals("bmp")){
            Image img = new Image("file:" + strFilePath);
            imgFileShow.setImage(img);
        }
        else {
            alert.showAndWait();
        }
    }

    // gets the extension of the selected file for comparison with valid types in clickChooseFile
    public String getFileExtension(File f)
    {
        String fileName = f.getName();
        return fileName.substring(fileName.indexOf(".") + 1, f.getName().length());
    }
}