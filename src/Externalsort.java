import java.util.*;

/**
 * 
 * @author Joey Rodgers jdr14
 * @author Jovany Cabrera jovanyc4
 * @version 1.0
 */
public class Externalsort 
{
	// On my honor: 
	//
	// - I have not used source code obtained from another student,
	// or any other unauthorized source, either modified or
	// unmodified. 
	//
	// - All source code and documentation used in my program is
	// either my original work, or was derived by me from the
	// source code published in the textbook for this course. 
	//
	// - I have not discussed coding details about this project with
	// anyone other than my partner (in the case of a joint
	// submission), instructor, ACM/UPE tutors or the TAs assigned
	// to this course. I understand that I may discuss the concepts
	// of this program with other students, and that another student
	// may help me debug my program so long as neither of us writes
	// anything during the discussion or modifies any computer file
	// during the discussion. I have violated neither the spirit nor
	// letter of this restriction.
	
	/**
	 * Default Constructor
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
	    
	    MinHeap inputBuffer = p.ParseAsBytes(args[0]);
	    
	    // for (int i = 0; i < inputBuffer.getHeapSize(); i++)
	    int i = 0;
	    while (inputBuffer.getHeapSize() > 0)
	    {
	    	Record temp = inputBuffer.removeSmallest(); // inputBuffer.getRecord(i);
	    	System.out.print(i + ":  ");
	    	System.out.print("This is ID: " + temp.getID() + " ");
	    	System.out.println("This is key: " + temp.getKey());
	        i++;
	    }
	}

}
