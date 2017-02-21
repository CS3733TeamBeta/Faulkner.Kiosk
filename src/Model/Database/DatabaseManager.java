package Model.Database;

import Domain.Map.*;

import java.sql.*;
import java.util.*;

/**
 * Created by Brandon on 2/4/2017.
 */
public class DatabaseManager {

    private final String framework = "embedded";
   // private final String protocol = "";
    private final String protocol = CustomFilePath.myFilePath;

    private Connection conn = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private PreparedStatement psInsert;
    private PreparedStatement psUpdate;
    private Statement s;
    private ResultSet rs = null;
    private String username = "user1";
    private String uname = username.toUpperCase();

    public static HashMap<UUID, MapNode> mapNodes = new HashMap<>();
    public static HashMap<Integer, NodeEdge> edges = new HashMap<>();
    public static HashMap<String, Doctor> doctors = new HashMap<>();
    public static HashMap<String, Floor> floors = new HashMap<>();
    public static HashMap<UUID, Destination> destinations = new HashMap<>();
    public static HashMap<String, Office> offices = new HashMap<>();

    public static DatabaseManager instance = null;
    public static Hospital Faulkner;

    public static final String[] createTables = {
            "CREATE TABLE USER1.BUILDING (BUILDING_ID INT PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(100))",

            "CREATE TABLE USER1.FLOOR (FLOOR_ID VARCHAR(25) PRIMARY KEY NOT NULL, " +
                    "BUILDING_ID INT," +
                    "NUMBER INT, " +
                    "IMAGE VARCHAR(75), " +
                    "CONSTRAINT FLOOR_BUILDING_BUILDING_ID_FK FOREIGN KEY (BUILDING_ID) REFERENCES BUILDING (BUILDING_ID))",

            "CREATE TABLE USER1.NODE (NODE_ID CHAR(36) PRIMARY KEY NOT NULL, " +
                    "POSX DOUBLE, " +
                    "POSY DOUBLE, " +
                    "FLOOR_ID VARCHAR(25), " +
                    "TYPE INT, " +
                    "CONSTRAINT NODE_FLOOR_FLOOR_ID_FK FOREIGN KEY (FLOOR_ID) REFERENCES FLOOR (FLOOR_ID))",

            "CREATE TABLE USER1.DESTINATION (DEST_ID CHAR(36) PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(200), " +
                    "NODE_ID CHAR(36), " +
                    "FLOOR_ID VARCHAR(25), " +
                    "CONSTRAINT DESTINATION_NODE_NODE_ID_FK FOREIGN KEY (NODE_ID) REFERENCES NODE (NODE_ID), " +
                    "CONSTRAINT DESTINATION_FLOOR_FLOOR_ID_FK FOREIGN KEY (FLOOR_ID) REFERENCES FLOOR (FLOOR_ID))",

            "CREATE TABLE USER1.DOCTOR (DOC_ID CHAR(36) PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(50), " +
                    "DESCRIPTION VARCHAR(20), " +
                    "NUMBER VARCHAR(20), " +
                    "HOURS VARCHAR(20))",

            "CREATE TABLE USER1.OFFICES (OFFICE_ID CHAR(36) PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(200), " +
                    "DEST_ID CHAR(36), " +
                    "CONSTRAINT OFFICES_DESTINATION_DESTINATION_ID_FK FOREIGN KEY (DEST_ID) REFERENCES DESTINATION (DEST_ID))",

            "CREATE TABLE USER1.EDGE (EDGE_ID INT PRIMARY KEY NOT NULL," +
                    "NODEA CHAR(36), " +
                    "NODEB CHAR(36), " +
                    "COST DOUBLE, " +
                    "FLOOR_ID VARCHAR(25), " +
                    "CONSTRAINT EDGE_NODE_NODE_ID_FKA FOREIGN KEY (NODEA) REFERENCES NODE (NODE_ID), " +
                    "CONSTRAINT EDGE_NODE_NODE_ID_FKB FOREIGN KEY (NODEB) REFERENCES NODE (NODE_ID), " +
                    "CONSTRAINT EDGE_FLOOR_FLOOR_ID_FK FOREIGN KEY (FLOOR_ID) REFERENCES FLOOR (FLOOR_ID))",

            "CREATE TABLE DEST_DOC (DEST_ID CHAR(36), " +
                    "DOC_ID CHAR(36), " +
                    "CONSTRAINT DESTINATION_DOC_DESTINATION_DESTINATION_ID_FK1 FOREIGN KEY (DEST_ID) REFERENCES DESTINATION (DEST_ID), " +
                    "CONSTRAINT DESTINATION_DOC_DOCTOR_DOCTOR_ID_FK2 FOREIGN KEY (DOC_ID) REFERENCES DOCTOR (DOC_ID))"};

