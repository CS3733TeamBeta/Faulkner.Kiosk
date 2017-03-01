package main.Map.Entity;

import javafx.scene.image.Image;

/**
 * Created by Pattop on 2/18/2017.
 */
public class ProxyImage {

    private String fileName;

    Image realImage = null;

    public ProxyImage (String fileName)
    {
        this.fileName = fileName;
    }

    // Display image from associated file, create object for RealImage if necessary
    public Image getImage()
    {
        System.out.println("getImage called, current filename is " + fileName);
        if(realImage == null) {
            System.out.println("realImage is null");
            realImage = new Image(fileName);
        }

        return realImage;
    }
}
