package cane.brothers.rpc.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.service.fc.RpcBatch;

@RestController
@RequestMapping(value = "/api")
public class RpcController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RpcBatch rpc;

	@GetMapping(value = "/rpc")
	public ResponseEntity<List<String>> sendEmail() {
		String ticket = rpc.sendRequest();
		List<String> responseBody = new ArrayList<String>();

		if (ticket != null) {
			responseBody.add("Ok");
			return new ResponseEntity<List<String>>(responseBody, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(responseBody, HttpStatus.ACCEPTED);
		}

	}
}
