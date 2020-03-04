package me.ad.kanban.repository;

import me.ad.kanban.entity.Project;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);

    // criteria specification
    static Specification<Project> nameLikeSpec(String nameLike) {
        return (projectRoot, cq, cb) -> cb.like(projectRoot.get("name"), nameLike);
    }
    static Specification<Project> descriptionLikeSpec(String descriptionLike) {
        return (projectRoot, cq, cb) -> cb.like(projectRoot.get("description"), descriptionLike);
    }
    static Specification<Project> ownerIdSpec(String ownerId) {
        return (projectRoot, cq, cb) -> cb.equal(projectRoot.get("ownerId"), ownerId);
    }
    static Specification<Project> startDateSpec(String startDate) {
        return (projectRoot, cq, cb) -> cb.equal(projectRoot.get("startDate"),
                LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
    static Specification<Project> endDateSpec(String endDate) {
        return (projectRoot, cq, cb) -> cb.equal(projectRoot.get("endDate"),
                LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
    static Specification<Project> startDateBeforeSpec(String startDate) {
        return (projectRoot, cq, cb) -> cb.lessThanOrEqualTo(projectRoot.get("startDate"),
                LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
    static Specification<Project> endDateBeforeSpec(String endDate) {
        return (projectRoot, cq, cb) -> cb.lessThanOrEqualTo(projectRoot.get("endDate"),
                LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
    static Specification<Project> startDateAfterSpec(String startDate) {
        return (projectRoot, cq, cb) -> cb.greaterThanOrEqualTo(projectRoot.get("startDate"),
                LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
    static Specification<Project> endDateAfterSpec(String endDate) {
        return (projectRoot, cq, cb) -> cb.greaterThanOrEqualTo(projectRoot.get("endDate"),
                LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
