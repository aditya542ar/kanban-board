package me.ad.kanban.controller;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.query.ProjectGetAllQueryDto;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/project")
public class ProjectController {

    public static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ProjectService projectService;

    @Autowired
    public ProjectController(CustomMessageProperties message, MapperService mapperService, ProjectService projectService) {
        this.message = message;
        this.mapperService = mapperService;
        this.projectService = projectService;
    }

    @GetMapping(path = {"", "/", "/all"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ProjectDto> getAllProject(Optional<ProjectGetAllQueryDto> queryDto) {
        return projectService.findAllProjectsWithFilterQuery(queryDto);
    }

    @PostMapping(path = {"", "/", "/all"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ProjectDto> getAllProjectPost(@RequestBody ProjectGetAllQueryDto queryDto) {
        return projectService.findAllProjectsWithFilterQuery(Optional.of(queryDto));
    }

    @GetMapping(path = {"/count"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getProjectCount(Optional<ProjectGetAllQueryDto> queryDto) {
        return projectService.findTotalCount(queryDto);
    }

    @PostMapping(path = {"/count"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getProjectCountPost(@RequestBody ProjectGetAllQueryDto queryDto) {
        return projectService.findTotalCount(Optional.of(queryDto));
    }

    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        log.info("CreateProject request received.");
        return projectService.saveOrUpdateProjectByDto(projectDto);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProjectDto getProjectById(@PathVariable("id") String id) {
        return projectService.findProjectById(id);
        // we can use below exception to control the response status code,
        // per each controller method
        /*throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Project with ID: " + id + " doesn't exist!", null);*/
    }

    @GetMapping(path = "/{id}/teams", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<TeamDto> getTeamsByProjectId(@PathVariable("id") String id) {
        return projectService.findTeamsByProjectId(id);
    }

    @GetMapping(path = "/{id}/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<StageDto> getCategoriesByProjectId(@PathVariable("id") String id) {
        return projectService.findStagesByProjectId(id);
    }

    @PutMapping(path = "/{id}/update", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProjectDto updateProject(@PathVariable("id") String id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProjectById(id, projectDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteProjectById(@PathVariable("id") String id) {
        projectService.deleteProjectById(id);
    }

    @DeleteMapping(path = "/remove", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteProjectByIdList(@RequestBody List<String> idList) {
        projectService.deleteProjects(idList);
    }

}