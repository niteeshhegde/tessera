package com.jpmorgan.quorum.server.utils;

import com.quorum.tessera.config.ServerConfig;
import com.quorum.tessera.ssl.context.ServerSSLContextFactory;
import java.net.URI;
import java.nio.file.Paths;
import javax.net.ssl.SSLContext;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.unixsocket.UnixSocketConnector;

public class ServerUtils {

    public static Server buildWebServer(ServerConfig serverConfig) {
        Server server = new Server();
        URI uri = serverConfig.getBindingUri();

        if (serverConfig.isUnixSocket()) {

            HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();

            UnixSocketConnector connector = new UnixSocketConnector(server, httpConnectionFactory);
            connector.setAcceptQueueSize(128);

            String path = Paths.get(serverConfig.getServerUri()).toAbsolutePath().toString();
            connector.setUnixSocket(path);

            server.setConnectors(new Connector[] {connector});

            return server;
        }

        if (serverConfig.isSsl()) {
            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());

            SSLContext sslContext = ServerSSLContextFactory.create().from(uri.toString(), serverConfig.getSslConfig());

            SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
            sslContextFactory.setSslContext(sslContext);
            sslContextFactory.setNeedClientAuth(true);
            sslContextFactory.setRenegotiationAllowed(false);
            ServerConnector connector =
                    new ServerConnector(
                            server,
                            new SslConnectionFactory(sslContextFactory, "http/1.1"),
                            new HttpConnectionFactory(https));
            connector.setPort(uri.getPort());
            server.setConnectors(new Connector[] {connector});
            return server;
        }

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(uri.getPort());
        server.setConnectors(new Connector[] {connector});

        return server;
    }
}
