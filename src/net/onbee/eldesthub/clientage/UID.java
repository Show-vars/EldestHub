package net.onbee.eldesthub.clientage;

import java.util.Random;

public class UID extends Object {

    private String UID = "";
    private static Random rn = new Random();

    public UID(String ip, String port) {
        UID = generateUID(ip, port);
    }

    public boolean equals(UID uid) {
        return uid.toString().equals(UID);
    }

    @Override
    public String toString() {
        return UID;
    }

    private static int rand(int lo, int hi) {
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    private static String generateUID(String ip, String port) {
        int n = rand(5, 25);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('a', 'z');
        }
        return ip.intern() + port.intern() + new String(b) + "=" + rand(0, 999999) + System.currentTimeMillis();
    }
}
