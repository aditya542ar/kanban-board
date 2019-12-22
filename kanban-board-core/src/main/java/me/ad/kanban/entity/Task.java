package me.ad.kanban.entity;

import javax.persistence.*;

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
    @JoinColumn(name = "user_id")
    private User user;

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
}
