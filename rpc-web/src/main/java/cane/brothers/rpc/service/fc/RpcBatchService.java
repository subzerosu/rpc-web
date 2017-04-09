package cane.brothers.rpc.service.fc;

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
import org.springframework.stereotype.Service;

import javax.xml.ws.WebServiceException;
import java.util.HashSet;
import java.util.Set;

@Service
public class RpcBatchService implements RpcBatch {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private FederalClient federalClient = null;

    @Override
    public boolean autorize() {
        try {
            if (log.isInfoEnabled()) {
                log.info("Подключаюсь к сервису почтовых отправлений...");
            }

            ItemDataService service = new ItemDataService();
            federalClient = service.getItemDataServicePort();

            if (log.isInfoEnabled()) {
                log.info("Подключились.");
            }
        } catch (WebServiceException ex) {
            log.error("Проблемы с доступом к сервису почтовых отправлений.", ex.getMessage());
            log.info("Попробуйте еще раз позднее.");
            federalClient = null;
            return false;
        }
        return true;
    }

    @Override
    public String sendRequest(TicketRequest ticketRequest) {
        String ticketId = null;

        try {
            TicketResponse ticketResponse = federalClient.getTicket(ticketRequest);
            ticketId = ticketResponse.getValue();
            log.info("Тикет отправлен: " + ticketId);

            Error err = ticketResponse.getError();
            if (err != null) {
                log.warn("Есть ошибки");
                log.error(err.getErrorTypeID() + ": " + err.getErrorName());
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Ошибок нет");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return ticketId;
    }

    @Override
    public File getResponse(AnswerByTicketRequest ticketAnswer) {
        File responseFile = null;

        try {
            AnswerByTicketResponse ticketResponce = federalClient.getResponseByTicket(ticketAnswer);

            if (ticketResponce != null) {
                if (ticketResponce.getError() != null) {
                    log.error("error: " + ticketResponce.getError().getErrorName() + " ("
                            + ticketResponce.getError().getErrorTypeID() + ")");
                } else {
                    responseFile = ticketResponce.getValue();
                }
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return responseFile;
    }
}
