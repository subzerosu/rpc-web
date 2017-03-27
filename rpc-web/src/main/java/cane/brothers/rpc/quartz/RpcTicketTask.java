package cane.brothers.rpc.quartz;

import java.io.Serializable;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.service.fc.RpcBatch;
import cane.brothers.rpc.service.sheets.RpcSheets;

/**
 *
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

        // 4. получить операции по тикету
        Set<PostEntry> ticketResponce = rpcBatch.getResponce(ticket);

        // TODO
        // 5. обработать операции

        // TODO
        // 6. подготовить и отправить результаты проверки письмом
    }
}
