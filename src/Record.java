import java.nio.*;
/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 2.1
 */
public class Record 
{
	/**
	 * 1st 8 bytes in the record should be converted to a long
	 */
    private long recordID;
    
    /**
     * 2nd 8 bytes in the record should be converted to a double
     */
    private double recordKey;
    
    /**
     * Provide a default constructor
     */
    public Record()
    {
    	recordID = 0;
    	recordKey = 0;
    }
    
    /**
     * Constructor
     * @param id provides record id as a long
     * @param key provides record key value as a double
     */
	public Record(long id, double key)
	{
		recordID = id;
		recordKey = key;
	}
    
	/**
	 * 
	 * @param id as a byte array of length 8
	 */
	public void setID(long id)
	{
		// Convert the ID buffer to a long
		recordID = id;
	}
	
	/**
	 * 
	 * @param key as a byte array of length 8
	 */
	public void setKey(double key)
	{
		// Convert the Key buffer to a double
		recordKey = key;
	}
	
	/**
	 * 
	 * @return the recordID (1st 8 bytes of a record)
	 */
	public long getID()
	{
		return recordID;
	}
	
	/**
	 * 
	 * @return the recordKey (2nd 8 bytes of a record)
	 */
	public double getKey()
	{
		return recordKey;
	}
}
