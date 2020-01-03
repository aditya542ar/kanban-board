package me.ad.kanban.service.impl;

import me.ad.kanban.config.CustomMessageProperties;
import me.ad.kanban.dto.*;
import me.ad.kanban.entity.*;
import me.ad.kanban.repository.*;
import me.ad.kanban.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntityDtoMapperService implements MapperService {

    private final CustomMessageProperties message;
    private final ProjectRepository projectRepository;
    private final StageRepository stageRepository;
    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public EntityDtoMapperService(CustomMessageProperties message, ProjectRepository projectRepository,
                                  StageRepository stageRepository, TaskRepository taskRepository,
                                  TeamRepository teamRepository, UserRepository userRepository) {
        this.message = message;
        this.projectRepository = projectRepository;
        this.stageRepository = stageRepository;
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectDto mapProjectToDto(Project project) {
        if(project == null) return null;
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setOwnerId(project.getOwner().getId());
        return dto;
    }

    @Override
    public Project mapDtoToProject(ProjectDto dto) {
        if(dto == null) return null;
        Project p = new Project();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        Optional<User> optionalUser = userRepository.findById(dto.getOwnerId());
        optionalUser.ifPresent(p::setOwner);
        optionalUser.orElseThrow(() -> new IllegalArgumentException(
                MessageFormat.format(message.getUserNotExist(), dto.getOwnerId())));
        return p;
    }

    @Override
    public StageDto mapStageToDto(Stage stage) {
        if(stage == null) return null;
        StageDto dto = new StageDto();
        dto.setId(stage.getId());
        dto.setName(stage.getName());
        dto.setProjectId(stage.getProject().getId());
        return dto;
    }

    @Override
    public Stage mapDtoToStage(StageDto dto) {
        if(dto == null) return null;
        Stage stage = new Stage();
        stage.setId(dto.getId());
        stage.setName(dto.getName());
        Optional<Project> optionalProject = projectRepository.findById(dto.getProjectId());
        optionalProject.ifPresent(stage::setProject);
        optionalProject.orElseThrow(() -> new IllegalArgumentException(
                String.format(message.getProjectNotExist(), dto.getProjectId())));
        return stage;
    }

    @Override
    public TaskDto mapTaskToDto(Task task) {
        if(task == null) return null;
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setCategory(mapStageToDto(task.getCategory()));
        dto.setTeam(mapTeamToDto(task.getTeam()));
        dto.setOwner(mapUserToDto(task.getOwner()));
        dto.setTaskStageChanges(task.getTaskStageChangeSet()
                .stream()
                .map(this::mapTaskStageChangeToDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public Task mapDtoToTask(TaskDto dto) {
        if(dto == null) return null;
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setCategory(mapDtoToStage(dto.getCategory()));
        task.setTeam(mapDtoToTeam(dto.getTeam()));
        task.setOwner(mapDtoToUser(dto.getOwner()));
        task.setTaskStageChangeSet(dto.getTaskStageChanges()
                .stream()
                .map(this::mapDtoToTaskStageChange)
                .collect(Collectors.toSet()));
        return task;
    }

    @Override
    public TeamDto mapTeamToDto(Team team) {
        if(team == null) return null;
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setProjectId(team.getProject().getId());
        return dto;
    }

    @Override
    public Team mapDtoToTeam(TeamDto dto) {
        if(dto == null) return null;
        Team team = new Team();
        team.setId(dto.getId());
        team.setName(dto.getName());
        Optional<Project> optionalProject = projectRepository.findById(dto.getProjectId());
        optionalProject.ifPresent(team::setProject);
        optionalProject.orElseThrow(() -> new IllegalArgumentException(
                String.format(message.getProjectNotExist(), dto.getProjectId())));
        return team;
    }

    @Override
    public UserDto mapUserToDto(User user) {
        if(user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserId(user.getUserId());
        Optional.ofNullable(user.getProfilePic()).ifPresent(
                (pic) -> dto.setProfilePic(new String(pic))
        );
        return dto;
    }

    @Override
    public User mapDtoToUser(UserDto dto) {
        if(dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUserId(dto.getUserId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        Optional.ofNullable(dto.getProfilePic()).ifPresent(
                (pic) -> user.setProfilePic(pic.getBytes())
        );
        return user;
    }

    @Override
    public TaskStageChangeDto mapTaskStageChangeToDto(TaskStageChange tsc) {
        if(tsc == null) return null;
        TaskStageChangeDto dto = new TaskStageChangeDto();
        dto.setId(tsc.getId());
        dto.setStage(mapStageToDto(tsc.getStage()));
        dto.setStartTime(tsc.getStartTime());
        dto.setEndTime(tsc.getEndTime());
        dto.setVersion(tsc.getVersion());
        return dto;
    }

    @Override
    public TaskStageChange mapDtoToTaskStageChange(TaskStageChangeDto dto) {
        if(dto == null) return null;
        TaskStageChange tsc = new TaskStageChange();
        tsc.setId(dto.getId());
        tsc.setStage(mapDtoToStage(dto.getStage()));
        tsc.setStartTime(dto.getStartTime());
        tsc.setEndTime(dto.getEndTime());
        tsc.setVersion(dto.getVersion());
        return tsc;
    }
}
