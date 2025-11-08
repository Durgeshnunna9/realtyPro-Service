package com.realtypro.schema;

import jakarta.persistence.*;

@Entity
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id", nullable = false)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name="property_id", nullable = false)
    private Property property;

    @Column(name="image_group", nullable = false)
    private String imageGroup;

    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;


    public Image() {}

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
