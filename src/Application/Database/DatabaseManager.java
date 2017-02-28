package Application.Database;

import Directory.*;
import Map.Entity.*;

import java.sql.*;
import java.util.*;

/**
 * Created by Brandon on 2/4/2017.
 */
public class DatabaseManager {

    private final String framework = "embedded";
    private final String protocol = CustomFilePath.myFilePath;

    private Connection conn = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private PreparedStatement psInsert;
    private PreparedStatement psUpdate;
    private Statement s;
    private ResultSet rs = null;
    private String username = "user1";
    private String uname = username.toUpperCase();

    private static String CAMPUS_ID = "CAMPUS";

    public static HashMap<UUID, MapNode> mapNodes = new HashMap<>();
    public static HashMap<UUID, NodeEdge> edges = new HashMap<>();
    public static HashMap<String, Doctor> doctors = new HashMap<>();
    public static HashMap<String, Floor> floors = new HashMap<>();
    public static HashMap<UUID, Destination> destinations = new HashMap<>();
    public static HashMap<String, Office> offices = new HashMap<>();

    public static DatabaseManager instance = null;

    public DatabaseManager() throws SQLException
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
        //executeStatements(dropTables);
        //executeStatements(createTables);
    }


    private void executeStatements(String[] states) throws SQLException {
        Statement state = conn.createStatement();
        for (String s : states) {
            state.executeUpdate(s);
            conn.commit();
        }
        state.close();
    }

    public Hospital loadData() throws SQLException {
        s = conn.createStatement();

        Hospital h = new Hospital();

        loadHospital(h);

        conn.commit();

        return h;
    }

    public void saveData(Hospital h) throws SQLException {
        s = conn.createStatement();
        executeStatements(dropTables);
        executeStatements(createTables);
        saveHospital(h);
        s.close();

        System.out.println("Data Saved Correctly");
    }

