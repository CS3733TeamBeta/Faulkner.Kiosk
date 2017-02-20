package Domain.Map;

/**
 * Created by Pattop on 2/18/2017.
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;



public class RealImage implements Image {
    private String fileName;
    private BufferedImage img;

    public RealImage(String fileName) {
        this.fileName = fileName;
        this.img = null;
        loadFromDisk(fileName);
    }

    // Getter for image in this class
    BufferedImage getImg () {
        return img;
    }

    javafx.scene.image.Image getFXImage(){
        return SwingFXUtils.toFXImage(img, null);
    }

    @Override
    public void display() {
        System.out.println("Displaying " + fileName);
        // TODO: Make display return the required format to update the map image
    }

    private void loadFromDisk(String fileName) {
        if (img == null) { // Only load image if it isn't loaded yet
            // Load image from filename to img
            System.out.println("Loading " + fileName); // Let us know we're loading
            try {
                System.out.println(fileName);
                img = ImageIO.read(new File(fileName));
            } catch (IOException e) {
                System.out.println(fileName);
                e.printStackTrace();
            }
        }

    }
}