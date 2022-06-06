package com.example.db.Database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    String username,password,ip, port, database;

    public Connection getConnection(){
        ip ="192.168.0.196";
        database="TrivagoDB";
        username="sa";
        password="123";
        port="1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionURL;
        Connection connection = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
