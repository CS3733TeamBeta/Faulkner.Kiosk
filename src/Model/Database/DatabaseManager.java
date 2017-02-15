package Model.Database;

import Domain.Map.*;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    public HashMap<Integer, MapNode> mapNodes = new HashMap<>();
    public HashMap<Integer, NodeEdge> edges = new HashMap<>();
    public HashMap<String, Doctor> doctors = new HashMap<>();
    public HashMap<Integer, Floor> floors = new HashMap<>();
    public HashMap<String, Suite> suites = new HashMap<>();

    public static final String[] createTables = {
            "CREATE TABLE USER1.DOCTOR (DOC_ID INT PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(50), " +
                    "DESCRIPTION VARCHAR(25), " +
                    //"NUMBER VARCHAR(20), " +
                    "HOURS VARCHAR(25)," +
                    "SUITES VARCHAR(50))",
            "CREATE TABLE USER1.SUITE (SUITE_ID INT PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(200), " +
                    "NODE_ID INT, " +
                    "FLOOR_ID INT, " +
                    "CONSTRAINT NODE_ID___FK FOREIGN KEY (NODE_ID) REFERENCES NODE (NODE_ID))",
            "CREATE TABLE USER1.SUITE_DOC (SUITE_ID INT," +
                    "DOC_ID INT," +
                    "CONSTRAINT SUITE_DOC___FK1 FOREIGN KEY (SUITE_ID) REFERENCES SUITE (SUITE_ID)," +
                    "CONSTRAINT SUITE_DOC___FK2 FOREIGN KEY (DOC_ID) REFERENCES DOCTOR (DOC_ID))",
            "CREATE TABLE USER1.NODE (NODE_ID INT PRIMARY KEY NOT NULL, " +
                    "POSX DOUBLE, " +
                    "POSY DOUBLE, " +
                    "TYPE INT)",
            "CREATE TABLE USER1.EDGE ( EDGE_ID INT PRIMARY KEY NOT NULL," +
                    "NODEA INT," +
                    "NODEB INT," +
                    "COST INT," +
                    "CONSTRAINT EDGE___FK1 FOREIGN KEY (NODEA) REFERENCES NODE (NODE_ID)," +
                    "CONSTRAINT EDGE___FK2 FOREIGN KEY (NODEB) REFERENCES NODE (NODE_ID))",
            "CREATE TABLE USER1.FLOOR (FLOOR_ID INT PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(5)," +
                    "BUILD_ID INT," +
                    "NUMBER INT)"};

    public static final String[] dropTables = {
            "DROP TABLE USER1.SUITE_DOC",
            "DROP TABLE USER1.DOCTOR",
            "DROP TABLE USER1.SUITE"};

    public void loadDoctorsSuites() throws SQLException{
        HashMap<String, Suite> suites = new HashMap<>();
        HashMap<String, Doctor> doctors = new HashMap<>();
        PreparedStatement suiteDoc = conn.prepareStatement("select suite_id from USER1.SUITE_DOC where doc_id = ?");
        PreparedStatement docSuite = conn.prepareStatement("select doc_id from USER1.DOCTOR where suites like '%?%'");
        s = conn.createStatement();

        ResultSet rs = s.executeQuery("select * from USER1.SUITE");
        while (rs.next()) {
            suites.put(rs.getString(2),
                    new Suite(rs.getInt(1),
                            rs.getString(2),
                            floors.get(rs.getInt(4)),
                            mapNodes.get(rs.getInt(3)),
                            new HashSet<String>()));
        }
        this.suites = suites;
        System.out.println(suites.size());

        rs = s.executeQuery("select * from USER1.DOCTOR order by NAME");

        while(rs.next()) {
            HashSet<Suite> locations = new HashSet<>();
            suiteDoc.setInt(1, rs.getInt(1));
            ResultSet results = suiteDoc.executeQuery();
            while(results.next()) {
                locations.add(suites.get(results.getInt(1)));
                //suites.get(results.getInt(1)).addDoctor(rs.getString(2));
            }

            doctors.put(rs.getString(2),
                    new Doctor(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            locations));
        }
        this.doctors = doctors;
        System.out.println(doctors);
    }

    private void loadMap() throws SQLException{
        HashMap<Integer, MapNode> mapNodes = new HashMap<>();
        HashMap<Integer, NodeEdge> edges = new HashMap<>();
        s = conn.createStatement();
        ResultSet rs = s.executeQuery("select * from USER1.NODE ORDER BY NODE_ID");
        while(rs.next()) {
            mapNodes.put(rs.getInt(1),
                    new MapNode(rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3)));
        }
        rs = s.executeQuery("select * from USER1.EDGE order by EDGE_ID");
        while(rs.next()) {
            edges.put(rs.getInt(1),
                    new NodeEdge(mapNodes.get(rs.getInt(2)),
                            mapNodes.get(rs.getInt(3)),
                            rs.getInt(4)));
        }
        this.mapNodes = mapNodes;
        this.edges = edges;
    }

    public void loadFloors() throws SQLException {
        HashMap<Integer,Floor> floors = new HashMap<>();
        s = conn.createStatement();
        ResultSet rs = s.executeQuery("select * from USER1.FLOOR");
        while (rs.next()) {
            floors.put(rs.getInt(1),
                    new Floor(rs.getInt(1),
                            rs.getInt(4)));
        }
        this.floors = floors;
        System.out.println(floors.size());
    }

    public void saveDoctors() throws SQLException {
        s.execute(dropTables[0]);
        s.execute(dropTables[1]);
        s.execute(dropTables[2]);
        s.execute(createTables[0]);
        s.execute(createTables[1]);
        s.execute(createTables[2]);
        String query = "";
        for (Doctor doc : doctors.values()) {
            String locations = "";
            for(Suite ste : doc.getSuites()) {
                String query2 = "INSERT INTO USER1.SUITE_DOC VALUES ("
                        + ste.getSuiteID() + ", "
                        + doc.getDocID() + ")";
                s.executeUpdate(query2);
                locations = locations + ", " + ste.getName();
            }
            query = "INSERT INTO USER1.DOCTOR VALUES ("
                    + doc.getDocID() + ", "
                    + doc.getName() + ", "
                    + doc.getDescription() + ", "
                    + doc.getHours() + ", "
                    + locations + ")";
            s.executeUpdate(query);
        }
    }

