package cane.brothers.rpc.service;

import com.google.api.services.sheets.v4.Sheets;

public interface GoogleConnection {
	
	Sheets getSheetsService();
}
