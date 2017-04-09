package cane.brothers.rpc.service.fc;

import cane.brothers.rpc.config.RpcProperties;
import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.data.PostError;
import cane.brothers.rpc.data.PostOperation;
import cane.brothers.rpc.data.TreatmentPostEntry;
import org.russianpost.fclient.Error;
import org.russianpost.fclient.File;
import org.russianpost.fclient.Item;
import org.russianpost.fclient.Operation;
import org.russianpost.fclient.postserver.AnswerByTicketRequest;
import org.russianpost.fclient.postserver.TicketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cane on 09.04.17.
 */
public class FcFactory {

    private static final Logger log = LoggerFactory.getLogger(FcFactory.class);

    @Autowired
    private static RpcProperties rpcProps;


    /**
     * Создает TicketRequest по списку баркодов.
     *
     * @param barcodes
     * @return
     */
    public static TicketRequest getTicketRequest(Map<String, PostEntry> barcodes) {
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setLogin(rpcProps.getLogin());
        ticketRequest.setPassword(rpcProps.getPassword());

        File requestFile = new File();

        // TODO unique id
        requestFile.setFileName("req1");

        if (CollectionUtils.isEmpty(barcodes)) {
            log.warn("Список баркодов пуст. Тикет будет пустой!");
        }

        else {
            if (log.isDebugEnabled()) {
                log.debug("Формирую тикет с баркодами");
            }

            for (PostEntry barcode : barcodes.values()) {
                Item item = new Item();
                item.setBarcode(barcode.getBarcode());
                requestFile.getItem().add(item);
            }
        }

        ticketRequest.setRequest(requestFile);

        return ticketRequest;
    }

    public static AnswerByTicketRequest getTicketAnswerRequest(String ticket) {
        AnswerByTicketRequest ticketRequest = new AnswerByTicketRequest();
        ticketRequest.setLogin(rpcProps.getLogin());
        ticketRequest.setPassword(rpcProps.getPassword());

        // TODO validate ticket format
        ticketRequest.setTicket(ticket);
        return ticketRequest;
    }

    /**
     * Преобразуем файл из ответа по тикету к списку ПО.
     *
     * @param responseFile
     * @return
     */
    public static Set<TreatmentPostEntry> getTicketResponse(File responseFile, Map<String, PostEntry> barcodes) {
        Set<TreatmentPostEntry> treatedBarcodes = new HashSet<>();

        if(responseFile == null || responseFile.getItem().isEmpty()) {
            log.warn("Файл ответа пуст!");
        }

        else {
            log.debug("Проходим по списку ответов.");
            for (Item item : responseFile.getItem()) {

                PostEntry b0 = barcodes.get(item.getBarcode());
                if(b0 != null) {

                    TreatmentPostEntry barcode = new TreatmentPostEntry(b0);

                    // copy errors
                    if (!item.getError().isEmpty()) {
                        for (Error e : item.getError()) {
                            barcode.getErrors().add(new PostError(e.getErrorTypeID(), e.getErrorName()));
                        }
                    }

                    // copy operations
                    if (!item.getOperation().isEmpty()) {
                        for (Operation op : item.getOperation()) {
                            barcode.getOperations().add(new PostOperation(op.getOperTypeID(), op.getOperCtgID(),
                                    op.getOperName(), op.getDateOper(), op.getIndexOper()));
                        }
                    }

                    treatedBarcodes.add(barcode);
                }
            }
        }

        return treatedBarcodes;
    }
}
