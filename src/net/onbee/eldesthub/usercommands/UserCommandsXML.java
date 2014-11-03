package net.onbee.eldesthub.usercommands;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.Logging;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class UserCommandsXML extends DefaultHandler {

    private static Configuration c = Configuration.getInstance();
    private static Logging l = Logging.getInstance();
    private String url;

    private UserCommandsXML() {
        url = "config" + File.separator + "UserCommands.xml";
        if (!new File(url).exists()) {
            new File(url).mkdirs();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes a) throws SAXException {
        if (qName.equalsIgnoreCase("entry")) {
            String[] sprofiles = a.getValue("profile").split(",");
            int[] profiles = new int[sprofiles.length];
            for (int i = 0; i < sprofiles.length; i++) {
                profiles[i] = Integer.parseInt(sprofiles[i]);
            }
            UserCommandsManager.register(profiles,
                    Integer.parseInt(a.getValue("type")),
                    Integer.parseInt(a.getValue("context")),
                    a.getValue("name"), a.getValue("command"));
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
        l.log(Logging.SYSTEM_LOG, "Warning at " + ex.getLineNumber() + " . " + ex.getColumnNumber() + "  -  " + ex.getMessage());
    }

    @Override
    public void error(SAXParseException ex) {
        l.log(Logging.SYSTEM_LOG, "Error at {" + ex.getLineNumber() + " . " + ex.getColumnNumber() + "  -  " + ex.getMessage());
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        l.log(Logging.SYSTEM_LOG, "Fatal error at {" + ex.getLineNumber() + " . " + ex.getColumnNumber() + "  -  " + ex.getMessage());
    }

    public static void init() {
        try {
            UserCommandsXML ucxml = new UserCommandsXML();
            File file = new File(ucxml.url);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            if (file.exists()) {
                sp.parse(file, ucxml);
            } else {
                l.log(Logging.SYSTEM_LOG, "Configuration file \"" + file + "\" does not exist!");
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
