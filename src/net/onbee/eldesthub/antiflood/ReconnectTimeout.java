package net.onbee.eldesthub.antiflood;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReconnectTimeout {
    
    private static Map<String, Long> conns = Collections.synchronizedMap(new HashMap<String, Long>());
    
    public static int check(String ip) {
        long ipl = conns.get(ip);
        if(ipl == -1) {
            conns.put(ip, System.currentTimeMillis());
            return 0;
        } else if((System.currentTimeMillis() - ipl) > 2000) {
            conns.put(ip, System.currentTimeMillis());
            return 1;
        } else {
            conns.put(ip, System.currentTimeMillis());
            return 2;
        }
    }
    public static void remove(String ip) {
        conns.remove(ip);
    }
}
