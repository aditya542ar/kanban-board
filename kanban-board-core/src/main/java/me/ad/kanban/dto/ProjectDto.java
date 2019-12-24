package me.ad.kanban.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProjectDto extends BaseDto{

    private String name;
    private String description;
    private UserDto owner;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<TeamDto> teams = new HashSet<>();
    private Set<StageDto> stages = new HashSet<>();

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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<TeamDto> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamDto> teams) {
        this.teams = teams;
    }

    public Set<StageDto> getStages() {
        return stages;
    }

    public void setStages(Set<StageDto> stages) {
        this.stages = stages;
    }
}
