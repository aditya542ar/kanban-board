package me.ad.kanban.service;

import me.ad.kanban.dto.StageDto;
import me.ad.kanban.dto.TaskDto;
import me.ad.kanban.dto.TeamDto;
import me.ad.kanban.dto.UserDto;
import me.ad.kanban.dto.query.StageGetAllQueryDto;
import me.ad.kanban.dto.query.TeamGetAllQueryDto;
import me.ad.kanban.entity.Stage;

import java.util.*;

public interface StageService {
    Set<String> SORT_BY_SET = new HashSet<String>(Arrays.asList("id", "name"));
    Set<String> SORT_ORDER_SET = new HashSet<String>(Arrays.asList("asc", "desc"));
    StageDto findStageById(String id);
    List<StageDto> findAllStageWithFilterQuery(Optional<StageGetAllQueryDto> queryDtoOpt);
    Long findTotalCount(Optional<StageGetAllQueryDto> queryDtoOpt);
    StageDto saveOrUpdateStageByDto(StageDto stageDto);
    Set<TaskDto> findTasksByStageId(String stageId);
    StageDto updateStageById(String id, StageDto stageDto);
    void deleteStageById(String id);
    void deleteStages(List<String> idList);
    void deleteAllStages();
}
