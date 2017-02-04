package DatabaseTest;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Brandon on 2/4/2017.
 */
public class CreateDBTest24 {
    public static void main(String[] args) {

        final String DB_URL = "jdbc:derby://localhost:1527/DBTest24";

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(DB_URL);

        }
        catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}
