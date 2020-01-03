package me.ad.kanban.dto;

import java.time.LocalDateTime;

public class TaskStageChangeDto extends BaseDto {
    private StageDto stage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer version;

    public StageDto getStage() {
        return stage;
    }

    public void setStage(StageDto stage) {
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
}
