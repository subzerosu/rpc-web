package cane.brothers.rpc.quartz;

import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.data.TreatmentPostEntry;
import cane.brothers.rpc.service.fc.FcFactory;
import cane.brothers.rpc.service.fc.RpcBatch;
import cane.brothers.rpc.service.sheets.RpcSheets;
import org.russianpost.fclient.File;
import org.russianpost.fclient.postserver.AnswerByTicketRequest;
import org.russianpost.fclient.postserver.TicketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
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
    private RpcBatch postService;

    @Autowired
    private RpcSheets sheetService;


    @Value("${cane.brothers.rpc.ticket.delay}")
    private Integer ticketDelay;

    // check barcodes
    public void execute() {
        log.info("Запустили RPC-задание.");

        // 1. получить таблицу ПО
        Map<String, PostEntry> barcodes = sheetService.getPostEntries();

        // TODO несколько попыток подключений через timeout
        // 2. подключаемся
        if(postService.autorize()) {

            // 2. создать тикет
            TicketRequest ticketRequest = FcFactory.getTicketRequest(barcodes);
            String ticket = postService.sendRequest(ticketRequest);

            // 3. delay
            waitForTicket();

            // 4. получить операции по тикету
            AnswerByTicketRequest ticketAnswer = FcFactory.getTicketAnswerRequest(ticket);
            File responseFile = postService.getResponse(ticketAnswer);
            Set<TreatmentPostEntry> ticketBarcodes = FcFactory.getTicketResponse(responseFile, barcodes);

            // TODO
            // 5. store into db

            // TODO
            // 6. обработать операции
        }

        // TODO
        // 7. подготовить и отправить результаты проверки письмом
    }

    private void waitForTicket() {
        try {
            log.info("Ожидаем обработки тикета " + ticketDelay + " мин.");
            TimeUnit.MINUTES.sleep(ticketDelay);
        } catch(InterruptedException ex) {
            log.info("С потоком что-то не так. обрываем", ex);
            Thread.currentThread().interrupt();
        }
    }
}
