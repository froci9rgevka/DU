package com.pea.du.db.remote;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

    // JDBC URL, username and password of MySQL server
    private static final String server = "78.47.195.208:3306";
    private static final String database = "zkh_gks3";
    private static final String user = "e.polyakov";
    private static final String password = "1";


    @SuppressLint("NewApi")
    public static Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            ConnectionURL = "jdbc:mysql://" + server + "/" + database + "?useUnicode=true&characterEncoding=utf-8";
            connection = DriverManager.getConnection(ConnectionURL,user,password);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}

