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

import cane.brothers.rpc.service.email.RpcMail;

@RestController
@RequestMapping(value = "/api")
public class EmailController extends BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RpcMail email;

	@GetMapping(value = "/email")
	public ResponseEntity<List<String>> sendEmail() {
		email.sendEmail("hello");
		
		List<String> responseBody = new ArrayList<String>();
		responseBody.add("Ok");
		return new ResponseEntity<List<String>>(responseBody, HttpStatus.OK);
	}
}
