package me.ad.kanban.dto.query;

import me.ad.kanban.dto.ProjectDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public class ProjectGetAllQueryDto {

        String nameLike;
        String descriptionLike;
        String ownerId;
        String startDate;
        String endDate;
        String startDateBefore;
        String startDateAfter;
        String endDateBefore;
        String endDateAfter;
        String sortBy;
        String sortOrder;

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getDescriptionLike() {
        return descriptionLike;
    }

    public void setDescriptionLike(String descriptionLike) {
        this.descriptionLike = descriptionLike;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDateBefore() {
        return startDateBefore;
    }

    public void setStartDateBefore(String startDateBefore) {
        this.startDateBefore = startDateBefore;
    }

    public String getStartDateAfter() {
        return startDateAfter;
    }

    public void setStartDateAfter(String startDateAfter) {
        this.startDateAfter = startDateAfter;
    }

    public String getEndDateBefore() {
        return endDateBefore;
    }

    public void setEndDateBefore(String endDateBefore) {
        this.endDateBefore = endDateBefore;
    }

    public String getEndDateAfter() {
        return endDateAfter;
    }

    public void setEndDateAfter(String endDateAfter) {
        this.endDateAfter = endDateAfter;
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
