package com.example.imagecommanderback.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto for transferring image content.
 */
@Getter
@Setter
public class ImageDto {

    private String keyName;

    private byte[] content;

    public ImageDto(String keyName, byte[] content) {
        this.keyName = keyName;
        this.content = content;
    }
}
