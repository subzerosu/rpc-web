package cane.brothers.rpc.web;

import java.util.Set;

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

import cane.brothers.rpc.data.quartz.TaskDto;
import cane.brothers.rpc.service.quartz.TaskService;

@RestController
@RequestMapping(value = "/api/rpc/tasks")
public class RpcTaskController extends BaseController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
        HttpStatus status = HttpStatus.OK;
        TaskDto resultTask = taskService.createTask(task);
        if (resultTask == null) {
            status = HttpStatus.CONFLICT;
        }

        return new ResponseEntity<TaskDto>(resultTask, status);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto task, @PathVariable Integer taskId) {
        HttpStatus status = HttpStatus.OK;
        // task.setId(taskId);
        TaskDto resultTask = null;
        if (task != null && !task.getId().equals(taskId)) {
            status = HttpStatus.FORBIDDEN;
        }
        else {
            resultTask = taskService.updateTask(task);
            if (resultTask == null) {
                status = HttpStatus.CONFLICT;
            }
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

}
