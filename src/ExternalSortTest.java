// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
import java.util.*;

import student.TestCase;

/**
 * 
 * @author Jovany Cabrera jovanyc4
 * @author Joseph Rodgers jdr14
 * @version 1.0.0
 *
 */
public class ExternalSortTest extends TestCase 
{
    
    @Override
    /**
     * Runs before each test case.  Set up the any structures 
     * to be used in all tests
     */
    public void setUp() 
    {   
    	// Create a new instance of a minheap
        //mh = new MinHeap();
        //r = new Record();
    }
    
    /**
     * 
     */
    public void testRecord()
    {
    	Record r = new Record();
    	// Test default constructor
    	assertEquals(r.getID(), 0, 0);
    	assertEquals(r.getKey(), 0, 0);
    	
    	r = new Record(7, 8.923);
    	assertEquals(r.getID(), 7);
    	assertEquals(r.getKey(), 8.923, 0.000);
    }
    
    /**
     * Test MinHeap Constructors
     */
	public void testMinHeap() 
	{
		MinHeap mh = new MinHeap();
		
		// test default constructor
		assertEquals(mh.getHeapSize(), 0);
		assertEquals(mh.getArraySize(), 0);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		
		// Create and populate the record array
		Record[] recArr = new Record[3];
		recArr[0] = new Record(7, 8.923);
		recArr[1] = new Record();
		recArr[2] = new Record(5, 9.62);
		
		mh.insert(new Record(7, 8.923));
		mh.insert(new Record());
		mh.insert(new Record(5, 9.62));
		assertEquals(mh.getRecord(0).getID(), recArr[1].getID());
		assertEquals(mh.getRecord(0).getKey(), recArr[1].getKey(), 0.000);
		assertEquals(mh.getRecord(1).getID(), recArr[0].getID());
		assertEquals(mh.getRecord(1).getKey(), recArr[0].getKey(), 0.000);
		assertEquals(mh.getRecord(2).getID(), recArr[2].getID());
		assertEquals(mh.getRecord(2).getKey(), recArr[2].getKey(), 0.000);
		
		// Create and populate the record array
		//Record[] recArr = new Record[3];
		recArr[0] = new Record(7, 8.923);
		recArr[1] = new Record();
		recArr[2] = new Record(5, 9.62);
		
		mh = new MinHeap(recArr, 3, 3);
		assertEquals(mh.getHeapSize(), 3);
		assertEquals(mh.getArraySize(), 3);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		assertEquals(mh.getRecord(0).getID(), 0);
		assertEquals(mh.getRecord(0).getKey(), 0, 0);
		assertEquals(mh.getRecord(1).getID(), 7);
		assertEquals(mh.getRecord(1).getKey(), 8.923, 0.000);
		assertEquals(mh.getRecord(2).getID(), 5);
		assertEquals(mh.getRecord(2).getKey(), 9.62, 0.00);
		
	}
}
