package cane.brothers.rpc.service;

import com.google.api.services.sheets.v4.Sheets;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Класс для связи с google сервисами.
 *
 * @author cane
 */
public interface GoogleConnection {

	/**
	 * @return Сервис google таблиц.
	 */
	Sheets getSheetsService();

	void setAccessToken(OAuth2AccessToken accessToken);
}
