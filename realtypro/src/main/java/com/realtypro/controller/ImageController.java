package com.realtypro.controller;

import com.realtypro.schema.Image;
import com.realtypro.schema.Property;
import com.realtypro.repository.ImageRepository;
import com.realtypro.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // 🆕 UPLOAD single image (stored in PostgreSQL)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("propertyId") Long propertyId,
            @RequestParam(value = "imageGroup", required = false) String imageGroup
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Property not found with ID: " + propertyId);
            }

            Image image = new Image();
            image.setProperty(propertyOpt.get());
            image.setImageGroup(imageGroup != null ? imageGroup : "property_photos");
            image.setImageData(file.getBytes()); // ✅ store binary data

            Image savedImage = imageRepository.save(image);
            return ResponseEntity.ok(savedImage);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error uploading image: " + e.getMessage());
        }
    }

    // 🖼 FETCH image binary by ID
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image.getImageData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 📋 GET all images (metadata only, not binary)
    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // ⚠️ Remove binary data from response (avoid sending large blobs)
        images.forEach(img -> img.setImageData(null));

        return ResponseEntity.ok(images);
    }

    // 🔍 GET images by Property ID (metadata only)
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Image>> getImagesByProperty(@PathVariable Long propertyId) {
        List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // ⚠️ Strip binary data
        images.forEach(img -> img.setImageData(null));

        return ResponseEntity.ok(images);
    }

    // ✏️ UPDATE image (replace or change metadata)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "imageGroup", required = false) String imageGroup
    ) {
        try {
            Optional<Image> existingOpt = imageRepository.findById(id);
            if (existingOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Image existing = existingOpt.get();

            if (imageGroup != null) {
                existing.setImageGroup(imageGroup);
            }

            if (file != null && !file.isEmpty()) {
                existing.setImageData(file.getBytes()); // ✅ replace image binary
            }

            Image saved = imageRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error updating image: " + e.getMessage());
        }
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
