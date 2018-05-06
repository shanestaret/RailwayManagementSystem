package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SQL {
    static String url = "jdbc:sqlserver://SQL2.cis245.mc3.edu:1433;databaseName=zz_CIS245_16;user=user16;password=JayHey99";
    static Connection myConn = null;
    static Statement myStmt = null;
    static ResultSet rs = null;
    static ArrayList<String> info = new ArrayList<>();
    static String[] columnNames;
    static boolean wentInLoop = false;
    static int count = 0;

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
        count = 0;
        info.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            myConn = DriverManager.getConnection(url);
            myStmt = myConn.createStatement();
            rs = myStmt.executeQuery(sqlString);
            columnNames = sqlString.substring(7, sqlString.indexOf(" ", 7)).split(",");

            while(rs.next()) {
                wentInLoop = true;
                for(int i = 0; i < columnNames.length; i++) {
                    info.add(rs.getString(columnNames[i]));
                }
                count++;
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
