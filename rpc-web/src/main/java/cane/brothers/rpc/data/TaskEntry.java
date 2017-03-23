package cane.brothers.rpc.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import cane.brothers.rpc.data.quartz.TaskDto;

@Entity
@Table(name = "RPC_TASKS", uniqueConstraints = @UniqueConstraint(columnNames = { "TSK_NAME", "TSK_GROUP" }))
public class TaskEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TSK_NAME", updatable = false, nullable = false)
    private String name;

    @Column(name = "TSK_GROUP", updatable = false, nullable = false, length = 1024)
    private String group;

    @Column
    private String description;

    @Column
    private Long interval;

    /**
     * Constructor for ORM
     */
    public TaskEntry() {
        super();
    }

    /**
     * Constructor
     *
     * @param task
     */
    public TaskEntry(TaskDto task, String group) {
        this.id = task.getId();
        this.name = task.getName();
        this.group = group;
        this.interval = task.getInterval();
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("TaskEntry [id=").append(id).append(", name=").append(name).append(", group=").append(group)
                .append(", description=").append(description).append(", interval=").append(interval).append("]");
        return builder.toString();
    }
}
