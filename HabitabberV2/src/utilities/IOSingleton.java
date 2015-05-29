package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;

/**
 * Singleton class for the IO in order to make this project extendable for Multithreading (e.g., by a Semaphore)
 * and to provide better maintenance conditions.
 */
public class IOSingleton {
	/**
	 * The singleton class.
	 */
	private static IOSingleton iOSingleton;

	/**
	 * The list of attribute names (i.e., titles).
	 */
	private static List<String> attributeNameLst = new ArrayList<String>();

	/**
	 * The iterator for the attributes.
	 */
	private Iterator<String> attributeIterator;

	/**
	 * The current attribute.
	 */
	private String currentAttribute;

	/**
	 * The reader.
	 */
	private BufferedReader br;

	/**
	 * The writer.
	 */
	private PrintWriter bw;

	private String readFileName = "file.txt";
	private String writerFileName = "file.txt";

	/**
	 * Block for initializing the buffered reader and writer when a new object is made.
	 */
	{
		try {			
			if (bw == null) {
				bw = new PrintWriter(new BufferedWriter(new FileWriter(writerFileName, false)));
			}
			if (br == null) {
				//				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getReadFileName()))));
				;
			} 
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}		

	private IOSingleton(boolean initialize) {
		this(initialize, false);
	}

	/**
	 * The private constructor.
	 * If initialize is set to true, the initial line of a CSV will be read and global variables will be initialized accordingly.
	 */
	private IOSingleton(boolean initialize, boolean retrieveOldSession) {

		try {			
			if (bw == null) {
				bw = new PrintWriter(new BufferedWriter(new FileWriter(writerFileName, retrieveOldSession)));
			}
			if (br == null || retrieveOldSession) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getReadFileName()))));
			} 
		} catch (IOException e) { 
			e.printStackTrace();
		}

		if (initialize) {						
			readLine();											
			String attribute = "";
			while ((attribute = readAttribute()) != null) {
				attributeNameLst.add(attribute);				
			}
		}
	}

	public void reset(boolean retrieveOldSession) {
		readFileName = "file.txt";
		writerFileName = "file.txt";
		currentAttribute = null;
		attributeNameLst = new ArrayList<String>();
		attributeIterator = null;
		if (bw != null) {
			bw.flush();
			bw.close();

		} 
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw = new PrintWriter(new BufferedWriter(new FileWriter(writerFileName, retrieveOldSession)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getReadFileName()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The getter for this singleton.
	 * @return IOSingleton
	 */
	public static IOSingleton getIOSingleton() {
		return getIOSingleton(false);
	}

	/**
	 * Retrieves the singleton.
	 * @return IOSingleton
	 */
	public static IOSingleton getIOSingleton(boolean readInitialLine) {
		return getIOSingleton(readInitialLine, false);
	}

	/**
	 * Retrieves the singleton.
	 * @return IOSingleton
	 */
	public static IOSingleton getIOSingleton(boolean readInitialLine, boolean retrieveOldSession) {
		if (iOSingleton == null) {
			iOSingleton = new IOSingleton(readInitialLine);					
		}
		return iOSingleton;
	}

	/**
	 * Getter for the current attribute.
	 * @return String
	 */
	public String getCurrentAttribute() {
		return currentAttribute;
	}

	/**
	 * Getter for attributeNames.
	 * @return
	 */
	public List<String> getAttributeNames() {
		return attributeNameLst;
	}		

	/**
	 * Read an attribute within the attributeIterator.
	 * @return String
	 */
	public String readAttribute() {		
		if (attributeIterator != null && attributeIterator.hasNext()) {
			currentAttribute = attributeIterator.next();
		} else {
			currentAttribute = null;
		}
		return currentAttribute;
	}	

	public void write(String str) {
		write(str, false);
	}

	/**
	 * Writes a value to the file "testdata.new.csv".
	 * @param str The value
	 */
	public void write(String str, boolean writeNClose) {		
		write(str, writeNClose, true);
	}

	/**
	 * Writes a value to the file "testdata.new.csv".
	 * @param str The value
	 */
	public void write(String str, boolean writeNClose, boolean append) {
		try {
			if (writeNClose) {
				if (bw != null) {
					closeWriter();
					bw = new PrintWriter(new BufferedWriter(new FileWriter(writerFileName, append)));					
				}			
			}		
			bw.write(str);	
			if (writeNClose) {
				closeWriter();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a line of the file "testdata.csv".
	 * @return String
	 */
	public String readLine() {		
		String result = null;
		try {			
			if (br.ready()) {				
				result = br.readLine();
				//				attributeIterator = Arrays.asList(result.split(Command.DELIMITER)).iterator();				
			} 			
		} catch (IOException e) {
			//			e.printStackTrace();			
			try {				
				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getReadFileName()))));				
				if (br.ready()) {
					result = br.readLine();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * Flushes and closes the reader and writer.
	 */
	public void closeIO() {
		closeReader();
		closeWriter();
	}

	public void closeReader() {
		try {
			if (br != null)
				br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void closeWriter() {
		if (bw != null) {
			bw.flush();
			bw.close();
		}
	}

	public void initializeReader() {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getReadFileName()))));
		} catch (IOException e) {
			System.out.println("Could not initialize the reader.");
			e.printStackTrace();
		}
	}

	public void initializeWriter() {
		try {
			bw = new PrintWriter(new BufferedWriter(new FileWriter(writerFileName, false)));
		} catch (IOException e) {
			System.out.println("Could not initialize the writer.");
			e.printStackTrace();
		}
	}

	public String getReadFileName() {
		return readFileName;
	}

	public void setReadFileName(String readFileName) {
		if (br != null && this.readFileName != null && !this.readFileName.equals(readFileName)) {
			initializeReader();
		}
		this.readFileName = readFileName;
	}

	public String getWriterFileName() {
		return writerFileName;
	}

	public void setWriterFileName(String writerFileName) {
		if (bw != null && this.writerFileName != null && !this.writerFileName.equals(writerFileName)) {
			initializeWriter();
		}
		this.writerFileName = writerFileName;
	}
}
