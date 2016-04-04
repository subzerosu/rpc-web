package cane.brothers.rpc.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;

import cane.brothers.spring.google.GoogleProperties;

@Controller
@RequestMapping("/")
@EnableConfigurationProperties(GoogleProperties.class)
public class HomeController {

	@Autowired
	private GoogleProperties properties;
	
	//@Value("${cane.brothers.spring.google.spreadsheet-name}")
	//private String spreadsheetName;
	
	@Autowired(required = false)
	private SpreadsheetEntry googleTable;
	
	@RequestMapping(method = RequestMethod.GET)
	public String helloGoogle(Model model) {
		//String title = "Нет доступа к таблице данных " + spreadsheetName;
		String title = "Нет доступа к таблице данных " + properties.getSpreadsheetName();
		if (googleTable != null) {
			title = googleTable.getTitle().getPlainText();
		}
		model.addAttribute("table", title);

		return "hello";
	}

	// public String connect(
	// @PathVariable("tableName") String reader) {
	// //book.setReader(reader);
	// //readingListRepository.save(book);
	// return "redirect:/{tableName}";
	// }

}
