package net.onbee.eldesthub.network;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class ServerFactory {

    public ServerFactory(int port) {
        ExecutorService bossExec = new OrderedMemoryAwareThreadPoolExecutor(1, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        ExecutorService ioExec = new OrderedMemoryAwareThreadPoolExecutor(4, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(bossExec, ioExec, 4));
        
        bootstrap.setOption("backlog", 500);
        bootstrap.setOption("connectTimeoutMillis", 5000);
        bootstrap.setPipelineFactory(new ServerPipelineFactory());

        bootstrap.bind(new InetSocketAddress(port));
    }
}
