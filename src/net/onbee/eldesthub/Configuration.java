package net.onbee.eldesthub;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class Configuration extends DefaultHandler {

    private Map<String, String> ConfigurationTable = new HashMap<>();
    private static Configuration instance;
    private static Logging l = Logging.getInstance();
    private static String url;

    Configuration() {
        url = "config" + File.separator + "Configuration.xml";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("entry")) {
            ConfigurationTable.put(attributes.getValue("key"), attributes.getValue("value"));
        }

    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    @Override
    public void endElement(String uri, String name, String qName) {
    }

    @Override
    public void warning(SAXParseException ex) {
        l.println("Warning at "
                + ex.getLineNumber() + " . "
                + ex.getColumnNumber() + "  -  "
                + ex.getMessage());
    }

    @Override
    public void error(SAXParseException ex) {
        l.println("Error at {"
                + ex.getLineNumber() + " . "
                + ex.getColumnNumber() + "  -  "
                + ex.getMessage());
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        l.println("Fatal error at {"
                + ex.getLineNumber() + " . "
                + ex.getColumnNumber() + "  -  "
                + ex.getMessage());
        System.exit(1);
    }

    public String KeyString(String s) {
        String ret = ConfigurationTable.get(s);
        if (ret == null) {
            ret = s;
        }
        return ret;
    }

    public int KeyInt(String s) {
        String out = ConfigurationTable.get(s);
        int ret;
        if (out == null) {
            ret = -1;
        } else {
            ret = Integer.parseInt(out);
        }
        return ret;
    }

    public boolean replace(String s, String value) {
        String out = ConfigurationTable.get(s);
        if (out != null) {
            ConfigurationTable.put(s, value);
            return true;
        } else {
            return false;
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public static void init() {
        getInstance();
        try {
            File file = new File(url);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            if (file.exists()) {
                sp.parse(file, instance);
            } else {
                l.log(Logging.SYSTEM_LOG, "Configuration file \"" + file + "\" does not exist!");
                System.exit(1);
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
        }
    }
}
