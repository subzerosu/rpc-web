package cane.brothers.rpc.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Простой класс описывающий почтовое отправление которое было обработано, т.е.
 * история запросов была получена.
 *
 * @author cane
 */
public class TreatmentPostEntry extends PostEntry {

	private boolean isTreated;

	private Set<PostError> errors = new HashSet<>();

	private List<PostOperation> operations = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param post
	 *            post entry
	 */
	public TreatmentPostEntry(PostEntry post) {
		this(post, false);
	}

	/**
	 * Constructor
	 *
	 * @param post
	 *            post entry
	 * @param treated
	 *            is treaded flag
	 */
	public TreatmentPostEntry(PostEntry post, boolean treated) {
		super(post);
		isTreated = treated;
	}

	public boolean isTreated() {
		return isTreated;
	}

	public void setTreated(boolean isTreated) {
		this.isTreated = isTreated;
	}

	public Set<PostError> getErrors() {
		return errors;
	}

	public List<PostOperation> getOperations() {
		return operations;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ПО ").append(getBarcode()).append("; номер заказа: ").append(getArticle())
				.append("; обработано: ").append(isTreated);
		return builder.toString();
	}

}
