package net.onbee.eldesthub.usercommands;

import java.util.ArrayList;
import java.util.Iterator;
import net.onbee.eldesthub.dc.DCUser;
import net.onbee.eldesthub.nmdc.NMDCResponse;

public class UserCommandsManager {

    private static ArrayList al = new ArrayList();

    public static void register(int[] profile, int type, int context, String title, String cmd) {
        al.add(new Command(profile, type, context, title, cmd));
    }

    public static void SendUserCommandList(DCUser u) {
        Iterator iterator = al.iterator();
        while (iterator.hasNext()) {
            Command cmd = (Command) iterator.next();
            if (cmd.checkProfile(-1) || cmd.checkProfile(u.getProfile())) {
                u.send(new NMDCResponse(cmd.build()).pushEndChar());
            }
        }
    }

    public static void ClearUserCommandList(DCUser u) {
        int[] i = new int[1];
        i[0] = -1;
        u.send(new NMDCResponse((new Command(i, 255, 15, null, null)).build()).pushEndChar());
    }
}
