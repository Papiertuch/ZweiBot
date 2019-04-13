package de.einjava.zweibot.utils;

import de.einjava.zweibot.ZweiBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Leon on 11.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class MySQL {

    private Connection connection;

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ZweiBot.getInstance().getFileHandler().getString("MySQL.Host") + ":3306/" + ZweiBot.getInstance().getFileHandler().getString("MySQL.Database") + "?autoReconnect=true", ZweiBot.getInstance().getFileHandler().getString("MySQL.User"), ZweiBot.getInstance().getFileHandler().getString("MySQL.Password"));
            ZweiBot.getInstance().sendMessage("§aEine Verbindung zur MySQL war erfolgreich");
        } catch (SQLException e) {
            ZweiBot.getInstance().sendMessage("§cDie Verbindung zum MySQL-Server ist fehlgeschlagen");
        }
    }

    public void createTable() {
        connect();
        update("CREATE TABLE IF NOT EXISTS verify (UUID varchar(64), NAME varchar(64), RANK varchar(64), TYPE int, ID varchar(64));");
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void update(String qry) {
        try {
            PreparedStatement ps = connection.prepareStatement(qry);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
