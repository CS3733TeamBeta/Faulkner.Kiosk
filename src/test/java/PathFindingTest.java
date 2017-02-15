package test.java;

import Domain.Map.*;
import Domain.Navigation.Guidance;
import Domain.Navigation.Path;
import Domain.Navigation.*;
import Exceptions.PathFindingNoPathException;
import junit.framework.TestCase;

import java.rmi.server.UID;
import java.util.LinkedList;


/**
 * Created by IanCJ on 2/2/2017.
 *
 * Jesus christ fuck this test
 *
 */
public class PathFindingTest extends TestCase{

    MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO, nodeP, nodeQ, nodeR, nodeS, nodeT, nodeX, nodeY, nodeZ;
    NodeEdge edgeAB, edgeBC, edgeCD, edgeBE, edgeCF, edgeDG, edgeEJ, edgeGK, edgeAH, edgeHI, edgeIJ, edgeJK, edgeLM, edgeMN, edgeMP, edgeNQ, edgeOP, edgePZ, edgePS, edgeQT, edgeLR, edgeRS, edgeST, edgeYX, edgeEZ;
    Path p1, p2, p3, p4, p5;
    Guidance g1, g2, g3, g4, g5;

    Hospital myHospital;
    Building myBuilding;
    Floor floor1;
    Floor floor2;

    @Override
    public void setUp() {
        myHospital = new Hospital();
        myBuilding = new Building();
        floor1 = new Floor(1);
        floor2 = new Floor(2);
        nodeA = new MapNode(1, 0, 10);
        nodeB = new MapNode(2, 10, 10);
        nodeC = new MapNode(3, 15, 10);
        nodeD = new MapNode(4, 20, 10);
        nodeE = new MapNode(5, 10, 5);
        nodeF = new MapNode(6, 15, 5);
        nodeG = new MapNode(7, 20, 5);
        nodeH = new MapNode(8, 0, 0);
        nodeI = new MapNode(9, 5, 0);
        nodeJ = new MapNode(10, 10, 0);
        nodeK = new MapNode(11, 20, 0);
        nodeL = new MapNode(12, 0, 10);
        nodeM = new MapNode(13, 10, 10);
        nodeN = new MapNode(14, 20, 10);
        nodeO = new MapNode(15, 5, 5);
        nodeP = new MapNode(16, 10, 5);
        nodeQ = new MapNode(17, 20, 5);
        nodeR = new MapNode(18, 0, 0);
        nodeS = new MapNode(19, 10, 0);
        nodeT = new MapNode(20, 20, 0);
        nodeX = new MapNode(22, 25, 0);
        nodeY = new MapNode(23, 25, 5);
        nodeZ = new MapNode(24, 15, 5);
        edgeAB = new NodeEdge(nodeA, nodeB, 8);
        edgeBC = new NodeEdge(nodeB, nodeC, 5);
        edgeCD = new NodeEdge(nodeC, nodeD, 5);
        edgeBE = new NodeEdge(nodeB, nodeE, 3);
        edgeCF = new NodeEdge(nodeC, nodeF, 2);
        edgeDG = new NodeEdge(nodeD, nodeG, 6);
        edgeEJ = new NodeEdge(nodeE, nodeJ, 2);
        edgeGK = new NodeEdge(nodeG, nodeK, 1);
        edgeAH = new NodeEdge(nodeA, nodeH, 5);
        edgeHI = new NodeEdge(nodeH, nodeI, 3);
        edgeIJ = new NodeEdge(nodeI, nodeJ, 5);
        edgeJK = new NodeEdge(nodeJ, nodeK, 10);
        edgeLM = new NodeEdge(nodeL, nodeM, 5);
        edgeMN = new NodeEdge(nodeM, nodeN, 10);
        edgeMP = new NodeEdge(nodeM, nodeP, 8);
        edgeNQ = new NodeEdge(nodeN, nodeQ, 3);
        edgeOP = new NodeEdge(nodeO, nodeP, 2);
        edgePZ = new NodeEdge(nodeP, nodeZ, 4);
        edgePS = new NodeEdge(nodeP, nodeS, 2);
        edgeQT = new NodeEdge(nodeQ, nodeT, 2);
        edgeLR = new NodeEdge(nodeL, nodeR, 6);
        edgeRS = new NodeEdge(nodeR, nodeS, 9);
        edgeST = new NodeEdge(nodeS, nodeT, 8);
        edgeYX = new NodeEdge(nodeY, nodeX, 3);
        edgeEZ = new NodeEdge(nodeE, nodeZ, 0);

        floor1.addNode(nodeA);
        floor1.addNode(nodeB);
        floor1.addNode(nodeC);
        floor1.addNode(nodeD);
        floor1.addNode(nodeE);
        floor1.addNode(nodeF);
        floor1.addNode(nodeG);
        floor1.addNode(nodeH);
        floor1.addNode(nodeI);
        floor1.addNode(nodeJ);
        floor1.addNode(nodeK);

        floor2.addNode(nodeL);
        floor2.addNode(nodeM);
        floor2.addNode(nodeN);
        floor2.addNode(nodeO);
        floor2.addNode(nodeP);
        floor2.addNode(nodeZ);
        floor2.addNode(nodeQ);
        floor2.addNode(nodeR);
        floor2.addNode(nodeS);
        floor2.addNode(nodeT);
        floor2.addNode(nodeY);
        floor2.addNode(nodeX);

        try {
            myBuilding.addFloor(floor1);
            myBuilding.addFloor(floor2);
        } catch(Exception e) {}
        myHospital.addBuilding(myBuilding);
    }

