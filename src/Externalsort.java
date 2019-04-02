import java.util.*;

/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 1.0
 */
public class Externalsort 
{
    
	/**
	 * 
	 */
	public Externalsort() 
	{
		// TODO Auto-generated constructor stub
	}
    
	/**
	 * 
	 * @param args should include the filename
	 */
	public static void main(String[] args) 
	{
	    BinParse p = new BinParse();
	    
	    List<Byte> inputBuffer = p.ParseAsBytes(args[0]);
	    
	    for (int i = 0; i < inputBuffer.size(); i++)
	    {
	    	System.out.print(i + ":  ");
	    	System.out.println(String.format("0x%02X", inputBuffer.get(i)));
	    }
	}

}
