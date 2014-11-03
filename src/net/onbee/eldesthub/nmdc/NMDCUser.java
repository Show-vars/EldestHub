package net.onbee.eldesthub.nmdc;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.antiflood.AntiFloodManager;
import net.onbee.eldesthub.antiflood.ReconnectTimeout;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.mysql.UserData;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public class NMDCUser extends DCUser {

    private Channel channel;
    private Logging l = Logging.getInstance();
    private Configuration c = Configuration.getInstance();
    private List<String> supports = new ArrayList<>();
    private int statusflag = 0;
    public AntiFloodManager af = new AntiFloodManager();
    public boolean getlist = false;
    public boolean getlistfirst = true;
    public boolean plist = false;
    private UserData dbdata = null;
    private boolean debug = false;
    private DCUser debugUser = null;

    public NMDCUser(UID id) {
        super(id);
    }

    public void handleResponse(String data) {
        NMDCProtocol.doCommand(this, data);
        if (debug && debugUser != null && !debugUser.isBot()) {
            debugUser.send(new NMDCResponse("$To: " + debugUser.getInfo("nick") + " From: " + c.KeyString("BotNick")
                    + " $<" + c.KeyString("BotNick") + "> [Incoming]" + (data).replace("$", "&#36;").replace("|", "&#124;")).pushEndChar());
        }
    }

    @Override
    public void init(Channel ch) {
        channel = ch;
        if (!ch.isOpen()) {
            disconnect();
            return;
        }
        int i = 0;//ReconnectTimeout.check(getIP());
        send(new NMDCResponse("$Lock EXTENDEDPROTOCOLthebindingofisaac Pk=EldestHub").pushEndChar());
        if (i == 1) {
            ReconnectTimeout.remove(getIP());
        } else if (i == 2) {
            //sendDisconnect(new NMDCResponse(t.Key("fastreconnect")));
            return;
        }
    }

    @Override
    public void send(NMDCResponse data) {
        try {
            if (channel.isOpen()) {
                channel.write(data.getBytes());
                if (debug && debugUser != null && !debugUser.isBot()) {
                    debugUser.send(new NMDCResponse("$To: " + debugUser.getInfo("nick") + " From: " + c.KeyString("BotNick")
                            + " $<" + c.KeyString("BotNick") + "> [Outgoing] " + (data.toString()).replace("$", "&#36;").replace("|", "&#124;")).pushEndChar());
                }
            }
        } catch (Exception ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    @Override
    public void sendDisconnect(NMDCResponse data) {
        try {
            if (channel.isOpen()) {
                channel.write(data.getBytes()).addListener(ChannelFutureListener.CLOSE);
                if (debug && debugUser != null && !debugUser.isBot()) {
                    debugUser.send(new NMDCResponse("$To: " + debugUser.getInfo("nick") + " From: " + c.KeyString("BotNick")
                            + " $<" + c.KeyString("BotNick") + "> [Outgoing] " + (data.toString()).replace("$", "&#36;").replace("|", "&#124;")).pushEndChar());
                }
            }
        } catch (Exception ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    @Override
    public void disconnect() {
        removeFromUserlist();
        try {
            channel.write(new NMDCResponse().pushEndChar().getBytes()).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
        }
    }

    public void putSupport(String feature) {
        supports.add(feature);
    }

    public String getAllSupports() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = supports.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }

    public void sendUserlist() {
        ArrayList<DCUser> normal = new ArrayList<>();
        ArrayList<DCUser> ops = new ArrayList<>();
        ArrayList<DCUser> bots = new ArrayList<>();
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            DCUser u = entry.getValue();
            if (u.isBot()) {
                bots.add(u);
                ops.add(u);
            } else if ((u.getProfile() == 0 || u.getProfile() == 1)) {
                ops.add(u);
            } else {
                normal.add(u);
            }
        }
        Iterator<DCUser> it = bots.iterator();
        while (it.hasNext()) {
            DCUser us = it.next();
            send(new NMDCResponse().putMyINFO(us, (us.getDBData() != null && us.getDBData().getUserValue("hideinfo") != null) ? (((Integer) us.getDBData().getUserValue("hideinfo")) == 1) : false));
        }
        it = ops.iterator();
        while (it.hasNext()) {
            DCUser us = it.next();
            send(new NMDCResponse().putMyINFO(us, (us.getDBData() != null && us.getDBData().getUserValue("hideinfo") != null) ? (((Integer) us.getDBData().getUserValue("hideinfo")) == 1) : false));

        }
        it = normal.iterator();
        while (it.hasNext()) {
            DCUser us = it.next();
            send(new NMDCResponse().putMyINFO(us, (us.getDBData() != null && us.getDBData().getUserValue("hideinfo") != null) ? (((Integer) us.getDBData().getUserValue("hideinfo")) == 1) : false));

        }
        NMDCResponse r = new NMDCResponse();
        r.pushBeginChar().pushString("OpList").pushSeparatorChar();
        it = bots.iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                r.pushString(it.next().getInfo("nick")).pushDoubleNickSeparator();
            }
        }
        it = ops.iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                r.pushString(it.next().getInfo("nick")).pushDoubleNickSeparator();
            }
        }
        send(r.pushEndChar());
        if (getProfile() == 0 || getProfile() == 1) {
            r = new NMDCResponse().pushBeginChar().pushString("UserIP").pushSeparatorChar();
            it = normal.iterator();
            while (it.hasNext()) {
                DCUser u = it.next();
                r.pushString(u.getInfo("nick")).pushSeparatorChar().pushString(u.getIP());
                if (it.hasNext()) {
                    r.pushDoubleNickSeparator();
                }
            }
            r.pushEndChar();
            send(r);
        }
    }

    public void setStatusFlag(int f) {
        statusflag = f;
    }

    public int getStatusFlag() {
        return statusflag;
    }

    public void enableDebug(DCUser us) {
        debug = true;
        debugUser = us;
    }

    public boolean isDebuging() {
        return debug;
    }
    
    public DCUser getDebugUser() {
        return debugUser;
    }

    public void disableDebug() {
        debug = false;
        debugUser = null;
    }

    @Override
    public String getIP() {
        return channel.getRemoteAddress().toString().substring(1).substring(0, channel.getRemoteAddress().toString().indexOf(":") - 1);
    }

    @Override
    public int getPort() {
        return ((InetSocketAddress) channel.getRemoteAddress()).getPort();
    }

    @Override
    public boolean isBot() {
        return false;
    }
}
