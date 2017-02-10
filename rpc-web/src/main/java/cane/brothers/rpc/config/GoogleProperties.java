package cane.brothers.rpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="cane.brothers.spring.google.spreadsheet")
public class GoogleProperties {

	private String id;
	
	private Sheet sheet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public static class Sheet {
		
		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
