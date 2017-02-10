package cane.brothers.rpc.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.config.GoogleProperties;

@RestController
@RequestMapping(value = "/api")
public class GoogleController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GoogleProperties properties;
	
//	@Value("cane.brothers.spring.google.spreadsheet.id")
//	private String sheetId;
//	
//	@Value("cane.brothers.spring.google.spreadsheet.sheet.name")
//	private String sheetName;
	
	@RequestMapping(value = "/google", method = RequestMethod.GET)
	public ResponseEntity<List<String>> readProperties() {
		logger.info(" get '/api/google'");

		List<String> responseBody = new ArrayList<String>();
		responseBody.add(properties.getId());
		responseBody.add(properties.getSheet().getName());
//		responseBody.add(sheetId);
//		responseBody.add(sheetName);
		logger.info("responce " + responseBody);
		
		return new ResponseEntity<List<String>>(responseBody, HttpStatus.OK);
	}
}
