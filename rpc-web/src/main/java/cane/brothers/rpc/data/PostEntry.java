package cane.brothers.rpc.data;

/**
 * Простой класс описывающий почтовое отправление. <br />
 * Все ПО сортируются по номеру заказа.
 *
 * @author cane
 */
public class PostEntry implements Comparable<PostEntry> {

	protected String barcode;
	protected String article;
	protected String date;
	protected boolean needAttention = false;
	
	/**
	 * default constructor
	 */
	public PostEntry() {
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param postEtrty not null post entry
	 */
	public PostEntry(PostEntry postEtrty) {
		this(postEtrty.getBarcode(), postEtrty.getArticle(), postEtrty.getDate(), postEtrty.isNeedAttention());
	}
	
	
	/**
	 * @param postEtrty
	 * @param isNeedAttention
	 */
	public PostEntry(PostEntry postEtrty, boolean isNeedAttention) {
		this(postEtrty.getBarcode(), postEtrty.getArticle(), postEtrty.getDate(), isNeedAttention);
	}
	
	/**
	 * Constructor
	 * 
	 * @param barcode
	 *            номер почтового отправления
	 * @param article
	 *            номер заказа
	 * @param date
	 *            Дата добавления
	 */
	public PostEntry(String barcode, String article, String date) {
		this(barcode, article, date, false);
	}
	
	/**
	 * @param barcode
	 * @param article
	 * @param date
	 * @param needAttention
	 */
	public PostEntry(String barcode, String article, String date, boolean needAttention) {
		super();
		this.barcode = barcode;
		this.article = article;
		this.date = date;
		this.needAttention = needAttention;
	}


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isNeedAttention() {
		return needAttention;
	}

	public void setNeedAttention(boolean needAttention) {
		this.needAttention = needAttention;
	}

	@Override
	public int compareTo(PostEntry o) {
		if (o != null && o.getArticle() != null) {
			return this.article.toUpperCase().compareTo(
					o.getArticle().toUpperCase());
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ПО ").append(getBarcode()).append("; номер заказа: ")
				.append(getArticle());
		return builder.toString();
	}
}
