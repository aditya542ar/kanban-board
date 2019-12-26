package me.ad.kanban.repository;

import me.ad.kanban.entity.Stage;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, String>, JpaSpecificationExecutor<Stage> {
    static Specification<Stage> idSpec(String id) {
        return (root, cq, cb) -> cb.equal(root.get("id"), id);
    }
    static Specification<Stage> idInSpec(List<String> idList) {
        return (root, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(root.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
    static Specification<Stage> nameSpec(String name) {
        return (root, cq, cb) -> cb.equal(root.get("name"), name);
    }
    static Specification<Stage> nameInSpec(List<String> nameList) {
        return (root, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(root.get("name"));
            nameList.forEach(in::value);
            return in;
        };
    }
    static Specification<Stage> nameLikeSpec(String nameLike) {
        return (root, cq, cb) -> cb.like(root.get("name"), nameLike);
    }
}
