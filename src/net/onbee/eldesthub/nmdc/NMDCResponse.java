package net.onbee.eldesthub.nmdc;

import java.io.UnsupportedEncodingException;
import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.dc.DCUser;

public final class NMDCResponse {

    private byte[] response = new byte[0];
    private Configuration c = Configuration.getInstance();
    private Logging l = Logging.getInstance();

    public NMDCResponse() {
    }

    public NMDCResponse(String input) {
        pushString(input);
    }

    public NMDCResponse pushBeginChar() {
        byte[] input = "$".getBytes();
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushString(String s) {
        byte[] input = null;
        try {
            input = s.getBytes(c.KeyString("HubEncoding"));
        } catch (UnsupportedEncodingException ex) {
            l.log(Logging.SYSTEM_LOG, ex.getMessage());
            return this;
        }
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushDoubleSeparator() {
        byte[] input = "$ $".getBytes();
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushDoubleNickSeparator() {
        byte[] input = "$$".getBytes();
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushSeparatorChar() {
        byte[] input = " ".getBytes();
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushEndChar() {
        byte[] input = "|".getBytes();
        byte[] temp = new byte[input.length + response.length];
        System.arraycopy(response, 0, temp, 0, response.length);
        System.arraycopy(input, 0, temp, response.length, input.length);
        response = temp;
        return this;
    }

    public NMDCResponse pushByte(byte b) {
        byte[] temp = new byte[response.length + 2];
        System.arraycopy(response, 0, temp, 0, response.length);
        temp[response.length + 1] = b;
        response = temp;
        return this;
    }

    public byte[] getBytes() throws UnsupportedEncodingException {
            return response;
    }

    public void flush() {
        response = new byte[0];
    }

    @Override
    public String toString() {
        return new String(response/*, c.KeyString("HubEncoding") */);
    }

    public NMDCResponse putMyINFO(DCUser user) {
        return putMyINFO(user, false);
    }

    public NMDCResponse putMyINFO(DCUser user, boolean hiden) {
        pushBeginChar();
        pushString("MyINFO");
        pushSeparatorChar();
        pushBeginChar();
        pushString("ALL");
        pushSeparatorChar();
        pushString(user.getInfo("nick"));
        pushSeparatorChar();
        pushString(user.getInfo("description"));
        pushString(!hiden ? "<" + user.getInfo("software")
                + " V:" + user.getInfo("version")
                + ",M:" + user.getInfo("mode").toUpperCase()
                + ",H:" + user.getInfo("hubs")
                + ",S:" + user.getInfo("slots") + ">" : "");
        pushDoubleSeparator();
        pushString(user.getInfo("con"));
        pushByte(user.getInfo("magic").getBytes()[0]);
        pushBeginChar();
        pushString(!hiden ? user.getInfo("email") : "");
        pushBeginChar();
        pushString(user.getInfo("share"));
        pushBeginChar();
        pushEndChar();
        return this;
    }
}