//    public void saveMap() throws SQLException{
//        s.execute(createTables [3]);
//        String query = "";
//        s = conn.createStatement();
//        for (int k = 0; k < edges.size(); k++) {
//            query = "INSERT INTO " + uname + ".EDGE VALUES (" + edges.get(k).getEdgeId() + ", " + edges.get(k).getNodeAId() + ", " + edges.get(k).getNodeBId() + ", " + edges.get(k).getCost();
//            s.executeUpdate(query);
//        }
//        conn.commit();
//    }
//    public void saveFloors() throws SQLException {
//        s.execute(createTables[4]);
//        String query = "";
//        s = conn.createStatement();
//        for (int k = 0; k < floors.size(); k++) {
//            query = "INSERT INTO " + uname + ".FLOOR VALUES (" + floors.get(k).getFloorId() + ", " + floors.get(k).getFloorNum();
//            s.executeUpdate(query);
//        }
//        conn.commit();
//    }
    public void saveSuites() throws SQLException {
        s.execute(dropTables[1]);
        s.execute(createTables[1]);
        String query = "";
        for (Suite suite : suites.values()) {
            query = "INSERT INTO USER1.SUITE VALUES ("
                    + suite.getSuiteID() + ", "
                    + suite.getName() + ", "
                    + suite.getMyFloor().getFloorNumber() + ", "
                    + suite.getLocation().getNodeID() + ")";
            s.executeUpdate(query);
        }
    }
//    public void saveDepartments() throws SQLException {
//        s.execute(createTables[1]);
//        String query = "";
//        s = conn.createStatement();
//        for (int k = 0; k < depts.size(); k++) {
//            query = "INSERT INTO " + uname + ".DEPARTMENTS VALUES (" + depts.get(k).getDeptId() + ", " + depts.get(k).getDeptName() + ", " + depts.get(k).getFloor() + ", " + depts.get(k).getNodeID();
//            s.executeUpdate(query);
//        }
//        conn.commit();
//    }
    public void executeStatements(String[] states) throws SQLException {
        Statement state = conn.createStatement();
        for (String s : states) {
            state.execute(s);
        }
        state.close();
    }

    public DatabaseManager(){

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";

        try
        {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

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
