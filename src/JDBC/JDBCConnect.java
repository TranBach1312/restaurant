package JDBC;

import common.Config;

import java.sql.*;

public class JDBCConnect {
    public static Connection JDBCConnector() {
        Connection conn = null;
        String url = "jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DBNAME;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, Config.USERNAME, Config.PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closePreparedStatement(PreparedStatement p) {
        try {
            if (p != null) {
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet r) {
        try {
            if(r != null){
                r.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println((new JDBCConnect()).JDBCConnector());
    }
}
