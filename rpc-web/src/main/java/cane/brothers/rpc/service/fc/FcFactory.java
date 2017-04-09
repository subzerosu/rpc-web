package cane.brothers.rpc.service.fc;

import cane.brothers.rpc.config.RpcProperties;
import org.russianpost.fclient.postserver.AnswerByTicketRequest;
import org.russianpost.fclient.postserver.TicketRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cane on 09.04.17.
 */
public class FcFactory {

    @Autowired
    private static RpcProperties rpcProps;


    public static TicketRequest getTicketRequest() {
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setLogin(rpcProps.getLogin());
        ticketRequest.setPassword(rpcProps.getPassword());
        return ticketRequest;
    }

    public static AnswerByTicketRequest getTicketAnswerRequest() {
        AnswerByTicketRequest ticketRequest = new AnswerByTicketRequest();
        ticketRequest.setLogin(rpcProps.getLogin());
        ticketRequest.setPassword(rpcProps.getPassword());
        return ticketRequest;
    }
}