    //Tests that UIDs are working properly
    public void testUID() {
        UID id = nodeA.getNodeUID();
        assertEquals(nodeA, nodeA);
        assertEquals(nodeB, nodeB);
        assertNotSame(nodeA, nodeB);
    }

    //Tests that IsValidPath is properly testing path validity
    public void testIsValidPath() {
        LinkedList<MapNode> listOfNodes = new LinkedList<MapNode>();
        LinkedList<MapNode> listOfInvalidNodes = new LinkedList<MapNode>();
        listOfNodes.add(nodeA);
        listOfNodes.add(nodeB);
        listOfInvalidNodes.add(nodeA);
        listOfInvalidNodes.add(nodeE);
        LinkedList<NodeEdge> listOfEdges = new LinkedList<NodeEdge>();
        listOfEdges.add(edgeAB);
        Path manPathValid = new Path(listOfEdges, listOfNodes);
        Path manPathInvalid = new Path(listOfEdges, listOfInvalidNodes);
        assertTrue(manPathValid.isValidPath());
        assertFalse(manPathInvalid.isValidPath());
    }


    public void testNoValidPathException() {
        try {
            Path p = new Path(nodeA, nodeX);
            fail("Failed to notice no possible path");
        } catch (Exception e) {
            assertTrue(e instanceof PathFindingNoPathException);
        }
        try {
            Path p = new Path(nodeY, nodeL);
            fail("Failed to notice no possible path");
        } catch (Exception e) {
            assertTrue(e instanceof PathFindingNoPathException);
        }

    }

    public void testEquals() {
        try {
            p1 = new Path(nodeA, nodeL);
            p2 = new Path(nodeA, nodeL);
            p3 = new Path(nodeL, nodeA);
            p4 = new Path(nodeB, nodeD);
            p5 = new Path(nodeA, nodeD);
            assertTrue(p1.equals(p2));
            assertTrue(p2.equals(p1));
            assertFalse(p1.equals(p3));
            assertFalse(p3.equals(p1));
            assertFalse(p4.equals(p5));
            assertFalse(p5.equals(p4));
            assertFalse(p3.equals(p5));
            assertFalse(p5.equals(p3));
        } catch(Exception e) {
            fail("Threw unexpected exception in testEquals");
        }
    }

    public void testGuidance() {
        try {
            g1 = new Guidance(nodeT, nodeH, false);
            g2 = new Guidance(nodeO, nodeI, true);
            assertTrue(true);
       } catch(Exception e) {
            fail("Threw unexpected exception in testGuidance");
        }
    }

    public void testBreadth() {
       try {
            p1 = new Path(nodeA, nodeL, true, "breadthfirst");
        } catch (Exception e) {
            fail("Threw unexpected exception in testBreadth");
        }
    }

    public void testDepth() {
        try {
            p1 = new Path(nodeA, nodeL, true, "depthfirst");
        } catch (Exception e) {
            fail("Threw unexpected exception in testDepth");
        }
    }

    public void testRandom() {
        try {
            p1 = new Path(nodeA, nodeL, true, "random");
        } catch (Exception e) {
            fail("Threw unexpected exception in testRandom");
        }
    }
}
