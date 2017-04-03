package cane.brothers.rpc.service.quartz;

import cane.brothers.rpc.RpcUtils;
import cane.brothers.rpc.data.TaskEntry;
import cane.brothers.rpc.data.quartz.TaskDto;
import cane.brothers.rpc.repo.RpcTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@Service
public class RpcTaskService implements TaskService {

    // private static Map<String, Long> map = new HashMap<>();

    // @Autowired
    // private JobService jobService;

    @Autowired
    private RpcTaskRepository taskRepo;

    @Override
    public TaskDto createTask(TaskDto task) {
        // TODO validation
        if (task == null || task.getId() != null || task.getName() == null || task.getName().isEmpty()) {
            return null;
        }

        // TriggerKey triggerKey = new TriggerKey(task.getName(),
        // RpcUtils.getGroupName());
        // if (jobService.createJob(triggerKey, task.getInterval())) {

        TaskEntry entry = new TaskEntry(task, RpcUtils.getGroupName());
        entry = taskRepo.saveAndFlush(entry);
        TaskDto resultTask = new TaskDto(entry);

        return resultTask;
        // }
        // return null;
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

    @Override
    public TaskDto getTask(Integer taskId) {
        TaskEntry entry = taskRepo.findOne(taskId);
        return (entry == null ? null : new TaskDto(entry));
    }

    @Override
    public TaskDto updateTask(TaskDto task) {
        Assert.notNull(task);
        Assert.notNull(task.getId());

        TaskDto resultTask = null;

        // update quartz
        // TriggerKey triggerKey = new TriggerKey(task.getName(),
        // RpcUtils.getGroupName());
        // if (jobService.resetJob(triggerKey, task.getInterval())) {

        // update in db
        TaskEntry entry = taskRepo.findOne(task.getId());
        entry.setInterval(task.getInterval());
        entry = taskRepo.saveAndFlush(entry);
        resultTask = new TaskDto(entry);
        // }

        return resultTask;
    }

}
