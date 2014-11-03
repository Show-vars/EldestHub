package net.onbee.eldesthub.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {
    private Map<String, Object> user = new HashMap<>();
    private Map<String, Object> ban = new HashMap<>();
    private Map<String, Object> gag = new HashMap<>();
    private ConcurrentHashMap<String, Object> custom = new ConcurrentHashMap<>();
    
    public void putUserValue(String vname, Object value) {
        user.put(vname, value);
    }
    
    public void putCustomValue(String vname, Object value) {
        custom.put(vname, value);
    }
    
    public void putBanValue(String vname, Object value) {
        ban.put(vname, value);
    }
    
    public void putGagValue(String vname, Object value) {
        gag.put(vname, value);
    }
    
    public Object getUserValue(String vname) {
        return user.get(vname);
    }
    
    public Object getCustomValue(String vname) {
        return custom.get(vname);
    }
    
    public Object getBanValue(String vname) {
        return ban.get(vname);
    }
    
    public Object getGagValue(String vname) {
        return gag.get(vname);
    }
    
    public void clearBan() {
        for (Map.Entry<String, Object> entry : ban.entrySet()) {
            entry.setValue(null);
        }
    }
    
    public void clearGag() {
        for (Map.Entry<String, Object> entry : gag.entrySet()) {
            entry.setValue(null);
        }
    }
}
