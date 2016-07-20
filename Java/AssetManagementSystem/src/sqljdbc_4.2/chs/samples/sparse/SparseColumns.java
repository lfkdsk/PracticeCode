/*=====================================================================
File: 	 SparseColumns.java
Summary: This Microsoft JDBC Driver for SQL Server sample application
         demonstrates how to detect column sets. It also shows a 
	 technique for parsing a column set's XML output, to get the 
	 data from the sparse columns.

	 See the documentation for the SQL script to create the ColdCalling table.
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SparseColumns {

	public static void main(String args[]) {
		final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;integratedSecurity=true;";
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(connectionUrl);
			
			stmt = conn.createStatement();
			//Determine the column set column
			String columnSetColName = null;
			String strCmd = "SELECT name FROM sys.columns WHERE object_id=(SELECT OBJECT_ID('ColdCalling')) AND is_column_set = 1";
			rs = stmt.executeQuery(strCmd);
			
			if (rs.next()) {
				columnSetColName = rs.getString(1);
				System.out.println(columnSetColName + " is the column set column!");
			}
			rs.close();

			rs = null; 
			    
			strCmd = "SELECT * FROM ColdCalling";
			rs = stmt.executeQuery(strCmd);
		      
			//Iterate through the result set
			ResultSetMetaData rsmd = rs.getMetaData();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			while (rs.next()) {
				//Iterate through the columns
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					String name = rsmd.getColumnName(i);
					String value = rs.getString(i);
	
					//If this is the column set column
					if (name.equalsIgnoreCase(columnSetColName)) {
						System.out.println(name);
						
						//Instead of printing the raw XML, parse it
						if (value != null) {
							//Add artificial root node "sparse" to ensure XML is well formed
							String xml = "<sparse>" + value + "</sparse>";
	
							is.setCharacterStream(new StringReader(xml));
							Document doc = db.parse(is);
	
							//Extract the NodeList from the artificial root node that was added
							NodeList list = doc.getChildNodes();
							Node root = list.item(0); //This is the <sparse> node
							NodeList sparseColumnList = root.getChildNodes(); //These are the xml column nodes
	
							//Iterate through the XML document
							for (int n = 0; n < sparseColumnList.getLength(); ++n) {
								Node sparseColumnNode = sparseColumnList.item(n);
								String columnName = sparseColumnNode.getNodeName();
								//Note that the column value is not in the sparseColumNode, it is the value of the first child of it
								Node sparseColumnValueNode = sparseColumnNode.getFirstChild();
								String columnValue = sparseColumnValueNode.getNodeValue();
								
								System.out.println("\t" + columnName + "\t: " + columnValue);
							}
						}
					} else { //Just print the name + value of non-sparse columns
						System.out.println(name + "\t: " + value);
					}
				}
				System.out.println();//New line between rows
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}		
}

