package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.ProjectDto;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.UserGetAllQueryDto;
import me.ad.kanban.entity.User;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.UserRepository;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static me.ad.kanban.repository.UserRepository.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ObjectFactory<FilterBuilder<User>> filterBuilderObjectFactory;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(CustomMessageProperties message, MapperService mapperService, FilterBuilder<User> filterBuilder, ObjectFactory<FilterBuilder<User>> filterBuilderObjectFactory, UserRepository userRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findUserById(String id) {
        return mapperService.mapUserToDto(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        MessageFormat.format(message.getUserNotExist(), id)
                )));
    }

    @Override
    public List<UserDto> findAllUsersWithFilterQuery(Optional<UserGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            UserGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<User> filterBuilder = applyFilterQuery(qd);
            // at this point, all filter criteria are valid, start filtering
            return userRepository.findAll(filterBuilder.getSpecification(), filterBuilder.getSort())
                    .stream().map(mapperService::mapUserToDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll()
                    .stream().map(mapperService::mapUserToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Long findTotalCount(Optional<UserGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            UserGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<User> filterBuilder = applyFilterQuery(qd);
            return userRepository.count(filterBuilder.getSpecification());
        } else {
            return userRepository.count();
        }
    }

    @Override
    public UserDto saveOrUpdateUserByDto(UserDto userDto) {
        return mapperService.mapUserToDto(
                userRepository.save(
                        mapperService.mapDtoToUser(userDto)));
    }

    @Override
    public Set<TeamDto> findTeamsByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            return userOpt.get().getTeams()
                    .stream().map(mapperService::mapTeamToDto)
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getUserNotExist(), userId));
    }

    @Override
    public Set<ProjectDto> findOwnedProjectsByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            return userOpt.get().getOwnedProjects()
                    .stream().map(mapperService::mapProjectToDto)
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getUserNotExist(), userId));
    }

    @Override
    public Set<ProjectDto> findRelatedProjectsByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            Set<ProjectDto> projects = new HashSet<ProjectDto>();
            userOpt.get().getTeams().forEach(
                    (team) -> projects.add(mapperService.mapProjectToDto(team.getProject())));
            userOpt.get().getOwnedProjects().forEach(
                    (project) -> projects.add(mapperService.mapProjectToDto(project))
            );
            return projects;
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getUserNotExist(), userId));
    }

    @Override
    public Set<TaskDto> findTasksByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            return userOpt.get().getTasks()
                    .stream().map(mapperService::mapTaskToDto)
                    .collect(Collectors.toSet());
        }
        throw new IllegalArgumentException(
                MessageFormat.format(message.getUserNotExist(), userId));
    }

    @Override
    public UserDto updateUserById(String id, UserDto userDto) {
        if(id == null || userDto == null || !id.equals(userDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        if(userRepository.existsById(id)) {
            return saveOrUpdateUserByDto(userDto);
        } else {
            throw new NoSuchElementException(MessageFormat.format(message.getUserNotExist(), id));
        }
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUsers(List<String> idList) {
        userRepository.deleteAll(userRepository.findAllById(idList));
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public List<UserDto> searchByUserIdOrName(UserGetAllQueryDto queryDto) {
        UserGetAllQueryDto qd = new UserGetAllQueryDto();
        if(queryDto.getUserIdLike() != null || queryDto.getFirstNameLike() != null
            || queryDto.getLastNameLike() != null) {
            qd.setUserIdLike(queryDto.getUserIdLike());
            qd.setFirstNameLike(queryDto.getFirstNameLike());
            qd.setLastNameLike(queryDto.getLastNameLike());
            FilterBuilder<User> filterBuilder = applySearchByUserIdOrNameFilterQuery(qd);
            return userRepository.findAll(filterBuilder.getSpecification())
                    .stream().map(mapperService::mapUserToDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private FilterBuilder<User> applyFilterQuery(UserGetAllQueryDto qd) {
        FilterBuilder<User> filterBuilder = filterBuilderObjectFactory.getObject();
        filterBuilder
                .addEqualFilter("id", Optional.ofNullable(qd.getId()), idSpec(qd.getId()))
                .addInFilter("idIn", Optional.ofNullable(qd.getIdIn()), idInSpec(qd.getIdIn()))
                .addEqualFilter("firstName", Optional.ofNullable(qd.getFirstName()), firstNameSpec(qd.getFirstName()))
                .addLikeFilter("firstNameList", Optional.ofNullable(qd.getFirstNameLike()), firstNameLikeSpec(qd.getFirstNameLike()))
                .addEqualFilter("lastName", Optional.ofNullable(qd.getLastName()), lastNameSpec(qd.getLastName()))
                .addLikeFilter("lastNameLike", Optional.ofNullable(qd.getLastNameLike()), lastNameLikeSpec(qd.getLastNameLike()))
                .addEqualFilter("userId", Optional.ofNullable(qd.getUserId()), userIdSpec(qd.getUserId()))
                .addInFilter("userIdIn", Optional.ofNullable(qd.getUserIdIn()), userIdInSpec(qd.getUserIdIn()))
                .addLikeFilter("userIdLike", Optional.ofNullable(qd.getUserIdLike()), userIdLikeSpec(qd.getUserIdLike()))
                .addSortByAndSortOrder(Optional.ofNullable(qd.getSortBy()), Optional.ofNullable(qd.getSortOrder()),
                        SORT_BY_SET, SORT_ORDER_SET);
        return filterBuilder;
    }

    private FilterBuilder<User> applySearchByUserIdOrNameFilterQuery(UserGetAllQueryDto qd) {
        FilterBuilder<User> filterBuilder = filterBuilderObjectFactory.getObject();
        filterBuilder
                .orStart()
                .addLikeFilter("userIdLike", Optional.ofNullable(qd.getUserIdLike()), userIdLikeSpec(qd.getUserIdLike()))
                .addLikeFilter("firstNameLike", Optional.ofNullable(qd.getFirstNameLike()), firstNameLikeSpec(qd.getFirstNameLike()))
                .addLikeFilter("lastNameLike", Optional.ofNullable(qd.getLastNameLike()), lastNameLikeSpec(qd.getLastNameLike()))
                .orEnd();
        return filterBuilder;
    }
}
