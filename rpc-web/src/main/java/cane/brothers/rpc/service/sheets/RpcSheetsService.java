package cane.brothers.rpc.service.sheets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cane.brothers.rpc.data.InvalidPostEntry;
import cane.brothers.rpc.data.InvalidReasons;
import cane.brothers.rpc.data.PostEntry;

@Service
public class RpcSheetsService implements RpcSheets {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GoogleSheets googleSheet;

	@Override
	public Set<PostEntry> getPostEntries() {
		Set<PostEntry> barcodes = new TreeSet<PostEntry>();
		if (log.isDebugEnabled()) {
			log.debug("Берем данные из таблицы в виде списка");
		}

		try {
			List<List<Object>> table = googleSheet.readTable();
			barcodes = castTable(table);

		} catch (IOException e) {
			// log.error("Проблемы c google сервисом: ", e);
			log.error("Проблемы с получением строковых даных: ", e);
		}

		return barcodes;
	}

	@Override
	public Set<PostEntry> castTable(List<List<Object>> table) {
		Set<PostEntry> barcodes = new TreeSet<PostEntry>();

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
						} else {
							PostEntry pe = new PostEntry(barcode, article, date);
							if (!barcodes.add(pe)) {
								doubledBarcodes.add(new InvalidPostEntry(pe, InvalidReasons.DUPLICATE));
								log.warn("Баркод {} в наборе уже существует.", barcode);
							} else {
								log.debug("Считали ПО: {}.", pe);
							}
						}
					} else {
						log.warn("Баркод не задан.");
					}
				}
			}

			// messages.addMessage1(" В исходных данных дублей: " +
			// doubledBarcodes.size());
			log.info("В исходных данных дублей: {}.", doubledBarcodes.size());

		} else {
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
