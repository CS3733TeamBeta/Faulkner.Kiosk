package Model.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Brandon on 2/4/2017.
 */
public class DatabaseManager {

    private final String framework = "embedded";
    private final String protocol = "jdbc:derby:";
    private Connection conn = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private PreparedStatement psInsert;
    private PreparedStatement psUpdate;
    private Statement s;
    private ResultSet rs = null;
    private String username = "user1";
    private String uname = username.toUpperCase();

    public DatabaseManager(){

        Properties props = new Properties(); // connection properties
        // providing a user name and password is optional in the embedded
        // and derbyclient frameworks
        props.put("user", username);
        props.put("password", "user1");

           /* By default, the schema APP will be used when no username is
            * provided.
            * Otherwise, the schema name is the same as the user name (in this
            * case "user1" or USER1.)
            *
            * Note that user authentication is off by default, meaning that any
            * user can connect to your database using any password. To enable
            * authentication, see the Derby Developer's Guide.
            */

        String dbName = "derbyDB"; // the name of the database

           /*
            * This connection specifies create=true in the connection URL to
            * cause the database to be created when connecting for the first
            * time. To remove the database, remove the directory derbyDB (the
            * same as the database name) and its contents.
            *
            * The directory derbyDB will be created under the directory that
            * the system property derby.system.home points to, or the current
            * directory (user.dir) if derby.system.home is not set.
            */

           try {
               conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
           }
           catch (SQLException se) {
               System.out.println(se.getMessage());
               System.out.println("Sluts Sluts Sluts");
           }

        if (conn != null) {
            System.out.println("Connected to and created database " + dbName);
        }

        // We want to control transactions manually. Autocommit is on by
        // default in JDBC.
        try {
            conn.setAutoCommit(false);
        }
        catch (SQLException se) {
            System.out.println(se.getMessage());

        }

           /* Creating a statement object that we can use for running various
            * SQL statements commands against the database.*/
           try {
               s = conn.createStatement();
           }
           catch (SQLException se) {
               System.out.println(se.getMessage());

           }
        statements.add(s);


    }

    //Add a row to the specified table
    public void addRow(String table, String rowName, int floor) throws SQLException{


        psInsert = conn.prepareStatement("insert into " + uname + "." + table + " values (?, ?)");
        statements.add(psInsert);

        psInsert.setString(1, rowName);
        psInsert.setInt(2, floor);
        //psInsert.setString(3, room);

        psInsert.executeUpdate();

        conn.commit();

        System.out.println("Row Added");

    }

    //Finds a row in a column according to the given name in the row in the given table
    //should have a return statement
    public void findRow(String table, String column, String keyword) throws SQLException{

        psInsert = conn.prepareStatement("SELECT * FROM " + uname + "." + table + " WHERE " + column + " LIKE " + keyword + "'%'");
        statements.add(psInsert);

        psInsert.executeUpdate();

        conn.commit();

        System.out.println("Something Found");


    }

    //should have a return statement
    public void showInfo(String table, String column) throws SQLException{

        psInsert = conn.prepareStatement("SELECT " + column + " FROM " + uname + "." + table);
        statements.add(psInsert);

        psInsert.executeUpdate();

        conn.commit();

        System.out.println("Column shown");
    }

    //alphabetize the given table
    //public void alpha(String table){}


    //Remove a a row in the given table
    public void deleteRow(String table, String column, int keyword) throws SQLException{

        psInsert = conn.prepareStatement("DELETE FROM " + uname + "." + table + " WHERE " + column + "=" + keyword);
        statements.add(psInsert);


        psInsert.executeUpdate();

        conn.commit();

        System.out.println("Row Deleted");
    }

    private void shutdown() {
        try
        {
            // the shutdown=true attribute shuts down Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

            // To shut down a specific database only, but keep the
            // engine running (for example for connecting to other
            // databases), specify a database in the connection URL:
            //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
        }
        catch (SQLException se)
        {
            if (( (se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                System.out.println(se.getStackTrace());
            }
        }
    }

    public void testDatabase() throws SQLException {

        s.execute("CREATE TABLE departments(Name VARCHAR(200), Floor INT, Room VARCHAR(200))");

        psInsert = conn.prepareStatement("insert into departments values (?, ?, ?)");
        statements.add(psInsert);

        psInsert.setString(1, "Addiction Recovery Program");
        psInsert.setInt(2, 2);
        psInsert.setString(3, "n/a");
        psInsert.executeUpdate();
        System.out.println("Good");

        // and add a few rows...

           /* It is recommended to use PreparedStatements when you are
            * repeating execution of an SQL statement. PreparedStatements also
            * allows you to parameterize variables. By using PreparedStatements
            * you may increase performance (because the Derby engine does not
            * have to recompile the SQL statement each time it is executed) and
            * improve security (because of Java type checking).
            */
        // parameter 1 is num (int), parameter 2 is addr (varchar)
        psInsert = conn.prepareStatement("insert into departments values (?, ?, ?)");
        statements.add(psInsert);

        psInsert.setString(1, "Allergy");
        psInsert.setInt(2, 4);
        psInsert.setString(3, "4G");
        psInsert.executeUpdate();
        System.out.println("Great");



            /*
            psInsert.setInt(1, 1910);
            psInsert.setString(2, "Union St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1910 Union");*/

            // Let's update some rows as well...


          /*  // parameter 1 and 3 are num (int), parameter 2 is addr (varchar)
            psUpdate = conn.prepareStatement("update location set num=?, addr=? where num=?");
            statements.add(psUpdate);*/

/*
            // delete the table
            s.execute("drop table location");
            System.out.println("Dropped table location");*/

            conn.commit();

    }
}
