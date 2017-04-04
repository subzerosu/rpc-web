package cane.brothers.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mniedre on 04.04.2017.
 */
@ConfigurationProperties("http")
public class ProxyProperties {

    private String proxyHost;
    private String proxyPort;

    public String getProxyHost() {
        return proxyHost;
    }
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}
