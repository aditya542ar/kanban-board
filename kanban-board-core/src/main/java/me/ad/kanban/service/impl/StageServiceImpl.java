package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.entity.Project;
import me.ad.kanban.entity.Stage;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.StageRepository;
import me.ad.kanban.service.ProjectService;
import me.ad.kanban.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StageServiceImpl implements StageService {

    private final CustomMessageProperties message;
    private final StageRepository stageRepository;

    @Autowired
    public StageServiceImpl(CustomMessageProperties message, StageRepository stageRepository) {
        this.message = message;
        this.stageRepository = stageRepository;
    }

    //@Override
    public Long findTotalCount() {
        return stageRepository.count();
    }

    @Override
    public List<Stage> findAll() {
        return stageRepository.findAll();
    }

    @Override
    public Optional<Stage> findById(String id) {
        return stageRepository.findById(id);
    }

    @Override
    public Stage save(Stage entity) {
        return stageRepository.save(entity);
    }

    @Override
    public void delete(Stage entity) {
        stageRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        stageRepository.deleteById(id);
    }

    @Override
    public FilterBuilder<Stage> filterBuilder() {
        return new FilterBuilder<>(message);
    }
}
