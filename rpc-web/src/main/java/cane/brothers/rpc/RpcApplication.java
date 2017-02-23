package cane.brothers.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

//@EnableOAuth2Sso
@SpringBootApplication
public class RpcApplication {

	private static Logger logger = LoggerFactory.getLogger(RpcApplication.class);

    //@Value("${server.port}")
    //private static String serverPort;

	public static void main(String[] args) {
	//	SpringApplication.run(RpcApplication.class, " -Djava.net.useSystemProxies=true");
	//	SpringApplication.run(RpcApplication.class, " -Dhttp.proxyHost=vrnsrv-proxy -Dhttp.proxyPort=8080");
		SpringApplication.run(RpcApplication.class, args);

		logger.info("запускаем приложение по адресу: http://localhost:" + 5000);
	}
}
