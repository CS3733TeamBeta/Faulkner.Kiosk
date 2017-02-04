package Domain.Navigation

import Domain.Map.*

import javax.xml.soap.Node


/**
 * Created by IanCJ on 2/2/2017.
 */
class PathTest extends GroovyTestCase {

    MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO, nodeP, nodeQ, nodeR, nodeS, nodeT, nodeZ;
    NodeEdge edgeAB, edgeBC, edgeCD, edgeBE, edgeCF, edgeDG, edgeEJ, edgeGK, edgeHI, edgeIJ, edgeJK, edgeLM, edgeMN, edgeMP, edgeNQ, edgeOP, edgePZ, edgePS, edgeQT, edgeLR, edgeRS, edgeST, edgeEZ;

    Hospital myHospital;
    Building myBuilding;
    Floor floor1;
    Floor floor2;

    @Override
    void setUp() {
        myHospital = new Hospital()
        myBuilding = new Building();
        floor1 = new Floor();
        floor2 = new Floor();
        nodeA = new MapNode(1);
        nodeB = new MapNode(2);
        nodeC = new MapNode(3);
        nodeD = new MapNode(4);
        nodeE = new MapNode(5);
        nodeF = new MapNode(6);
        nodeG = new MapNode(7);
        nodeH = new MapNode(8);
        nodeI = new MapNode(9);
        nodeJ = new MapNode(10);
        nodeK = new MapNode(11);
        nodeL = new MapNode(12);
        nodeM = new MapNode(13);
        nodeN = new MapNode(14);
        nodeO = new MapNode(15);
        nodeP = new MapNode(16);
        nodeQ = new MapNode(17);
        nodeR = new MapNode(18);
        nodeS = new MapNode(19);
        nodeT = new MapNode(20);
        nodeZ = new MapNode(24);
        edgeAB = new NodeEdge(nodeA, nodeB, 8);
        edgeBC = new NodeEdge(nodeA, nodeC, 5);
        edgeCD = new NodeEdge(nodeC, nodeD, 5);
        edgeBE = new NodeEdge(nodeB, nodeE, 3);
        edgeCF = new NodeEdge(nodeC, nodeF, 2);
        edgeDG = new NodeEdge(nodeD, nodeG, 6);
        edgeEJ = new NodeEdge(nodeE, nodeJ, 2);
        edgeGK = new NodeEdge(nodeG, nodeK, 1);
        edgeHI = new NodeEdge(nodeH, nodeJ, 3);
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

        myBuilding.addFloor(floor1);
        myBuilding.addFloor(floor2);

        myHospital.addBuilding(myBuilding);

    }

    void testDummy() {
        Path(nodeA, nodeB);
    }

}
