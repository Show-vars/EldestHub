package net.onbee.eldesthub.extensions;

import com.sun.cnpi.rss.elements.Item;
import com.sun.cnpi.rss.elements.Rss;
import com.sun.cnpi.rss.parser.RssParser;
import com.sun.cnpi.rss.parser.RssParserException;
import com.sun.cnpi.rss.parser.RssParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.Translation;
import net.onbee.eldesthub.Utils;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.nmdc.NMDCChatCommands;
import net.onbee.eldesthub.nmdc.NMDCResponse;
import net.onbee.eldesthub.nmdc.NMDCUser;

public class StandartChatCommands {

    private static StandartChatCommands instance;
    private static Logging l = Logging.getInstance();
    private static Translation t = Translation.getInstance();
    private static Configuration c = Configuration.getInstance();
    private int max = c.KeyInt("RSSCount");
    public static Map<String, DynamicReplace> dynm = Collections.synchronizedMap(new HashMap<String, DynamicReplace>());
    public ArrayList<String> antiAdv = new ArrayList<>();
    public ArrayList<String> dcblacklist = new ArrayList<>();
    public String MOTD = "";
    public String Rules = "";
    public String Help = "";
    private String lastRSS = "";

    public void init() {
        try {
            MOTD = Utils.readFileAsString("config" + File.separator + "motd.txt");
            Rules = Utils.readFileAsString("config" + File.separator + "rules.txt");
            Help = Utils.readFileAsString("config" + File.separator + "help.txt");
            String temp[] = Utils.readFileAsString("config" + File.separator + "advlist.txt").split("\n");
            String temp2[] = Utils.readFileAsString("config" + File.separator + "dcblacklist.txt").split("\n");
            antiAdv.addAll(Arrays.asList(temp));
            dcblacklist.addAll(Arrays.asList(temp2));
        } catch (IOException ex) {
            l.log(Logging.SYSTEM_LOG, "Can't read file from \"plugins" + File.separator + "texts\"");
            return;
        }
        NMDCChatCommands.getInstance().register("rules", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandRules(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = -1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("help", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandHelp(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = -1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-ban", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandBan(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-gag", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandGag(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-disconnect", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandDisconnect(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-sendtoallpm", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandFromBotToAllPM(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-sendtoall", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandFromBot(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-sendtouser", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandFromBotToUser(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-sendtouserpm", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandFromBotToUserPM(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-reloadtranslations", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandReloadTranslations(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-reloadtexts", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandReloadTexts(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-reloadadvlist", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandReloadAdvList(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-reloaddcblacklist", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandReloadDCBlackList(u);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-registeruser", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandRegisterUser(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[2];
                i[0] = 0;
                i[1] = 1;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-setconf", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandSetConfig(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        NMDCChatCommands.getInstance().register("admin-debuguser", new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                CommandDebugUser(u, cmd);
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = 0;
                return i;
            }
        });
        setDynamic("RSSInformer", new DynamicReplace() {

            @Override
            public String replace(DCUser u) {
                if (u.getDBData() != null && u.getDBData().getUserValue("hiderss") != null && ((Integer) u.getDBData().getUserValue("hiderss")) == 1) {
                    return "";
                }
                return getRSSCached();
            }
        });
        NMDCChatCommands.getInstance().register(c.KeyString("RSSCommand"), new ChatCommand() {

            @Override
            public void invoke(DCUser u, String[] cmd) {
                try {
                    u.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick"))).pushSeparatorChar().pushString(Utils.bbcodesCheck(u, getRSSNew())).pushEndChar());
                } catch (RssParserException ex) {
                    l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
                }
            }

            @Override
            public int[] getProfile() {
                int[] i = new int[1];
                i[0] = -1;
                return i;
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    lastRSS = getRSSNew();
                } catch (RssParserException ex) {
                    l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
                }
            }
        }, 0, 3600000);
    }

    private String getRSSCached() {
        return lastRSS;
    }

    private String getRSSNew() throws RssParserException {
        try {
            RssParser parser = RssParserFactory.createDefault();
            Rss rss = parser.parse(new URL(c.KeyString("RSSUrl")));
            Collection items = rss.getChannel().getItems();
            if (items != null && !items.isEmpty()) {
                String result = "";
                int it = 0;
                result += t.Key("lastfeeds") + ":\n";
                for (Iterator i = items.iterator(); i.hasNext();) {
                    if (it == max) {
                        break;
                    }
                    Item item = (Item) i.next();
                    String output = "[i]" + item.getCategories() + " " + item.getTitle() + ":[/i] " + item.getLink().getText();
                    output = output.replaceAll("\\|", "/");
                    output = HTMLDecoder.decode(output);
                    result += output;
                    if (i.hasNext()) {
                        result += "\n";
                    }
                    it++;
                }
                return result;
            }
            return "";
        } catch (IOException | RssParserException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
            return "";
        }
    }

    public static void setDynamic(String find, DynamicReplace replace) {
        dynm.put(find, replace);
    }

    private void CommandRules(DCUser u) {
        String ret = Rules;
        for (Map.Entry<String, DynamicReplace> e : dynm.entrySet()) {
            ret.replace("{" + e.getKey() + "}", e.getValue().replace(u));
        }
        ret = Utils.bbcodesCheck(u, ret);
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            u.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + ret).pushEndChar());
        }
    }

    private void CommandHelp(DCUser u) {
        String ret = Help;
        for (Map.Entry<String, DynamicReplace> e : dynm.entrySet()) {
            ret.replace("{" + e.getKey() + "}", e.getValue().replace(u));
        }
        ret = Utils.bbcodesCheck(u, ret);
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            u.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + ret).pushEndChar());
        }
    }

