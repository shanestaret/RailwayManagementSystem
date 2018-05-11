package project;

import java.sql.*;
import java.util.ArrayList;

public class SQL {
    static String url = "jdbc:sqlserver://SQL2.cis245.mc3.edu:1433;databaseName=zz_CIS245_16;user=xx;password=xx"; //name of SQL server and info
    static Connection myConn = null;
    static Statement myStmt = null; //all must be null to start
    static ResultSet rs = null;
    static ArrayList<String> info = new ArrayList<>(); //ArrayList containing information that will be passed to main class and sqlInfo
    static String[] columnNames; //the names of the columns that appear within a query
    static boolean wentInLoop = false; //at the start, we haven't gone through a loop yet because we haven't run through any methods
    static int numOfRowsAccessed = 0; //at the start, we haven't been called yet so the number of rows is defaulted to 0

    public static void sendToDatabase(String sqlString) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                myConn = DriverManager.getConnection(url);
                myStmt = myConn.createStatement(); //establish connection with server
                myStmt.executeUpdate(sqlString); //send string to database
                myConn.close();
            } catch (Exception except) {
                except.printStackTrace();
            } finally {
                if (myStmt != null) try {
                    myStmt.close();
                } catch (Exception except) {
                }
                if (myConn != null) try {
                    myConn.close();
                } catch (Exception except) {
                }
            }
    }

    public static ArrayList<String> getFromDatabase(String sqlString) {
        numOfRowsAccessed = 0;
        info.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            myConn = DriverManager.getConnection(url);
            myStmt = myConn.createStatement();
            rs = myStmt.executeQuery(sqlString);
            columnNames = sqlString.substring(7, sqlString.indexOf(" ", 7)).split(",");
            
            //while there are more rows to retrieve...
            while(rs.next()) {
                wentInLoop = true;
                //retrieve them and return them
                for(int i = 0; i < columnNames.length; i++) {
                    info.add(rs.getString(columnNames[i]));
                }
                numOfRowsAccessed++; //increment the number of rows that need to be processed every single iteration of the while loop
            }

            myConn.close();
        } catch (Exception except) {
            except.printStackTrace();
        } finally {
            if (myStmt != null) try {
                myStmt.close();
            } catch (Exception except) {
            }
            if (myConn != null) try {
                myConn.close();
            } catch (Exception except) {
            }
        }
        return info;
    }
}