    public static final String[] dropTables = {
            "DROP TABLE USER1.DEST_DOC",
            "DROP TABLE USER1.EDGE",
            "DROP TABLE USER1.OFFICES",
            "DROP TABLE USER1.DOCTOR",
            "DROP TABLE USER1.DESTINATION",
            "DROP TABLE USER1.NODE",
            "DROP TABLE USER1.FLOOR",
            "DROP TABLE USER1.BUILDING"};


    protected DatabaseManager() throws SQLException
    {

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
            conn = DriverManager.getConnection(protocol + dbName, props);
        }
        catch (SQLException se) {
            System.out.println(se.getMessage());
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
        loadData();
        //executeStatements(dropTables);
        //executeStatements(createTables);
    }

    public static DatabaseManager getInstance() {
        if(instance == null) {
            try
            {
                instance = new DatabaseManager();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public void executeStatements(String[] states) throws SQLException {
        Statement state = conn.createStatement();
        for (String s : states) {
            state.executeUpdate(s);
            conn.commit();
        }
        state.close();
    }

    public void loadData() throws SQLException {
        s = conn.createStatement();

        if(Faulkner==null) //if not initialized
        {
            Faulkner = new Hospital();
        }

       // executeStatements(dropTables);
        //executeStatements(createTables);

        loadHospital(Faulkner);
        conn.commit();
    }

    public void saveData() throws SQLException {
        s = conn.createStatement();
        executeStatements(dropTables);
        executeStatements(createTables);
        saveHospital(Faulkner);
        s.close();
        
        System.out.println("Data Saved Correctly");
    }

    private void loadHospital(Hospital h) throws SQLException {
        PreparedStatement floorsPS = conn.prepareStatement("SELECT * from FLOOR where BUILDING_ID = ?");
        PreparedStatement nodesPS = conn.prepareStatement("SELECT * from NODE where FLOOR_ID = ?");
        PreparedStatement edgesPS = conn.prepareStatement("SELECT * from EDGE where FLOOR_ID = ?");
        PreparedStatement destPS = conn.prepareStatement("SELECT * from DESTINATION where FLOOR_ID = ?");
        s = conn.createStatement();

        HashMap<Integer, Building> buildings = new HashMap<>();
        HashMap<UUID, MapNode> mapNodes = new HashMap<>();
        HashMap<Integer, NodeEdge> nodeEdges = new HashMap<>();

        ResultSet rs = s.executeQuery("select * from BUILDING ORDER BY NAME DESC");

        // load both buildingds in
        while (rs.next()) {

            // load floors per building
            HashMap<String, Floor> flr = new HashMap();
            floorsPS.setInt(1,rs.getInt(1));
            ResultSet floorRS = floorsPS.executeQuery();
            while(floorRS.next()) {

                // load nodes per floor
                HashMap<UUID, MapNode> nodes = new HashMap<>(); // create new nodes hashmap for each floor
                nodesPS.setString(1, floorRS.getString(1)); // set query for specific floor
                ResultSet nodeRS = nodesPS.executeQuery();
                while(nodeRS.next()) {
                    MapNode tempNode = new MapNode(UUID.fromString(nodeRS.getString(1)),
                            nodeRS.getDouble(2),
                            nodeRS.getDouble(3),
                            nodeRS.getInt(5));
                    System.out.println(tempNode.getPosX());
                    System.out.println("Added node to " + (floorRS.getString(1)));
                    nodes.put(UUID.fromString(nodeRS.getString(1)), tempNode);

                    mapNodes.put(UUID.fromString(nodeRS.getString(1)), tempNode);

                }
                System.out.println(nodes.values());

                // loading destinations per floor
                destPS.setString(1, floorRS.getString(1));
                ResultSet destRS = destPS.executeQuery();

                while(destRS.next()) {
                    MapNode changedNode = nodes.get(UUID.fromString(destRS.getString(3)));
                    Destination tempDest = new Destination(UUID.fromString(destRS.getString(1)),
                            changedNode,
                            destRS.getString(2),
                            floorRS.getString(1));
                    nodes.remove(UUID.fromString(destRS.getString(3)));
                    nodes.put(UUID.fromString(destRS.getString(3)), tempDest);
                    h.addDestinations(UUID.fromString(destRS.getString(3)), tempDest);
                }
                // print out list of nodes for each floor
                System.out.println(nodes.values());

                // select all edges that have floorID of current floor we are loading
                HashMap<Integer, NodeEdge> edges = new HashMap<>();
                edgesPS.setString(1, floorRS.getString(1));
                ResultSet edgeRS = edgesPS.executeQuery();
                while(edgeRS.next()) {
                    NodeEdge tempEdge = new NodeEdge(nodes.get(UUID.fromString(edgeRS.getString(2))),
                            nodes.get(UUID.fromString(edgeRS.getString(3))),
                            edgeRS.getInt(4));

                    tempEdge.setSource(nodes.get(UUID.fromString(edgeRS.getString(2))));
                    tempEdge.setTarget(nodes.get(UUID.fromString(edgeRS.getString(3))));

                    System.out.println(nodes.get(UUID.fromString(edgeRS.getString(2))).getEdges().contains(tempEdge));

                    System.out.println("Added Edge to" + floorRS.getString(1));
                    // stores nodeEdges per floor
                    edges.put(edgeRS.getInt(1), tempEdge);
                    //stores all nodeEdges
                    nodeEdges.put(edgeRS.getInt(1), tempEdge);

                }
                System.out.println(edges);
                System.out.println(nodeEdges);

                Floor tempFloor = new Floor(floorRS.getInt(3));
                tempFloor.setImageLocation(floorRS.getString(4));
                // add floor to list of floors for current building
                flr.put(floorRS.getString(1), tempFloor);

                // add correct mapNodes to their respective floor
                for (MapNode n : nodes.values()) {
                    tempFloor.addNode(n);
                }
                // add correct nodeEdges to their respective floor
                for (NodeEdge e : edges.values()) {
                    tempFloor.addEdge(e);
                }

            }
            System.out.println(flr);
            buildings.put(rs.getInt(1),
                    new Building(rs.getString(2)));
            for (Floor f : flr.values()) {
                try {
                    buildings.get(rs.getInt(1)).addFloor(f);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        for (Building b : buildings.values()) {
            h.addBuilding(b);
        }

        System.out.println(mapNodes);
        System.out.println(h.getBuildings());

        // loading doctors, destinations, and offices to hospital
        HashMap<UUID, Destination> destsID = new HashMap<>();
        HashMap<String, Doctor> doctors = new HashMap<>();
        HashMap<String, Office> offices = new HashMap<>();
        PreparedStatement destDoc = conn.prepareStatement("select DEST_ID from DEST_DOC where doc_id = ?");
        PreparedStatement destOff = conn.prepareStatement("select * from OFFICES where DEST_ID = ?");

        rs = s.executeQuery("select * from USER1.DESTINATION");
        while (rs.next()) {

            // add offices to suite
            destOff.setString(1, rs.getString(1));
            ResultSet offRS = destOff.executeQuery();
            while(offRS.next()) {
                Office tempOff = new Office(UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        (h.getDestinations().get(UUID.fromString(rs.getString(1)))));

                // add office to hospital offices list
                h.addOffices(rs.getString(2), tempOff);

                // add office to list of offices for a suite
                h.getDestinations().get(UUID.fromString(rs.getString(1))).addOffice(tempOff);
            }
        }


        rs = s.executeQuery("select * from USER1.DOCTOR order by NAME");

        while(rs.next()) {
            HashSet<Destination> locations = new HashSet<>();

            destDoc.setString(1, rs.getString(1));
            ResultSet results = destDoc.executeQuery();
            while(results.next()) {
                locations.add(h.getDestinations().get(UUID.fromString(results.getString(1))));
            }

            Doctor tempDoc = new Doctor(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(5),
                    locations);

//            doctors.put(rs.getString(2),
//                    new Doctor(UUID.fromString(rs.getString(1)),
//                            rs.getString(2),
//                            rs.getString(3),
//                            rs.getString(5),
//                            locations));
            h.addDoctors(rs.getString(2), tempDoc);

        }
        System.out.println(doctors.keySet());
  
        rs.close();

    }

    private void saveHospital(Hospital h) throws SQLException {
        PreparedStatement insertBuildings = conn.prepareStatement("INSERT INTO BUILDING (BUILDING_ID, NAME) VALUES (?, ?)");
        PreparedStatement insertFloors = conn.prepareStatement("INSERT INTO FLOOR (FLOOR_ID, BUILDING_ID, NUMBER, IMAGE) VALUES (?, ?, ?, ?)");
        PreparedStatement insertNodes = conn.prepareStatement("INSERT INTO NODE (NODE_ID, POSX, POSY, FLOOR_ID, TYPE) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement insertEdges = conn.prepareStatement("INSERT INTO EDGE (EDGE_ID, NODEA, NODEB, COST, FLOOR_ID) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement insertDoctors = conn.prepareStatement("INSERT INTO USER1.DOCTOR (DOC_ID, NAME, DESCRIPTION, NUMBER, HOURS) VALUES (?, ?, ?, ?, ?)");
        PreparedStatement insertDestinations = conn.prepareStatement("INSERT INTO USER1.DESTINATION (DEST_ID, NAME, NODE_ID, FLOOR_ID) VALUES (?, ?, ?, ?)");
        PreparedStatement insertAssoc = conn.prepareStatement("INSERT INTO USER1.DEST_DOC (DEST_ID, DOC_ID) VALUES (?, ?)");
        PreparedStatement insertOffices = conn.prepareStatement("INSERT INTO USER1.OFFICES (OFFICE_ID, NAME, DEST_ID) VALUES (?, ?, ?)");

        int counter = 1;

        // insert buildings into database
        for (Building b : h.getBuildings()) {
            try {
                insertBuildings.setInt(1, counter);
                insertBuildings.setString(2, b.getName());
                insertBuildings.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error saving building " + e.getMessage());
            }
            conn.commit();

            int edgesCount = 1;
            // insert floors into database
            for (Floor f : b.getFloors()) {
                String floorID = "" + b.getName() + Integer.toString(f.getFloorNumber()) + "";
                try {
                    insertFloors.setString(1, floorID);
                    insertFloors.setInt(2, counter);
                    insertFloors.setInt(3, f.getFloorNumber());
                    insertFloors.setString(4, f.getImageLocation());
                    insertFloors.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error saving floor " + e.getMessage());
                }
                conn.commit();

                // insert nodes into database
                for (MapNode n : f.getFloorNodes()) {
                    if (n instanceof Destination)
                    {
                        if (!(h.getDestinations().containsKey(((Destination) n).getDestUID())))
                        {
                            h.addDestinations(((Destination) n).getDestUID(), (Destination) n);
                        }
                    }
                    try {
                        insertNodes.setString(1, n.getNodeID().toString());
                        insertNodes.setDouble(2, n.getPosX());
                        insertNodes.setDouble(3, n.getPosY());
                        insertNodes.setString(4, floorID);
                        insertNodes.setInt(5, n.getType());
                        insertNodes.executeUpdate();
                    }
                    catch (SQLException e) {
                        System.out.println("Error saving node " + e.getMessage());
                    }
                    conn.commit();
                }
                // insert edges into database
                for (NodeEdge edge : f.getFloorEdges()) {
                    try {
                        insertEdges.setInt(1, edgesCount);
                        insertEdges.setString(2, edge.getSource().getNodeID().toString());
                        insertEdges.setString(3, edge.getOtherNode(edge.getSource()).getNodeID().toString());
                        insertEdges.setDouble(4, edge.getCost());
                        insertEdges.setString(5, floorID);
                        insertEdges.executeUpdate();
                    }
                    catch (SQLException e) {
                        System.out.println("Error saving edge " + e.getMessage());
                    }
                    conn.commit();
                    edgesCount = edgesCount + 1;
                }
            }
            counter = counter + 1;
        }

        System.out.println("Here");
        // saves Suites
        for (Destination dest : h.getDestinations().values()) {
            try {
                insertDestinations.setString(1, dest.getDestUID().toString());
                insertDestinations.setString(2, dest.getName());
                insertDestinations.setString(3, dest.getNodeID().toString());
                insertDestinations.setString(4, dest.getFloorID());
                insertDestinations.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error saving suite " + e.getMessage());
            }
            conn.commit();
        }
        // saves Doctors
        for (Doctor doc : h.getDoctors().values()) {
            try {
                insertDoctors.setString(1, doc.getDocID().toString());
                insertDoctors.setString(2, doc.getName());
                insertDoctors.setString(3, doc.getDescription());
                insertDoctors.setString(4, "");
                insertDoctors.setString(5, doc.getHours());
                insertDoctors.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error saving doctor " + e.getMessage());
            }
            conn.commit();
            // saves Suite and Doctor Relationships
            for (Destination dest : doc.getDestinations()) {
                try {
                    insertAssoc.setString(1, dest.getDestUID().toString());
                    insertAssoc.setString(2, doc.getDocID().toString());
                    insertAssoc.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error saving suite_doc " + e.getMessage());
                }
                conn.commit();
                System.out.println("Added Suites");
            }
        }
        // saves Offices
        for (Office office : h.getOffices().values()) {
            try {
                insertOffices.setString(1, office.getId().toString());
                insertOffices.setString(2, office.getName());
                insertOffices.setString(3, office.getDestination().getDestUID().toString());
                insertOffices.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error saving edge" + e.getMessage());
            }
            conn.commit();
        }

    }

    public void shutdown() {
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
}
