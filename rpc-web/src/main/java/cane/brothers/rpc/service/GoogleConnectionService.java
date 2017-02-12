package cane.brothers.rpc.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

@Service
public class GoogleConnectionService implements GoogleConnection {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OAuth2ClientContext oAuth2ClientContext;

	@Value("${cane.brothers.spring.google.app.name}")
	String appName;

	// TODO properties
	@Value("security.oauth2.client.client-id")
	String clientId;

	@Value("security.oauth2.client.clientSecret")
	String clientSecret;

	/**
	 * Sheets service
	 */
	private Sheets sheetsService = null;

	public static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	public static HttpTransport HTTP_TRANSPORT;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	private GoogleCredential googleCredentials = null;

	/**
	 * Constructor
	 */
	public GoogleConnectionService() {
		logger.debug("create google connection");
	}

	private Credential getCredentials() {
		if (googleCredentials == null) {
			googleCredentials = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
					.setClientSecrets(clientId, clientSecret).build()
					.setAccessToken(oAuth2ClientContext.getAccessToken().getValue())
					.setExpiresInSeconds((long) oAuth2ClientContext.getAccessToken().getExpiresIn());
			logger.debug("google credentials: " + googleCredentials);
		}
		return googleCredentials;
	}

	@Override
	public Sheets getSheetsService() {
		if (this.sheetsService == null) {
			this.sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
					.setApplicationName(appName).build();
		}
		return this.sheetsService;
	}
}