//    private synchronized void loadHospital(Hospital h) throws SQLException {
//        System.out.println("Started loading hospital from database...");
//
//        PreparedStatement floorsPS = conn.prepareStatement("SELECT * from FLOOR where BUILDING_ID = ?");
//        PreparedStatement nodesPS = conn.prepareStatement("SELECT * from NODE where FLOOR_ID = ?");
//        PreparedStatement destPS = conn.prepareStatement("SELECT * from DESTINATION where FLOOR_ID = ?");
//
//        PreparedStatement campusNodePS = conn.prepareStatement("SELECT * FROM NODE WHERE NODE_ID = ?");
//        s = conn.createStatement();
//
//        HashMap<UUID, Building> buildings = new HashMap<>();
//        HashMap<UUID, MapNode> mapNodes = new HashMap<>();
//        HashMap<Integer, NodeEdge> nodeEdges = new HashMap<>();
//
//        ResultSet rs = s.executeQuery("SELECT * FROM USER1.CAMPUS");
//
//        final int NODE_ID_COL = 1;
//
//        while(rs.next())
//        {
//            String node_UUID = rs.getString(1);
//            campusNodePS.setString(1, node_UUID);
//
//            ResultSet campusNodeRS = campusNodePS.executeQuery();
//
//            while(campusNodeRS.next())
//            {
//                MapNode tempNode = new MapNode(UUID.fromString(campusNodeRS.getString(1)),
//                        campusNodeRS.getDouble(2),
//                        campusNodeRS.getDouble(3),
//                        campusNodeRS.getInt(5));
//
//              //  for(tempNode)
//                mapNodes.put(UUID.fromString(node_UUID), tempNode);
//                h.getCampusFloor().addNode(tempNode);
//            }
//        }
//
//        rs = s.executeQuery("select * from BUILDING ORDER BY NAME DESC");
//
//        // load both buildingds in
//        while (rs.next())
//        {
//            if (!rs.getString(2).equals(CAMPUS_ID))
//            {
//                // load floors per building
//                HashMap<String, Floor> flr = new HashMap();
//                floorsPS.setString(1, rs.getString(1));
//                ResultSet floorRS = floorsPS.executeQuery();
//                while (floorRS.next())
//                {
//                    // load nodes per floor
//                    HashMap<UUID, MapNode> nodes = new HashMap<>(); // create new nodes hashmap for each floor
//                    nodesPS.setString(1, floorRS.getString(1)); // set query for specific floor
//                    ResultSet nodeRS = nodesPS.executeQuery();
//
//                    while (nodeRS.next())
//                    {
//                        UUID node_UUID = UUID.fromString(nodeRS.getString(1));
//
//                        MapNode tempNode;
//
//                        if (mapNodes.containsKey(node_UUID))
//                        {
//                            tempNode = mapNodes.get(node_UUID);
//                        }
//                        else
//                        {
//                            tempNode = new MapNode(node_UUID,
//                                    nodeRS.getDouble(2),
//                                    nodeRS.getDouble(3),
//                                    nodeRS.getInt(5));
//
//                            mapNodes.put(node_UUID, tempNode);
//                        }
//
//                        nodes.put(node_UUID, tempNode);
//
//                    }
//
//                    // loading destinations per floor
//                    destPS.setString(1, floorRS.getString(1));
//                    ResultSet destRS = destPS.executeQuery();
//
//                    while (destRS.next())
//                    {
//                        MapNode changedNode = nodes.get(UUID.fromString(destRS.getString(3)));
//                        Destination tempDest = new Destination(UUID.fromString(destRS.getString(1)),
//                                changedNode,
//                                destRS.getString(2),
//                                floorRS.getString(1));
//
//                        nodes.remove(UUID.fromString(destRS.getString(3)));
//                        nodes.put(UUID.fromString(destRS.getString(3)), tempDest);
//
//                        mapNodes.remove(UUID.fromString(destRS.getString(3)));
//                        mapNodes.put(UUID.fromString(destRS.getString(3)), tempDest);
//
//                        h.addDestinations(UUID.fromString(destRS.getString(1)), tempDest);
//                    }
//
//
//                    HashMap<Integer, NodeEdge> edges = new HashMap<>();
//
//
//                    // select all edges that have floorID of current floor we are loading
//                    edgesPS.setString(1, floorRS.getString(1));
//                    ResultSet edgeRS = edgesPS.executeQuery();
//
//                    while (edgeRS.next())
//                    {
//                        NodeEdge tempEdge = new NodeEdge();
//                        if (edgeRS.getDouble(4) != 0.0)
//                        {
//                            tempEdge = new NodeEdge(mapNodes.get(UUID.fromString(edgeRS.getString(2))),
//                                    mapNodes.get(UUID.fromString(edgeRS.getString(3))),
//                                    edgeRS.getFloat(4));
//
//                            tempEdge.setSource(mapNodes.get(UUID.fromString(edgeRS.getString(2)))); //should be redundant?
//                            tempEdge.setTarget(mapNodes.get(UUID.fromString(edgeRS.getString(3)))); //should be redundant?
//
//                            // stores nodeEdges per floor
//                            edges.put(edgeRS.getInt(1), tempEdge);
//                            //stores all nodeEdges
//                            nodeEdges.put(edgeRS.getInt(1), tempEdge);
//                        }
//                    }
//
//                    Floor tempFloor = new Floor(floorRS.getInt(3));
//                    tempFloor.setImageLocation(floorRS.getString(4));
//                    // add floor to list of floors for current building
//                    flr.put(floorRS.getString(1), tempFloor);
//
//                    // add correct mapNodes to their respective floor
//                    for (MapNode n : nodes.values())
//                    {
//                        tempFloor.addNode(n);
//                    }
//                }
//
//                //select all campus edges
//                edgesPS.setString(1, CAMPUS_ID);
//                ResultSet edgeRS = edgesPS.executeQuery();
//
//                while(edgeRS.next())
//                {
//                    NodeEdge tempEdge = new NodeEdge();
//                    if (edgeRS.getDouble(4) != 0.0)
//                    {
//                        try
//                        {
//                            tempEdge = new NodeEdge(mapNodes.get(UUID.fromString(edgeRS.getString(2))),
//                                mapNodes.get(UUID.fromString(edgeRS.getString(3))),
//                                edgeRS.getFloat(4));
//
//                            tempEdge.setSource(mapNodes.get(UUID.fromString(edgeRS.getString(2)))); //should be redundant?
//                            tempEdge.setTarget(mapNodes.get(UUID.fromString(edgeRS.getString(3)))); //should be redundant?
//                        }
//                        catch (Exception e)
//                        {
//
//                        }
//
//                        //stores all nodeEdges
//                        nodeEdges.put(edgeRS.getInt(1), tempEdge);
//                    }
//
//                }
//
//                buildings.put(UUID.fromString(rs.getString(1)),
//                        new Building(rs.getString(2)));
//                for (Floor f : flr.values())
//                {
//                    try
//                    {
//                        buildings.get(UUID.fromString(rs.getString(1))).addFloor(f);
//                    } catch (Exception e)
//                    {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//        }
//        for (Building b : buildings.values()) {
//            h.addBuilding(b);
//        }
//
//        rs = s.executeQuery("SELECT * from EDGE where COST = 0");
//        while(rs.next()) {
//            MapNode source = mapNodes.get(UUID.fromString(rs.getString(2)));
//            MapNode target = mapNodes.get(UUID.fromString(rs.getString(3)));
//            LinkEdge tempEdge = new LinkEdge(source, target);
//
//            nodeEdges.put(rs.getInt(1), tempEdge);
//        }
//
//        // loading doctors, destinations, and offices to hospital
//        HashMap<UUID, Destination> destsID = new HashMap<>();
//        HashMap<String, Doctor> doctors = new HashMap<>();
//        HashMap<String, Office> offices = new HashMap<>();
//        PreparedStatement destDoc = conn.prepareStatement("select DEST_ID from DEST_DOC where doc_id = ?");
//        PreparedStatement destOff = conn.prepareStatement("select * from OFFICES where DEST_ID = ?");
//
//        rs = s.executeQuery("select * from USER1.DESTINATION");
//        while (rs.next()) {
//
//            // add offices to suite
//            destOff.setString(1, rs.getString(1));
//            ResultSet offRS = destOff.executeQuery();
//            while(offRS.next()) {
//                Office tempOff = new Office(UUID.fromString(rs.getString(1)),
//                        offRS.getString(2),
//                        (h.getDestinations().get(UUID.fromString(rs.getString(1)))));
//
//                // add office to hospital offices list
//                h.addOffices(offRS.getString(2), tempOff);
//
//                // add office to list of offices for a suite
//                h.getDestinations().get(UUID.fromString(rs.getString(1))).addOffice(tempOff);
//            }
//        }
//
//
//        rs = s.executeQuery("select * from USER1.DOCTOR order by NAME");
//
//        while(rs.next()) {
//            HashSet<Destination> locations = new HashSet<>();
//
//            Doctor tempDoc = new Doctor(UUID.fromString(rs.getString(1)),
//                    rs.getString(2),
//                    rs.getString(3),
//                    rs.getString(5),
//                    locations);
//
//            destDoc.setString(1, rs.getString(1));
//            ResultSet results = destDoc.executeQuery();
//            while(results.next()) {
//                h.getDestinations().get(UUID.fromString(results.getString(1))).addDoctor(tempDoc);
//                locations.add(h.getDestinations().get(UUID.fromString(results.getString(1))));
//            }
//
//            h.addDoctors(rs.getString(2), tempDoc);
//
//        }
//        rs.close();
//
//        System.out.println("Database load finished");
//    }

    private void loadHospital(Hospital h) throws SQLException {
        loadCampus(h);
        loadBuilding(h);
        loadDestinationOffice(h);
        loadDoctors(h);
        loadEdges(h);
    }

    private void loadCampus(Hospital h) throws SQLException{
        PreparedStatement campusNodePS = conn.prepareStatement("SELECT * FROM NODE WHERE NODE_ID = ?");

        HashMap<UUID, MapNode> mapNodes = new HashMap<>();

        ResultSet rs = s.executeQuery("SELECT * FROM USER1.CAMPUS");

        final int NODE_ID_COL = 1;

        while(rs.next())
        {
            String node_UUID = rs.getString(1);
            campusNodePS.setString(1, node_UUID);

            ResultSet campusNodeRS = campusNodePS.executeQuery();

            while(campusNodeRS.next())
            {
                MapNode tempNode = new MapNode(UUID.fromString(campusNodeRS.getString(1)),
                        campusNodeRS.getDouble(2),
                        campusNodeRS.getDouble(3),
                        campusNodeRS.getInt(5));

                //  for(tempNode)
                mapNodes.put(UUID.fromString(node_UUID), tempNode);
                h.getCampusFloor().addNode(tempNode);
            }
        }
    }

    private void loadBuilding(Hospital h) throws SQLException
    {
        s = conn.createStatement();
        rs = s.executeQuery("SELECT * FROM BUILDING");

        Building b;

        while(rs.next()) {
            b = new Building(UUID.fromString(rs.getString(1)), rs.getString(2));
            loadFloors(h, b);
            h.addBuilding(b);
        }
    }

    private void loadFloors(Hospital h, Building b) throws SQLException {
        PreparedStatement floorsPS = conn.prepareStatement("SELECT * FROM FLOOR WHERE BUILDING_ID = ?");
        floorsPS.setString(1, b.getBuildID().toString());
        ResultSet floorRS = floorsPS.executeQuery();

        //floorsPS.setString(1, uuid.toString());

        UUID floor_id;
        Floor f;

        while(floorRS.next()) {
            floor_id = UUID.fromString(floorRS.getString(1));
            f = new Floor(floor_id, floorRS.getInt(3));
            f.setImageLocation(floorRS.getString(4));

            loadNodes(h, f);
            try {
                b.addFloor(f);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private void loadNodes(Hospital h, Floor f) throws SQLException
    {
        PreparedStatement nodesPS = conn.prepareStatement("SELECT * from NODE where FLOOR_ID = ?");
        PreparedStatement destPS = conn.prepareStatement("SELECT * FROM DESTINATION WHERE FLOOR_ID = ?");

        String floorID = f.getFloorID().toString();

        // load all nodes with floorID
        nodesPS.setString(1, floorID); // set query for specific floor
        ResultSet nodeRS = nodesPS.executeQuery();

        UUID nodeid;

        HashMap<UUID, MapNode> nodes = new HashMap<>();

        while(nodeRS.next()) {
            nodeid = UUID.fromString(nodeRS.getString(1));
            MapNode tempNode = new MapNode((nodeid),
                    nodeRS.getDouble(2),
                    nodeRS.getDouble(3),
                    nodeRS.getInt(5));

            // keep track of all nodes in hospital
            mapNodes.put(nodeid, tempNode);

            // nodes per floor
            nodes.put(nodeid, tempNode);
        }

        // load all destinations with floorID
        destPS.setString(1, floorID);
        ResultSet destRS = destPS.executeQuery();

        while(destRS.next()) {
            // UUID for node we are dealing with
            UUID tempNodeID = UUID.fromString(destRS.getString(3));
            // get node to be replaced by destination
            MapNode changedNode = mapNodes.get(tempNodeID);
            // create destination
            Destination tempDest = new Destination(UUID.fromString(destRS.getString(1)),
                    changedNode,
                    destRS.getString(2),
                    UUID.fromString(floorID));

            // replace regular nodes with destination nodes
            mapNodes.remove(tempNodeID);
            mapNodes.put(tempNodeID, tempDest);

            // replace nodes in current floor
            nodes.remove(tempNodeID);
            nodes.put(tempNodeID, tempDest);

            // adds destination to hospital list of destinations
            h.addDestinations(UUID.fromString(destRS.getString(1)), tempDest);
        }

        // add all nodes to floor
        for (MapNode n : nodes.values()) {
            f.addNode(n);
        }
    }

    private void loadEdges(Hospital h) throws SQLException
    {
        // select all for edges table
        rs = s.executeQuery("SELECT * FROM EDGE");


        while(rs.next()) {
            // create new edge
            NodeEdge tempEdge = new NodeEdge(UUID.fromString(rs.getString(1)),
                    mapNodes.get(UUID.fromString(rs.getString(2))),
                    mapNodes.get(UUID.fromString(rs.getString(3))),
                    rs.getFloat(4));

            tempEdge.setSource(mapNodes.get(UUID.fromString(rs.getString(2)))); //should be redundant?
            tempEdge.setTarget(mapNodes.get(UUID.fromString(rs.getString(3)))); //should be redundant?

            //System.out.println(mapNodes.get(UUID.fromString(rs.getString(2))).getEdges().contains(tempEdge));

            //stores all nodeEdges
            this.edges.put(UUID.fromString(rs.getString(1)), tempEdge);

        }
    }

    private void loadDoctors(Hospital h) throws SQLException
    {
        PreparedStatement destDoc = conn.prepareStatement("select DEST_ID from DEST_DOC where doc_id = ?");

        rs = s.executeQuery("select * from USER1.DOCTOR order by NAME");

        while(rs.next()) {
            HashSet<Destination> locations = new HashSet<>();

            // create new Doctor
            Doctor tempDoc = new Doctor(UUID.fromString(rs.getString(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(5),
                    locations);

            destDoc.setString(1, rs.getString(1));
            ResultSet results = destDoc.executeQuery();
            // create doctor - destination relationships within the objects
            while(results.next()) {
                h.getDestinations().get(UUID.fromString(results.getString(1))).addDoctor(tempDoc);
                locations.add(h.getDestinations().get(UUID.fromString(results.getString(1))));
            }
//            doctors.put(UUID.fromString(rs.getString(2)),
//                    new Doctor(UUID.fromString(rs.getString(1)),
//                            rs.getString(2),
//                            rs.getString(3),
//                            rs.getString(5),
//                            locations));
            h.addDoctors(rs.getString(2), tempDoc);

        }
        //System.out.println(doctors.keySet());
    }

    private void loadDestinationOffice(Hospital h) throws SQLException
    {
        PreparedStatement destOff = conn.prepareStatement("select * from OFFICES where DEST_ID = ?");


        rs = s.executeQuery("SELECT * FROM USER1.DESTINATION");
        while (rs.next()) {

            destOff.setString(1, rs.getString(1));
            // set of offices with particular destination ID foreign key
            ResultSet offRS = destOff.executeQuery();
            while(offRS.next()) {
                Office tempOff = new Office(UUID.fromString(rs.getString(1)),
                        offRS.getString(2),
                        (h.getDestinations().get(UUID.fromString(rs.getString(1)))));

                // add office to hospital offices list
                h.addOffices(offRS.getString(2), tempOff);
                //System.out.println("******************************" + tempOff.getName());

                // add office to list of offices for a destination
                h.getDestinations().get(UUID.fromString(rs.getString(1))).addOffice(tempOff);
            }
        }
    }

    public void addDocToDB(Doctor d) throws SQLException{
        PreparedStatement insertDoc = conn.prepareStatement("INSERT INTO DOCTOR (DOC_ID, NAME, DESCRIPTION, NUMBER, HOURS) " +
                "VALUES (?, ?, ?, ?, ?)");
        // insert doctor to database
        insertDoc.setString(1, d.getDocID().toString());
        insertDoc.setString(2, d.getName());
        insertDoc.setString(3, d.getDescription());
        insertDoc.setString(4, d.getPhoneNum());
        insertDoc.setString(5, d.getHours());
        insertDoc.executeUpdate();
        conn.commit();
    }

    public void updateDoctor(Doctor d) throws SQLException {
        PreparedStatement upDoc = conn.prepareStatement("UPDATE DOCTOR " +
                "SET NAME = ?, DESCRIPTION = ?, NUMBER = ?, HOURS = ?" +
                "WHERE DOC_ID = ?");

        // update Doctor in database with all info in case it changed
        upDoc.setString(1, d.getName());
        upDoc.setString(2, d.getDescription());
        upDoc.setString(3, d.getPhoneNum());
        upDoc.setString(4, d.getHours());
        upDoc.setString(5, d.getDocID().toString());
        upDoc.executeUpdate();
        conn.commit();
    }

    public void delDocFromDB(Doctor doctor) throws SQLException{
        PreparedStatement deleteDoc = conn.prepareStatement("DELETE FROM DOCTOR WHERE DOC_ID = ?");

        String docUUID = doctor.getDocID().toString();

        // delete doctor
        deleteDoc.setString(1, docUUID);
        deleteDoc.executeUpdate();
        conn.commit();
    }

    public void addDestToDB(Destination dest) throws SQLException {
        PreparedStatement insertDest = conn.prepareStatement("INSERT INTO DESTINATION (DEST_ID, NAME, NODE_ID, FLOOR_ID) " +
                "VALUES (?, ?, ?, ?)");
        // insert destination to database
        insertDest.setString(1, dest.getDestUID().toString());
        insertDest.setString(2, dest.getName());
        insertDest.setString(3, dest.getNodeID().toString());
        insertDest.setString(4, dest.getMyFloor().getFloorID().toString());
        insertDest.executeUpdate();
        conn.commit();
    }

    public void updateDestination(Destination dest) throws SQLException {
        PreparedStatement upDest = conn.prepareStatement("UPDATE DESTINATION " +
                "SET NAME = ? " +
                "WHERE DEST_ID = ?");

        upDest.setString(1, dest.getName());
        upDest.setString(2, dest.getDestUID().toString());
        upDest.executeUpdate();
        conn.commit();
    }

    public void delDestFromDB(Destination dest) throws SQLException {
        String destUUID = dest.getDestUID().toString();

        PreparedStatement deleteDest = conn.prepareStatement("DELETE FROM DESTINATION WHERE DEST_ID = ?");

        // delete destination
        deleteDest.setString(1, destUUID);
        deleteDest.executeUpdate();
        conn.commit();
    }

    public void addDestToDoc(Doctor doc, Destination dest) throws SQLException {
        PreparedStatement destDoc = conn.prepareStatement("INSERT INTO DEST_DOC (DEST_ID, DOC_ID) " +
                "VALUES (?, ?)");
        // adds destination - doctor relationships to database
        destDoc.setString(1, dest.getDestUID().toString());
        destDoc.setString(2, doc.getDocID().toString());
        destDoc.executeUpdate();
        conn.commit();
    }

    public void addKioskToDB(Kiosk k) throws SQLException {
        PreparedStatement insertKiosk = conn.prepareStatement("INSERT INTO KIOSK (NAME, NODE_ID, DIRECTION, FLAG) " +
                "VALUES (?, ?, ?, ?)");
        // insert kiosk to database
        insertKiosk.setString(1, k.getName());
        insertKiosk.setString(2, k.getNodeID().toString());
        insertKiosk.setString(3, k.getDirection());
        insertKiosk.setBoolean(4, false);
        insertKiosk.executeUpdate();
        conn.commit();
    }

    public void updateCurKiosk(Kiosk k) throws SQLException {
        PreparedStatement upOldKiosk = conn.prepareStatement("UPDATE KIOSK " +
                "SET FLAG = ? " +
                "WHERE FLAG = ?");

        upOldKiosk.setBoolean(1, false);
        upOldKiosk.setBoolean(2, true);
        upOldKiosk.executeUpdate();
        conn.commit();

        PreparedStatement upKiosk = conn.prepareStatement("UPDATE KIOSK " +
                "SET FLAG = ? " +
                "WHERE NODE_ID = ?");

        upKiosk.setBoolean(1, true);
        upKiosk.setString(2, k.getNodeID().toString());
        upKiosk.executeUpdate();
        conn.commit();
    }

    public void addNodeToDB(MapNode m) throws SQLException {
        PreparedStatement insertNode = conn.prepareStatement("INSERT INTO NODE (NODE_ID, POSX, POSY, FLOOR_ID, TYPE) " +
                "VALUES (?, ?, ?, ?, ?)");

        insertNode.setString(1, m.getNodeID().toString());
        insertNode.setDouble(2, m.getPosX());
        insertNode.setDouble(3, m.getPosY());
        insertNode.setString(4, m.getMyFloor().getFloorID().toString());
        insertNode.setInt(5, m.getType().ordinal());
        insertNode.executeUpdate();
        conn.commit();
        System.out.println("Added node to database");
    }

    public void updateNode(MapNode m) throws SQLException {
        PreparedStatement upNode = conn.prepareStatement("UPDATE NODE " +
                "SET POSX = ?, POSY = ?" +
                "WHERE NODE_ID = ?");

        // update Node in database with new pos X and pos Y
        upNode.setDouble(1, m.getPosX());
        upNode.setDouble(2, m.getPosY());
        upNode.setString(3, m.getNodeID().toString());
        upNode.executeUpdate();
        conn.commit();
    }

    public void delNodeFromDB(MapNode m) throws SQLException {
        UUID nodeUUID = m.getNodeID();

        PreparedStatement delNode = conn.prepareStatement("DELETE FROM NODE WHERE NODE_ID = ?");

        // delete node with given uuid
        delNode.setString(1, nodeUUID.toString());
        delNode.executeUpdate();
        conn.commit();
    }

    public void addEdgeToDB(NodeEdge e) throws SQLException {
        PreparedStatement insertEdge = conn.prepareStatement("INSERT INTO EDGE (EDGE_ID, NODEA, NODEB, COST) " +
                "VALUES (?, ?, ?, ?)");

        insertEdge.setString(1, e.getEdgeID().toString());
        insertEdge.setString(2, e.getSource().getNodeID().toString());
        insertEdge.setString(3, e.getTarget().getNodeID().toString());
        insertEdge.setDouble(4, e.getCost());
        insertEdge.executeUpdate();
        conn.commit();
    }

    public void updateEdge(NodeEdge e) throws SQLException {
        PreparedStatement upEdge = conn.prepareStatement("UPDATE EDGE " +
                "SET COST = ? " +
                "WHERE EDGE_ID = ?");

        // update edge in database with new cost
        upEdge.setDouble(1, e.getCost());
        upEdge.setString(2, e.getEdgeID().toString());
    }

    public void delEdgeFromDB(NodeEdge e) throws SQLException { // TODO change edges to have UUID
        PreparedStatement delEdge = conn.prepareStatement("DELETE FROM EDGE WHERE EDGE_ID = ?");

        // delete all edges that have given node as source or target
        delEdge.setString(1, e.getEdgeID().toString());
        delEdge.executeUpdate();
        conn.commit();
    }

    public void addOfficeToDB(Office o) throws SQLException {
        PreparedStatement insertOffice = conn.prepareStatement("INSERT INTO OFFICES (OFFICE_ID, NAME, DEST_ID) " +
                "VALUES (?, ?, ?)");
        // insert office to database
        insertOffice.setString(1, o.getId().toString());
        insertOffice.setString(2, o.getName());
        insertOffice.setString(3, o.getDestination().getDestUID().toString());
        insertOffice.executeUpdate();
        conn.commit();
    }

    public void updateOffice(Office o) throws SQLException {
        PreparedStatement upOff = conn.prepareStatement("UPDATE OFFICES " +
                "SET NAME = ?, DEST_ID = ? " +
                "WHERE OFFICE_ID = ?");
        // get office UUID
        String offUUID = o.getId().toString();
        // get destination UUID for office
        String offDest = o.getDestination().getDestUID().toString();
        // update office info in database
        upOff.setString(1, o.getName());
        upOff.setString(2, offDest);
        upOff.setString(3, offUUID);
        upOff.executeUpdate();
        conn.commit();
    }

    public void delOfficeFromDB(Office o) throws SQLException {
        PreparedStatement delOffice = conn.prepareStatement("DELETE FROM OFFICES WHERE OFFICE_ID = ?");
        // get office UUID
        String offUUID = o.getId().toString();
        // delete office from database
        delOffice.setString(1, offUUID);
        delOffice.executeUpdate();
        conn.commit();
    }

    public void deleteFloor(Floor floor) throws SQLException {
        PreparedStatement floorsPS = conn.prepareStatement("DELETE FROM FLOOR WHERE FLOOR_ID = ?");
        s = conn.createStatement();

        s.executeQuery("SELECT * FROM USER1.FLOOR ORDER BY NUMBER DESC ");

        while(rs.next())
        {
            floorsPS.setString(1, floor.getFloorID().toString());
            floorsPS.executeUpdate();
        }
    }

    public void addFloor(Floor f, Building b) throws SQLException {
        PreparedStatement insertFloors = conn.prepareStatement("INSERT INTO FLOOR (FLOOR_ID, BUILDING_ID, NUMBER, IMAGE) VALUES (?, ?, ?, ?)");

        insertFloors.setString(1, f.getFloorID().toString());
        insertFloors.setString(2, b.getBuildID().toString());
        insertFloors.setInt(3, f.getFloorNumber());
        insertFloors.setString(4, f.getImageLocation());
        insertFloors.executeUpdate();
    }

    public void deleteBuilding(Building building) throws SQLException {
        PreparedStatement buildingPS = conn.prepareStatement("DELETE FROM BUILDING WHERE BUILDING_ID = ?");
        s = conn.createStatement();

        s.executeQuery("SELECT * FROM USER1.BUILDING");

        while(rs.next())
        {
            buildingPS.setString(1, building.getBuildID().toString());
            buildingPS.executeUpdate();
        }
    }

    public void addBuilding(Building building) throws SQLException {
        PreparedStatement insertBuildings = conn.prepareStatement("INSERT INTO BUILDING (BUILDING_ID, NAME) VALUES (?, ?)");

        insertBuildings.setString(1, building.getBuildID().toString());
        insertBuildings.setString(2, building.getName());
        insertBuildings.executeUpdate();
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

        PreparedStatement insertCampusNodes = conn.prepareStatement("INSERT INTO USER1.CAMPUS (NODE_ID) VALUES ?");

        int counter = 1;

        HashMap<NodeEdge, String> collectedEdges = new HashMap<>();

        for(MapNode n: h.getCampusFloor().getCampusNodes())
        {
            insertBuildings.setInt(1, counter);
            insertBuildings.setString(2, CAMPUS_ID);
            insertBuildings.executeUpdate();

            insertFloors.setString(1, CAMPUS_ID);
            insertFloors.setInt(2, counter);
            insertFloors.setInt(3, 1);
            insertFloors.setString(4, h.getCampusFloor().getImageLocation());
            insertFloors.executeUpdate();

            insertNodes.setString(1, n.getNodeID().toString());
            insertNodes.setDouble(2, n.getPosX());
            insertNodes.setDouble(3, n.getPosY());
            insertNodes.setString(4, CAMPUS_ID);
            insertNodes.setInt(5, n.getType().ordinal());
            insertNodes.executeUpdate();

            insertCampusNodes.setString(1, n.getNodeID().toString());
            insertCampusNodes.executeUpdate();
            conn.commit();

            for(NodeEdge edge: n.getEdges())
            {
                if(!collectedEdges.containsKey(edge)) //if current collection of edges doesn't contain this edge,
                {                                   //add it. (Will load ot db later)
                    collectedEdges.put(edge, CAMPUS_ID);
                }
            }
        }

        counter++;

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
                    try {
                        insertNodes.setString(1, n.getNodeID().toString());
                        insertNodes.setDouble(2, n.getPosX());
                        insertNodes.setDouble(3, n.getPosY());
                        insertNodes.setString(4, floorID);
                        insertNodes.setInt(5, n.getType().ordinal());
                        insertNodes.executeUpdate();
                    }
                    catch (SQLException e) {
                        System.out.println("Error saving node " + e.getMessage());
                    }
                    conn.commit();
                    if (n instanceof Destination)
                    {
                        try {
                            insertDestinations.setString(1, ((Destination)n).getDestUID().toString());
                            insertDestinations.setString(2, ((Destination)n).getName());
                            insertDestinations.setString(3, n.getNodeID().toString());
                            insertDestinations.setString(4, floorID);
                            insertDestinations.executeUpdate();
                        }
                        catch (SQLException e) {
                            System.out.println("Error saving suite " + e.getMessage());
                        }
                        conn.commit();
                    }

                    for(NodeEdge edge: n.getEdges())
                    {
                        if(!collectedEdges.containsKey(edge)) //if current collection of edges doesn't contain this edge,
                        {                                   //add it. (Will load ot db later)
                            collectedEdges.put(edge, floorID);
                        }
                    }
                }
            }
            counter = counter + 1;
        }

        int edgesCount = 1;
        // insert edges into database
        for (NodeEdge edge : collectedEdges.keySet()) {
            try {
                insertEdges.setInt(1, edgesCount);
                insertEdges.setString(2, edge.getSource().getNodeID().toString());
                insertEdges.setString(3, edge.getOtherNode(edge.getSource()).getNodeID().toString());
                insertEdges.setDouble(4, edge.getCost());
                insertEdges.setString(5, collectedEdges.get(edge));
                insertEdges.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error saving edge " + e.getMessage());
            }

            conn.commit();
            edgesCount++;
        }

        
        for (Doctor doc : h.getDoctors().values()) {
            try {
                insertDoctors.setString(1, doc.getDocID().toString());
                insertDoctors.setString(2, doc.getName());
                insertDoctors.setString(3, doc.getDescription());
                insertDoctors.setString(4, doc.getPhoneNum());
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

            "CREATE TABLE KIOSK" +
                    "NAME VARCHAR(30)," +
                    "NODE_ID CHAR(36)," +
                    "FLAG SMALLINT," +
                    "CONSTRAINT KIOSK_NODE_NODE_ID_FK FOREIGN KEY (NODE_ID) REFERENCES NODE (NODE_ID) ON DELETE CASCADE\n" +
                    ")",

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
                    "CONSTRAINT DESTINATION_DOC_DOCTOR_DOCTOR_ID_FK2 FOREIGN KEY (DOC_ID) REFERENCES DOCTOR (DOC_ID))",

            "CREATE TABLE CAMPUS (NODE_ID CHAR(36), " +
                    "CONSTRAINT CAMPUS_NODE_NODE_ID_FK FOREIGN KEY (NODE_ID) REFERENCES NODE (NODE_ID))"
    };

    public static final String[] dropTables = {
            "DROP TABLE USER1.CAMPUS",
            "DROP TABLE USER1.DEST_DOC",
            "DROP TABLE USER1.EDGE",
            "DROP TABLE USER1.OFFICES",
            "DROP TABLE USER1.DOCTOR",
            "DROP TABLE USER1.DESTINATION",
            "DROP TABLE USER1.KIOSK",
            "DROP TABLE USER1.NODE",
            "DROP TABLE USER1.FLOOR",
            "DROP TABLE USER1.BUILDING"};
}
