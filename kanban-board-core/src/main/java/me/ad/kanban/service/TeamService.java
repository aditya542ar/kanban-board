package me.ad.kanban.service;

import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.TeamGetAllQueryDto;
import me.ad.kanban.entity.Team;

import java.util.*;

public interface TeamService {
    Set<String> SORT_BY_SET = new HashSet<String>(Arrays.asList("id", "name"));
    Set<String> SORT_ORDER_SET = new HashSet<String>(Arrays.asList("asc", "desc"));
    TeamDto findTeamById(String id);
    List<TeamDto> findAllTeamWithFilterQuery(Optional<TeamGetAllQueryDto> queryDtoOpt);
    Long findTotalCount(Optional<TeamGetAllQueryDto> queryDtoOpt);
    TeamDto saveOrUpdateTeamByDto(TeamDto teamDto);
    Set<UserDto> findUsersByTeamId(String teamId);
    Set<TaskDto> findTasksByTeamId(String teamId);
    TeamDto updateTeamById(String id, TeamDto teamDto);
    void deleteTeamById(String id);
    void deleteTeams(List<String> idList);
    void deleteAllTeams();
}
