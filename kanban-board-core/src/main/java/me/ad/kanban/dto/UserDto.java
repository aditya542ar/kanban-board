package me.ad.kanban.dto;

import java.util.HashSet;
import java.util.Set;

public class UserDto extends BaseDto {

    private String firstName;
    private String lastName;
    private String userId;
    private Set<TeamDto> teams = new HashSet<>();
    private Set<ProjectDto> ownedProjects = new HashSet<>();
    private Set<TaskDto> tasks = new HashSet<>();
    private byte[] profilePic;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<TeamDto> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamDto> teams) {
        this.teams = teams;
    }

    public Set<ProjectDto> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<ProjectDto> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public Set<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }
}
