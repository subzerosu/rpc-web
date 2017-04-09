package cane.brothers.rpc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by mniedre on 04.04.2017.
 */
@Configuration
@ConditionalOnProperty(prefix = "http", name = "proxyHost", matchIfMissing = true)
@EnableConfigurationProperties(ProxyProperties.class)
public class ProxyAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ProxyAutoConfiguration.class);

    @Autowired
    private ProxyProperties properties;

    @Bean
    public SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
                connection.setUseCaches(false);
            }
        };
        if(properties.getProxyHost() != null) {
            factory.setProxy(getProxy());
        }
        return factory;
    }

    private Proxy getProxy() {
        InetSocketAddress address = new InetSocketAddress(properties.getProxyHost(), getPort());
        Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
        logger.info("proxy: " + proxy);
        return proxy;
    }

    private int getPort() {
        int portNr = -1;
        try {
            portNr = Integer.parseInt(properties.getProxyPort());
        } catch (NumberFormatException e) {
            logger.error("Unable to parse the proxy port number");
        }
        return portNr;
    }
}
