package me.ad.kanban.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Stage extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Task> tasks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stage")
    private Set<TaskStageChange> taskStageChangeSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("equals called for Stage");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return Objects.equals(getId(), stage.getId());
    }

    @Override
    public int hashCode() {
        System.out.println("HashCode called for stage");
        return Objects.hash(getId());
    }
}
