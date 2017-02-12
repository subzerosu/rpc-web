package cane.brothers.rpc.data;

/**
 * Простой класс описывающий почтовое отправление которое должно быть
 * удалено из исходного списка. <br />
 * Причины могут быть разные: 
 * <ul>
 * <li>ПО было вручено</li>
 * <li>Вышел срок и оно вернулось/возвращается назад</li>
 * <li>Старое ПО.</li>
 * </ul>
 * 
 * TODO copy constructor
 * 
 * @author cane
 */
public class OldPostEntry extends PostEntry {

	private String reason;
	private int delay = -1;

	/**
	 * Constructor
	 * 
	 * @param barcode
	 * @param article
	 * @param reason
	 * @param delay
	 */
	public OldPostEntry(String barcode, String article, String reason, int delay) {
		super(barcode, article, null);
		this.reason = reason;
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public String getReason() {
		return reason;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ПО ").append(getBarcode()).append("; номер заказа: ")
				.append(getArticle());

		if (reason != null && !reason.isEmpty()) {
			builder.append("; причина: ").append(reason);
		}

		if (delay != -1) {
			builder.append("; срок: ").append(delay).append(" дней");
		}
		return builder.toString();
	}
}
