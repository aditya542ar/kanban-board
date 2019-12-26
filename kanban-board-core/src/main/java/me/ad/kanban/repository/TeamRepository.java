package me.ad.kanban.repository;

import me.ad.kanban.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String>, JpaSpecificationExecutor<Team> {
    static Specification<Team> idSpec(String id) {
        return (teamRoot, cq, cb) -> cb.equal(teamRoot.get("id"), id);
    }
    static Specification<Team> idInSpec(List<String> idList) {
        return (teamRoot, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(teamRoot.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
    static Specification<Team> nameSpec(String name) {
        return (teamRoot, cq, cb) -> cb.equal(teamRoot.get("name"), name);
    }
    static Specification<Team> nameInSpec(List<String> nameList) {
        return (teamRoot, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(teamRoot.get("name"));
            nameList.forEach(in::value);
            return in;
        };
    }
    static Specification<Team> nameLikeSpec(String nameLike) {
        return (teamRoot, cq, cb) -> cb.like(teamRoot.get("name"), nameLike);
    }
}
