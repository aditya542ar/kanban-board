package me.ad.kanban.controller;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.UserGetAllQueryDto;
import me.ad.kanban.service.UserService;
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
@RequestMapping("/user")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final CustomMessageProperties message;
    private final UserService userService;

    @Autowired
    public UserController(CustomMessageProperties message, UserService userService) {
        this.message = message;
        this.userService = userService;
    }

    @GetMapping(path = {"", "/", "/all"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserDto> getAllUser(Optional<UserGetAllQueryDto> queryDto) {
        return userService.findAllUsersWithFilterQuery(queryDto);
    }

    @PostMapping(path = {"", "/", "/all"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserDto> getAllUserPost(@RequestBody UserGetAllQueryDto queryDto) {
        return userService.findAllUsersWithFilterQuery(Optional.of(queryDto));
    }

    @GetMapping(path = {"/count"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getUserCount(Optional<UserGetAllQueryDto> queryDto) {
        return userService.findTotalCount(queryDto);
    }

    @PostMapping(path = {"/count"}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Long getUserCountPost(@RequestBody UserGetAllQueryDto queryDto) {
        return  userService.findTotalCount(Optional.of(queryDto));
    }

    @PostMapping(path = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.saveOrUpdateUserByDto(userDto);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserDto getUserById(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @GetMapping(path = "/{id}/teams", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<TeamDto> getTeamsByUserId(@PathVariable("id") String id) {
        return userService.findTeamsByUserId(id);
    }

    @GetMapping(path = "/{id}/owned-projects", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<ProjectDto> getOwnedProjectsByUserId(@PathVariable("id") String id) {
        return userService.findOwnedProjectsByUserId(id);
    }

    @GetMapping(path = "/{id}/tasks", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<TaskDto> getTasksByUserId(@PathVariable("id") String id) {
        return userService.findTasksByUserId(id);
    }

    @PutMapping(path = "/{id}/update", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserDto updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        return userService.updateProjectById(id, userDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUserById(@PathVariable("id") String id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteUserById(@RequestBody List<String> idList) {
        userService.deleteUsers(idList);
    }

}
