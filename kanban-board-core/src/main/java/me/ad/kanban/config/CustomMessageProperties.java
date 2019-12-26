package me.ad.kanban.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "message")
public class CustomMessageProperties {
    private String projectNotExist;
    private String userNotExist;
    private String teamNotExist;
    private String stageNotExist;
    private String taskNotExist;
    private String invalidLikeFilterCriteria;
    private String invalidDateFilterCriteria;
    private String missingSortOrder;
    private String missingSortBy;
    private String invalidSortOrder;
    private String invalidSortBy;
    private String invalidIdentifier;

    public String getProjectNotExist() {
        return projectNotExist;
    }

    public void setProjectNotExist(String projectNotExist) {
        this.projectNotExist = projectNotExist;
    }

    public String getUserNotExist() {
        return userNotExist;
    }

    public void setUserNotExist(String userNotExist) {
        this.userNotExist = userNotExist;
    }

    public String getTeamNotExist() {
        return teamNotExist;
    }

    public void setTeamNotExist(String teamNotExist) {
        this.teamNotExist = teamNotExist;
    }

    public String getStageNotExist() {
        return stageNotExist;
    }

    public void setStageNotExist(String stageNotExist) {
        this.stageNotExist = stageNotExist;
    }

    public String getTaskNotExist() {
        return taskNotExist;
    }

    public void setTaskNotExist(String taskNotExist) {
        this.taskNotExist = taskNotExist;
    }

    public String getInvalidLikeFilterCriteria() {
        return invalidLikeFilterCriteria;
    }

    public void setInvalidLikeFilterCriteria(String invalidLikeFilterCriteria) {
        this.invalidLikeFilterCriteria = invalidLikeFilterCriteria;
    }

    public String getInvalidDateFilterCriteria() {
        return invalidDateFilterCriteria;
    }

    public void setInvalidDateFilterCriteria(String invalidDateFilterCriteria) {
        this.invalidDateFilterCriteria = invalidDateFilterCriteria;
    }

    public String getMissingSortOrder() {
        return missingSortOrder;
    }

    public void setMissingSortOrder(String missingSortOrder) {
        this.missingSortOrder = missingSortOrder;
    }

    public String getMissingSortBy() {
        return missingSortBy;
    }

    public void setMissingSortBy(String missingSortBy) {
        this.missingSortBy = missingSortBy;
    }

    public String getInvalidSortOrder() {
        return invalidSortOrder;
    }

    public void setInvalidSortOrder(String invalidSortOrder) {
        this.invalidSortOrder = invalidSortOrder;
    }

    public String getInvalidSortBy() {
        return invalidSortBy;
    }

    public void setInvalidSortBy(String invalidSortBy) {
        this.invalidSortBy = invalidSortBy;
    }

    public String getInvalidIdentifier() {
        return invalidIdentifier;
    }

    public void setInvalidIdentifier(String invalidIdentifier) {
        this.invalidIdentifier = invalidIdentifier;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomMessageProperties{");
        sb.append("projectNotExist='").append(projectNotExist).append('\'');
        sb.append(", userNotExist='").append(userNotExist).append('\'');
        sb.append(", teamNotExist='").append(teamNotExist).append('\'');
        sb.append(", stageNotExist='").append(stageNotExist).append('\'');
        sb.append(", taskNotExist='").append(taskNotExist).append('\'');
        sb.append(", invalidLikeFilterCriteria='").append(invalidLikeFilterCriteria).append('\'');
        sb.append(", invalidDateFilterCriteria='").append(invalidDateFilterCriteria).append('\'');
        sb.append(", missingSortOrder='").append(missingSortOrder).append('\'');
        sb.append(", missingSortBy='").append(missingSortBy).append('\'');
        sb.append(", invalidSortOrder='").append(invalidSortOrder).append('\'');
        sb.append(", invalidSortBy='").append(invalidSortBy).append('\'');
        sb.append(", invalidIdentifier='").append(invalidIdentifier).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
