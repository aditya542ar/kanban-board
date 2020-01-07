package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.TeamGetAllQueryDto;
import me.ad.kanban.entity.Team;
import me.ad.kanban.entity.User;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.ProjectRepository;
import me.ad.kanban.repository.TeamRepository;
import me.ad.kanban.repository.UserRepository;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static me.ad.kanban.repository.TeamRepository.*;

@Service
public class TeamServiceImpl implements TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ObjectFactory<FilterBuilder<Team>> filterBuilderObjectFactory;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamServiceImpl(CustomMessageProperties message, MapperService mapperService, ObjectFactory<FilterBuilder<Team>> filterBuilderObjectFactory, TeamRepository teamRepository,
                           ProjectRepository projectRepository, UserRepository userRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TeamDto findTeamById(String id) {
        return mapperService.mapTeamToDto(teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        MessageFormat.format(message.getTeamNotExist(), id)
                )));
    }

    @Override
    public List<TeamDto> findAllTeamWithFilterQuery(Optional<TeamGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            TeamGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Team> filterBuilder = applyFilterQuery(qd);
            return  teamRepository.findAll(filterBuilder.getSpecification(), filterBuilder.getSort())
                    .stream().map(mapperService::mapTeamToDto)
                    .collect(Collectors.toList());
        } else {
            return teamRepository.findAll()
                    .stream().map(mapperService::mapTeamToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Long findTotalCount(Optional<TeamGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            TeamGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Team> filterBuilder = applyFilterQuery(qd);
            return teamRepository.count(filterBuilder.getSpecification());
        } else {
            return teamRepository.count();
        }
    }

    @Override
    @Transactional
    public TeamDto saveOrUpdateTeamByDto(TeamDto teamDto) {
        if(teamDto != null && teamDto.getId() == null) {
            // new team
            // add project owner as a user to the team
            Team savedTeam = teamRepository.save(
                    mapperService.mapDtoToTeam(teamDto));
            // get project Details
            User owner = savedTeam.getProject().getOwner();
            savedTeam.getUsers().add(owner);
            owner.getTeams().add(savedTeam);
            teamRepository.save(savedTeam);
            return mapperService.mapTeamToDto(savedTeam);
        }
        return mapperService.mapTeamToDto(
                teamRepository.save(
                        mapperService.mapDtoToTeam(teamDto)));
    }

    @Override
    public Set<UserDto> findUsersByTeamId(String teamId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if(teamOpt.isPresent()) {
            return teamOpt.get().getUsers()
                    .stream().map(mapperService::mapUserToDto)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format(message.getTeamNotExist(), teamId));
        }
    }

    @Override
    public void addUserToTeam(String teamId, UserDto userDto) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if(teamOpt.isPresent()) {
            // check user
            Optional<User> userOpt = userRepository.findById(userDto.getId());
            if(userOpt.isPresent()) {
                teamOpt.get().getUsers().add(userOpt.get());
                userOpt.get().getTeams().add(teamOpt.get());
                teamRepository.save(teamOpt.get());
                userRepository.save(userOpt.get());
            } else {
                throw new IllegalArgumentException(
                        MessageFormat.format(message.getUserNotExist(), userDto.getId()));
            }
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format(message.getTeamNotExist(), teamId));
        }
    }

    @Override
    public void removeUserFromTeam(String teamId, String userId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if(teamOpt.isPresent()) {
            // check user
            Optional<User> userOpt = userRepository.findById(userId);
            if(userOpt.isPresent()) {
                teamOpt.get().getUsers().remove(userOpt.get());
                userOpt.get().getTeams().remove(teamOpt.get());
                teamRepository.save(teamOpt.get());
                userRepository.save(userOpt.get());
            } else {
                throw new IllegalArgumentException(
                        MessageFormat.format(message.getUserNotExist(), userId));
            }
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format(message.getTeamNotExist(), teamId));
        }
    }

    @Override
    public Set<TaskDto> findTasksByTeamId(String teamId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if(teamOpt.isPresent()) {
            return teamOpt.get().getTasks()
                    .stream().map(mapperService::mapTaskToDto)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format(message.getTeamNotExist(), teamId));
        }
    }

    @Override
    public TeamDto updateTeamById(String id, TeamDto teamDto) {
        if(id == null || teamDto == null || !id.equals(teamDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        if(teamRepository.existsById(id)) {
            return saveOrUpdateTeamByDto(teamDto);
        } else {
            throw new NoSuchElementException(MessageFormat.format(message.getTeamNotExist(), id));
        }
    }

    @Override
    public void deleteTeamById(String id) {
        teamRepository.deleteById(id);
    }

    @Override
    public void deleteTeams(List<String> idList) {
        teamRepository.deleteAll(teamRepository.findAllById(idList));
    }

    @Override
    public void deleteAllTeams() {
        teamRepository.deleteAll();
    }

    private FilterBuilder<Team> applyFilterQuery(TeamGetAllQueryDto qd) {
        FilterBuilder<Team> filterBuilder = filterBuilderObjectFactory.getObject();
        filterBuilder
                .addEqualFilter("id", Optional.ofNullable(qd.getId()), idSpec(qd.getId()))
                .addInFilter("idIn", Optional.ofNullable(qd.getIdIn()), idInSpec(qd.getIdIn()))
                .addEqualFilter("name", Optional.ofNullable(qd.getName()), nameSpec(qd.getName()))
                .addInFilter("nameIn", Optional.ofNullable(qd.getNameIn()), nameInSpec(qd.getNameIn()))
                .addLikeFilter("nameLike", Optional.ofNullable(qd.getNameLike()), nameLikeSpec(qd.getNameLike()))
                .addEqualFilter("projectId", Optional.ofNullable(qd.getProjectId()), projectIdSpec(qd.getProjectId()))
                .addInFilter("projectIdIn", Optional.ofNullable(qd.getProjectIdIn()), projectIdInSpec(qd.getProjectIdIn()))
                .addSortByAndSortOrder(Optional.ofNullable(qd.getSortBy()), Optional.ofNullable(qd.getSortOrder()),
                        SORT_BY_SET, SORT_ORDER_SET);
        return filterBuilder;
    }
}
