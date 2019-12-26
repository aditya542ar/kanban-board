package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.query.TaskGetAllQueryDto;
import me.ad.kanban.entity.Task;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.TaskRepository;
import me.ad.kanban.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.ad.kanban.repository.TaskRepository.*;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ObjectFactory<FilterBuilder<Task>> filterBuilderObjectFactory;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(CustomMessageProperties message, MapperService mapperService,
                           ObjectFactory<FilterBuilder<Task>> filterBuilderObjectFactory, TaskRepository taskRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto findTaskById(String id) {
        return mapperService.mapTaskToDto(taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        MessageFormat.format(message.getTaskNotExist(), id))));
    }

    @Override
    public List<TaskDto> findAllTaskWithFilterQuery(Optional<TaskGetAllQueryDto> queryDtoOpt) {
        List<Task> all = null;
        if(queryDtoOpt.isPresent()) {
            TaskGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Task> filterBuilder = applyFilterQuery(qd);
            all = taskRepository.findAll(filterBuilder.getSpecification(), filterBuilder.getSort());
        } else {
            all = taskRepository.findAll();
        }
        return all.stream().map(mapperService::mapTaskToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long findTotalCount(Optional<TaskGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            TaskGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Task> filterBuilder = applyFilterQuery(qd);
            return taskRepository.count(filterBuilder.getSpecification());
        } else {
            return taskRepository.count();
        }
    }

    @Override
    public TaskDto saveOrUpdateTaskByDto(TaskDto taskDto) {
        return mapperService.mapTaskToDto(
                taskRepository.save(
                        mapperService.mapDtoToTask(taskDto)));
    }

    @Override
    public TaskDto updateTaskById(String id, TaskDto taskDto) {
        if(id == null || taskDto == null || !id.equals(taskDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        if(taskRepository.existsById(id)) {
            return saveOrUpdateTaskByDto(taskDto);
        } else {
            throw new NoSuchElementException(MessageFormat.format(message.getTaskNotExist(), id));
        }
    }

    @Override
    public void deleteTaskById(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteTasks(List<String> idList) {
        taskRepository.deleteAll(taskRepository.findAllById(idList));
    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    private FilterBuilder<Task> applyFilterQuery(TaskGetAllQueryDto qd) {
        FilterBuilder<Task> filterBuilder = filterBuilderObjectFactory.getObject();
        filterBuilder
                .addEqualFilter("id", Optional.ofNullable(qd.getId()), idSpec(qd.getId()))
                .addInFilter("idIn", Optional.ofNullable(qd.getIdIn()), idInSpec(qd.getIdIn()))
                .addEqualFilter("name", Optional.ofNullable(qd.getName()), nameSpec(qd.getName()))
                .addInFilter("nameIn", Optional.ofNullable(qd.getNameIn()), nameInSpec(qd.getNameIn()))
                .addLikeFilter("nameLike", Optional.ofNullable(qd.getNameLike()), nameLikeSpec(qd.getNameLike()))
                .addEqualFilter("categoryId", Optional.ofNullable(qd.getCategoryId()), categoryIdSpec(qd.getCategoryId()))
                .addEqualFilter("teamId", Optional.ofNullable(qd.getTeamId()), teamIdSpec(qd.getTeamId()))
                .addEqualFilter("userId", Optional.ofNullable(qd.getUserId()), userIdSpec(qd.getUserId()))
                .addSortByAndSortOrder(Optional.ofNullable(qd.getSortBy()), Optional.ofNullable(qd.getSortOrder()),
                        SORT_BY_SET, SORT_ORDER_SET);
        return filterBuilder;
    }
}
