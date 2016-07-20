/*=====================================================================
File: 	 cacheRS.java
Summary: This Microsoft JDBC Driver for SQL Server sample application
         demonstrates how to use a result set to retrieve a large set
         of data from a SQL Server database. In addition, it
         demonstrates how to control the amount of data that is fetched
         from the database and cached on the client.
---------------------------------------------------------------------
This file is part of the Microsoft JDBC Driver for SQL Server Code Samples.
Copyright (C) Microsoft Corporation.  All rights reserved.
 
This source code is intended only as a supplement to Microsoft
Development Tools and/or on-line documentation.  See these other
materials for detailed information regarding Microsoft code samples.
 
THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF
ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
PARTICULAR PURPOSE.
=====================================================================*/
import java.sql.*;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;

public class cacheRS {
   
   public static void main(String[] args) {
      
      // Create a variable for the connection string.
      String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=AdventureWorks;integratedSecurity=true;"; 
      
      // Declare the JDBC objects.
      Connection con = null;
      Statement stmt = null;
      ResultSet rs = null;
      
      try {
         
         // Establish the connection.
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         con = DriverManager.getConnection(connectionUrl);
         
         // Create and execute an SQL statement that returns a large
         // set of data and then display it.
         String SQL = "SELECT * FROM Sales.SalesOrderDetail;";
         stmt = con.createStatement(SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY, +
               SQLServerResultSet.CONCUR_READ_ONLY);
         
         // Perform a fetch for every row in the result set.
         rs = stmt.executeQuery(SQL);
         timerTest(1, rs);
         rs.close();
         
         // Perform a fetch for every 10th row in the result set.
         rs = stmt.executeQuery(SQL);
         timerTest(10, rs);
         rs.close();
         
         // Perform a fetch for every 100th row in the result set.
         rs = stmt.executeQuery(SQL);
         timerTest(100, rs);
         rs.close();
         
         // Perform a fetch for every 1000th row in the result set.
         rs = stmt.executeQuery(SQL);
         timerTest(1000, rs);
         rs.close();
         
         // Perform a fetch for every 128th row (the default) in the result set.
         rs = stmt.executeQuery(SQL);
         timerTest(0, rs);
         rs.close();
      }
      
      // Handle any errors that may have occurred.
      catch (Exception e) {
         e.printStackTrace();
      }
      
      finally {
         if (rs != null) try { rs.close(); } catch(Exception e) {}
         if (stmt != null) try { stmt.close(); } catch(Exception e) {}
         if (con != null) try { con.close(); } catch(Exception e) {}
      }
   }
   
   private static void timerTest(int fetchSize, ResultSet rs) {
      try {
         
         // Declare the variables for tracking the row count and elapsed time.
         int rowCount = 0;
         long startTime = 0;
         long stopTime = 0;
         long runTime = 0;
         
         // Set the fetch size and then iterate through the result set to
         // cache the data locally.
         rs.setFetchSize(fetchSize);
         startTime = System.currentTimeMillis();
         while (rs.next()) {
            rowCount++;
         }
         stopTime = System.currentTimeMillis();
         runTime = stopTime - startTime;
         
         // Display the results of the timer test.
         System.out.println("FETCH SIZE: " + rs.getFetchSize());
         System.out.println("ROWS PROCESSED: " + rowCount);
         System.out.println("TIME TO EXECUTE: " + runTime);
         System.out.println();
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}