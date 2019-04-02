/**
 * 
 * @author Jovany Cabrera jovanyc4
 * @author Joey Destin Rodgers jdr14
 * @version 1.1.0
 *
 */
public class MinHeap {
	/**
	 * maximum size of min heap
	 */
	private int MAX_SIZE = 4096;
	
	/**
	 * 
	 */
	private Record[] heap;
	
	/**
	 * Current size of heap
	 */
	private int HEAP_SIZE;
	
	/**
	 * Default Constructor
	 */
	public MinHeap()
	{
		heap = new Record[MAX_SIZE];
		HEAP_SIZE = 0;
	}
	
	/**
	 * 
	 * @param newEntry of type Record
	 */
	public void insert(Record newEntry)
	{
		insertHelp(newEntry);
		HEAP_SIZE++;
	}
	
	private void insertHelp(Record newEntry)
	{
		
	}
}
