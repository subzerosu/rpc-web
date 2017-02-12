package cane.brothers.rpc.service.sheets;

import java.io.IOException;
import java.util.List;

public interface GoogleSheets {

	List<List<Object>> readTable() throws IOException;
}
