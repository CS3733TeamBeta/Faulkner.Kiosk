package Domain.Map;

import javafx.scene.image.Image;
import sun.security.krb5.internal.crypto.Des;

/**
 * Destination is a type of node that you'd want to navigate to
 */

public class Destination extends MapNode {
    Info myInfo;
    Image icon;
    Image destinationView;

    public Destination() {
        myInfo = new Info("1", "2", "3");
    }
}
