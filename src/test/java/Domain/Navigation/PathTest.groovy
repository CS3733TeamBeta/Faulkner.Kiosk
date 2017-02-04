package Domain.Navigation

import Domain.Map.Destination


/**
 * Created by IanCJ on 2/2/2017.
 */
class PathTest extends GroovyTestCase {

    Destination myDest1 = new Destination(1);
    Destination myDest2 = new Destination(1);
    Destination myDest3 = new Destination(2);

    void testDummy() {
        assertEquals(myDest1, myDest2);
    }

}
