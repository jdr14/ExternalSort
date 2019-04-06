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
	/**
	 * Create a minheap variable
	 */
    private MinHeap mh;
	
    /**
     * Create a record variable
     */
    private Record r;
    
    @Override
    /**
     * Runs before each test case.  Set up the any structures 
     * to be used in all tests
     */
    public void setUp() 
    {   
    	// Create a new instance of a minheap
        mh = new MinHeap();
        r = new Record();
    }
    
    /**
     * 
     */
    public void testRecord()
    {
    	assertEquals(r.getID(), (long)0, 0);
    	assertEquals(r.getKey(), (double)0, 0);
    	
    	// Create a test ID (decimal value = 708222784)
    	byte[] testID = new byte[8]; //{(byte)0x2, (byte)0xa, (byte)0x3, (byte)0x6, 
    			//(byte)0x9, (byte)0xf, (byte)0x4, (byte)0x0};
    	
    	testID[0] = (byte)0x0;
    	testID[1] = (byte)0x0;
    	testID[2] = (byte)0x3;
    	testID[3] = (byte)0x6;
    	testID[4] = (byte)0x9;
    	testID[5] = (byte)0xf;
    	testID[6] = (byte)0x4;
    	testID[7] = (byte)0x0;
    	
    	// Create a test key (decimal value = 2076342047)
    	byte[] testKey = {(byte)0x7, (byte)0xb, (byte)0xc, (byte)0x2, 
    			(byte)0x7, (byte)0x7, (byte)0x1, (byte)0xf};
    	
    	System.out.println("ID and key size = " + testID.length + "   " + testKey.length);
    	
    	/*r.setID(testID);
    	assertEquals(r.getID(), (long)708222784);
    	assertEquals(r.getKey(), (double)0);
    	
    	r.setKey(testKey);
    	assertEquals(r.getID(), (long)708222784);
    	assertEquals(r.getKey(), (double)2076342047);*/
    	
    	r = new Record(testKey, testID);
    	assertEquals(r.getID(), (long)2076342047);
    	//assertEquals(r.getKey(), (double)507512585856680207);
    }
    
    /**
     * Test MinHeap Constructors
     */
	public void testMinHeap() 
	{
		assertEquals(mh.getHeapSize(), 0);
		assertEquals(mh.getArraySize(), 0);
		assertEquals(mh.getNumItemsOutsideHeap(), 0);
		
		Record[] recArr = new Record[5];
		//recArr[0] = 
		//mh = new MinHeap(recArr, 3, 2);
	}

}
