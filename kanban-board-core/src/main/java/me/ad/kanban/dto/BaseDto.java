package me.ad.kanban.dto;

import java.io.Serializable;
import java.util.Objects;

public class BaseDto implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDto baseDto = (BaseDto) o;
        return Objects.equals(getId(), baseDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
