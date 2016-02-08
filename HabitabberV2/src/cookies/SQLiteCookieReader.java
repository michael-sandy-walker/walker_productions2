package cookies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SQLiteCookieReader {
	public static void main(String[] args) 
    {
      Connection connection = null;
      try
      {
        // create a database connection
        connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Anugeshtu/AppData/Local/Google/Chrome/User Data/Default/Cookies");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

//        statement.executeUpdate("drop table if exists person");
//        statement.executeUpdate("create table person (id integer, name string)");
//        statement.executeUpdate("insert into person values(1, 'leo')");
//        statement.executeUpdate("insert into person values(2, 'yui')");
        ResultSet rs = statement.executeQuery("select * from cookies");
        while(rs.next())
        {
          // read the result set
          System.out.println("creation_utc = " + rs.getInt("creation_utc"));
          System.out.println("host_key = " + rs.getString("host_key"));
          System.out.println("name = " + rs.getString("name"));
          System.out.println("value = " + rs.getString("value"));
          System.out.println("path = " + rs.getString("path"));
          System.out.println("expires_utc = " + rs.getInt("expires_utc"));
          System.out.println("secure = " + rs.getInt("secure"));
          System.out.println("httponly = " + rs.getInt("httponly"));
          System.out.println("last_access_utc = " + rs.getInt("last_access_utc"));
          System.out.println("has_expires = " + rs.getInt("has_expires"));
          System.out.println("persistent = " + rs.getInt("persistent"));
          System.out.println("priority = " + rs.getInt("priority"));
          System.out.println("encrypted_value = " + rs.getBytes("encrypted_value"));
          System.out.println("firstpartyonly = " + rs.getInt("firstpartyonly"));
        }
      }
      catch(SQLException e)
      {
        // if the error message is "out of memory", 
        // it probably means no database file is found
        System.err.println(e.getMessage());
      }
      finally
      {
        try
        {
          if(connection != null)
            connection.close();
        }
        catch(SQLException e)
        {
          // connection close failed.
          System.err.println(e);
        }
      }
    }
	
	public static Map<String, Map<String, SQLiteCookie>> getSQLiteCookies() {
		
		Map<String, Map<String, SQLiteCookie>> cookieMap = new HashMap<>();		
		
		Connection connection = null;
	      try
	      {
	        // create a database connection
	        connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Anugeshtu/AppData/Local/Google/Chrome/User Data/Default/Cookies");
	        Statement statement = connection.createStatement();
	        statement.setQueryTimeout(30);  // set timeout to 30 sec.

//	        statement.executeUpdate("drop table if exists person");
//	        statement.executeUpdate("create table person (id integer, name string)");
//	        statement.executeUpdate("insert into person values(1, 'leo')");
//	        statement.executeUpdate("insert into person values(2, 'yui')");
	        ResultSet rs = statement.executeQuery("select * from cookies");
	        while(rs.next())
	        {
	        	Map<String, SQLiteCookie> subCookieMap = null;
	        	SQLiteCookie cookie = new SQLiteCookie();
	          // read the result set
	          cookie.setCreation_utc(rs.getInt("creation_utc"));
	          cookie.setHost_key(rs.getString("host_key"));
	          cookie.setName(rs.getString("name"));
	          cookie.setValue(rs.getString("value"));
	          cookie.setPath(rs.getString("path"));
	          cookie.setExpires_utc(rs.getInt("expires_utc"));
	          cookie.setSecure(rs.getInt("secure"));
	          cookie.setHttponly(rs.getInt("httponly"));
	          cookie.setLast_access_utc(rs.getInt("last_access_utc"));
	          cookie.setHas_expires(rs.getInt("has_expires"));
	          cookie.setPersistent(rs.getInt("persistent"));
	          cookie.setPriority(rs.getInt("priority"));
	          cookie.setEncrypted_value(rs.getBytes("encrypted_value"));
	          cookie.setFirstpartyonly(rs.getInt("firstpartyonly"));	          
	          
	          String hostKey = cookie.getHost_key();
	          if (cookieMap.containsKey(cookie.getHost_key())) {
	        	  subCookieMap = cookieMap.get(hostKey);
	        	  subCookieMap.put(cookie.getName(), cookie);
	          }
	          else {
	        	  subCookieMap = new HashMap<>();
	        	  subCookieMap.put(cookie.getName(), cookie);	        	  
	          }
	          cookieMap.put(hostKey, subCookieMap);
	        }
	      }
	      catch(SQLException e)
	      {
	        // if the error message is "out of memory", 
	        // it probably means no database file is found
	        System.err.println(e.getMessage());
	      }
	      finally
	      {
	        try
	        {
	          if(connection != null)
	            connection.close();
	        }
	        catch(SQLException e)
	        {
	          // connection close failed.
	          System.err.println(e);
	        }
	      }
	      return cookieMap;
	}
}
