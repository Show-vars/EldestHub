package net.onbee.eldesthub.extensions;

import net.onbee.eldesthub.dc.DCUser;

public interface ChatCommand {

    public void invoke(DCUser e, String[] cmd);

    public int[] getProfile();
}
