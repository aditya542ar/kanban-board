package me.ad.kanban.service;

import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.ProjectGetAllQueryDto;

import java.util.*;

public interface ProjectService {
    Set<String> SORT_BY_SET = new HashSet<String>(Arrays.asList("id", "name", "startDate", "endDate"));
    Set<String> SORT_ORDER_SET = new HashSet<String>(Arrays.asList("asc", "desc"));
    ProjectDto findProjectById(String id);
    List<ProjectDto> findAllProjectsWithFilterQuery(Optional<ProjectGetAllQueryDto> queryDtoOpt);
    Long findTotalCount(Optional<ProjectGetAllQueryDto> queryDtoOpt);
    ProjectDto saveOrUpdateProjectByDto(ProjectDto projectDto);
    Set<TeamDto> findTeamsByProjectId(String projectId);
    Set<UserDto> findUsersByProjectId(String projectId);
    Set<StageDto> findStagesByProjectId(String projectId);
    ProjectDto updateProjectById(String id, ProjectDto projectDto);
    void deleteProjectById(String id);
    void deleteProjects(List<String> idList);
    void deleteAllProjects();
}
