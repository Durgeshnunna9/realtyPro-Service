package com.realtypro.service;

import com.realtypro.schema.Image;
import com.realtypro.schema.Property;
import com.realtypro.repository.ImageRepository;
import com.realtypro.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ➕ Upload image to DB
    public Image uploadImage(MultipartFile file, Long propertyId, String imageGroup) throws IOException {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            throw new RuntimeException("Property not found with ID: " + propertyId);
        }

        Image image = new Image();
        image.setProperty(propertyOpt.get());
        image.setImageGroup(imageGroup != null ? imageGroup : "property_photos");
        image.setImageData(file.getBytes()); // ✅ store binary data


        return imageRepository.save(image);
    }

    // 🔍 Get single image by ID
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    // 📋 Get all images
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    // 🔍 Get images by property ID
    public List<Image> getImagesByProperty(Long propertyId) {
        return imageRepository.findByProperty_PropertyId(propertyId);
    }

    // ✏️ Update image details
    public Image updateImage(Long id, Image updatedImage) {
        Image existing = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (updatedImage.getImageGroup() != null)
            existing.setImageGroup(updatedImage.getImageGroup());

        if (updatedImage.getProperty() != null && updatedImage.getProperty().getPropertyId() != null) {
            propertyRepository.findById(updatedImage.getProperty().getPropertyId())
                    .ifPresent(existing::setProperty);
        }

        return imageRepository.save(existing);
    }

    // ❌ Delete image
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("Image not found");
        }
        imageRepository.deleteById(id);
    }
}