/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

public class Translation extends DefaultHandler {

    private Map<String, String> TranslationTable = new HashMap<String, String>();
    private static Configuration c = Configuration.getInstance();
    private static Translation instance = new Translation();
    private static Logging l = Logging.getInstance();
    private static String url;

    Translation() {
        url = "lang" + File.separator + c.KeyString("HubLanguage").toLowerCase() + File.separator;
        if (!(new File("lang" + File.separator + c.KeyString("HubLanguage").toLowerCase())).exists()) {
            (new File("lang" + File.separator + c.KeyString("HubLanguage").toLowerCase())).mkdirs();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("entry")) {
            TranslationTable.put(attributes.getValue("key"), attributes.getValue("value"));
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

    public String Key(String s) {
        String ret = TranslationTable.get(s);
        if (ret == null) {
            ret = "%" + s;
        }
        return ret;
    }

    public static Translation getInstance() {
        if (instance == null) {
            instance = new Translation();
        }
        return instance;
    }

    public String switchProfile(int p) {
        switch (p) {
            case 0:
                return Key("administrator");
            case 1:
                return Key("operator");
            case 2:
                return Key("vip");
            case 3:
                return Key("user");
            default:
                return Key("quest");
        }
    }

    public static void init() {
//        getInstance();
        try {
            File dir = new File(url);
            //if (!(new File(url)).exists()) {
            //    l.println("File "+new File(url).getCanonicalPath()+" does not exists.");
            //    System.exit(1);
            //}
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.getName().toLowerCase().lastIndexOf(".xml") != -1) {
                        sp.parse(file, instance);
                    }
                }
            } else {
                l.log(Logging.SYSTEM_LOG, "The directory \"" + dir + "\" does not exist!");
                System.exit(1);
            }
        } catch (ParserConfigurationException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
        } catch (IOException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
        } catch (SAXException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getLocalizedMessage());
        }
    }
}
