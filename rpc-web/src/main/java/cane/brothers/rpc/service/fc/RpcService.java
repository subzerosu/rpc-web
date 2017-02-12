package cane.brothers.rpc.service.fc;

import java.util.List;

import org.russianpost.fclient.Error;
import org.russianpost.fclient.FederalClient;
import org.russianpost.fclient.File;
import org.russianpost.fclient.Item;
import org.russianpost.fclient.ItemDataService;
import org.russianpost.fclient.postserver.TicketRequest;
import org.russianpost.fclient.postserver.TicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cane.brothers.rpc.config.RpcProperties;

@Service
public class RpcService implements Rpc {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RpcProperties config;

	@Override
	public String sendRequest() {
		String ticketId = null;
		try {
			ItemDataService service = new ItemDataService();
			FederalClient client = service.getItemDataServicePort();

			TicketRequest ticketRequest = new TicketRequest();
			ticketRequest.setLogin(config.getLogin());
			ticketRequest.setPassword(config.getPassword());

			File request = new File();
			request.setFileName("req1");
			List<Item> barcodes = request.getItem();

			Item barcode = new Item();
			barcode.setBarcode("39405679002898");
			barcodes.add(barcode);

			barcode = new Item();
			barcode.setBarcode("39405601009001");
			barcodes.add(barcode);

			ticketRequest.setRequest(request);
			TicketResponse ticketResponse = client.getTicket(ticketRequest);

			ticketId = ticketResponse.getValue();
			logger.info("ticket: " + ticketId);
			

			Error err = ticketResponse.getError();
			if (err != null) {
				logger.error(err.getErrorTypeID() + " " + err.getErrorName());
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		return ticketId;
	}
}
