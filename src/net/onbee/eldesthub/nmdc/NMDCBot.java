package net.onbee.eldesthub.nmdc;

import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.dc.DCUser;
import org.jboss.netty.channel.Channel;

public class NMDCBot extends DCUser {

    private Configuration c = Configuration.getInstance();

    @Override
    public void init(Channel ch) {
        putInfo("nick", c.KeyString("BotNick"));
        putInfo("description", c.KeyString("BotDesc"));
        putInfo("software", "Bot");
        putInfo("version", "1.0");
        putInfo("mode", "A");
        putInfo("hubs", "0/0/1");
        putInfo("con", "100");
        putInfo("magic", "0");
        putInfo("email", c.KeyString("BotEmail"));
        putInfo("slots", 0);
        putInfo("share", 0);
        setProfile(0);
    }

    public NMDCBot(UID id) {
        super(id);
    }

    @Override
    public void send(NMDCResponse data) {
    }

    @Override
    public void sendDisconnect(NMDCResponse data) {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public String getIP() {
        return "";
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public boolean isBot() {
        return true;
    }
}
