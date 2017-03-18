package cane.brothers.rpc.data.quartz;

import javax.validation.constraints.NotNull;

import cane.brothers.rpc.data.TaskEntry;

public class TaskDto {

    private Integer id;

    @NotNull
    private Long interval;

    @NotNull
    private String name;

    /**
     * Default constructor
     */
    public TaskDto() {
        super();
    }

    public TaskDto(TaskEntry entry) {
        this(entry.getId(), entry.getName(), entry.getInterval());
    }

    /**
     * @param id
     * @param taskName
     * @param intervalInHours
     */
    public TaskDto(Integer id, String taskName, Long intervalInHours) {
        this.id = id;
        this.name = taskName;
        this.interval = intervalInHours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
