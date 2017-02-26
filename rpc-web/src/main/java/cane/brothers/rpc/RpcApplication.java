package cane.brothers.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RpcApplication {

    private static Logger logger = LoggerFactory.getLogger(RpcApplication.class);

    private static int serverPort = 5000;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(RpcApplication.class)
                .properties(getAppProperties())
                .run(args);
        // TODO
        logger.info("запускаем приложение по адресу: http://localhost:" + serverPort);
    }

    private static Map<String, Object> getAppProperties() {
        Map<String, Object> appProperties = new HashMap<>();
        appProperties.put("server.port", serverPort);
        return appProperties;
    }
}
