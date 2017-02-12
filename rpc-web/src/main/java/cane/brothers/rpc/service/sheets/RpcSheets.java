package cane.brothers.rpc.service.sheets;

import java.util.List;
import java.util.Set;

import cane.brothers.rpc.data.PostEntry;

/**
 * Класс служит для работы с таблицей ПО. Читает записи и удаляет старые.
 *
 * @author cane
 */
public interface RpcSheets {

	/**
	 * Читаем данные из таблицы google и преобразуем списочные данные к набору
	 * ПО.
	 *
	 * @return
	 */
	Set<PostEntry> getPostEntries();

	/**
	 * Преобразуем сырые табличные данные к набору ПО.
	 *
	 * @param table
	 * @return
	 */
	Set<PostEntry> castTable(List<List<Object>> table);

	/**
	 * @param oldEntries
	 * @return true если все старые записи были удалены успешно или не удаленого
	 *         ничего. В ином случае - false.
	 */
	boolean removePostEntries(Set<PostEntry> oldEntries);
}
