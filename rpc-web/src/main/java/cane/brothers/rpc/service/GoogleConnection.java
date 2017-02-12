package cane.brothers.rpc.service;

import com.google.api.services.sheets.v4.Sheets;

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
}
