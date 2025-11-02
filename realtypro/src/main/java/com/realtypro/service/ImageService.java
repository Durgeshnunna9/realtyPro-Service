package com.realtypro.service;

import com.realtypro.schema.Image;
import com.realtypro.schema.Property;
import com.realtypro.repository.ImageRepository;
import com.realtypro.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ➕ CREATE new image
    public Image createImage(Image image) {
        if (image.getProperty() == null || image.getProperty().getPropertyId() == null) {
            throw new IllegalArgumentException("Property reference is required");
        }

        Property property = propertyRepository.findById(image.getProperty().getPropertyId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Property not found with ID: " + image.getProperty().getPropertyId()));

        image.setProperty(property);
        return imageRepository.save(image);
    }

    // 📋 GET all images
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    // 🔍 GET images by Property ID
    public List<Image> getImagesByProperty(Long propertyId) {
        return imageRepository.findByPropertyPropertyId(propertyId);
    }

    // ✏️ UPDATE existing image
    public Image updateImage(Long id, Image updatedImage) {
        Image existing = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found with ID: " + id));

        if (updatedImage.getImageGroup() != null) {
            existing.setImageGroup(updatedImage.getImageGroup());
        }
        if (updatedImage.getImageUrl() != null) {
            existing.setImageUrl(updatedImage.getImageUrl());
        }

        // If property is being changed
        if (updatedImage.getProperty() != null && updatedImage.getProperty().getPropertyId() != null) {
            Property property = propertyRepository.findById(updatedImage.getProperty().getPropertyId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Property not found with ID: " + updatedImage.getProperty().getPropertyId()));
            existing.setProperty(property);
        }

        return imageRepository.save(existing);
    }

    // ❌ DELETE image
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new IllegalArgumentException("Image not found with ID: " + id);
        }
        imageRepository.deleteById(id);
    }
}
