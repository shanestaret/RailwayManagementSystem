package sample;

import java.sql.*;
import java.util.ArrayList;

public class SQL {
    static String url = "jdbc:sqlserver://SQL2.cis245.mc3.edu:1433;databaseName=zz_CIS245_16;user=user16;password=JayHey99";
    static Connection myConn = null;
    static Statement myStmt = null;
    static ResultSet rs = null;
    static ArrayList<String> info = new ArrayList<>();
    static String columnName;
    static boolean wentInLoop = false;

    public static void sendToDatabase(String sqlString) {
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

    public static ArrayList<String> getFromDatabase(String sqlString) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            myConn = DriverManager.getConnection(url);
            myStmt = myConn.createStatement();
            rs = myStmt.executeQuery(sqlString);
            columnName = sqlString.substring(7, sqlString.indexOf(" ", 7));

            while(rs.next()) {
                wentInLoop = true;
                info.add(rs.getString(columnName));
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
