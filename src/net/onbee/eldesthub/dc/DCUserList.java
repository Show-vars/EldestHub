package net.onbee.eldesthub.dc;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.nmdc.NMDCUser;

public class DCUserList {

    private ConcurrentHashMap<UID, DCUser> userlist = new ConcurrentHashMap<UID, DCUser>();
    
    public DCUser getUser(UID uid) {
        return userlist.get(uid);
    }
    
    public void putUser(UID uid, DCUser user) {
        userlist.put(uid, user);
    }
    
    public void removeUser(UID uid) {
        userlist.remove(uid);
    }
    
    public Set<Entry<UID, DCUser>> entrySet() {
        return userlist.entrySet();
    }
}
