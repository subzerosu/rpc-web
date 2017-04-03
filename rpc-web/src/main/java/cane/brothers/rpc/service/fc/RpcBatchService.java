package cane.brothers.rpc.service.fc;

import cane.brothers.rpc.config.RpcProperties;
import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.data.PostError;
import cane.brothers.rpc.data.PostOperation;
import cane.brothers.rpc.data.TreatmentPostEntry;
import org.russianpost.fclient.Error;
import org.russianpost.fclient.*;
import org.russianpost.fclient.postserver.AnswerByTicketRequest;
import org.russianpost.fclient.postserver.AnswerByTicketResponse;
import org.russianpost.fclient.postserver.TicketRequest;
import org.russianpost.fclient.postserver.TicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class RpcBatchService implements RpcBatch {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RpcProperties rpcProps;

    @Override
    public String sendRequest(Set<PostEntry> barcodes) {
        String ticketId = null;

        if (CollectionUtils.isEmpty(barcodes)) {
            logger.warn("Список баркодов пуст. Нечего обрабатывать!");
        }

        else {
            try {
                ItemDataService service = new ItemDataService();
                FederalClient client = service.getItemDataServicePort();

                TicketRequest ticketRequest = new TicketRequest();
                ticketRequest.setLogin(rpcProps.getLogin());
                ticketRequest.setPassword(rpcProps.getPassword());

                File request = new File();
                request.setFileName("req1");

                for (PostEntry barcode : barcodes) {
                    Item item = new Item();
                    item.setBarcode(barcode.getBarcode());
                    request.getItem().add(item);
                }

                ticketRequest.setRequest(request);
                TicketResponse ticketResponse = client.getTicket(ticketRequest);

                ticketId = ticketResponse.getValue();
                logger.info("ticket: " + ticketId);

                Error err = ticketResponse.getError();
                if (err != null) {
                    logger.error(err.getErrorTypeID() + ": " + err.getErrorName());
                }

            }
            catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }

        return ticketId;
    }

    @Override
    public Set<PostEntry> getResponce(String ticket) {
        Set<PostEntry> barcodes = new HashSet<>();
        if (ticket != null) {
            try {

                ItemDataService service = new ItemDataService();
                FederalClient client = service.getItemDataServicePort();

                // AnswerByTicketResponse ticketResponce = new
                // AnswerByTicketResponse();
                // ticketResponce.setValue(value);
                AnswerByTicketRequest ticketRequest = new AnswerByTicketRequest();
                ticketRequest.setLogin(rpcProps.getLogin());
                ticketRequest.setPassword(rpcProps.getPassword());

                // TODO validate ticket format
                ticketRequest.setTicket(ticket);

                AnswerByTicketResponse ticketResponce = client.getResponseByTicket(ticketRequest);

                if (ticketResponce != null) {
                    if (ticketResponce.getError() != null) {
                        logger.error(ticketResponce.getError().getErrorName() + " ("
                                + ticketResponce.getError().getErrorTypeID() + ")");
                    }

                    else {
                        File responce = ticketResponce.getValue();

                        // logger.debug(responce);
                        for (Item item : responce.getItem()) {
                            // TODO connect with order
                            TreatmentPostEntry barcode = new TreatmentPostEntry(
                                    new PostEntry(item.getBarcode(), null, null), true);

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

                            barcodes.add(barcode);
                        }
                    }
                }

            }
            catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return barcodes;
    }
}
