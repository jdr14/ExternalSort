import java.nio.*;
/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
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
     * Constructor
     */
	public Record(byte[] id, byte[] key)
	{
		// ID should be 8 bytes long
		assert id.length != 8 : "Error in Record: "
				+ "byte array for ID passed in not 8 bytes long.";
		// Key should be 8 bytes long
		assert key.length != 8 : "Error in Record: "
				+ "byte array for key passed in not 8 bytes long.";
		
		// Convert the key and id buffers to double and long 
		recordID = ByteBuffer.wrap(id).getLong();
		recordKey = ByteBuffer.wrap(key).getDouble();
	}
    
	/**
	 * 
	 * @param id as a byte array of length 8
	 */
	public void setID(byte[] id)
	{
		// ID should be 8 bytes long
		assert id.length != 8 : "Error in Record: "
				+ "byte array for ID passed in not 8 bytes long.";
		
		// Convert the ID buffer to a long
		recordID = ByteBuffer.wrap(id).getLong();
	}
	
	/**
	 * 
	 * @param key as a byte array of length 8
	 */
	public void setKey(byte[] key)
	{
		// Key should be 8 bytes long
		assert key.length != 8 : "Error in Record: "
				+ "byte array for key passed in not 8 bytes long.";
		
		// Convert the Key buffer to a double
		recordKey = ByteBuffer.wrap(key).getDouble();
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
