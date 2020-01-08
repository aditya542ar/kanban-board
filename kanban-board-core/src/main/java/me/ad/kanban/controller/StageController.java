package me.ad.kanban.controller;

import me.ad.kanban.auth.AuthorizationService;
import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.StageGetAllQueryDto;
import me.ad.kanban.dto.query.TeamGetAllQueryDto;
import me.ad.kanban.service.StageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/stage")
public class StageController {
    public static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final CustomMessageProperties message;
    private final StageService stageService;
    private final AuthorizationService authorizationService;

    @Autowired
    public StageController(CustomMessageProperties message, StageService StageService, AuthorizationService authorizationService) {
        this.message = message;
        this.stageService = StageService;
        this.authorizationService = authorizationService;
    }

    @GetMapping(path = {"", "/", "/all"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StageDto> getAllStage(Optional<StageGetAllQueryDto> queryDto) {
        return stageService.findAllStageWithFilterQuery(queryDto);
    }

    @PostMapping(path = {"", "/", "/all"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StageDto> getAllStagePost(@RequestBody StageGetAllQueryDto queryDto) {
        return stageService.findAllStageWithFilterQuery(Optional.of(queryDto));
    }

    @GetMapping(path = {"/count"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getStageCount(Optional<StageGetAllQueryDto> queryDto) {
        return stageService.findTotalCount(queryDto);
    }

    @PostMapping(path = {"/count"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getStageCountPost(@RequestBody StageGetAllQueryDto queryDto) {
        return stageService.findTotalCount(Optional.of(queryDto));
    }

    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public StageDto createStage(@RequestBody StageDto stageDto, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfProject(auth, stageDto.getProjectId());
        return stageService.saveOrUpdateStageByDto(stageDto);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public StageDto getStageById(@PathVariable("id") String id) {
        return stageService.findStageById(id);
    }

    @GetMapping(path = "/{id}/tasks", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<TaskDto> getTasksById(@PathVariable("id") String id) {
        return stageService.findTasksByStageId(id);
    }

    @PutMapping(path = "/{id}/update", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public StageDto updateStage(@PathVariable("id") String id, @RequestBody StageDto stageDto, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfStage(auth, id);
        return stageService.updateStageById(id, stageDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStageById(@PathVariable("id") String id, Authentication auth) {
        authorizationService.ensureAuthUserIsOwnerOfStage(auth, id);
        stageService.deleteStageById(id);
    }

    @PostMapping(path = "/remove", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteStageByIdList(@RequestBody List<String> idList, Authentication auth) {
        idList.forEach((id) -> authorizationService.ensureAuthUserIsOwnerOfStage(auth, id));
        stageService.deleteStages(idList);
    }

}
