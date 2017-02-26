package Domain.Map;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        if(realImage == null) {
            realImage = new Image(fileName);
        }

        return realImage;
    }
}
