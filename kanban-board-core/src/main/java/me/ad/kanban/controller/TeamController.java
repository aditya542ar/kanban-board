package me.ad.kanban.controller;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.TeamGetAllQueryDto;
import me.ad.kanban.service.TeamService;
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
@RequestMapping("/team")
public class TeamController {
    public static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final CustomMessageProperties message;
    private final TeamService teamService;

    @Autowired
    public TeamController(CustomMessageProperties message, TeamService teamService) {
        this.message = message;
        this.teamService = teamService;
    }

    @GetMapping(path = {"", "/", "/all"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TeamDto> getAllTeam(Optional<TeamGetAllQueryDto> queryDto) {
        return teamService.findAllTeamWithFilterQuery(queryDto);
    }

    @PostMapping(path = {"", "/", "/all"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TeamDto> getAllTeamPost(@RequestBody TeamGetAllQueryDto queryDto) {
        return teamService.findAllTeamWithFilterQuery(Optional.of(queryDto));
    }

    @GetMapping(path = {"/count"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getTeamCount(Optional<TeamGetAllQueryDto> queryDto) {
        return teamService.findTotalCount(queryDto);
    }

    @PostMapping(path = {"/count"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getTeamCountPost(@RequestBody TeamGetAllQueryDto queryDto) {
        return teamService.findTotalCount(Optional.of(queryDto));
    }

    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto createTeam(@RequestBody TeamDto teamDto) {
        return teamService.saveOrUpdateTeamByDto(teamDto);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public TeamDto getTeamById(@PathVariable("id") String id) {
        return teamService.findTeamById(id);
    }

    @GetMapping(path = "/{id}/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<UserDto> getUsersById(@PathVariable("id") String id) {
        return teamService.findUsersByTeamId(id);
    }

    @GetMapping(path = "/{id}/tasks", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<TaskDto> getTasksById(@PathVariable("id") String id) {
        return teamService.findTasksByTeamId(id);
    }

    @PutMapping(path = "/{id}/update", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public TeamDto updateTeam(@PathVariable("id") String id, @RequestBody TeamDto teamDto) {
        return teamService.updateTeamById(id, teamDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTeamById(@PathVariable("id") String id) {
        teamService.deleteTeamById(id);
    }

    @DeleteMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteTeamById(@RequestBody List<String> idList) {
        teamService.deleteTeams(idList);
    }

}
