package me.ad.kanban.dto;

import java.io.Serializable;

public class BaseDto implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
