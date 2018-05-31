/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author J_Administrator
 */
public class SimpleDataSource {
   private static String url;
   private static String username;
   private static String password;

   /**
    * Initializes the data source.
    * @param fileName the name of the property file that 
    * contains the database driver, URL, username, and password
    * source: BJLO
    * modified by W. Pijnacker Hordijk
    */
   
   public static void init(String fileName)
         throws IOException, ClassNotFoundException
   {  
      FileInputStream in = new FileInputStream(fileName);

      Properties props = new Properties();
      props.load(in);

      url = props.getProperty("jdbc.url");

      username = props.getProperty("jdbc.username");
      if (username == null) { username = ""; }

      password = props.getProperty("jdbc.password");
      if (password == null) { password = ""; }

      String driver = props.getProperty("jdbc.driver");
      if (driver != null) { 
          Class.forName(driver);  // load the specified JDBC-driver
      }
   }

   /**
    * Gets a connection to the database.
    * @return the database connection
    */
   public static Connection getConnection() throws SQLException
   {
      return DriverManager.getConnection(url, username, password);
   }    
}
