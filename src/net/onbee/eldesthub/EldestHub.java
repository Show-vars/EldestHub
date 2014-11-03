package net.onbee.eldesthub;

import java.io.File;
import net.onbee.eldesthub.dc.DCUserList;
import net.onbee.eldesthub.mysql.MySql;
import net.onbee.eldesthub.nmdc.NMDCServer;

public class EldestHub {

    private static NMDCServer mServer;
    private static DCUserList mUserlist;
    private static MySql mysql;
    
    public static void main(String[] args) {
        Logging l = Logging.getInstance();
        l.println("NMDC Server EldestHub 2 InDev");
        Configuration.init();
        Translation.init();
        mysql = new MySql();
        mUserlist = new DCUserList();
        mServer = new NMDCServer();
        mServer.start();
        l.println("Running on " + mServer.bindIP() + ":" + mServer.bindPort());
    }
    
    public static NMDCServer getServer() {
        return mServer;
    }
    
    public static MySql getSQL() {
        return mysql;
    }
    
    public static DCUserList getUserlist() {
        return mUserlist;
    }
}
