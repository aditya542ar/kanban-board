package me.ad.kanban.dto.query;

import java.util.List;

public class UserGetAllQueryDto {
    private String id;
    private List<String> idIn;
    private String firstName;
    private String firstNameLike;
    private String lastName;
    private String lastNameLike;
    private String userId;
    private List<String> userIdIn;
    private String sortBy;
    private String sortOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIdIn() {
        return idIn;
    }

    public void setIdIn(List<String> idIn) {
        this.idIn = idIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstNameLike() {
        return firstNameLike;
    }

    public void setFirstNameLike(String firstNameLike) {
        this.firstNameLike = firstNameLike;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameLike() {
        return lastNameLike;
    }

    public void setLastNameLike(String lastNameLike) {
        this.lastNameLike = lastNameLike;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserIdIn() {
        return userIdIn;
    }

    public void setUserIdIn(List<String> userIdIn) {
        this.userIdIn = userIdIn;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
