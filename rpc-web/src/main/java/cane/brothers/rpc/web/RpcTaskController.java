package cane.brothers.rpc.web;

import java.util.Set;

import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.data.TaskEntry;
import cane.brothers.rpc.data.quartz.RpcTaskOperation;
import cane.brothers.rpc.data.quartz.TaskDto;
import cane.brothers.rpc.data.quartz.TaskOperationDto;
import cane.brothers.rpc.repo.RpcTaskRepository;
import cane.brothers.rpc.service.quartz.JobService;
import cane.brothers.rpc.service.quartz.TaskService;

@RestController
@RequestMapping(value = "/api/rpc/tasks")
public class RpcTaskController extends BaseController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    @Autowired
    private JobService jobService;

    @Autowired
    private RpcTaskRepository taskRepo;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
        HttpStatus status = HttpStatus.OK;
        TaskDto resultTask = taskService.createTask(task);
        if (resultTask == null) {
            status = HttpStatus.CONFLICT;
        }

        return new ResponseEntity<TaskDto>(resultTask, status);
    }

    @GetMapping()
    public ResponseEntity<Set<TaskDto>> getAllTasks() {
        HttpStatus status = HttpStatus.OK;
        Set<TaskDto> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<Set<TaskDto>>(tasks, status);
    }

    // TODO deny access to the foreign tasks
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer taskId) {
        HttpStatus status = HttpStatus.OK;
        TaskDto task = null;
        if (taskId <= 0) {
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            task = taskService.getTask(taskId);
            if (task == null) {
                status = HttpStatus.NO_CONTENT;
            }
        }
        return new ResponseEntity<TaskDto>(task, status);
    }

    /**
     * Fetch current task operation.
     *
     * @param oper
     * @param taskId
     * @return
     */
    @GetMapping("/{taskId}/operation")
    public ResponseEntity<TaskOperationDto> getTaskOperation(@PathVariable Integer taskId) {
        HttpStatus status = HttpStatus.OK;

        RpcTaskOperation oper = RpcTaskOperation.UNDEFINED;
        TaskEntry task = taskRepo.findOne(taskId);
        if (task != null) {
            TriggerKey triggerKey = new TriggerKey(task.getName(), task.getGroup());
            oper = jobService.getJobState(triggerKey);
        }

        else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<TaskOperationDto>(new TaskOperationDto(oper), status);
    }

    /**
     * Perform the operation on a task.
     *
     * @param oper
     * @param taskId
     * @return
     */
    @PutMapping("/{taskId}/operation")
    public ResponseEntity<TaskOperationDto> performTaskOperation(@RequestBody TaskOperationDto oper,
            @PathVariable Integer taskId) {
        HttpStatus status = HttpStatus.OK;
        RpcTaskOperation taskOper = RpcTaskOperation.UNDEFINED;

        TaskEntry task = taskRepo.findOne(taskId);
        if (task != null) {
            TriggerKey triggerKey = new TriggerKey(task.getName(), task.getGroup());

            // TODO service
            if (oper == null) {
                status = HttpStatus.ACCEPTED;
            }

            else if (RpcTaskOperation.START.toString().equals(oper.getOperation())) {
                if (jobService.startJob(triggerKey, task.getInterval())) {
                    taskOper = RpcTaskOperation.START;
                }
                else {
                    taskOper = RpcTaskOperation.UNDEFINED;
                }
            }

            else if (RpcTaskOperation.STOP.toString().equals(oper.getOperation())) {
                if (jobService.stopJob(triggerKey)) {
                    taskOper = RpcTaskOperation.STOP;
                }
                else {
                    taskOper = RpcTaskOperation.UNDEFINED;
                }
            }

            else if (RpcTaskOperation.PAUSE.toString().equals(oper.getOperation())) {
                // TODO pause
                taskOper = RpcTaskOperation.UNDEFINED;
            }
        }

        else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<TaskOperationDto>(new TaskOperationDto(taskOper), status);
    }
}
