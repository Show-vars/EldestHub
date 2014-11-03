package net.onbee.eldesthub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.onbee.eldesthub.dc.DCUser;

public class Utils {

    private static Translation t = Translation.getInstance();
    private static Configuration c = Configuration.getInstance();

    public static String quote(String in) {
        return "<" + in + ">";
    }

    public static String unquote(String in) {
        return in.replace("<", "").replace(">", "");
    }

    public static String formatTime(long input) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Date date = new Date(input);
        return sdf.format(date);
    }

    public static String[] getAddr(String in) {
        in = in.toLowerCase();
        if (in.contains("//")) {
            return in.split("//")[1].replace("/", "").split(":");
        }
        return in.replace("/", "").split(":");
    }

    public static String md5(String input) {
        String res = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] md5 = algorithm.digest();
            String tmp = "";
            for (int i = 0; i < md5.length; i++) {
                tmp = (Integer.toHexString(0xFF & md5[i]));
                if (tmp.length() == 1) {
                    res += "0" + tmp;
                } else {
                    res += tmp;
                }
            }
        } catch (NoSuchAlgorithmException ex) {
        }
        return res;
    }

    public static boolean checkPort(String ip, int port) {
        Socket s = null;
        try {
            s = new Socket();
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(ip, port);
            s.connect(sa, 3000);
            return true;
        } catch (IOException e) {
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static String readFileAsString(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public static boolean bbcodesAvaliable(DCUser u) {
        String bbclients[] = c.KeyString("BBCodesClients").split(";");
        boolean bbyes = false;
        for (int i = 0; i < bbclients.length; i++) {
            if (u.getInfo("software").toLowerCase().contains(bbclients[i].toLowerCase())) {
                bbyes = true;
                break;
            }
        }
        return bbyes;
    }

    public static String bbcodesCheck(DCUser u, String input) {
        if (!bbcodesAvaliable(u)) {
            input = input.replaceAll("\\[b\\]", "");
            input = input.replaceAll("\\[/b\\]", "");
            input = input.replaceAll("\\[i\\]", "");
            input = input.replaceAll("\\[/i\\]", "");
            input = input.replaceAll("\\[u\\]", "");
            input = input.replaceAll("\\[/u\\]", "");
            input = input.replaceAll("\\[s\\]", "");
            input = input.replaceAll("\\[/s\\]", "");
        }
        return input;
    }
}
