package cane.brothers.rpc.data.quartz;

/**
 * @author cane
 *
 *         TODO number, description, DB
 */
public class SimpleQuartzDto {

    private Long id;

    private Long interval;

    private String name;

    /**
     * Default constructor
     */
    public SimpleQuartzDto() {
        super();
    }

    public SimpleQuartzDto(Long id, String taskName, Long intervalInHours) {
        this.id = id;
        this.name = taskName;
        this.interval = intervalInHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long intervalInHours) {
        this.interval = intervalInHours;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Simple task [interval=").append(interval)
                .append(" (h)").append(", name=")
                .append(name).append("]");
        return builder.toString();
    }

}
