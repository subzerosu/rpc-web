package cane.brothers.rpc.service.google;

/**
 * Created by cane on 09.04.17.
 */
public interface RpcPostHandler {

    /**
     * проверяем историю посылок. Ищем зависшие посылки
     */
    void checkHistory();
}
