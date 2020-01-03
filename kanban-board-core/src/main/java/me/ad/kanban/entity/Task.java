package me.ad.kanban.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private int priority;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage category;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private Set<TaskStageChange> taskStageChangeSet = new HashSet<>();

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

    public Stage getCategory() {
        return category;
    }

    public void setCategory(Stage category) {
        this.category = category;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<TaskStageChange> getTaskStageChangeSet() {
        return taskStageChangeSet;
    }

    public void setTaskStageChangeSet(Set<TaskStageChange> taskStageChangeSet) {
        this.taskStageChangeSet = taskStageChangeSet;
    }
}
