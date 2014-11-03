package net.onbee.eldesthub.dc;

import java.util.HashMap;
import java.util.Map;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.mysql.UserData;
import net.onbee.eldesthub.nmdc.NMDCResponse;
import org.jboss.netty.channel.Channel;

public abstract class DCUser {

    private UID uid;
    private Map<String, String> info = new HashMap<>();
    private int profile = -1;
    private UserData dbdata = null;

    public DCUser(UID id) {
        uid = id;
    }

    public abstract void init(Channel ch);

    public abstract void send(NMDCResponse data);

    public abstract void sendDisconnect(NMDCResponse data);

    public abstract void disconnect();

    public void removeFromUserlist() {
        EldestHub.getUserlist().removeUser(uid);
    }

    public String getInfo(String feature) {
        return info.get(feature.toLowerCase());
    }

    public int getInfoInt(String feature) {
        return Integer.parseInt(info.get(feature.toLowerCase()));
    }

    public void putInfo(String feature, String value) {
        info.put(feature.toLowerCase(), value);
    }

    public void putInfo(String feature, int value) {
        info.put(feature.toLowerCase(), value + "");
    }

    public UID getUID() {
        return uid;
    }

    public void setProfile(int p) {
        profile = p;
    }

    public int getProfile() {
        return profile;
    }

    public void setDBData(UserData p) {
        dbdata = p;
    }

    public UserData getDBData() {
        return dbdata;
    }

    public abstract String getIP();

    public abstract int getPort();

    public abstract boolean isBot();
}
