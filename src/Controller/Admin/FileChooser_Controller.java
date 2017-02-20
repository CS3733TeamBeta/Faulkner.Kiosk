package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.Building;
import Domain.Map.Floor;
import Domain.Map.Hospital;
import Model.Database.DatabaseManager;
import javafx.collections.FXCollections;
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

    @FXML private ChoiceBox<String> dropDown;



    Stage newStage = new Stage();
    FileChooser newFileChooser = new FileChooser();
    String strFilePath = new String();
    Alert alert = new Alert(Alert.AlertType.WARNING);
    long size;
    FileChannel outChannel;
    FileChannel source;
    String fullName = null;

    String building;

    Floor newFloor;

    Hospital theHospital = DatabaseManager.getInstance().Faulkner;

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


        outChannel.force(true);
        source.force(true);
        outChannel.close();
        source.close();

        System.out.println("Loading image to: " + fullName);

        int floorNum;

        try{
            floorNum = Integer.parseInt(inputFloorNumber.getText());
            building = dropDown.getValue();
            if(fullName != null) {
                outChannel.close();
                source.close();
                addFloorInfo(fullName, floorNum, building);
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
    private void initialize(){
        for( Building b : theHospital.getBuildings()) {
            dropDown.getItems().add(b.getName());
            dropDown.setValue(b.getName());
        }

    }

    private void addFloorInfo(String name, int floorNum, String building){
        boolean isDuplicate = false;
        for(Building b : theHospital.getBuildings()){
            if(b.getName().equals(building)){
                for(Floor f : b.getFloors()){
                    if(f.getFloorNumber() == floorNum){
                        f.setImageLocation(name);
                        isDuplicate = true;
                    }
                }
                if(!isDuplicate){
                    Floor newFloor = new Floor(floorNum);
                    newFloor.setImageLocation(name);
                    try {
                        b.addFloor(newFloor);
                    }
                    catch(Exception e){
                        System.out.println("ERROR IN ADDING FLOOR");
                    }
                }
            }
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
        fullName = "resources/FloorMaps/" + filename;
        source = new FileInputStream(strFilePath).getChannel();

        size = 0;
        try {
            size = source.size();
        }
        catch (Exception e){

        }
        outChannel = new FileOutputStream(fullName).getChannel();
    }

    // gets the extension of the selected file for comparison with valid types in clickChooseFile
    public String getFileExtension(File f)
    {
        String fileName = f.getName();
        return fileName.substring(fileName.indexOf(".") + 1, f.getName().length());
    }
}
