package cane.brothers.rpc.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cane.brothers.rpc.data.quartz.TaskDto;

@Entity
public class TaskEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TSK_NAME")
    private String name;

    @Column(name = "TSK_GROUP")
    private String group;

    @Column
    private String description;

    @Column
    private Long interval;

    public TaskEntry() {
        super();
    }

    public TaskEntry(TaskDto task) {
        this.name = task.getName();
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
}
