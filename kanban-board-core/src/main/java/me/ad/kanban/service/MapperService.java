package me.ad.kanban.service;

import me.ad.kanban.dto.*;
import me.ad.kanban.entity.*;

public interface MapperService {
    ProjectDto mapProjectToDto(Project project);
    Project mapDtoToProject(ProjectDto dto);
    StageDto mapStageToDto(Stage stage);
    Stage mapDtoToStage(StageDto dto);
    TaskDto mapTaskToDto(Task task);
    Task mapDtoToTask(TaskDto dto);
    TeamDto mapTeamToDto(Team team);
    Team mapDtoToTeam(TeamDto dto);
    UserDto mapUserToDto(User user);
    User mapDtoToUser(UserDto dto);
}
