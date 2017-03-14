package cane.brothers.rpc.service.quartz;

import java.util.Set;

import cane.brothers.rpc.data.quartz.TaskDto;

public interface TaskService {

    TaskDto createTask(TaskDto task);

    Set<TaskDto> getAllTasks();
}
