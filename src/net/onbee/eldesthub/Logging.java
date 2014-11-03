package net.onbee.eldesthub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

    public static String SYSTEM_LOG = "System";
    public static String CHAT_LOG = "Chat";
    public static String PM_LOG = "PrivateMassage";
    private static Logging instance;
    private static DateFormat logFormat = new SimpleDateFormat("[dd/MM/yyyy] [HH:mm:ss]");
    private static DateFormat consoleFormat = new SimpleDateFormat("[HH:mm:ss]");
    private static DateFormat fileFormat = new SimpleDateFormat("dd-MM-yyyy");
    private PrintStream out;
    private PrintStream err;

    Logging() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                out = new PrintStream(System.out, true, "Cp866");
                err = new PrintStream(System.err, true, "Cp866");
                System.setOut(new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                    }
                }));
                System.setErr(err);
            } else {
                out = new PrintStream(System.out, true);
                err = new PrintStream(System.err, true);
                System.setOut(new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                    }
                }));
                System.setErr(err);
            }
        } catch (UnsupportedEncodingException e) {
            err.println("Couldn't change console encoding " + e);
        }
        if (!(new File("logs" + File.separator)).exists()) {
            (new File("logs" + File.separator)).mkdir();
        }

        if (!(new File("logs" + File.separator + SYSTEM_LOG + File.separator)).exists()) {
            (new File("logs" + File.separator + SYSTEM_LOG + File.separator)).mkdir();
        }
        if (!(new File("logs" + File.separator + CHAT_LOG + File.separator)).exists()) {
            (new File("logs" + File.separator + CHAT_LOG + File.separator)).mkdir();
        }
        if (!(new File("logs" + File.separator + PM_LOG + File.separator)).exists()) {
            (new File("logs" + File.separator + PM_LOG + File.separator)).mkdir();
        }
    }

    public void log(String p, String msg) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(new File("logs" + File.separator + p + File.separator + fileFormat.format(new Date()) + "." + p + ".log"), true), true);
        } catch (IOException ex) {
            println(ex.getMessage());
            System.exit(1);
        }
        pw.println(logFormat.format(new Date()) + " " + msg);
    }

    public void log(String p, String msg, String console) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(new File("logs" + File.separator + p + File.separator + fileFormat.format(new Date()) + "." + p + ".log"), true), true);
        } catch (IOException ex) {
            println(ex.getMessage());
            System.exit(1);
        }
        pw.println(logFormat.format(new Date()) + " " + msg);
        if (console == null) {
            System.out.println(consoleFormat.format(new Date()) + " " + msg);
        } else {
            System.out.println(consoleFormat.format(new Date()) + " " + console);
        }
    }

    public final void println(String console) {
        out.println(console);
    }

    public final void printlnD(String console) {
        out.println(consoleFormat.format(new Date()) + " " + console);
    }

    public static Logging getInstance() {
        if (instance == null) {
            instance = new Logging();
        }
        return instance;
    }
}
