package cane.brothers.rpc.service.quartz;

import java.util.HashSet;
import java.util.Set;

import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

import cane.brothers.rpc.RpcUtils;
import cane.brothers.rpc.data.TaskEntry;
import cane.brothers.rpc.data.quartz.TaskDto;
import cane.brothers.rpc.repo.RpcTaskRepository;

public class RpcTaskService implements TaskService {

    // private static Map<String, Long> map = new HashMap<>();

    @Autowired
    private JobService jobService;

    @Autowired
    private RpcTaskRepository taskRepo;

    @Override
    public TaskDto createTask(TaskDto task) {
        // TODO validation
        if (task == null || task.getId() != null || task.getName() == null || task.getName().isEmpty()) {
            return null;
        }

        TriggerKey triggerKey = new TriggerKey(task.getName(), RpcUtils.getGroupName());
        if (jobService.createJob(triggerKey, task.getInterval())) {

            TaskEntry entry = new TaskEntry(task);
            entry.setGroup(RpcUtils.getGroupName());
            entry = taskRepo.saveAndFlush(entry);
            TaskDto resultTask = new TaskDto(entry);

            return resultTask;
        }
        return null;
    }

    @Override
    public Set<TaskDto> getAllTasks() {
        Set<TaskDto> taskList = new HashSet<>();
        // Set<TriggerKey> keys =
        // jobService.getTriggersByGroup(RpcUtils.getGroupName());
        // for (TriggerKey key : keys) {
        // Trigger trigger = jobService.getTrigger(key);
        // if (trigger instanceof SimpleTrigger) {
        // Long repeatInterval = ((SimpleTrigger) trigger).getRepeatInterval() /
        // 1000;
        // taskList.add(new TaskDto(1l, trigger.getKey().getName(),
        // repeatInterval));
        // }
        // }
        for (TaskEntry entry : taskRepo.findByGroup(RpcUtils.getGroupName())) {
            taskList.add(new TaskDto(entry));
        }
        return taskList;
    }

}
