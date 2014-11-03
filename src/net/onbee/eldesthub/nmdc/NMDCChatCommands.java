package net.onbee.eldesthub.nmdc;

import java.util.HashMap;
import java.util.Map;
import net.onbee.eldesthub.Translation;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.extensions.ChatCommand;

public class NMDCChatCommands {

    private Translation t = Translation.getInstance();
    private static NMDCChatCommands instance;
    private Map<String, ChatCommand> m;

    NMDCChatCommands() {
        m = new HashMap<>();
    }

    public boolean register(String etype, ChatCommand listener) {
        if (m.get(etype) == null) {
            m.put(etype, listener);
            return true;
        }
        return false;
    }

    public void fireCommand(DCUser u, String etype, String[] cmd) {
        ChatCommand cm = m.get(etype);
        if (cm != null) {
            for (int i = 0; i < cm.getProfile().length; i++) {
                if (cm.getProfile()[i] == -1 || u.getProfile() == cm.getProfile()[i]) {
                    cm.invoke(u, cmd);
                    return;
                }
            }
            u.send(new NMDCResponse(t.Key("donthavepermission")).pushEndChar());
            return;
        }
        u.send(new NMDCResponse(t.Key("unknowchatcommand")).pushSeparatorChar().pushString(etype).pushEndChar());
        return;
    }

    public static NMDCChatCommands getInstance() {
        if (instance == null) {
            instance = new NMDCChatCommands();
        }
        return instance;
    }
}
