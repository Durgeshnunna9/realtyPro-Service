package com.realtypro.dto;

import com.realtypro.schema.Property;

import java.util.List;
import java.util.Map;


public class PropertyResponseDTO {
    private Property property;
    private Map<String, List<String>> images;
    private String videoUrl;


    public PropertyResponseDTO(Property property,
                               Map<String, List<String>> images,
                               String video){
        this.property = property;
        this.images = images;
        this.videoUrl = video;

    }

    public Property getProperty() { return property; }
    public Map<String, List<String>> getImages() { return images; }
    public String getVideo() { return videoUrl; }

}
