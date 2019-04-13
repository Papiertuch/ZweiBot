package de.einjava.zweibot.utils;

import de.einjava.zweibot.ZweiBot;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Leon on 11.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class VerifyHandler {

    private static UUID uuid;
    private static String clientid;
    private static ProxiedPlayer player;

    public VerifyHandler(String clientid) {
        this.clientid = clientid;
    }


    public VerifyHandler(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public void delete() {
        ZweiBot.getInstance().getMySQL().update("DELETE FROM verify WHERE UUID = '" + uuid + "'");
    }

    public String getRankbyUUID() {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE UUID = ?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return (rs.getString("RANK"));
                }
                rs.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void setRankbyUUID(String rank) {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("UPDATE verify SET RANK = ? WHERE UUID = ?");
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.setString(1, rank);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayerIngame();
            setRankbyUUID(rank);
        }
    }

    public void createPlayerIngame() {
        if (!isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO verify (UUID, NAME, RANK, TYPE, ID) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setString(3, ZweiBot.getInstance().getFileHandler().getString("Module.verify.default_rank"));
                preparedStatement.setInt(4, 0);
                preparedStatement.setString(5, null);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getTypebyID() {
        if (isExistPlayer()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE ID = ?");
                preparedStatement.setString(1, clientid);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getInt("TYPE");
                }
                rs.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return 0;
        }
        return 0;
    }

    public Integer getTypebyUUID() {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE UUID = ?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getInt("TYPE");
                }
                rs.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void setTypebyUUID(Integer type) {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("UPDATE verify SET TYPE = ? WHERE UUID = ?");
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.setInt(1, type);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayerIngame();
            setTypebyUUID(type);
        }
    }

    public String getIDbyUUID() {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE UUID = ?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getString("ID");
                }
                rs.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void setIDbyUUID(String id) {
        if (isExistPlayerIngame()) {
            try {
                PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("UPDATE verify SET ID = ? WHERE UUID = ?");
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayerIngame();
            setIDbyUUID(id);
        }
    }

    public boolean isExistPlayer() {
        try {
            PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE ID = ?");
            preparedStatement.setString(1, clientid);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (rs.getString("ID") != null) {
                    return true;
                }
                return false;
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isExistPlayerIngame() {
        try {
            PreparedStatement preparedStatement = ZweiBot.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM verify WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (rs.getString("UUID") != null) {
                    return true;
                }
                return false;
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
