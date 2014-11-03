/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.onbee.eldesthub.antiflood;

/**
 *
 * @author ASUS
 */
public class AntiFlood {
    
    private int mcount;
    private long mtime;
    
    private int count = 0;
    private long time = 0;
    
    public AntiFlood(int MaxCount, long MaxTime) {
        mcount = MaxCount;
        mtime = MaxTime;
    }
    
    public boolean check(long now) {
        if (time == 0) {
            time = now;
            return false;
        }
        if(count < mcount) {
            count++;
        } else {
            count = 0;
            if((now - time) < mtime) {
                time = now;
                return true;
            }
            time = now;
        }
        return false;
    }
    
}
