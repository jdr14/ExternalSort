// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
//import java.util.*;

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
    private MinHeap mh;
    
    @Override
    /**
     * Runs before each test case.  Set up the any structures 
     * to be used in all tests
     */
    public void setUp() 
    {   
    	// Create a new instance of a minheap
        mh = new MinHeap();
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
    	
    	r = new Record();
        r.setID(5);
        r.setKey(4.739);
        assertEquals(r.getID(), 5);
        assertEquals(r.getKey(), 4.739, 0.000);
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
	}
	
	public void testRemoveSmallest()
	{
		Record temp = mh.removeSmallest();
		assertEquals(temp.getID(), 0);
		assertEquals(temp.getKey(), 0, 0);
		mh.insert(new Record(7, 8.923));
		mh.insert(new Record());
		mh.insert(new Record(5, 9.62));
		
		assertEquals(mh.arrayIsFull(), false);
		assertEquals(mh.heapIsFull(), false);
		
		assertEquals(mh.getleftChildIndex(0), 1);
		assertEquals(mh.getRightChildIndex(0), 2);
		assertEquals(mh.getParentIndex(1), 0);
		assertEquals(mh.getParentIndex(2), 0);
		assertEquals(mh.getRecord(1).getID(), 7);
		assertEquals(mh.getRecord(1).getKey(), 8.923, 0.000);
		
	    Record smallest = mh.removeSmallest();
	    assertEquals(smallest.getID(), 0);
	    assertEquals(smallest.getKey(), 0, 0);
		assertEquals(mh.getHeapSize(), 2);
		assertEquals(mh.getArraySize(), 2);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		
		smallest = mh.removeSmallest();
	    assertEquals(smallest.getID(), 7);
	    assertEquals(smallest.getKey(), 8.923, 0.000);
		assertEquals(mh.getHeapSize(), 1);
		assertEquals(mh.getArraySize(), 1);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		
		smallest = mh.removeSmallest();
	    assertEquals(smallest.getID(), 5);
	    assertEquals(smallest.getKey(), 9.62, 0.00);
		assertEquals(mh.getHeapSize(), 0);
		assertEquals(mh.getArraySize(), 0);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
	}
	
	public void testMinHeapify()
	{
		// Insert some values to create a basic minheap
		mh.insert(new Record(7, 8.923));
		mh.insert(new Record());
		mh.insert(new Record(5, 9.62));
		
		// Test the add to array method as well
		mh.addToArray(new Record(3, 2.71));
	    
		assertEquals(mh.getHeapSize(), 3);
		assertEquals(mh.getArraySize(), 4);
		assertEquals(mh.getNumItemsOutsideHeap(), 1);
		
		// Add a couple more values to the array
		mh.addToArray(new Record(4, 4.54));
		mh.addToArray(new Record(2, 1.79));
		
		assertEquals(mh.getHeapSize(), 3);
		assertEquals(mh.getArraySize(), 6);
		assertEquals(mh.getNumItemsOutsideHeap(), 3);    
		
		// Remove the 3 smallest items still in the heap
		mh.removeSmallest();
		mh.removeSmallest();
		mh.removeSmallest();
		
		assertEquals(mh.getHeapSize(), 0);
		assertEquals(mh.getArraySize(), 3);
		assertEquals(mh.getNumItemsOutsideHeap(), 3);
		
		// Transform items stored outside the array into a minheap
		mh.minHeapify(); 
		
		// Check the integrity of the new minHeap
		assertEquals(mh.getHeapSize(), 3);
		assertEquals(mh.getArraySize(), 3);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		
		Record[] r = mh.getArray();
		
		for (int i = 0; i < 4096; i++)
		{
			if (r[i] != null)
			{
				System.out.println("i = " + i + " ID = " + r[i].getID() 
						+ " : Key = " + r[i].getKey());
			}
		}
		
		// Now check the values of the minheap
	    assertEquals(mh.getRecord(0).getID(), 2);
		assertEquals(mh.getRecord(0).getKey(), 1.79, 0.00);
	}
	
	/**
	 * 
	 */
	public void testMergeSort()
	{
		// create merge sort using default constructor
		MergeSort m = new MergeSort(mh);
		
		// test the insert function
		m.mergeInsert(new Record(7, 8.923));
		m.mergeInsert(new Record(3, 1.023));
		m.mergeInsert(new Record(5, 9.62));
		
		// check specification to see if merge has correct numbers
		assertEquals(m.getMergeSize(), 3);
		assertEquals(m.getMaxSize(), 4096);
		assertEquals(m.isMergeFull(), false);
		
		// test the remove smallest function
		Record small = m.removeSmallest();
		
		// check correct record was returned
		assertEquals(small.getID(), 3);
		assertEquals(small.getKey(), 1.023);
	}
	
	/**
	 * 
	 */
	public void testPair()
	{
		// create pair using default constructor
		Pair<Integer, Boolean> p = new Pair<Integer, Boolean>();
		
		p.setKey(23);
		p.setValue(true);
		
		assertEquals((int) p.getKey(), 23);
		assertEquals((boolean) p.getValue(), true);
		
		// creates pair using constructor with parameters
		Pair<Integer, Boolean> p1 = new Pair<Integer, Boolean>(32, false);
		
		assertEquals((int) p1.getKey(), 32);
		assertEquals((boolean) p1.getValue(), false);
		
	}
}
