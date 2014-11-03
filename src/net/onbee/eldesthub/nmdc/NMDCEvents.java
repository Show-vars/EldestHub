package net.onbee.eldesthub.nmdc;

import java.util.Iterator;
import java.util.Map;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.Translation;
import net.onbee.eldesthub.Utils;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.extensions.DynamicReplace;
import net.onbee.eldesthub.extensions.StandartChatCommands;
import net.onbee.eldesthub.mysql.UserData;
import net.onbee.eldesthub.usercommands.UserCommandsManager;

public class NMDCEvents {

    private static Logging l = Logging.getInstance();
    private static Configuration c = Configuration.getInstance();
    private static Translation t = Translation.getInstance();

    public static void eventKey(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.Key)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.Key, data);
        if (cmd != null) {
            if (u.getStatusFlag() < 1) {
                u.setStatusFlag(1);
                u.send(new NMDCResponse("$HubName " + c.KeyString("HubName") + " - " + c.KeyString("HubTopic")).pushEndChar());
            } else {
                u.disconnect();
                return;
            }
        }
    }

    public static void eventSupports(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.Supports)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.Supports, data);
        if (cmd != null) {
            for (int i = 0; i < cmd.length - 1; i++) {
                u.putSupport(cmd[i + 1]);
            }
            u.send(EldestHub.getServer().getSupports());
        }
    }

    public static void eventValidateNick(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.ValidateNick)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() != 1) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.ValidateNick, data);
        if (cmd != null) {
            if (cmd[1].length() > 255) {
                u.disconnect();
                return;
            }
            if (cmd[1].matches("(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9])[.]){3}(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9]))")
                    || EldestHub.getServer().validateNick(cmd[1])) {
                u.sendDisconnect(new NMDCResponse("$ValidateDenide " + cmd[1]).pushEndChar());
                return;
            }
            u.putInfo("nick", cmd[1]);

            u.setDBData(EldestHub.getSQL().getUserData(u.getInfo("nick"), u.getIP()));

            UserData ud = u.getDBData();
            if (ud.getUserValue("bindip") != null && !((String) ud.getUserValue("bindip")).equals("") && !((String) ud.getUserValue("bindip")).equalsIgnoreCase(u.getIP())) {
                u.sendDisconnect(new NMDCResponse(t.Key("badip")).pushEndChar());
                return;
            }
            if (ud.getBanValue("username") != null || ud.getBanValue("ip") != null) {
                if (((String) ud.getBanValue("username")).equals(u.getInfo("nick")) || ((String) ud.getBanValue("ip")).equals(u.getIP())) {
                    if (((Long) ud.getBanValue("time")) == 0) {
                        u.send(new NMDCResponse(t.Key("youblocked")).pushEndChar());
                        u.sendDisconnect(new NMDCResponse(t.Key("reason") + " " + ud.getBanValue("reason")).pushEndChar());
                        return;
                    } else if (((Long) ud.getBanValue("time")) > System.currentTimeMillis()) {
                        u.send(new NMDCResponse(t.Key("youblockedperiodof") + " " + Utils.formatTime((Long) ud.getBanValue("time") + System.currentTimeMillis())).pushEndChar());
                        u.sendDisconnect(new NMDCResponse(t.Key("reason") + " " + ud.getBanValue("reason")).pushEndChar());
                        return;
                    } else {
                        EldestHub.getSQL().removeBan(u.getInfo("nick"), u.getIP());
                        ud.clearBan();
                        u.send(new NMDCResponse(t.Key("blockoff")).pushEndChar());
                    }
                }

            }

            if (ud.getUserValue("id") != null) {
                u.send(new NMDCResponse("$GetPass").pushEndChar());
                u.setStatusFlag(2);
            } else {
                if (c.KeyInt("Private") == 0) {
                    u.setStatusFlag(3);
                    u.send(new NMDCResponse("$Hello").pushSeparatorChar().pushString(u.getInfo("nick")).pushEndChar());
                    u.send(new NMDCResponse(t.Key("youloggedas")).pushSeparatorChar().pushString(t.switchProfile(u.getProfile())).pushEndChar());
                } else {
                    u.sendDisconnect(new NMDCResponse(t.Key("hubinofflinemode")).pushEndChar());
                }
            }
        }
    }

    public static void eventMyPass(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.MyPass)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() != 2) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.MyPass, data);
        if (cmd != null) {
            UserData ud = u.getDBData();
            if (Utils.md5(cmd[1]).equals(((String) ud.getUserValue("password")))) {
                if ((Integer) ud.getUserValue("profile") == 0 || (Integer) ud.getUserValue("profile") == 1) {
                    u.send(new NMDCResponse("$LogedIn " + u.getInfo("nick")).pushEndChar());
                    u.setProfile((Integer) ud.getUserValue("profile"));
                    u.setStatusFlag(3);
                    u.send(new NMDCResponse("$Hello" + " " + u.getInfo("nick")).pushEndChar());
                    u.send(new NMDCResponse(t.Key("youloggedas") + " " + t.switchProfile(u.getProfile())).pushEndChar());
                } else {
                    u.setProfile((Integer) ud.getUserValue("profile"));
                    u.setStatusFlag(3);
                    u.send(new NMDCResponse("$Hello" + " " + u.getInfo("nick")).pushEndChar());
                    u.send(new NMDCResponse(t.Key("youloggedas") + " " + t.switchProfile(u.getProfile())).pushEndChar());
                }
            } else {
                u.send(new NMDCResponse(t.Key("badpass")).pushEndChar());
                u.sendDisconnect(new NMDCResponse("$BadPass").pushEndChar());
                return;
            }
        }
    }

    public static void eventVersion(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.Version)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() > 3) {
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.Version, data);
        if (cmd != null) {
            u.setStatusFlag(4);
        }
    }

    public static void eventGetNickList(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.GetNickList)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.GetNickList, data);
        if (cmd != null) {
            if (u.getlist) {
                u.sendUserlist();
            }
        }

    }

    public static void eventMyINFO(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.MyINFO)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.MyINFO, data);
        if (cmd != null) {
            try {
                String tdata = cmd[4];
                String temp;
                String clientname = tdata.substring(0, tdata.indexOf(" "));
                tdata = tdata.substring(tdata.indexOf(" ") + 1);
                temp = tdata.substring(tdata.indexOf("V:") + 2);
                String clientver = temp.substring(0, temp.indexOf(","));
                temp = tdata.substring(tdata.indexOf("M:") + 2);
                String connmode = temp.substring(0, temp.indexOf(","));
                temp = tdata.substring(tdata.indexOf("H:") + 2);
                String hubs = temp.substring(0, temp.indexOf(","));
                temp = tdata.substring(tdata.indexOf("S:") + 2);
                String slots;
                if (temp.indexOf(",") == -1) {
                    slots = temp.substring(0);
                } else {
                    slots = temp.substring(0, temp.indexOf(","));
                }

                if (cmd[2].equals(u.getInfo("nick"))) {
                    u.putInfo("description", cmd[3]);
                    u.putInfo("software", clientname);
                    u.putInfo("version", clientver);
                    u.putInfo("mode", connmode);
                    u.putInfo("hubs", hubs);
                    u.putInfo("slots", slots);
                    u.putInfo("con", cmd[5]);
                    u.putInfo("email", cmd[7]);
                    u.putInfo("share", cmd[8]);
                    u.putInfo("MyINFO", "$MyINFO " + data);
                    u.putInfo("magic", cmd[6]);

                }
            } catch (NullPointerException ex) {
                l.log(Logging.SYSTEM_LOG, "Bad MyINFO command: " + data);
                u.disconnect();
                return;
            }
            if (u.getInfo("realmod") != null && u.getInfo("virtmod") != null && u.getInfo("realmod").equalsIgnoreCase("a") && u.getInfo("virtmod").equalsIgnoreCase("p")) {
                u.putInfo("mode", "P");
                //boolean hinfo = conn.getDBData().getUserValue("hideinfo") != null ? (((Integer) conn.getDBData().getUserValue("hideinfo")) == 1) : false;
            }
            if (u.getlistfirst) {
                u.getlist = true;
                u.getlistfirst = false;

                EldestHub.getServer().newUser(u);

                if (u.getProfile() == 0 || u.getProfile() == 1) {
                    EldestHub.getServer().newOp(u);
                }

                u.sendUserlist();
                eventUserEnter(u);
            } else {
                EldestHub.getServer().sendToAll(new NMDCResponse().putMyINFO(u, (u.getDBData() != null && u.getDBData().getUserValue("hideinfo") != null) ? (((Integer) u.getDBData().getUserValue("hideinfo")) == 1) : false));
            }
        }
    }

    public static void eventGetINFO(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.GetINFO)) {
            eventFlood(u, t.Key("floodsystem"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.GetINFO, data);
        if (cmd != null) {
            if (u.getInfo("nick").equals(cmd[2]) && EldestHub.getServer().validateNick(cmd[1])) {
                NMDCUser ru = (NMDCUser) EldestHub.getServer().findUser(cmd[1]);
                if (ru != null && ru.getInfo("nick") != null) {
                    u.send(new NMDCResponse().putMyINFO(ru, (ru.getDBData() != null && ru.getDBData().getUserValue("hideinfo") != null) ? (((Integer) ru.getDBData().getUserValue("hideinfo")) == 1) : false));
                }
            }
        }
    }

    public static void eventChat(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.Chat)) {
            eventFlood(u, t.Key("floodchat"), 2);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.Chat, data);
        if (cmd != null) {
            if (EldestHub.getServer().validateNick(cmd[0])) {
                if (!(cmd[1].length() > 256)) {
                    if (cmd[1].substring(0, 1).equals("!") || cmd[1].substring(0, 1).equals("+") && cmd[1].length() > 1) {
                        eventChatCommand(u, cmd);
                        return;
                    }

                    UserData ud = u.getDBData();
                    if (ud.getGagValue("username") != null || ud.getGagValue("ip") != null) {
                        if (((String) ud.getGagValue("username")).equals(u.getInfo("nick")) || ((String) ud.getGagValue("ip")).equals(u.getIP())) {
                            if (((Long) ud.getGagValue("time")) == 0) {
                                u.send(new NMDCResponse(t.Key("chatblockedforyou")).pushEndChar());
                                u.send(new NMDCResponse(t.Key("reason") + " " + ud.getGagValue("reason")).pushEndChar());
                                return;
                            } else if (((Long) ud.getGagValue("time")) > System.currentTimeMillis()) {
                                u.send(new NMDCResponse(t.Key("chatblockedforyoupeperiodof") + " " + Utils.formatTime((Long) ud.getGagValue("time") + System.currentTimeMillis())).pushEndChar());
                                u.send(new NMDCResponse(t.Key("reason") + " " + ud.getGagValue("reason")).pushEndChar());
                                return;
                            } else {
                                EldestHub.getSQL().removeGag(u.getInfo("nick"), u.getIP());
                                ud.clearGag();
                                u.send(new NMDCResponse(t.Key("chatblockoff")).pushEndChar());
                            }
                        }

                    }
                    if (c.KeyInt("AntiAdvEnabled") == 1 || (u.getProfile() != 0 && u.getProfile() != 1)) {
                        Iterator<String> it = StandartChatCommands.getInstance().antiAdv.iterator();
                        while (it.hasNext()) {
                            String s = it.next();
                            if (!s.equalsIgnoreCase("") && cmd[1].toLowerCase().contains(s.toLowerCase())) {
                                u.send(new NMDCResponse(t.Key("advfound")).pushEndChar());
                                return;
                            }
                        }
                    }

                    EldestHub.getServer().sendToAll(new NMDCResponse("<" + cmd[0] + "> " + cmd[1]).pushEndChar());
                    l.log(Logging.CHAT_LOG, " (" + u.getIP() + ") <" + cmd[0] + "> " + cmd[1]);
                } else {
                    u.send(new NMDCResponse(t.Key("tobigmessage")).pushEndChar());
                }
            }
        }
    }

    public static void eventTo(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.To)) {
            eventFlood(u, t.Key("floodpm"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.To, data);
        if (cmd != null) {
            DCUser ru = EldestHub.getServer().findUser(cmd[1]);
            if (ru != null && cmd[2].equals(u.getInfo("nick")) && (cmd[3]).equals(u.getInfo("nick"))) {
                if (!ru.isBot()) {
                    ru.send(new NMDCResponse("$To: " + cmd[1] + " From: " + cmd[2] + " $<" + cmd[3] + "> " + cmd[4]).pushEndChar());
                }
                l.log(Logging.PM_LOG, "To: " + cmd[1] + " (" + u.getIP() + ") <" + cmd[3] + "> " + cmd[4]);
            }
        }
    }

    public static void eventConnectToMe(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.ConnectToMe)) {
            eventFlood(u, t.Key("floodconnect"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.ConnectToMe, data);
        if (cmd != null) {
            String to;
            String ip;
            final int port;
            String flag = "";
            if (cmd.length == 5) {
                to = cmd[2];
                ip = cmd[3];
                if (cmd[4].matches("\\d*\\D")) {
                    port = Integer.parseInt(cmd[4].substring(0, cmd[4].length() - 1));
                    flag = cmd[4].substring(cmd[4].length() - 1);
                } else {
                    port = Integer.parseInt(cmd[4]);
                }
            } else {
                to = cmd[1];
                ip = cmd[2];
                if (cmd[3].matches("\\d*\\D")) {
                    port = Integer.parseInt(cmd[3].substring(0, cmd[3].length() - 1));
                    flag = cmd[3].substring(cmd[3].length() - 1);
                } else {
                    port = Integer.parseInt(cmd[3]);
                }
            }
            if (c.KeyInt("ClientPortTestEnabled") == 1) {
                if (to.equals(c.KeyString("BotNick")) && u.getInfo("porttest") != null) {
                    if (!Utils.checkPort(ip, port)) {
                        u.send(new NMDCResponse(t.Key("closedportsinactivemode")).pushEndChar());
                        u.putInfo("realmod", "A");
                        u.putInfo("virtmod", "P");
                        u.putInfo("mode", "P");
                        EldestHub.getServer().sendToAll(new NMDCResponse().putMyINFO(u, u.getDBData().getUserValue("hideinfo") != null ? (((Integer) u.getDBData().getUserValue("hideinfo")) == 1) : false));
                    }
                    u.putInfo("porttest", null);
                    return;
                } else if (u.getInfo("realmod") != null && u.getInfo("virtmod") != null && u.getInfo("realmod").equalsIgnoreCase("a") && u.getInfo("virtmod").equalsIgnoreCase("p")) {
                    DCUser remote = EldestHub.getServer().findUser(to);
                    if (remote == null) {
                        return;
                    }
                    if (remote.getInfo("mode").equalsIgnoreCase("p")) {
                        return;
                    }
                    //u.getDBData().putCustomValue("connectcount", (Long) u.getDBData().getCustomValue("connectcount") + 1);
                    remote.send(new NMDCResponse("$RevConnectToMe " + u.getInfo("nick") + " " + to).pushEndChar());
                    return;
                } else if (u.getInfo("mode").equalsIgnoreCase("p")) {
                    DCUser remote = EldestHub.getServer().findUser(to);
                    if (remote == null) {
                        return;
                    }
                    if (flag.equalsIgnoreCase("N") || flag.equalsIgnoreCase("R")) {
                        if (c.KeyInt("ForceChangeIPtoReal") == 1) {
                            remote.send(new NMDCResponse("$ConnectToMe " + to + " " + u.getIP() + ":" + port + flag).pushEndChar());
                        } else {
                            remote.send(new NMDCResponse("$ConnectToMe " + to + " " + ip + ":" + port + flag).pushEndChar());
                        }
                    } else {
                        if (remote.getInfo("mode").equalsIgnoreCase("p")) {
                            return;
                        }
                    }
                }
            }

            DCUser ru = EldestHub.getServer().findUser(to);
            if (ru != null && !ru.isBot()) {
                if (c.KeyInt("ForceChangeIPtoReal") == 1) {
                    ru.send(new NMDCResponse("$ConnectToMe " + to + " " + u.getIP() + ":" + port + flag).pushEndChar());
                } else {
                    ru.send(new NMDCResponse("$ConnectToMe " + to + " " + ip + ":" + port + flag).pushEndChar());
                }
            }
        }
    }

    public static void eventRevConnectToMe(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.RevConnectToMe)) {
            eventFlood(u, t.Key("floodconnect"), 1);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.RevConnectToMe, data);
        if (cmd != null) {
            DCUser ru = EldestHub.getServer().findUser(cmd[2]);
            if (cmd[1].equals(u.getInfo("nick")) && ru != null) {
                if (!ru.isBot()) {
                    if (c.KeyInt("ClientPortTestEnabled") == 1) {
                        if (u.getInfo("realmod") != null && !u.getInfo("mode").equalsIgnoreCase("p")) {
                            if (u.getInfo("realmod").equalsIgnoreCase("a") || u.getInfo("mode").equalsIgnoreCase("a")) {
                                if (ru.getInfo("mode").equalsIgnoreCase("p")) {
                                    return;
                                }
                            }
                        }
                    }
                    ru.send(new NMDCResponse("$RevConnectToMe " + cmd[1] + " " + cmd[2]).pushEndChar());
                }
            }
        }
    }

    public static void eventSearch(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.Search)) {
            eventFlood(u, t.Key("floodsearch"), 0);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        String[] cmd = NMDCParser.Parse(NMDCCommands.Search, data);
        if (cmd != null) {
            if (c.KeyInt("ClientPortTestEnabled") == 1 && u.getInfo("realmod") != null && u.getInfo("virtmod") != null && u.getInfo("realmod").equalsIgnoreCase("a") && u.getInfo("virtmod").equalsIgnoreCase("p")) {
                if (cmd.length == 8) {
                    NMDCResponse r = new NMDCResponse();
                    r.pushBeginChar();
                    r.pushString("Search");
                    r.pushSeparatorChar();
                    r.pushString("Hub");
                    r.pushString(":");
                    r.pushString(u.getInfo("nick"));
                    r.pushSeparatorChar();
                    r.pushString(cmd[3]);
                    r.pushString("?");
                    r.pushString(cmd[4]);
                    r.pushString("?");
                    r.pushString(cmd[5]);
                    r.pushString("?");
                    r.pushString(cmd[6]);
                    r.pushString("?");
                    r.pushString(cmd[7]);
                    r.pushEndChar();
                    //conn.getDBData().putCustomValue("searchcount", (Long) conn.getDBData().getCustomValue("searchcount") + 1);
                    EldestHub.getServer().sendToAll(r);
                    return;
                }
            } else {
                EldestHub.getServer().sendToAll(new NMDCResponse(data).pushEndChar());
            }
        }
    }

    public static void eventSR(NMDCUser u, String data) {
        if (u.af.check(NMDCCommands.SR)) {
            eventFlood(u, t.Key("floodsearch"), 0);
            return;
        }
        if (u.getStatusFlag() < 4) {
            u.disconnect();
            return;
        }
        DCUser ru = EldestHub.getServer().findUser(data.substring(data.lastIndexOf(0x05) + 1));
        if (ru != null && !ru.isBot()) {
            ru.send(new NMDCResponse(data.substring(0, data.lastIndexOf(0x05))).pushEndChar());
        }

    }

    private static void eventChatCommand(NMDCUser u, String[] cmd) {
        String[] args;
        String etype;
        if (cmd[1].indexOf(" ") != -1) {
            etype = cmd[1].substring(1, cmd[1].indexOf(" ")).toLowerCase();
            args = cmd[1].split(" ");
        } else {
            etype = cmd[1].substring(1).toLowerCase();
            args = new String[1];
            args[0] = "";
        }
        NMDCChatCommands.getInstance().fireCommand(u, etype, args);
    }

    public static void eventUnknow(NMDCUser u, String data) {
        if (u.af.check(-1)) {
            eventFlood(u, t.Key("floodunknow"), 1);
            return;
        }
    }

    public static void eventUserEnter(NMDCUser u) {
        if (c.KeyInt("ClientPortTestEnabled") == 1) {
            //int cmode = conn.getDBData().getUserValue("cmode") != null ? ((Integer) conn.getDBData().getUserValue("cmode")) : -1;
            if (u.getInfo("mode").equalsIgnoreCase("a")) {
                //if (cmode != 2) {
                u.send(new NMDCResponse(t.Key("testconnection")).pushEndChar());
                u.putInfo("porttest", "true");
                u.send(new NMDCResponse("$RevConnectToMe " + c.KeyString("BotNick") + " " + u.getInfo("nick")).pushEndChar());
                //}
            }
        }
        if (c.KeyInt("ForceChangeIPtoReal") == 1) {
            u.send(new NMDCResponse("$UserIP").pushSeparatorChar().pushString(u.getInfo("nick")).pushSeparatorChar().pushString(u.getIP()).pushEndChar());
        }
        String ret = StandartChatCommands.getInstance().MOTD;
        for (Map.Entry<String, DynamicReplace> e : StandartChatCommands.dynm.entrySet()) {
            ret = ret.replaceAll("\\{" + e.getKey() + "\\}", e.getValue().replace(u));
        }
        ret = Utils.bbcodesCheck(u, ret);
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            u.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + ret).pushEndChar());
        }
        if (u.getDBData().getUserValue("hidemenu") == null || ((Integer) u.getDBData().getUserValue("hidemenu")) != 1) {
            UserCommandsManager.SendUserCommandList(u);
        }
        if (c.KeyInt("DCBlacklistEnabled") == 1 || (u.getProfile() != 0 && u.getProfile() != 1)) {
            Iterator<String> it = StandartChatCommands.getInstance().dcblacklist.iterator();
            while (it.hasNext()) {
                String s = it.next();
                if (!s.equalsIgnoreCase("") && u.getInfo("software").toLowerCase().contains(s.toLowerCase())) {
                    u.send(new NMDCResponse(t.Key("yourdcclientinblacklist")).pushEndChar());
                }
            }
        }
    }

    public static void eventFlood(NMDCUser u, String data, int t) {
        switch (t) {
            case 0: {
                u.send(new NMDCResponse(data));
            }
            case 1: {
                u.sendDisconnect(new NMDCResponse(data));
                return;
            }
            case 2: {
                u.send(new NMDCResponse(data));
                //NMDCServer.sql.addGag(conn.getNick(), conn.getIP(), System.currentTimeMillis() + 10000, c.KeyString("BotNick"), data);
            }
            case 3: {
                //NMDCServer.sql.addBan(conn.getNick(), conn.getIP(), System.currentTimeMillis() + 10000, c.KeyString("BotNick"), data);
                u.sendDisconnect(new NMDCResponse(data));
                return;
            }
        }

    }
}
