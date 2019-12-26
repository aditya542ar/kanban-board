package me.ad.kanban.service;

import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.UserGetAllQueryDto;

import java.util.*;

public interface UserService {
    Set<String> SORT_BY_SET = new HashSet<String>(Arrays.asList("id", "firstName", "lastName", "userId"));
    Set<String> SORT_ORDER_SET = new HashSet<String>(Arrays.asList("asc", "desc"));
    UserDto findUserById(String id);
    List<UserDto> findAllUsersWithFilterQuery(Optional<UserGetAllQueryDto> queryDtoOpt);
    Long findTotalCount(Optional<UserGetAllQueryDto> queryDtoOpt);
    UserDto saveOrUpdateUserByDto(UserDto userDto);
    Set<TeamDto> findTeamsByUserId(String userId);
    Set<ProjectDto> findOwnedProjectsByUserId(String userId);
    Set<TaskDto> findTasksByUserId(String userId);
    UserDto updateProjectById(String id, UserDto userDto);
    void deleteUserById(String id);
    void deleteUsers(List<String> idList);
    void deleteAllUsers();
}
