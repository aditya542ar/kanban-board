package me.ad.kanban.dto;

public class TaskDto extends BaseDto {

    private String name;
    private String description;
    private int priority;
    private StageDto category;
    private TeamDto team;
    private UserDto user;

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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
