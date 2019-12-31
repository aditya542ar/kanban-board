package me.ad.kanban.repository;

import me.ad.kanban.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    static Specification<User> idSpec(String id) {
        return (userRoot, cq, cb) -> cb.equal(userRoot.get("id"), id);
    }
    static Specification<User> idInSpec(List<String> idList) {
        return (userRoot, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(userRoot.get("id"));
            idList.forEach(in::value);
            return in;
        };
    }
    static Specification<User> firstNameSpec(String firstName) {
        return (userRoot, cq, cb) -> cb.equal(userRoot.get("firstName"), firstName);
    }
    static Specification<User> firstNameLikeSpec(String firstNameLike) {
        return (userRoot, cq, cb) -> cb.like(userRoot.get("firstName"), firstNameLike);
    }
    static Specification<User> lastNameSpec(String lastName) {
        return (userRoot, cq, cb) -> cb.equal(userRoot.get("lastName"), lastName);
    }
    static Specification<User> lastNameLikeSpec(String lastNameLike) {
        return (userRoot, cq, cb) -> cb.like(userRoot.get("lastName"), lastNameLike);
    }
    static Specification<User> userIdSpec(String userId) {
        return (userRoot, cq, cb) -> cb.equal(userRoot.get("userId"), userId);
    }
    static Specification<User> userIdInSpec(List<String> userIdList) {
        return (userRoot, cq, cb) -> {
            CriteriaBuilder.In<String> in = cb.in(userRoot.get("userId"));
            userIdList.forEach(in::value);
            return in;
        };
    }
    static Specification<User> userIdLikeSpec(String userIdLike) {
        return (userRoot, cq, cb) -> cb.like(userRoot.get("userId"), userIdLike);
    }
}
