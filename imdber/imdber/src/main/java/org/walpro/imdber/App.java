package org.walpro.imdber;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class App 
{
	
	public static final String SEPERATOR = "/";
	public static final String PROTOCOL = "ftp";
	public static final String SERVER = "ftp.fu-berlin.de";
	public static final String PATH = "/pub/misc/movies/database";
	public static final String USERNAME = "anonymous";
	public static final String PASSWORD = "";
	public static final int PORT = 21;
	
	public App() {
		connect();
	}
	
    public static void main( String[] args )
    {
    	new App();
    }
    
    public void connect() {
    	FTPClient ftp = new FTPClient();
//        FTPClientConfig config = new FTPClientConfig();
//        config.setXXX(YYY); // change required options
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")
//        ftp.configure(config );
        boolean error = false;
        try {
          int reply;
//          ftp.connect(PROTOCOL + "://" + getIPv4ByHostName(SERVER),PORT);
          ftp.connect("ftp.fu-berlin.de");
          System.out.println("login: "+ftp.login(USERNAME, PASSWORD));
          System.out.println("Connected to " + SERVER + ".");
          System.out.print(ftp.getReplyString());

          // After connection attempt, you should check the reply code to verify
          // success.
          reply = ftp.getReplyCode();

          if(!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.err.println("FTP server refused connection.");
            System.exit(1);
          }
//          ... // transfer files
          System.out.println("pwd: " +ftp.printWorkingDirectory());
          List<FTPFile> fileList = new ArrayList<>();
          List<String> fileDirList = Arrays.asList(ftp.listNames());
          recCall(fileList, ftp);
         
//          InputStream is = 
//          BufferedInputStream bis = new BufferedInputStream(is);
//          Scanner s = new Scanner(is);
//          System.out.println("****************************\nBegin Content:");
//          while (s.hasNextLine()) {
//        	  System.out.println(s.nextLine());
//          }
//          System.out.println("End Content\n****************************");
          ftp.logout();
        } catch(IOException e) {
          error = true;
          e.printStackTrace();
        } finally {
          if(ftp.isConnected()) {
            try {
              ftp.disconnect();
            } catch(IOException ioe) {
              // do nothing
            }
          }
          System.exit(error ? 1 : 0);
        }
    }
    
    public void recCall(List<FTPFile> fileList, FTPClient ftp) throws IOException {
    	 for (FTPFile dir : ftp.listDirectories()) {
    		 ftp.cwd(dir.getName());
    		 List<FTPFile> files = Arrays.asList(ftp.listFiles());
    		 System.out.println("Adding files: " + files);
    		 fileList.addAll(files);
    		 recCall(fileList, ftp);
    		 ftp.cwd("..");
      }
    }
    
    public String getIPv4ByHostName(String hostName) {
    	String result = "";
    	try
        {
            InetAddress[] inets = InetAddress.getAllByName(hostName);
            InetAddress inet = null;

            if (inet != null)
            {
            	result = inet.getHostAddress();
                System.out.println(inet.getCanonicalHostName() + " Host Reached\t" + result);
            }
            else
            {
                System.out.println(hostName + " Host Unreachable");
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Host does not exists");
        }
    	return result;
    }
}
