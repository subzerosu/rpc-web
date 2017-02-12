package cane.brothers.rpc.data;

import java.math.BigInteger;

public class PostOperation {

	/**
	 * OperTypeID
	 */
	protected BigInteger type;

	/**
	 * OperCtgID
	 */
	protected BigInteger attr;

	/**
	 * OperName
	 */
	protected String name;

	// TODO date format
	/**
	 * DateOper
	 */
	protected String date;

	/**
	 * IndexOper
	 */
	protected String index;

	/**
	 * Constructor
	 */
	public PostOperation() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param type
	 * @param attr
	 * @param name
	 * @param date
	 * @param index
	 */
	public PostOperation(BigInteger type, BigInteger attr, String name, String date, String index) {
		this.type = type;
		this.attr = attr;
		this.name = name;
		this.date = date;
		this.index = index;
	}

	public BigInteger getType() {
		return type;
	}

	public void setType(BigInteger type) {
		this.type = type;
	}

	public BigInteger getAttr() {
		return attr;
	}

	public void setAttr(BigInteger attr) {
		this.attr = attr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PostOperation [type=").append(type).append(", attr=").append(attr).append(", name=")
				.append(name).append(", date=").append(date).append(", index=").append(index).append("]");
		return builder.toString();
	}

}
