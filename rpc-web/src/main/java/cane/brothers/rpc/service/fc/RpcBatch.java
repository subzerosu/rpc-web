package cane.brothers.rpc.service.fc;

import java.util.Set;

import cane.brothers.rpc.data.PostEntry;

/**
 * Сервис для отслеживания ПО в пакетном режиме.
 *
 * @author cane
 */
public interface RpcBatch {

	/**
	 * Отправить запрос на подготовку информации по списку идентификаторов.
	 *
	 * @param barcodes
	 *            список ПО для отправки.
	 *
	 * @return ticket id
	 */
	String sendRequest(Set<PostEntry> barcodes);

	/**
	 * Опросить сервис почтовых отправлений для получения результата для ранее
	 * отпраленного запроса.
	 *
	 * @param ticket
	 *            id запроса
	 * @return
	 */
	Set<PostEntry> getResponce(String ticket);
}
