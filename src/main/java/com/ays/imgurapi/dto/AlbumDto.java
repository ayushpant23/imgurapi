package com.ays.imgurapi.dto;

import java.util.Set;

import com.ays.imgurapi.entity.Images;

import lombok.Data;

@Data
public class AlbumDto {
    private String Id;
    private String username; 
    private String albumName;
    private String albumDescription;
    private String deleteHash;
    private Set<Images> images;
}
