package com.nagarro.af.bookingtablesystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class MenuDTO {
    @Id
    private UUID id;
    @NotBlank
    private String fileName;
    @NotNull
    private byte[] content;
    @NotBlank
    private String contentType;
    @NotNull
    private UUID restaurantId;

    public MenuDTO(UUID id, String fileName, byte[] content, String contentType, UUID restaurantId) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.contentType = contentType;
        this.restaurantId = restaurantId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
