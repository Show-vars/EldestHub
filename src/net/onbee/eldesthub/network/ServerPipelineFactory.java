package net.onbee.eldesthub.network;

import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.nmdc.NMDCClientHandler;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class ServerPipelineFactory implements ChannelPipelineFactory {

    private Configuration c = Configuration.getInstance();

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline pipeline = Channels.pipeline();

        ChannelBuffer delimiter = ChannelBuffers.dynamicBuffer();
        delimiter.writeBytes("|".getBytes());
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter));

        pipeline.addLast("decoder", new OneToOneDecoder() {

            @Override
            protected Object decode(ChannelHandlerContext ctx, org.jboss.netty.channel.Channel channel, Object msg) throws Exception {
                if (!(msg instanceof ChannelBuffer)) {
                    return msg;
                }
                //String input = ((ChannelBuffer) msg).toString(java.nio.charset.Charset.forName(c.KeyString("HubEncoding")));
                return ((ChannelBuffer) msg).toString(java.nio.charset.Charset.forName(c.KeyString("HubEncoding")));
            }
        });
        pipeline.addLast("encoder", new ServerEncoder());

        pipeline.addLast("handler", new NMDCClientHandler());
        return pipeline;
    }
}