    private void CommandBan(DCUser u, String[] args) {
        if (args.length < 4) {
            u.send(new NMDCResponse(t.Key("younotspecifiedfields")).pushEndChar());
            return;
        }
        String nickip = args[1];
        String time = args[2];
        String reason = args[3];
        if (nickip.equals("") || time.equals("") || reason.equals("")) {
            u.send(new NMDCResponse(t.Key("younotspecifiedfields")).pushEndChar());
            return;
        }
        if (!time.matches("^\\d*")) {
            u.send(new NMDCResponse(t.Key("incorrecttimefield")).pushEndChar());
            return;
        }
        long itime = Long.parseLong(time);
        long ltime = 0;
        boolean succes = false;
        if (itime != 0) {
            ltime = System.currentTimeMillis() + (itime * 60 * 60 * 1000);
        }
        if (nickip.matches("(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9])[.]){3}(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9]))")) {
            succes = EldestHub.getSQL().addBan("", nickip, ltime, u.getInfo("nick"), reason);
        } else {
            DCUser remote = null;//NMDCServer.getUser(nickip);
            if (remote != null) {
                succes = EldestHub.getSQL().addBan(nickip, remote.getIP(), ltime, u.getInfo("nick"), reason);
                if (succes) {
                    remote.sendDisconnect(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + t.Key("youhavebeenbannedfromthishub")).pushEndChar());
                }
            } else {
                succes = EldestHub.getSQL().addBan(nickip, "", ltime, u.getInfo("nick"), reason);
            }
        }
        if (succes) {
            u.send(new NMDCResponse(t.Key("yousuccesbanuser")).pushEndChar());
        } else {
            u.send(new NMDCResponse(t.Key("erroronbaninguser")).pushEndChar());
        }
    }

    private void CommandGag(DCUser u, String[] args) {
        if (args.length < 4) {
            u.send(new NMDCResponse(t.Key("younotspecifiedfields")).pushEndChar());
            return;
        }
        String nickip = args[1];
        String time = args[2];
        String reason = args[3];
        if (nickip.equals("") || time.equals("") || reason.equals("")) {
            u.send(new NMDCResponse(t.Key("younotspecifiedfields")).pushEndChar());
            return;
        }
        if (!time.matches("^\\d*")) {
            u.send(new NMDCResponse(t.Key("incorrecttimefield")).pushEndChar());
            return;
        }
        long itime = Long.parseLong(time);
        long ltime = 0;
        boolean succes = false;
        if (itime != 0) {
            ltime = System.currentTimeMillis() + (itime * 60 * 60 * 1000);
        }
        if (nickip.matches("(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9])[.]){3}(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9]))")) {
            succes = EldestHub.getSQL().addBan("", nickip, ltime, u.getInfo("nick"), reason);
        } else {
            DCUser remote = null;//NMDCServer.getUser(nickip);
            if (remote != null) {
                succes = EldestHub.getSQL().addBan(nickip, remote.getIP(), ltime, u.getInfo("nick"), reason);
                if (succes) {
                    remote.getDBData().putGagValue("username", nickip);
                    remote.getDBData().putGagValue("ip", remote.getIP());
                    remote.getDBData().putGagValue("time", ltime);
                    remote.getDBData().putGagValue("by", u.getInfo("nick"));
                    remote.getDBData().putGagValue("reason", reason);
                    remote.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + t.Key("youhavebeengaggedfromthishub")).pushEndChar());
                }
            } else {
                succes = EldestHub.getSQL().addBan(nickip, "", ltime, u.getInfo("nick"), reason);
            }
        }
        if (succes) {
            u.send(new NMDCResponse(t.Key("yousuccesgaguser")).pushEndChar());
        } else {
            u.send(new NMDCResponse(t.Key("errorongaginguser")).pushEndChar());
        }
    }

    private void CommandDisconnect(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("notspecifiednickname")).pushEndChar());
            return;
        }
        l.printlnD(args[1]);
        l.printlnD("asd");
        DCUser r = EldestHub.getServer().findUser(args[1]);
        if (r != null) {
            r.sendDisconnect(new NMDCResponse(t.Key("youhavebeendisconnectedbyop")).pushEndChar());
        } else {
            u.send(new NMDCResponse(t.Key("specifiedusernotfound")).pushEndChar());
            return;
        }
    }

    private void CommandFromBot(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("younotspecifiedmessage")).pushEndChar());
            return;
        }
        String data = "";
        for (int i = 1; i < args.length; i++) {
            data += " ";
            data += args[i];
        }
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            EldestHub.getServer().sendToAll(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + data).pushEndChar());
        }
    }

    private void CommandFromBotToAllPM(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("younotspecifiedmessage")).pushEndChar());
            return;
        }
        String data = "";
        for (int i = 1; i < args.length; i++) {
            data += " ";
            data += args[i];
        }
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            EldestHub.getServer().sendToAllPm(data, c.KeyString("BotNick"));
            u.send(new NMDCResponse(t.Key("messagesentsuccesfull")).pushEndChar());
        }
    }

    private void CommandFromBotToUser(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("notspecifiednickname")).pushEndChar());
            return;
        }
        if (args.length < 3) {
            u.send(new NMDCResponse(t.Key("younotspecifiedmessage")).pushEndChar());
            return;
        }
        String data = "";
        for (int i = 2; i < args.length; i++) {
            data += " ";
            data += args[i];
        }
        DCUser ru = EldestHub.getServer().findUser(args[1]);
        if (ru == null) {
            u.send(new NMDCResponse(t.Key("specifiedusernotfound")).pushEndChar());
            return;
        }
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            ru.send(new NMDCResponse(Utils.quote(c.KeyString("BotNick")) + " " + data).pushEndChar());
            u.send(new NMDCResponse(t.Key("messagesentsuccesfull")).pushEndChar());
        }
    }

    private void CommandFromBotToUserPM(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("notspecifiednickname")).pushEndChar());
            return;
        }
        if (args.length < 3) {
            u.send(new NMDCResponse(t.Key("younotspecifiedmessage")).pushEndChar());
            return;
        }
        String data = "";
        for (int i = 2; i < args.length; i++) {
            data += " ";
            data += args[i];
        }
        DCUser ru = EldestHub.getServer().findUser(args[1]);
        if (ru == null) {
            u.send(new NMDCResponse(t.Key("specifiedusernotfound")).pushEndChar());
            return;
        }
        if (!c.KeyString("BotNick").equalsIgnoreCase("")) {
            ru.send(new NMDCResponse("$To: " + args[1] + " From: " + c.KeyString("BotNick") + " $<" + c.KeyString("BotNick") + "> " + data).pushEndChar());
            u.send(new NMDCResponse(t.Key("messagesentsuccesfull")).pushEndChar());
        }
    }

    private void CommandRegisterUser(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("notspecifiednickname")).pushEndChar());
            return;
        }
        if (args.length < 3) {
            u.send(new NMDCResponse(t.Key("younotspecifiedpassword")).pushEndChar());
            return;
        }
        if (args.length < 4) {
            u.send(new NMDCResponse(t.Key("younotspecifiedprofile")).pushEndChar());
            return;
        }
        int profile = Integer.parseInt(args[3]);
        EldestHub.getSQL().registerUser(args[1], Utils.md5(args[2]), profile);
        DCUser ru = EldestHub.getServer().findUser(args[1]);
        if (ru != null && !ru.isBot()) {
            NMDCUser run = (NMDCUser) ru;
            run.setProfile(profile);
            if (run.getProfile() == 0 || run.getProfile() == 1) {
                EldestHub.getServer().newOp(run);
            }
        }
        u.send(new NMDCResponse(t.Key("usersucessfullregitered")).pushEndChar());
    }

    private void CommandReloadTranslations(DCUser u) {
        Translation.init();
        u.send(new NMDCResponse(t.Key("translationsreloaded")).pushEndChar());
    }

    private void CommandReloadTexts(DCUser u) {
        try {
            MOTD = Utils.readFileAsString("config" + File.separator + "motd.txt");
            Rules = Utils.readFileAsString("config" + File.separator + "rules.txt");
            Help = Utils.readFileAsString("config" + File.separator + "help.txt");
        } catch (IOException ex) {
            l.log(Logging.SYSTEM_LOG, "Can't read file from \"plugins" + File.separator + "texts\"");
            return;
        }
        u.send(new NMDCResponse(t.Key("textsreloaded")).pushEndChar());
    }

    private void CommandReloadAdvList(DCUser u) {
        try {
            String temp[] = Utils.readFileAsString("config" + File.separator + "advlist.txt").split("\n");
            antiAdv.clear();
            antiAdv.addAll(Arrays.asList(temp));
            u.send(new NMDCResponse(t.Key("advlistsreloaded")).pushEndChar());
        } catch (IOException ex) {
            l.log(Logging.SYSTEM_LOG, "Can't read file from \"plugins" + File.separator + "texts\"");
            return;
        }
    }

    private void CommandReloadDCBlackList(DCUser u) {
        try {
            String temp[] = Utils.readFileAsString("config" + File.separator + "dcblacklist.txt").split("\n");
            dcblacklist.clear();
            dcblacklist.addAll(Arrays.asList(temp));
            u.send(new NMDCResponse(t.Key("dcblacklistreloaded")).pushEndChar());
        } catch (IOException ex) {
            l.log(Logging.SYSTEM_LOG, "Can't read file from \"plugins" + File.separator + "texts\"");
            return;
        }
    }

    private void CommandSetConfig(DCUser u, String[] args) {
        if (args.length < 3) {
            u.send(new NMDCResponse(t.Key("younotspecifiedfields")).pushEndChar());
            return;
        }
        String data = "";
        for (int i = 2; i < args.length; i++) {

            data += args[i];
            if (args.length < i) {
                data += " ";
            }
        }
        if (c.replace(args[1], data)) {
            u.send(new NMDCResponse(t.Key("valuereplaced")).pushEndChar());
        } else {
            u.send(new NMDCResponse(t.Key("speckeydoesnotexist")).pushEndChar());
        }
    }

    private void CommandDebugUser(DCUser u, String[] args) {
        if (args.length < 2) {
            u.send(new NMDCResponse(t.Key("notspecifiednickname")).pushEndChar());
            return;
        }
        DCUser ru = EldestHub.getServer().findUser(args[1]);
        if (ru == null) {
            u.send(new NMDCResponse(t.Key("specifiedusernotfound")).pushEndChar());
            return;
        }
        NMDCUser run = (NMDCUser) ru;
        if(run.isDebuging()) {
            run.disableDebug();
            u.send(new NMDCResponse(t.Key("userdebugoff")).pushEndChar());
        } else {
            run.enableDebug(u);
            u.send(new NMDCResponse(t.Key("userdebugon")).pushEndChar());
        }
    }

    public static StandartChatCommands getInstance() {
        if (instance == null) {
            instance = new StandartChatCommands();
        }
        return instance;
    }
}
