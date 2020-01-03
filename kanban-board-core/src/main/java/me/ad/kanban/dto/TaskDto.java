package me.ad.kanban.dto;

import me.ad.kanban.entity.TaskStageChange;

import java.util.Set;

public class TaskDto extends BaseDto {

    private String name;
    private String description;
    private int priority;
    private StageDto category;
    private TeamDto team;
    private UserDto owner;
    private Set<TaskStageChangeDto> taskStageChanges;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public StageDto getCategory() {
        return category;
    }

    public void setCategory(StageDto category) {
        this.category = category;
    }

    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public Set<TaskStageChangeDto> getTaskStageChanges() {
        return taskStageChanges;
    }

    public void setTaskStageChanges(Set<TaskStageChangeDto> taskStageChanges) {
        this.taskStageChanges = taskStageChanges;
    }
}
