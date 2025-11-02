package com.realtypro.controller;

import com.realtypro.repository.ImageRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.Image;
import com.realtypro.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ➕ CREATE single image
    @PostMapping("/create")
    public ResponseEntity<?> createImage(@RequestBody Image image) {
        try {
            // Validate and attach property
            if (image.getProperty() == null || image.getProperty().getPropertyId() == null) {
                return ResponseEntity.badRequest().body("Property reference is required");
            }

            Optional<Property> propertyOpt = propertyRepository.findById(image.getProperty().getPropertyId());
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Property not found with ID: " + image.getProperty().getPropertyId());
            }

            image.setProperty(propertyOpt.get());
            Image savedImage = imageRepository.save(image);
            return ResponseEntity.ok(savedImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving image: " + e.getMessage());
        }
    }

    // 📋 GET all images
    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    // 🔍 GET images by Property ID
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Image>> getImagesByProperty(@PathVariable Long propertyId) {
        List<Image> images = imageRepository.findByPropertyPropertyId(propertyId);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    // ✏️ UPDATE image details
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestBody Image updatedImage) {
        Optional<Image> existingOpt = imageRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Image existing = existingOpt.get();

        existing.setImageGroup(updatedImage.getImageGroup());
        existing.setImageUrl(updatedImage.getImageUrl());

        // If property is updated
        if (updatedImage.getProperty() != null && updatedImage.getProperty().getPropertyId() != null) {
            propertyRepository.findById(updatedImage.getProperty().getPropertyId())
                    .ifPresent(existing::setProperty);
        }

        Image saved = imageRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ DELETE image
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        if (!imageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        imageRepository.deleteById(id);
        return ResponseEntity.ok("Image deleted successfully");
    }
}