/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.onbee.eldesthub.antiflood;

import java.util.HashMap;
import java.util.Map;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.nmdc.NMDCCommands;

/**
 *
 * @author ASUS
 */
public class AntiFloodManager {
    
    Map<Integer, AntiFlood> m = new HashMap<Integer, AntiFlood>();
    private Configuration c = Configuration.getInstance();
    
    public AntiFloodManager() {
        m.put(-1, new AntiFlood(c.KeyInt("AntiFloodUnknowCount"), c.KeyInt("AntiFloodUnknowDelay")));
        m.put(NMDCCommands.Chat, new AntiFlood(c.KeyInt("AntiFloodChatCount"), c.KeyInt("AntiFloodChatDelay")));
        m.put(NMDCCommands.Close, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.ConnectToMe, new AntiFlood(c.KeyInt("AntiFloodConnectCount"), c.KeyInt("AntiFloodConnectDelay")));
        m.put(NMDCCommands.GetINFO, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.GetNickList, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.Key, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.Kick, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.MultiConnectToMe, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.MultiSearch, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.MyINFO, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.MyPass, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.OpForceMove, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.Quit, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.RevConnectToMe, new AntiFlood(c.KeyInt("AntiFloodConnectCount"), c.KeyInt("AntiFloodConnectDelay")));
        m.put(NMDCCommands.SR, new AntiFlood(c.KeyInt("AntiFloodSearchCount"), c.KeyInt("AntiFloodSearchDelay")));
        m.put(NMDCCommands.Search, new AntiFlood(c.KeyInt("AntiFloodSearchCount"), c.KeyInt("AntiFloodSearchDelay")));
        m.put(NMDCCommands.Supports, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.To, new AntiFlood(c.KeyInt("AntiFloodPMCount"), c.KeyInt("AntiFloodPMDelay")));
        m.put(NMDCCommands.UserIP, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.ValidateNick, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
        m.put(NMDCCommands.Version, new AntiFlood(c.KeyInt("AntiFloodSystemCount"), c.KeyInt("AntiFloodSystemDelay")));
    }
    public boolean check(int command) {
        AntiFlood af = m.get(command);
        if(af != null) {
            return af.check(System.currentTimeMillis());
        }
        return false;
    }
    
}
