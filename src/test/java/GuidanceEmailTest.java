package test.java;

import Domain.Map.*;
import Domain.Navigation.Guidance;
import Domain.Navigation.Path;
import junit.framework.TestCase;


/**
 * Created by IanCJ on 2/2/2017.
 *
 * Jesus christ fuck this test
 *
 */
public class GuidanceEmailTest extends TestCase{

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
        nodeA = new MapNode(0, 5);
        nodeB = new MapNode(10, 5);
        nodeC = new MapNode( 15, 10);
        nodeD = new MapNode( 20, 10);
        nodeE = new MapNode( 10, 5);
        nodeF = new MapNode( 15, 5);
        nodeG = new MapNode( 20, 5);
        nodeH = new MapNode( 0, 0);
        nodeI = new MapNode( 5, 0);
        nodeJ = new MapNode(10, 0);
        nodeK = new MapNode( 20, 0);
        nodeL = new MapNode( 0, 10);
        nodeM = new MapNode( 10, 10);
        nodeN = new MapNode( 20, 10);
        nodeO = new MapNode( 5, 5);
        nodeP = new MapNode( 10, 5);
        nodeQ = new MapNode( 20, 5);
        nodeR = new MapNode( 0, 0);
        nodeS = new MapNode(10, 0);
        nodeT = new MapNode(20, 0);
        nodeX = new MapNode( 25, 0);
        nodeY = new MapNode( 25, 5);
        nodeZ = new MapNode( 15, 5);
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

    public void testSendEmail() {
        try {
            Guidance g1 = new Guidance(nodeA, nodeO, true);
            //Please don't uncomment this line unless you replace the email with your own. I'm getting a lot of spam -Ian
            g1.sendEmailGuidance("iancj97@gmail.com");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Threw exception");
        }
    }
}
