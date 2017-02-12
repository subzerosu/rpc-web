package cane.brothers.rpc.service.fc;

/**
 * Сервис для отслеживания ПО в пакетном режиме.
 *
 * @author cane
 */
public interface RpcBatch {

	/**
	 * Отправить запрос на подготовку информации по списку идентификаторов.
	 *
	 * @return ticket id
	 */
	String sendRequest();
}
