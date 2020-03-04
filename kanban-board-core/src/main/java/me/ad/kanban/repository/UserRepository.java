package me.ad.kanban.repository;

import me.ad.kanban.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByUserId(String userId);

    @Modifying
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.profilePic = :profilePic WHERE u.id = :id")
    void updateUserDetail(@Param("id") String id, @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("profilePic") byte[] profilePic);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") String id, @Param("password") String password);

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
