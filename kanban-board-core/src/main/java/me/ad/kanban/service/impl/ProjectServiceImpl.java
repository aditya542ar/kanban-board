package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.ProjectGetAllQueryDto;
import me.ad.kanban.entity.Project;
import me.ad.kanban.repository.ProjectRepository;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.ProjectService;
import me.ad.kanban.filter.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static me.ad.kanban.repository.ProjectRepository.*;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ObjectFactory<FilterBuilder<Project>> filterBuilderObjectFactory;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(CustomMessageProperties message, MapperService mapperService,
                              FilterBuilder<Project> filterBuilder, ObjectFactory<FilterBuilder<Project>> filterBuilderObjectFactory, ProjectRepository projectRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDto findProjectById(String id) {
        return mapperService.mapProjectToDto(projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        MessageFormat.format(message.getProjectNotExist(), id)
                )));
    }

    @Override
    public List<ProjectDto> findAllProjectsWithFilterQuery(Optional<ProjectGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            ProjectGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Project> filterBuilder = applyFilterQuery(qd);
            // at this point, all filter criteria are valid, start filtering
            return projectRepository.findAll(filterBuilder.getSpecification(), filterBuilder.getSort())
                    .stream().map(mapperService::mapProjectToDto)
                    .collect(Collectors.toList());
        } else {
            return projectRepository.findAll()
                    .stream().map(mapperService::mapProjectToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Long findTotalCount(Optional<ProjectGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            ProjectGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Project> filterBuilder = applyFilterQuery(qd);
            // at this point, all filter criteria are valid, start filtering
            return projectRepository.count(filterBuilder.getSpecification());
        } else {
            return projectRepository.count();
        }
    }

    @Override
    public ProjectDto saveOrUpdateProjectByDto(ProjectDto projectDto) {
        return mapperService.mapProjectToDto(
                projectRepository.save(
                        mapperService.mapDtoToProject(projectDto)));
    }

    @Override
    public Set<TeamDto> findTeamsByProjectId(String projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if(projectOpt.isPresent()) {
            return projectOpt.get().getTeams()
                    .stream().map(mapperService::mapTeamToDto)
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getProjectNotExist(), projectId));
    }

    @Override
    public Set<UserDto> findUsersByProjectId(String projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if(projectOpt.isPresent()) {
            Set<UserDto> users = new HashSet<>();
            projectOpt.get().getTeams().forEach((team) -> {
                team.getUsers().forEach((user) -> {
                    users.add(mapperService.mapUserToDto(user));
                });
            });
            return users;
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getProjectNotExist(), projectId));
    }

    @Override
    public Set<StageDto> findStagesByProjectId(String projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if(projectOpt.isPresent()) {
            return projectOpt.get().getStages()
                    .stream().map(mapperService::mapStageToDto)
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getProjectNotExist(), projectId));
    }

    @Override
    public ProjectDto updateProjectById(String id, ProjectDto projectDto) {
        if(id == null || projectDto == null || !id.equals(projectDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        if(projectRepository.existsById(id)) {
            return saveOrUpdateProjectByDto(projectDto);
        } else {
            throw new NoSuchElementException(MessageFormat.format(message.getProjectNotExist(), id));
        }
    }

    @Override
    public void deleteProjectById(String id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void deleteProjects(List<String> idList) {
        projectRepository.deleteAll(projectRepository.findAllById(idList));
    }

    @Override
    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }

    private FilterBuilder<Project> applyFilterQuery(ProjectGetAllQueryDto qd) {
        FilterBuilder<Project> filterBuilder = filterBuilderObjectFactory.getObject();
        filterBuilder
                .addLikeFilter("nameLike", Optional.ofNullable(qd.getNameLike()), nameLikeSpec(qd.getNameLike()))
                .addLikeFilter("descriptionLike", Optional.ofNullable(qd.getDescriptionLike()), descriptionLikeSpec(qd.getDescriptionLike()))
                .addEqualFilter("ownerId", Optional.ofNullable(qd.getOwnerId()), ownerIdSpec(qd.getOwnerId()))
                .addDateFilter("startDate", Optional.ofNullable(qd.getStartDate()), startDateSpec(qd.getStartDate()))
                .addDateFilter("endDate", Optional.ofNullable(qd.getEndDate()), endDateSpec(qd.getEndDate()))
                .addDateFilter("startDateBefore", Optional.ofNullable(qd.getStartDateBefore()), startDateBeforeSpec(qd.getStartDateBefore()))
                .addDateFilter("startDateAfter", Optional.ofNullable(qd.getStartDateAfter()), startDateAfterSpec(qd.getStartDateAfter()))
                .addDateFilter("endDateBefore", Optional.ofNullable(qd.getEndDateBefore()), endDateBeforeSpec(qd.getEndDateBefore()))
                .addDateFilter("endDateAfter", Optional.ofNullable(qd.getEndDateAfter()), endDateAfterSpec(qd.getEndDateBefore()))
                .addSortByAndSortOrder(Optional.ofNullable(qd.getSortBy()), Optional.ofNullable(qd.getSortOrder()),
                        SORT_BY_SET, SORT_ORDER_SET);
        return filterBuilder;
    }
}
