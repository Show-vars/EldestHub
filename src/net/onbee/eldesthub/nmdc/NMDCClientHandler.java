package net.onbee.eldesthub.nmdc;

import java.net.InetSocketAddress;
import net.onbee.eldesthub.EldestHub;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.clientage.UID;
import net.onbee.eldesthub.network.ServerHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NMDCClientHandler extends SimpleChannelUpstreamHandler {

    private Logging l = Logging.getInstance();
    private NMDCUser u;

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        String ip = e.getChannel().getRemoteAddress().toString().substring(1).substring(0, e.getChannel().getRemoteAddress().toString().indexOf(":")  - 1);
        UID uid = new UID(ip, ((InetSocketAddress) e.getChannel().getRemoteAddress()).getPort() + "");
        u = new NMDCUser(uid);
        u.init(e.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        EldestHub.getServer().quitUser(u.getUID());
    }
    
    @Override
    public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        EldestHub.getServer().quitUser(u.getUID());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String request = (String) e.getMessage();
        u.handleResponse(request);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        Throwable cause = e.getCause();
        l.log(Logging.SYSTEM_LOG, e.getCause() + "");
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
