package me.ad.kanban.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Task_Stage_Change")
public class TaskStageChange extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = true)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
