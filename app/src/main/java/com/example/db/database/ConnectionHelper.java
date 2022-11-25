package com.example.db.database;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class ConnectionHelper {
    String username = "sa",
            password = "123",
            ip = "192.168.0.196",
            port = "1433",
            database = "TrivagoDB";

    public Optional<Connection> getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionURL;
        Optional<Connection> connection = Optional.empty();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + username +
                    ";password=" + password + ";";
            connection = Optional.of(DriverManager.getConnection(connectionURL));
        } catch (Exception e) {
            Log.d("Connection error", "Problem połączenia z bazą danych: " + e.getMessage());
        }

        return connection;
    }
}
