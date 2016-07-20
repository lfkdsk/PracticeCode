/*=====================================================================
File: 	 basicDT.java
Summary: This Microsoft JDBC Driver for SQL Server sample application
         demonstrates how to use result set getter methods to retrieve
         basic SQL Server data type values, and how to use result set
         update methods to update those values.
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

/*
// First, execute the following Transact-SQL script to create a table in the sample database:
use AdventureWorks
CREATE TABLE DataTypesTable 
   (Col1 int IDENTITY, 
    Col2 char,
    Col3 varchar(50), 
    Col4 bit,
    Col5 decimal(18, 2),
    Col6 money,
    Col7 datetime,
    Col8 date,
    Col9 time,
    Col10 datetime2,
    Col11 datetimeoffset
    );

INSERT INTO DataTypesTable 
VALUES ('A', 'Some text.', 0, 15.25, 10.00, '01/01/2006 23:59:59.991', '01/01/2006', '23:59:59', '01/01/2006 23:59:59.12345', '01/01/2006 23:59:59.12345 -1:00')

*/

import java.sql.*;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import microsoft.sql.DateTimeOffset;

public class basicDT {
   public static void main(String[] args) {

      // Create a variable for the connection string.
      String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;integratedSecurity=true;";
	   
      // Declare the JDBC objects.
      Connection con = null;
      Statement stmt = null;
      ResultSet rs = null;

      try {
         // Establish the connection.
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
         con = DriverManager.getConnection(connectionUrl);

         // Create and execute an SQL statement that returns some data
         // and display it.
         String SQL = "SELECT * FROM DataTypesTable";
         stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);         
         rs.next();
         displayRow("ORIGINAL DATA", rs);
         
         // Update the data in the result set.
         rs.updateString(2, "B");
         rs.updateString(3, "Some updated text.");
         rs.updateBoolean(4, true);
         rs.updateDouble(5, 77.89);
         rs.updateDouble(6, 1000.01);
         long timeInMillis = System.currentTimeMillis();
         Timestamp ts = new Timestamp(timeInMillis);
         rs.updateTimestamp(7, ts);
         rs.updateDate(8, new Date(timeInMillis));
         rs.updateTime(9, new Time(timeInMillis));
         rs.updateTimestamp(10, ts);

         //-480 indicates GMT - 8:00 hrs
         ((SQLServerResultSet)rs).updateDateTimeOffset(11, DateTimeOffset.valueOf(ts, -480));
         
         rs.updateRow();

         // Get the updated data from the database and display it.
         rs = stmt.executeQuery(SQL);
         rs.next();
         displayRow("UPDATED DATA", rs);
      }

      // Handle any errors that may have occurred.
      catch (Exception e) {
         e.printStackTrace();
      }

      finally {
         if (rs != null) 
        	 try { 
        		 rs.close(); 
        	 } 
         catch(Exception e) {}
         
         if (stmt != null) 
        	 try { stmt.close(); 
        	 } 
         catch(Exception e) {}
         
         if (con != null) 
        	 try { 
        		 con.close(); 
        		 } 
         catch(Exception e) {}
      }
   }

   private static void displayRow(String title, ResultSet rs) {
      try {
         System.out.println(title);
         System.out.println(rs.getInt(1) + " , " +  		// SQL integer type.
               rs.getString(2) + " , " +            		// SQL char type.
               rs.getString(3) + " , " +            		// SQL varchar type.
               rs.getBoolean(4) + " , " +           		// SQL bit type.
               rs.getDouble(5) + " , " +            		// SQL decimal type.
               rs.getDouble(6) + " , " +            		// SQL money type.
               rs.getTimestamp(7) + " , " +        		// SQL datetime type.
               rs.getDate(8) + " , " +              		// SQL date type.
               rs.getTime(9) + " , " +              		// SQL time type.
               rs.getTimestamp(10) + " , " +            	// SQL datetime2 type.
               ((SQLServerResultSet)rs).getDateTimeOffset(11)); // SQL datetimeoffset type. 
         
         System.out.println();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
