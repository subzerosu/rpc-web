package cane.brothers.rpc.web;

import cane.brothers.rpc.data.TaskEntry;
import cane.brothers.rpc.data.quartz.RpcTaskOperation;
import cane.brothers.rpc.data.quartz.TaskDto;
import cane.brothers.rpc.data.quartz.TaskOperationDto;
import cane.brothers.rpc.repo.RpcTaskRepository;
import cane.brothers.rpc.service.GoogleConnection;
import cane.brothers.rpc.service.quartz.JobService;
import cane.brothers.rpc.service.quartz.TaskService;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @Autowired
    private GoogleConnection google;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    OAuth2ClientContext oauth2ClientContext;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
        HttpStatus status = HttpStatus.OK;
        TaskDto resultTask = taskService.createTask(task);
        if (resultTask == null) {
            status = HttpStatus.CONFLICT;
        }

        return new ResponseEntity<>(resultTask, status);
    }

    @GetMapping()
    public ResponseEntity<Set<TaskDto>> getAllTasks() {
        HttpStatus status = HttpStatus.OK;
        Set<TaskDto> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(tasks, status);
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
        return new ResponseEntity<>(task, status);
    }

    /**
     * Fetch current task operation.
     *
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

        return new ResponseEntity<>(new TaskOperationDto(oper), status);
    }

    /**
     * Perform the operation on a task.
     *
     * @param oper
     * @param taskId
     * @return
     */
    @PutMapping("/{taskId}/operation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskOperationDto> performTaskOperation(@RequestBody TaskOperationDto oper,
            @PathVariable Integer taskId) throws ExecutionException, InterruptedException {
        HttpStatus status = HttpStatus.OK;
        RpcTaskOperation taskOper = RpcTaskOperation.UNDEFINED;

        log.info("AccessToken    " + oauth2ClientContext.getAccessToken());
        google.setAccessToken(oauth2ClientContext.getAccessToken());
        printAccessToken();

        TaskEntry task = taskRepo.findOne(taskId);
        if (task != null) {
            TriggerKey triggerKey = new TriggerKey(task.getName(), task.getGroup());

            // TODO service
            if (oper == null) {
                status = HttpStatus.ACCEPTED;
            }

            else if (RpcTaskOperation.START.toString().equals(oper.getOperation())) {
                Future<Boolean> result = jobService.startJob(triggerKey, task.getInterval());
                while (!result.isDone()) {
                    Thread.sleep(100);
                }

                if (result.get()) {
                    taskOper = RpcTaskOperation.START;
                }
                else {
                    taskOper = RpcTaskOperation.UNDEFINED;
                }
            }

            else if (RpcTaskOperation.STOP.toString().equals(oper.getOperation())) {
                Future<Boolean> result = jobService.stopJob(triggerKey);
                while (!result.isDone()) {
                    Thread.sleep(100);
                }

                if (result.get()) {
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

        return new ResponseEntity<>(new TaskOperationDto(taskOper), status);
    }

    private void printAccessToken() {
        // TODO
        SecurityContext sContext = SecurityContextHolder.getContext();
        Authentication principal = sContext.getAuthentication();
        log.info("Authentication " + SecurityContextHolder.getContext().getAuthentication());

        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            Object details = authentication.getDetails();
            log.info("details " + details);

            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails) details;
                log.info("token " + oauthsDetails.getTokenValue());
            }
        }
    }
}
