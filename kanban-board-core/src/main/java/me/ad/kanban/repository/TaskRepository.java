package me.ad.kanban.repository;

import me.ad.kanban.entity.Project;
import me.ad.kanban.entity.Stage;
import me.ad.kanban.entity.Task;
import me.ad.kanban.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
    static Specification<Task> idSpec(String id) {
        return (root, cq, cb) -> cb.equal(root.get("id"), id);
    }
    static Specification<Task> idInSpec(List<String> idList) {
        return (root, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(root.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
    static Specification<Task> nameSpec(String name) {
        return (root, cq, cb) -> cb.equal(root.get("name"), name);
    }
    static Specification<Task> nameInSpec(List<String> nameList) {
        return (root, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(root.get("name"));
            nameList.forEach(in::value);
            return in;
        };
    }
    static Specification<Task> nameLikeSpec(String nameLike) {
        return (root, cq, cb) -> cb.like(root.get("name"), nameLike);
    }
    static Specification<Task> categoryIdSpec(String stageId) {
        return (root, cq, cb) -> cb.equal(root.get("category").get("id"), stageId);
    }
    static Specification<Task> teamIdSpec(String teamId) {
        return (root, cq, cb) -> cb.equal(root.get("team").get("id"), teamId);
    }
    static Specification<Task> userIdSpec(String userId) {
        return (root, cq, cb) -> cb.equal(root.get("user").get("id"), userId);
    }
    static Specification<Task> projectIdSpec(String projectId) {
        return (root, cq, cb) -> {
            Join<Task, Team> teamRelation = root.join("team");
            Join<Project, Task> projectRelation = teamRelation.join("project");
            return cb.equal(projectRelation.get("id"), projectId);
        };
    }
    static Specification<Task> categoryIdInSpec(List<String> idList) {
        return (root, cq, cb) -> {
            Join<Task, Stage> stageRelation = root.join("category");
            CriteriaBuilder.In<String> in = cb.in(stageRelation.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
    static Specification<Task> teamIdInSpec(List<String> idList) {
        return (root, cq, cb) -> {
            Join<Task, Team> teamRelation = root.join("team");
            CriteriaBuilder.In<String> in = cb.in(teamRelation.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
}
