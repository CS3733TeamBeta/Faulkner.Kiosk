package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.Floor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.nio.channels.FileChannel;

public class FileChooser_Controller extends AbstractController
{

    @FXML
    ImageView imgFileShow;
    @FXML
    Button backButton;
    @FXML
    Button saveButton;
    @FXML
    Button btnChooseFile;
    @FXML
    TextArea txtText;
    @FXML
    TextField inputFloorNumber;


    Stage newStage = new Stage();
    FileChooser newFileChooser = new FileChooser();
    String strFilePath = new String();
    Alert alert = new Alert(Alert.AlertType.WARNING);
    long size;
    FileChannel outChannelTwo;
    FileChannel outChannel;
    FileChannel source;
    String fullName = null;
    String fullNameTwo = null;

    Floor newFloor;

    @FXML
    private void clickGoBack()throws IOException{
        SceneSwitcher.switchToScene(this.getStage(), "../Admin/MapBuilder/MapEditorView.fxml");
    }

    @FXML
    private void clickSaveFile() throws IOException{
        try
        {
            source.transferTo(0, size, outChannel);
        }
        catch(Exception e){}
        try
        {
            source.transferTo(0, size, outChannelTwo);
        }
        catch(Exception e){}

        System.out.println("Loading image to: " + fullName);
        System.out.println("Loading image to: " + fullNameTwo);

        int floorNum;

        try{
            floorNum = Integer.parseInt(inputFloorNumber.getText());
            newFloor = new Floor(floorNum);
            if(fullName != null) {
                newFloor.setImageLocation(fullName);
                SceneSwitcher.switchToScene(this.getStage(), "../Admin/MapBuilder/MapEditorView.fxml");
            }
            else{
                System.out.println("ERROR: No image selected");
            }
        }catch(NumberFormatException e){
            System.out.println("ERROR: Input non-int value");

        }

    }


    @FXML
    private void clickChooseFile() throws FileNotFoundException
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
            DialogPane message = new DialogPane();
            alert.showAndWait();
        }
        File userFile = new File(strFilePath);
        String filename = userFile.getName();
        fullName = "src/View/Admin/MapBuilder/" + filename;
        fullNameTwo = "src/View/User/MapViewer/" + filename;
        source = new FileInputStream(strFilePath).getChannel();

        size = 0;
        try {
            size = source.size();
        }
        catch (Exception e){

        }
        outChannel = new FileOutputStream(fullName).getChannel();
        outChannelTwo = new FileOutputStream(fullNameTwo).getChannel();

    }

    // gets the extension of the selected file for comparison with valid types in clickChooseFile
    public String getFileExtension(File f)
    {
        String fileName = f.getName();
        return fileName.substring(fileName.indexOf(".") + 1, f.getName().length());
    }
}
