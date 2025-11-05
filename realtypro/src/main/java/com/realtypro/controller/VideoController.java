package com.realtypro.controller;

import com.realtypro.repository.VideoRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.Video;
import com.realtypro.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ➕ CREATE single video
    @PostMapping("/upload")
    public ResponseEntity<?> createVideo(@RequestBody Video video) {
        try {
            // Validate and attach property
            if (video.getProperty() == null || video.getProperty().getPropertyId() == null) {
                return ResponseEntity.badRequest().body("Property reference is required");
            }

            Optional<Property> propertyOpt = propertyRepository.findById(video.getProperty().getPropertyId());
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Property not found with ID: " + video.getProperty().getPropertyId());
            }

            video.setProperty(propertyOpt.get());
            Video savedVideo = videoRepository.save(video);
            return ResponseEntity.ok(savedVideo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving video: " + e.getMessage());
        }
    }

    // 📋 GET all videos
    @GetMapping("/all")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoRepository.findAll();
        if (videos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videos);
    }

    // 🔍 GET videos by Property ID
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<Video> getVideoByProperty(@PathVariable Long propertyId) {
        Video video = videoRepository.findByPropertyPropertyId(propertyId);
        if (video == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(video);
    }

    // ✏️ UPDATE video details
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVideo(@PathVariable Long id, @RequestBody Video updatedVideo) {
        Optional<Video> existingOpt = videoRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Video existing = existingOpt.get();

        existing.setVideoUrl(updatedVideo.getVideoUrl());

        // If property is updated
        if (updatedVideo.getProperty() != null && updatedVideo.getProperty().getPropertyId() != null) {
            propertyRepository.findById(updatedVideo.getProperty().getPropertyId())
                    .ifPresent(existing::setProperty);
        }

        Video saved = videoRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ DELETE video
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        if (!videoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        videoRepository.deleteById(id);
        return ResponseEntity.ok("Video deleted successfully");
    }
}