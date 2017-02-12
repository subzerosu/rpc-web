package cane.brothers.rpc.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.config.GoogleProperties;
import cane.brothers.rpc.data.PostEntry;
import cane.brothers.rpc.service.fc.RpcBatch;
import cane.brothers.rpc.service.sheets.RpcSheets;

@RestController
@RequestMapping(value = "/api")
public class GoogleController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GoogleProperties properties;

	@Autowired
	private RpcSheets sheets;

	@Autowired
	private RpcBatch fc;

	@GetMapping(value = "/google")
	public ResponseEntity<List<String>> readProperties() {
		logger.info(" get '/api/google'");

		List<String> responseBody = new ArrayList<String>();
		responseBody.add(properties.getId());
		responseBody.add(properties.getSheet().getName());
		logger.info("responce " + responseBody);

		return new ResponseEntity<List<String>>(responseBody, HttpStatus.OK);
	}

	@GetMapping(value = "/table")
	public ResponseEntity<Set<PostEntry>> readTable() {
		logger.info(" get '/api/table'");

		Set<PostEntry> table = sheets.getPostEntries();

		return new ResponseEntity<Set<PostEntry>>(table, HttpStatus.OK);
	}

}
