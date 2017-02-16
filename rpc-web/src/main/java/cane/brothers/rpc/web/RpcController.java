package cane.brothers.rpc.web;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.service.fc.RpcBatch;
import cane.brothers.rpc.service.sheets.RpcSheets;

@RestController
@RequestMapping(value = "/api/rpc/ticket")
public class RpcController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RpcBatch rpcBatch;

	@Autowired
	private RpcSheets rpcSheet;

	// TODO testcase
	// @GetMapping(value = "/fc/request")
	// public ResponseEntity<List<String>> sendDummyRequest() {
	// // TODO
	// // "39405679002898"
	// // "39405601009001"
	//
	// String ticket = rpcBatch.sendRequest(null);
	// List<String> responseBody = new ArrayList<String>();
	//
	// if (ticket != null) {
	// responseBody.add("ticket:" + ticket);
	// return new ResponseEntity<List<String>>(responseBody, HttpStatus.OK);
	// } else {
	// return new ResponseEntity<List<String>>(responseBody,
	// HttpStatus.ACCEPTED);
	// }
	// }

	@GetMapping()
	public ResponseEntity<String> getRpcTicket() {
		Set<PostEntry> barcodes = rpcSheet.getPostEntries();
		String ticket = rpcBatch.sendRequest(barcodes);
		return new ResponseEntity<String>(ticket, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Set<PostEntry>> getResponceByRpcTicket(String ticket) {
		// TODO validate ticket format
		Set<PostEntry> barcodes = rpcBatch.getResponce(ticket);
		return new ResponseEntity<Set<PostEntry>>(barcodes, HttpStatus.OK);
	}
}
