package net.onbee.eldesthub.mysql;

import java.sql.*;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.Logging;

public class MySql {

    private Logging l = Logging.getInstance();
    private Configuration c = Configuration.getInstance();
    private Connection conn;
    private Statement st;

    public MySql() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            l.log(Logging.SYSTEM_LOG, "Mysql connector not found!");
            System.exit(1);
        }
        makeconnect();
    }

    private void makeconnect() {
        try {
            if (conn != null && conn.isClosed() || conn == null) {
                conn = DriverManager.getConnection("jdbc:mysql://" + c.KeyString("DBHost") + ":" + c.KeyInt("DBPort") + "/" + c.KeyString("DBName") + "?autoReconnect=true"
                        + "&connectTimeout=5000", c.KeyString("DBUser"), c.KeyString("DBPass"));
                st = conn.createStatement();
            }
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    public void queryUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    public ResultSet query(String query) {
        try {
            return st.executeQuery(query);
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            return null;
        }
    }

    public UserData getUserData(String usernamein, String userip) {
        UserData ud = new UserData();
        makeconnect();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM " + c.KeyString("DBTableUsers") + " WHERE `" + c.KeyString("DBColumnUsersNick") + "`='" + usernamein + "' LIMIT 1;");
            if (r.next()) {
                ud.putUserValue("id", r.getInt(c.KeyString("DBColumnUsersId")));
                ud.putUserValue("username", r.getString(c.KeyString("DBColumnUsersNick")));
                ud.putUserValue("password", r.getString(c.KeyString("DBColumnUsersPass")));
                ud.putUserValue("bindip", r.getString(c.KeyString("DBColumnUsersBindIp")));
                ud.putUserValue("profile", r.getInt(c.KeyString("DBColumnUsersProfile")));
                ud.putUserValue("hideinfo", r.getInt(c.KeyString("DBColumnUsersHideInfo")));
                ud.putUserValue("hiderss", r.getInt(c.KeyString("DBColumnUsersHideRSS")));
                ud.putUserValue("hidemenu", r.getInt(c.KeyString("DBColumnUsersHideMenu")));
                ud.putUserValue("enabled", r.getInt(c.KeyString("DBColumnUsersEnabled")));
            }
            r = st.executeQuery("SELECT * FROM " + c.KeyString("DBTableBans") + " WHERE `" + c.KeyString("DBColumnBansNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnBansIp") + "`='" + userip + "' LIMIT 1;");
            if (r.next()) {
                ud.putBanValue("id", r.getInt(c.KeyString("DBColumnBansId")));
                ud.putBanValue("username", r.getString(c.KeyString("DBColumnBansNick")));
                ud.putBanValue("ip", r.getString(c.KeyString("DBColumnBansIp")));
                ud.putBanValue("time", r.getLong(c.KeyString("DBColumnBansTime")));
                ud.putBanValue("by", r.getString(c.KeyString("DBColumnBansBy")));
                ud.putBanValue("reason", r.getString(c.KeyString("DBColumnBansReason")));
            }
            r = st.executeQuery("SELECT * FROM " + c.KeyString("DBTableGags") + " WHERE `" + c.KeyString("DBColumnGagsNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnGagsIp") + "`='" + userip + "' LIMIT 1;");
            if (r.next()) {
                ud.putGagValue("id", r.getInt(c.KeyString("DBColumnGagsId")));
                ud.putGagValue("username", r.getString(c.KeyString("DBColumnGagsNick")));
                ud.putGagValue("ip", r.getString(c.KeyString("DBColumnGagsIp")));
                ud.putGagValue("time", r.getLong(c.KeyString("DBColumnGagsTime")));
                ud.putGagValue("by", r.getString(c.KeyString("DBColumnGagsBy")));
                ud.putGagValue("reason", r.getString(c.KeyString("DBColumnGagsReason")));
            }
            return ud;
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            return null;
        }
    }

    public void registerUser(String usernamein, String password, int profile) {
        try {
            st.executeUpdate("INSERT INTO " + c.KeyString("DBTableUsers") + " (" + c.KeyString("DBColumnUsersNick") + "," + c.KeyString("DBColumnUsersPass") + "," + c.KeyString("DBColumnUsersProfile") + "," + c.KeyString("DBColumnUsersEnabled") + ") "
                    + "VALUES('" + usernamein + "','" + password + "','" + profile + "',1)");
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    public boolean addBan(String usernamein, String ip, long time, String by, String reason) {
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM " + c.KeyString("DBTableBans") + " WHERE `" + c.KeyString("DBColumnBansNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnBansIp") + "`='" + ip + "' LIMIT 1;");
            if (!r.next()) {
                String query = "INSERT INTO " + c.KeyString("DBTableBans") + " (" + c.KeyString("DBColumnBansNick") + "," + c.KeyString("DBColumnBansIp") + ","+c.KeyString("DBColumnBansTime")+","+c.KeyString("DBColumnBansBy")+") VALUES ('" + usernamein + "','" + ip + "','"+time+"','"+by+"')";
                l.log(Logging.SYSTEM_LOG, query);
                st.execute(query);
                
                //st.execute("INSERT INTO " + c.KeyString("DBTableBans") + " (" + c.KeyString("DBColumnBansNick") + "," + c.KeyString("DBColumnBansIp") + "," + c.KeyString("DBColumnBansTime") + "," + c.KeyString("DBColumnBansBy") + "," + c.KeyString("DBColumnBansReason") + ") VALUES (`" + usernamein + "`,`" + ip + "`,`" + time + "`,`" + by + "`,`" + reason + "`)");
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            return false;
        }
    }

    public void removeBan(String usernamein, String userip) {
        try {
            st.executeUpdate("DELETE FROM " + c.KeyString("DBTableBans") + " WHERE `" + c.KeyString("DBColumnBansNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnBansIp") + "`='" + userip + "'");
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    public boolean addGag(String usernamein, String ip, long time, String by, String reason) {
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM " + c.KeyString("DBTableGags") + " WHERE `" + c.KeyString("DBColumnGagsNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnGagsIp") + "`='" + ip + "' LIMIT 1;");
            if (!r.next()) {
                st.execute("INSERT INTO " + c.KeyString("DBTableGags") + " (" + c.KeyString("DBColumnGagsNick") + "," + c.KeyString("DBColumnGagsIp") + "," + c.KeyString("DBColumnGagsTime") + "," + c.KeyString("DBColumnGagsBy") + "," + c.KeyString("DBColumnGagsReason") + ") VALUES ('" + usernamein + "','" + ip + "','" + time + "','" + by + "','" + reason + "')");
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            return false;
        }
    }

    public void removeGag(String usernamein, String userip) {
        try {
            st.executeUpdate("DELETE FROM " + c.KeyString("DBTableGags") + " WHERE `" + c.KeyString("DBColumnGagsNick") + "`='" + usernamein + "' OR `" + c.KeyString("DBColumnGagsIp") + "`='" + userip + "'");
        } catch (SQLException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }
}
