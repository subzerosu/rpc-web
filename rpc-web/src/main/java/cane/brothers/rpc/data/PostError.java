package cane.brothers.rpc.data;

import java.math.BigInteger;

public class PostError {

	/**
	 * errorTypeID
	 */
	protected BigInteger type;

	/**
	 * errorName
	 */
	protected String name;

	/**
	 * Constructor
	 */
	public PostError() {
	}

	/**
	 * Constructor
	 *
	 * @param errorTypeID
	 * @param errorName
	 */
	public PostError(BigInteger errorTypeID, String errorName) {
		this.type = errorTypeID;
		this.name = errorName;
	}

	public BigInteger getType() {
		return type;
	}

	public void setType(BigInteger type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ошибка ПО: ").append(getName()).append(" (").append(getType()).append(")");
		return builder.toString();
	}
}
