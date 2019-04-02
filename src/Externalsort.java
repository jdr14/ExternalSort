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
	    
	    Record[] inputBuffer = p.ParseAsBytes(args[0]);
	    
	    for (int i = 0; i < inputBuffer.length; i++)
	    {
	    	System.out.print(i + ":  ");
	    	System.out.print("This is ID: " + inputBuffer[i].getID() + " ");
	    	System.out.println("This is key: " + inputBuffer[i].getKey());
	    }
	}

}
