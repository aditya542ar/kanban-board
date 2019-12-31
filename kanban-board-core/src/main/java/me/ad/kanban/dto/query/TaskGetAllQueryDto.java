package me.ad.kanban.dto.query;

import java.util.List;

public class TaskGetAllQueryDto {
    private String id;
    private List<String> idIn;
    private String name;
    private List<String> nameIn;
    private String nameLike;
    private String categoryId;
    private List<String> categoryIdIn;
    private String teamId;
    private List<String> teamIdIn;
    private String userId;
    private String projectId;
    private String sortBy;
    private String sortOrder;
    private String groupBy;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNameIn() {
        return nameIn;
    }

    public void setNameIn(List<String> nameIn) {
        this.nameIn = nameIn;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCategoryIdIn() {
        return categoryIdIn;
    }

    public void setCategoryIdIn(List<String> categoryIdIn) {
        this.categoryIdIn = categoryIdIn;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<String> getTeamIdIn() {
        return teamIdIn;
    }

    public void setTeamIdIn(List<String> teamIdIn) {
        this.teamIdIn = teamIdIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}
