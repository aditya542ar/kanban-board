package me.ad.kanban.dto;

import java.util.HashSet;
import java.util.Set;

public class TeamDto extends BaseDto {

    private String name;
    private String projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
