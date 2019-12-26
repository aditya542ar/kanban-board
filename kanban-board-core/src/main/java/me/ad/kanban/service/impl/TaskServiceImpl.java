package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.entity.Task;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.TaskRepository;
import me.ad.kanban.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(CustomMessageProperties message, MapperService mapperService,
                           TaskRepository taskRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.taskRepository = taskRepository;
    }
}
