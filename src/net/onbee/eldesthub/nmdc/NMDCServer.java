package net.onbee.eldesthub.nmdc;

import java.util.Map;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.dc.DCServer;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.extensions.StandartChatCommands;
import net.onbee.eldesthub.network.ServerFactory;
import net.onbee.eldesthub.usercommands.UserCommandsXML;

public class NMDCServer implements DCServer {

    private ServerFactory serverfactory;
    private static Configuration c = Configuration.getInstance();
    private static NMDCResponse supports = new NMDCResponse();

    @Override
    public void start() {
        supports.pushBeginChar();
        supports.pushString("Supports");
        supports.pushSeparatorChar();
        supports.pushString("NoHello");
        supports.pushSeparatorChar();
        supports.pushString("UserIP2");
        supports.pushSeparatorChar();
        supports.pushString("UserCommand");
        supports.pushEndChar();
        UID uid = new UID("0.0.0.0", "00");
        NMDCBot bot = new NMDCBot(uid);
        bot.init(null);
        EldestHub.getUserlist().putUser(uid, bot);
        StandartChatCommands.getInstance().init();
        UserCommandsXML.init();
        serverfactory = new ServerFactory(c.KeyInt("BindPort"));

    }

    public void sendToAll(NMDCResponse response) {
        EldestHub.getUserlist().entrySet();
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (!entry.getValue().isBot() && ((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                entry.getValue().send(response);
            }
        }
    }
    
    public void sendToAllPm(String text, String from) {
        EldestHub.getUserlist().entrySet();
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (!entry.getValue().isBot() && ((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                entry.getValue().send(new NMDCResponse("$To: " + entry.getValue().getInfo("nick") + " From: " + from
                        + " $<" + entry.getValue().getInfo("nick") + "> " + text).pushEndChar());
            }
        }
    }

    public void sendToAllExcept(NMDCResponse response, UID uid) {
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (!entry.getKey().equals(uid) && !entry.getValue().isBot() && ((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                entry.getValue().send(response);
            }
        }
    }

    public void sendTo(UID uid, NMDCResponse response) {
        NMDCUser u = (NMDCUser) EldestHub.getUserlist().getUser(uid);
        if (u != null && !u.isBot() && u.getStatusFlag() >= 4) {
            u.send(response);
        }
    }

    public void sendTo(String nick, NMDCResponse response) {
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (entry.getValue().getInfo("nick").equals(nick) && !entry.getValue().isBot() && ((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                entry.getValue().send(response);
            }
        }
    }

    public void sendToOps(NMDCResponse response) {
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (!entry.getValue().isBot() && (entry.getValue().getProfile() == 0 || entry.getValue().getProfile() == 1) && ((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                entry.getValue().send(response);
            }
        }
    }

    public DCUser findUser(String nick) {
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (entry.getValue().getInfo("nick").equals(nick) && !entry.getValue().isBot() &&((NMDCUser) entry.getValue()).getStatusFlag() >= 4) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean validateNick(String nick) {
        for (Map.Entry<UID, DCUser> entry : EldestHub.getUserlist().entrySet()) {
            if (entry.getValue().getInfo("nick") != null && entry.getValue().getInfo("nick").equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void newUser(NMDCUser u) {
        EldestHub.getUserlist().putUser(u.getUID(), u);
        sendToAll(new NMDCResponse().putMyINFO(u, (u.getDBData() != null && u.getDBData().getUserValue("hideinfo") != null) ? (((Integer) u.getDBData().getUserValue("hideinfo")) == 1) : false));
        sendToOps(new NMDCResponse("$UserIP").pushSeparatorChar().pushString(u.getInfo("nick")).pushSeparatorChar().pushString(u.getIP()).pushEndChar());
    }

    public void newOp(NMDCUser u) {
        NMDCResponse r = new NMDCResponse();
        r.pushBeginChar();
        r.pushString("OpList");
        r.pushSeparatorChar();
        r.pushString(u.getInfo("nick"));
        r.pushDoubleNickSeparator();
        r.pushEndChar();
        sendToAllExcept(r, u.getUID());
    }

    public void quitUser(UID uid) {
        NMDCUser u = (NMDCUser) EldestHub.getUserlist().getUser(uid);
        if (u != null) {
            if (u.getInfo("nick") != null && u.getStatusFlag() != -1) {
                EldestHub.getUserlist().removeUser(uid);
                if (u.getStatusFlag() > 2) {
                    sendToAllExcept(new NMDCResponse("$Quit").pushSeparatorChar().pushString(u.getInfo("nick")).pushEndChar(), uid);
                }
            }
        }
    }

    public NMDCResponse getSupports() {
        return supports;
    }

    @Override
    public String bindIP() {
        return "0.0.0.0";
    }

    @Override
    public int bindPort() {
        return c.KeyInt("BindPort");
    }
}
