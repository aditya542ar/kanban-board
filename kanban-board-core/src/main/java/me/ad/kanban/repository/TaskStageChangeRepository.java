package me.ad.kanban.repository;

import me.ad.kanban.entity.TaskStageChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStageChangeRepository extends JpaRepository<TaskStageChange, Long> {
    TaskStageChange findFirstByTaskAndStageOrderByVersionDesc(String taskId, String stageId);
}
