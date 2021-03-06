package cane.brothers.rpc.service.sheets;

import cane.brothers.rpc.config.GoogleProperties;
import cane.brothers.rpc.service.google.GoogleConnection;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис который позволяет читать сырые данные из google-таблиц. Нужно знать
 * только имя таблицы (spreadsheetId) и страницы (sheetName), а также к моменту
 * чтения данных, обмен информацией (авторизацю коди пр.) с google уже должен
 * произойти. См. see {@link GoogleConnection}
 *
 * @author cane
 */
@Service
public class GoogleSheetsService implements GoogleSheets {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoogleProperties properties;

    @Autowired
    private GoogleConnection google;

    @Override
    public List<List<Object>> readTable() throws IOException {
        List<List<Object>> table = new ArrayList<>();
        Sheets service = google.getSheetsService();
        if (google != null) {
            table = readTable(service, properties.getId(), properties.getSheet().getName());
        }
        return table;
    }

    private List<List<Object>> readTable(Sheets service, String spreadsheetId, String sheetName) throws IOException {
        Sheets.Spreadsheets ss = service.spreadsheets();
        Sheets.Spreadsheets.Values v = ss.values();
        ValueRange table = v.get(spreadsheetId, sheetName).execute();

        List<List<Object>> values = table.getValues();
        printTable(values);

        return values;
    }

    private void printTable(List<List<Object>> values) {
        if (values == null || values.size() == 0) {
            logger.debug("No data found.");
        }
        else {
            logger.debug("read data: ");
            for (List<Object> row : values) {
                logger.debug(" " + row);
            }
        }
    }

}
