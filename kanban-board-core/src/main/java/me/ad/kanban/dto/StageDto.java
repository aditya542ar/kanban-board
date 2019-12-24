package me.ad.kanban.dto;

import java.util.Set;

public class StageDto extends BaseDto {

    private String name;
    private ProjectDto project;
    private Set<TaskDto> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public Set<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskDto> tasks) {
        this.tasks = tasks;
    }
}
