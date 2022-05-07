package com.post.dto;

import java.util.List;

public class ConnectionsDto {
    private List<String> uuid;

    public ConnectionsDto(List<String> uuid) {
        this.uuid = uuid;
    }

    public List<String> getUuid() {
        return uuid;
    }
}
