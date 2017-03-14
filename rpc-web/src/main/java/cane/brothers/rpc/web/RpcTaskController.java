package cane.brothers.rpc.web;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<TaskDto> createTask(TaskDto task) {
        HttpStatus status = HttpStatus.OK;
        TaskDto resultTask = taskService.createTask(task);
        if (resultTask == null) {
            status = HttpStatus.NOT_ACCEPTABLE;
        }

        return new ResponseEntity<TaskDto>(resultTask, status);
    }

    @GetMapping()
    public ResponseEntity<Set<TaskDto>> getAllTasks() {
        HttpStatus status = HttpStatus.OK;
        Set<TaskDto> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<Set<TaskDto>>(tasks, status);
    }

}
