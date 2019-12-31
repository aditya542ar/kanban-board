package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.query.StageGetAllQueryDto;
import me.ad.kanban.entity.Stage;
import me.ad.kanban.filter.FilterBuilder;
import me.ad.kanban.repository.StageRepository;
import me.ad.kanban.service.MapperService;
import me.ad.kanban.service.StageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static me.ad.kanban.repository.StageRepository.*;

@Service
public class StageServiceImpl implements StageService {

    private static final Logger log = LoggerFactory.getLogger(StageServiceImpl.class);
    private final CustomMessageProperties message;
    private final MapperService mapperService;
    private final ObjectFactory<FilterBuilder<Stage>> filterBuilderObjectFactory;
    private final StageRepository stageRepository;

    @Autowired
    public StageServiceImpl(CustomMessageProperties message, MapperService mapperService,
                            ObjectFactory<FilterBuilder<Stage>> filterBuilderObjectFactory,
                            StageRepository stageRepository) {
        this.message = message;
        this.mapperService = mapperService;
        this.filterBuilderObjectFactory = filterBuilderObjectFactory;
        this.stageRepository = stageRepository;
    }

    @Override
    public StageDto findStageById(String id) {
        return mapperService.mapStageToDto(stageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        MessageFormat.format(message.getStageNotExist(), id))));
    }

    @Override
    public List<StageDto> findAllStageWithFilterQuery(Optional<StageGetAllQueryDto> queryDtoOpt) {
        List<Stage> all = new ArrayList<>();
        if(queryDtoOpt.isPresent()) {
            StageGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Stage> filterBuilder = applyFilterQuery(qd);
            all = stageRepository.findAll(filterBuilder.getSpecification(), filterBuilder.getSort());
        } else {
            all = stageRepository.findAll();
        }
        return all.stream().map(mapperService::mapStageToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long findTotalCount(Optional<StageGetAllQueryDto> queryDtoOpt) {
        if(queryDtoOpt.isPresent()) {
            StageGetAllQueryDto qd = queryDtoOpt.get();
            FilterBuilder<Stage> filterBuilder = applyFilterQuery(qd);
            return stageRepository.count(filterBuilder.getSpecification());
        } else {
            return stageRepository.count();
        }
    }

    @Override
    public StageDto saveOrUpdateStageByDto(StageDto stageDto) {
        return mapperService.mapStageToDto(
                stageRepository.save(
                        mapperService.mapDtoToStage(stageDto)));
    }

    @Override
    public Set<TaskDto> findTasksByStageId(String stageId) {
        Optional<Stage> stageOpt = stageRepository.findById(stageId);
        if(stageOpt.isPresent()) {
            return stageOpt.get().getTasks()
                    .stream().map(mapperService::mapTaskToDto)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format(message.getStageNotExist(), stageId));
        }
    }

    @Override
    public StageDto updateStageById(String id, StageDto stageDto) {
        if(id == null || stageDto == null || !id.equals(stageDto.getId())) {
            throw new IllegalArgumentException(message.getInvalidIdentifier());
        }
        if(stageRepository.existsById(id)) {
            return saveOrUpdateStageByDto(stageDto);
        } else {
            throw new NoSuchElementException(MessageFormat.format(message.getStageNotExist(), id));
        }
    }

    @Override
    public void deleteStageById(String id) {
        stageRepository.deleteById(id);
    }

    @Override
    public void deleteStages(List<String> idList) {
        stageRepository.deleteAll(stageRepository.findAllById(idList));
    }

    @Override
    public void deleteAllStages() {
        stageRepository.deleteAll();
    }

    private FilterBuilder<Stage> applyFilterQuery(StageGetAllQueryDto qd) {
        FilterBuilder<Stage> filterBuilder = filterBuilderObjectFactory.getObject();
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
