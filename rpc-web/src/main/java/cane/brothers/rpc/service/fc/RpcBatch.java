package cane.brothers.rpc.service.fc;

import org.russianpost.fclient.File;
import org.russianpost.fclient.postserver.AnswerByTicketRequest;
import org.russianpost.fclient.postserver.TicketRequest;

/**
 * Сервис для отслеживания ПО в пакетном режиме.
 *
 * @author cane
 */
public interface RpcBatch {

	/**
	 * Подключаемся к сервису почтовых отправлений клиента
	 *
	 * @return true если клиент готов
     */
	boolean autorize();

	/**
	 * Отправить запрос на подготовку информации по списку идентификаторов.
	 *
	 * @param ticketRequest
	 *            список ПО для отправки.
	 *
	 * @return ticket id
	 */
	String sendRequest(TicketRequest ticketRequest);

	/**
	 * Опросить сервис почтовых отправлений для получения результата для ранее
	 * отпраленного запроса.
	 *
	 * @param ticketAnswer
	 *            id запроса
	 * @return
	 */
	File getResponse(AnswerByTicketRequest ticketAnswer);
}
