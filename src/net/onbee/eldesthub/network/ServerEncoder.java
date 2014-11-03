package net.onbee.eldesthub.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class ServerEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext chc, org.jboss.netty.channel.Channel chnl, Object o) throws Exception {
        if(!(o instanceof byte[])) {
            return o;
        }
        byte[] by = (byte[]) o;
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(by);
        return buffer;
    }
}
