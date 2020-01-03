package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.query.TaskGetAllQueryDto;
import me.ad.kanban.entity.Stage;
import me.ad.kanban.entity.Task;
import me.ad.kanban.entity.TaskStageChange;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.TaskRepository;
import me.ad.kanban.repository.TaskStageChangeRepository;
import me.ad.kanban.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final TaskStageChangeRepository taskStageChangeRepository;

//    private final Comparator<Stage> stageComparator = (s1, s2) -> s1.get

    @Autowired
    public TaskServiceImpl(CustomMessageProperties message, MapperService mapperService,
                           ObjectFactory<FilterBuilder<Task>> filterBuilderObjectFactory, TaskRepository taskRepository, TaskStageChangeRepository taskStageChangeRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.taskRepository = taskRepository;
        this.taskStageChangeRepository = taskStageChangeRepository;
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
    @Transactional
    public TaskDto saveOrUpdateTaskByDto(TaskDto taskDto) {
        log.info("inside save or update task by dto");
        Task task = mapperService.mapDtoToTask(taskDto);
        log.info("saveTask" + task + "id:" + task.getId());
        Optional<Task> taskOpt = task.getId() == null ? Optional.ofNullable(null) : taskRepository.findById(task.getId());
        Task finalTask = null;
        if(taskOpt.isPresent()) {
            log.info("Task with id: " + task.getId() + " already exists");
            Task dbTask = taskOpt.get();
            // set all changed properties to dbTask
            dbTask.setTeam(task.getTeam());
            dbTask.setPriority(task.getPriority());
            dbTask.setDescription(task.getDescription());
            dbTask.setName(task.getName());
            dbTask.setOwner(task.getOwner());

            if(task.getCategory().equals(dbTask.getCategory())) {
                // don't add new task-stage-change
                log.info("New Task category is same with old Task");
            } else {
                log.info("New Task category is different than old Task");
                // mark old stage as ended
                TaskStageChange latestTaskStageChange = dbTask.getTaskStageChangeSet().stream()
                        .filter((tsc) -> tsc.getTask().getId().equals(dbTask.getId()))
                        .filter((tsc) -> tsc.getStage().getId().equals(dbTask.getCategory().getId()))
                        .max(Comparator.comparing(TaskStageChange::getVersion)).get();
                latestTaskStageChange.setEndTime(LocalDateTime.now());

                // set new category
                dbTask.setCategory(task.getCategory());

                // add new stage change
                TaskStageChange taskStageChange = new TaskStageChange();
                taskStageChange.setTask(dbTask);
                taskStageChange.setStartTime(LocalDateTime.now());
                taskStageChange.setStage(dbTask.getCategory());
                taskStageChange.setVersion(latestTaskStageChange.getVersion() + 1);
                dbTask.getTaskStageChangeSet().add(taskStageChange);
            }
            finalTask = taskRepository.save(dbTask);
        } else {
            log.info("Task with id: " + task.getId() + " is new");
            // add new stage change
            TaskStageChange taskStageChange = new TaskStageChange();
            taskStageChange.setTask(task);
            taskStageChange.setStartTime(LocalDateTime.now());
            taskStageChange.setStage(task.getCategory());
            taskStageChange.setVersion(1);
            task.getTaskStageChangeSet().add(taskStageChange);
            finalTask = taskRepository.save(task);
        }
        return mapperService.mapTaskToDto(finalTask);
    }

    @Override
    public TaskDto updateTaskById(String id, TaskDto taskDto) {
        log.info("updateTaskById:" + id + ", taskDto:" + taskDto.getId());
        if(id == null || taskDto == null || !id.equals(taskDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        log.info("updateTaskById:" + id + ",taskDto" + taskDto.getId());
        log.info("taskRepository.existsById(taskDto.getId())" + taskRepository.existsById(taskDto.getId()));
        if(taskRepository.existsById(taskDto.getId())) {
            log.info("inside existById true");
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
                .addEqualFilter("ownerId", Optional.ofNullable(qd.getOwnerId()), ownerIdSpec(qd.getOwnerId()))
                .addEqualFilter("projectId", Optional.ofNullable(qd.getProjectId()), projectIdSpec(qd.getProjectId()))
                .addInFilter("teamIdIn", Optional.ofNullable(qd.getTeamIdIn()), teamIdInSpec(qd.getTeamIdIn()))
                .addInFilter("categoryIdIn", Optional.ofNullable(qd.getCategoryIdIn()), categoryIdInSpec(qd.getCategoryIdIn()))
                .addSortByAndSortOrder(Optional.ofNullable(qd.getSortBy()), Optional.ofNullable(qd.getSortOrder()),
                        SORT_BY_SET, SORT_ORDER_SET);
        return filterBuilder;
    }
}
