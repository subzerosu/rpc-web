package cane.brothers.rpc.quartz;

import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.service.fc.RpcBatch;
import cane.brothers.rpc.service.sheets.RpcSheets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author cane
 */
@Component("task")
public class RpcTicketTask implements Serializable {

    private static final long serialVersionUID = -8586754748391351562L;

    private static final Logger log = LoggerFactory.getLogger(RpcTicketTask.class);

    @Autowired
    private RpcBatch rpcBatch;

    @Autowired
    private RpcSheets rpcSheet;

    // check barcodes
    public void execute() {
        log.info("Запустили RPC-задание.");

        // 1. получить таблицу ПО
        Set<PostEntry> barcodes = rpcSheet.getPostEntries();

        // 2. создать тикет
        String ticket = rpcBatch.sendRequest(barcodes);

        // 3. delay
        waitForTicket();


        // 4. получить операции по тикету
        Set<PostEntry> ticketResponce = rpcBatch.getResponce(ticket);

        // TODO
        // 5. store into db

        // TODO
        // 6. обработать операции

        // TODO
        // 7. подготовить и отправить результаты проверки письмом
    }

    private void waitForTicket() {
        try {
            log.info("Ожидаем обработки тикета 15 мин.");
            TimeUnit.MINUTES.sleep(15);
        } catch(InterruptedException ex) {
            log.info("С потоком что-то не так. обрываем", ex);
            Thread.currentThread().interrupt();
        }
    }
}
