package me.ad.kanban.controller;

import me.ad.kanban.auth.AuthorizationService;
import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.query.TaskGetAllQueryDto;
import me.ad.kanban.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {
    public static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final CustomMessageProperties message;
    private final TaskService taskService;
    private final AuthorizationService authorizationService;

    @Autowired
    public TaskController(CustomMessageProperties message, TaskService taskService, AuthorizationService authorizationService) {
        this.message = message;
        this.taskService = taskService;
        this.authorizationService = authorizationService;
    }

    @GetMapping(path = {"", "/", "/all"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TaskDto> getAllTask(Optional<TaskGetAllQueryDto> queryDto) {
        return taskService.findAllTaskWithFilterQuery(queryDto);
    }

    @PostMapping(path = {"", "/", "/all"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TaskDto> getAllTaskPost(@RequestBody TaskGetAllQueryDto queryDto) {
        return taskService.findAllTaskWithFilterQuery(Optional.of(queryDto));
    }

    @GetMapping(path = {"/count"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getTaskCount(Optional<TaskGetAllQueryDto> queryDto) {
        return taskService.findTotalCount(queryDto);
    }

    @PostMapping(path = {"/count"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getTaskCountPost(@RequestBody TaskGetAllQueryDto queryDto) {
        return taskService.findTotalCount(Optional.of(queryDto));
    }

    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody TaskDto taskDto, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfProject(auth, taskDto.getTeam().getProjectId());
        return taskService.saveOrUpdateTaskByDto(taskDto);
    }

    @PutMapping(path = "/{id}/update", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public TaskDto updateTaskById(@PathVariable("id") String id, @RequestBody TaskDto taskDto, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfTask(auth, id);
        return taskService.updateTaskById(id, taskDto);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public TaskDto getTaskById(@PathVariable("id") String id) {
        return taskService.findTaskById(id);
    }


    @DeleteMapping(path = "/{id}")
    public void deleteTaskById(@PathVariable("id") String id, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfTask(auth, id);
        taskService.deleteTaskById(id);
    }

    @PostMapping(path = "/remove", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteTaskByIdList(@RequestBody List<String> idList, Authentication auth) {
        idList.forEach((id) -> authorizationService.ensureAuthUserIsOwnerOfTask(auth, id));
        taskService.deleteTasks(idList);
    }

}
