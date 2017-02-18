package test.java;

import Domain.Map.*;
import Domain.Navigation.Guidance;
import Domain.Navigation.Path;
import Domain.Navigation.SendEmail;
import junit.framework.TestCase;


/**
 * Created by Samuel on 2/14/2017.\
 * pushing
 */
public class PathFindingTest2  extends TestCase{
    MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO, nodeP, nodeQ, nodeR, nodeS, nodeT;
    NodeEdge edgeAB, edgeBC, edgeBD, edgeDE, edgeAF, edgeCH, edgeHL, edgeEJ, nodeEP, edgePJ, edgeFQ, edgeFG, edgeGH, edgeFK, edgeJI, edgeSR, edgeRT, edgeRQ, edgeNM, edgeML, edgeMO, edgeOP;
    Path p1, p2, p3, p4;

    Floor floor1, floor2, floor3, floor4;
    Building building1;
    Hospital hospital1;

    public void setUp() {
        nodeA = new MapNode(1, 0, 0);
        nodeB = new MapNode(2, 0, 2);
        nodeC = new MapNode(3, 0, 4);
        nodeD = new MapNode(4, 3, 2);
        nodeE = new MapNode(5, 7, 2);
        nodeF = new MapNode(6, 0, 0);
        nodeG = new MapNode(7, 0, 2);
        nodeH = new MapNode(8, 0, 4);
        nodeI = new MapNode(9, 3, 2);
        nodeJ = new MapNode(10, 7, 2);
        nodeK = new MapNode(11, 3, 0);
        nodeL = new MapNode(12, 0, 4);
        nodeM = new MapNode(13, 0, 2);
        nodeN = new MapNode(14, 0, 0);
        nodeO = new MapNode(15, 3, 2);
        nodeP = new MapNode(16, 7, 2);
        nodeQ = new MapNode(17, 0, 0);
        nodeR = new MapNode(18, 0, 2);
        nodeS = new MapNode(19, 0, 4);
        nodeT = new MapNode(20, 7, 2);

        edgeAB = new NodeEdge(nodeA, nodeB, 2);
        edgeBC = new NodeEdge(nodeB, nodeC, 2);
        edgeBD = new NodeEdge(nodeB, nodeD, 3);
        edgeDE = new NodeEdge(nodeD, nodeE, 4);
        edgeAF = new NodeEdge(nodeA, nodeF, 0);
        edgeCH = new NodeEdge(nodeC, nodeH, 0);
        edgeFG = new NodeEdge(nodeF, nodeG, 2);
        edgeGH = new NodeEdge(nodeG, nodeH, 2);
        edgeJI = new NodeEdge(nodeJ, nodeI, 4);
        edgeFQ = new NodeEdge(nodeF, nodeQ, 0);
        edgeFK = new NodeEdge(nodeF, nodeK, 3);
        edgeNM = new NodeEdge(nodeN, nodeM, 2);
        edgeML = new NodeEdge(nodeM, nodeL, 2);
        edgeMO = new NodeEdge(nodeM, nodeO, 3);
        edgeOP = new NodeEdge(nodeO, nodeP, 4);
        edgeHL = new NodeEdge(nodeH, nodeL, 0);
        edgeEJ = new NodeEdge(nodeE, nodeJ, 0);
        edgePJ = new NodeEdge(nodeP, nodeJ, 0);
        edgeRQ = new NodeEdge(nodeR, nodeQ, 2);
        edgeSR = new NodeEdge(nodeS, nodeR, 2);
        edgeRT = new NodeEdge(nodeR, nodeT, 7);

        floor1 = new Floor(1);
        floor2 = new Floor(2);
        floor3 = new Floor(3);
        floor4 = new Floor(4);

        floor1.addNode(nodeA);
        floor1.addNode(nodeB);
        floor1.addNode(nodeC);
        floor1.addNode(nodeD);
        floor1.addNode(nodeE);

        floor2.addNode(nodeF);
        floor2.addNode(nodeG);
        floor2.addNode(nodeH);
        floor2.addNode(nodeI);
        floor2.addNode(nodeJ);
        floor2.addNode(nodeK);

        floor3.addNode(nodeL);
        floor3.addNode(nodeM);
        floor3.addNode(nodeM);
        floor3.addNode(nodeN);
        floor3.addNode(nodeO);
        floor3.addNode(nodeP);

        floor4.addNode(nodeS);
        floor4.addNode(nodeR);
        floor4.addNode(nodeQ);
        floor4.addNode(nodeT);

        building1 = new Building();
        hospital1 = new Hospital();

        try {
            building1.addFloor(floor1);
            building1.addFloor(floor2);
            building1.addFloor(floor3);
            building1.addFloor(floor4);
        } catch (Exception e) {
            hospital1.addBuilding(building1);
        }

    }

    public void testEqualPaths() throws Exception{
        try{
            p1 = new Path(nodeT, nodeC);
            p2 = new Path(nodeT, nodeC);

            assertTrue(p1.equals(p2));
            assertTrue(p2.equals(p1));

        } catch (Exception e){
            fail("Unexpected exception");
        }

    }

    public void testGuidance() {
        try{
            Guidance g1 = new Guidance(nodeT, nodeE, false);
            Guidance g2 = new Guidance(nodeB, nodeM, false);
            Guidance g3 = new Guidance(nodeA, nodeI, true);
            Guidance g4 = new Guidance(nodeA, nodeN, false);
            Guidance g5 = new Guidance(nodeB, nodeJ, false);
            Guidance g6 = new Guidance(nodeK, nodeI, false);
        } catch (Exception e){
            fail("Unexpected Exception");
        }
    }
    

}

