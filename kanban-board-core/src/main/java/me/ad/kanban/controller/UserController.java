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
    private static final String DEFAULT_PROFILE_PIC = "data:image/png;base64,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";
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
        if(userDto != null && userDto.getProfilePic() == null)
            userDto.setProfilePic(DEFAULT_PROFILE_PIC);
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

    @GetMapping(path = "/{id}/projects", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<ProjectDto> getRelatedProjectsByUserId(@PathVariable("id") String id) {
        return userService.findRelatedProjectsByUserId(id);
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
        return userService.updateUserById(id, userDto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUserById(@PathVariable("id") String id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void deleteUserById(@RequestBody List<String> idList) {
        userService.deleteUsers(idList);
    }

    @PostMapping(path = "/search", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserDto> searchByUserIdOrName(@RequestBody UserGetAllQueryDto queryDto) {
        return userService.searchByUserIdOrName(queryDto);
    }

}
