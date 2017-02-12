package cane.brothers.rpc.service.sheets;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с таблицами google.
 *
 * @author cane
 */
public interface GoogleSheets {

	/**
	 * Читаем google таблицу сырых данных.
	 *
	 * @return
	 * @throws IOException
	 */
	List<List<Object>> readTable() throws IOException;
}
