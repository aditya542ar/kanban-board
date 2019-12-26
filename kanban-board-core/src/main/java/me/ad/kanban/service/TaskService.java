package me.ad.kanban.service;

import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.query.TaskGetAllQueryDto;
import me.ad.kanban.entity.Task;

import java.util.*;

public interface TaskService {
    Set<String> SORT_BY_SET = new HashSet<String>(Arrays.asList("id", "name", "priority", "category", "team", "user"));
    Set<String> SORT_ORDER_SET = new HashSet<String>(Arrays.asList("asc", "desc"));
    TaskDto findTaskById(String id);
    List<TaskDto> findAllTaskWithFilterQuery(Optional<TaskGetAllQueryDto> queryDtoOpt);
    Long findTotalCount(Optional<TaskGetAllQueryDto> queryDtoOpt);
    TaskDto saveOrUpdateTaskByDto(TaskDto taskDto);
    TaskDto updateTaskById(String id, TaskDto taskDto);
    void deleteTaskById(String id);
    void deleteTasks(List<String> idList);
    void deleteAllTasks();
}
