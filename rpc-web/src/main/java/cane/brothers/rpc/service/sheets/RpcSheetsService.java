package cane.brothers.rpc.service.sheets;

import cane.brothers.rpc.data.InvalidPostEntry;
import cane.brothers.rpc.data.InvalidReasons;
import cane.brothers.rpc.data.PostEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RpcSheetsService implements RpcSheets {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoogleSheets googleSheet;

    @Override
    public Map<String, PostEntry> getPostEntries() {
        Map<String, PostEntry> barcodes = new TreeMap<>();
        if (log.isDebugEnabled()) {
            log.debug("Берем данные из таблицы в виде списка");
        }

        try {
            List<List<Object>> table = googleSheet.readTable();
            barcodes = castTable(table);

        }
        catch (IOException e) {
            // log.error("Проблемы c google сервисом: ", e);
            log.error("Проблемы с получением строковых даных: ", e);
        }

        return barcodes;
    }

    /**
     * Преобразуем сырые табличные данные к набору ПО.
     *
     * @param table
     * @return
     */
    private Map<String, PostEntry> castTable(List<List<Object>> table) {
        //Set<PostEntry> barcodes = new TreeSet<>();
        Map<String, PostEntry> barcodes = new HashMap<>();

        int rows = (table == null ? 0 : table.size());
        // messages.addMessage1("В таблице баркодов " + rows + " строк.");
        log.info("В таблице баркодов {} строк.", rows);

        if (table != null) {
            log.debug("Трансформируем в почтовые записи");
            List<InvalidPostEntry> doubledBarcodes = new ArrayList<InvalidPostEntry>();

            for (List<Object> row : table) {
                if (row != null) {
                    String barcode = Objects.toString(row.get(0), null);
                    String article = Objects.toString(row.get(1), null);
                    String date = Objects.toString(row.get(2), null);

                    if (barcode != null) {
                        if (barcode.startsWith("#")) {
                            log.debug("Пропускаем header.");
                        }
                        else {
                            PostEntry pe = new PostEntry(barcode, article, date);
                            //if (!barcodes.add(pe)) {
                            if(barcodes.put(barcode, pe) != null) {
                                doubledBarcodes.add(new InvalidPostEntry(pe, InvalidReasons.DUPLICATE));
                                log.warn("Баркод {} в наборе уже существует.", barcode);
                            }
                            else {
                                log.debug("Считали ПО: {}.", pe);
                            }
                        }
                    }
                    else {
                        log.warn("Баркод не задан.");
                    }
                }
            }

            // messages.addMessage1(" В исходных данных дублей: " +
            // doubledBarcodes.size());
            log.info("В исходных данных дублей: {}.", doubledBarcodes.size());

        }
        else {
            log.error("Нет исходного списка данных.");
        }

        // messages.addMessage1(" Уникальных строк с баркодами: " +
        // barcodes.size());
        log.info(" Уникальных строк с баркодами: {}", barcodes.size());

        return barcodes;
    }

    @Override
    public boolean removePostEntries(Set<PostEntry> outputEntries) {
        // TODO
        return false;
    }

}
