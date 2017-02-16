package cane.brothers.rpc.data.quartz;

/**
 *
 * @author cane
 */
public class SimpleQuartzDto {

	private short intervalInHours;

	/**
	 * Default constructor
	 */
	public SimpleQuartzDto() {
		super();
	}

	public SimpleQuartzDto(short intervalInHours) {
		this.intervalInHours = intervalInHours;
	}

	public short getIntervalInHours() {
		return intervalInHours;
	}

	public void setIntervalInHours(short intervalInHours) {
		this.intervalInHours = intervalInHours;
	}

	@Override
	public String toString() {
		return "SimpleQuartzDto [intervalInHours=" + intervalInHours + "]";
	}

}
