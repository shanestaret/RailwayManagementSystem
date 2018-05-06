package sample;

import java.sql.*;

public class SQL {
    static String url = "jdbc:sqlserver://SQL2.cis245.mc3.edu:1433;databaseName=zz_CIS245_16;user=user16;password=JayHey99";
    static Connection myConn = null;
    static Statement myStmt = null;

    public static void sendToDatabase(String sqlString) {
        if(sqlString.toLowerCase().startsWith("insert")) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                myConn = DriverManager.getConnection(url);
                myStmt = myConn.createStatement();
                myStmt.executeUpdate(sqlString);
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
        else if(sqlString.toLowerCase().startsWith("update")) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                myConn = DriverManager.getConnection(url);
                myStmt = myConn.createStatement();
                myStmt.executeUpdate(sqlString);
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
    }
}
