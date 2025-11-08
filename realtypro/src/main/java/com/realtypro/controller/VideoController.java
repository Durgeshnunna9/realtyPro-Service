package com.realtypro.controller;

import com.realtypro.repository.VideoRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.Video;
import com.realtypro.schema.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // 🎥 UPLOAD single video file
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("propertyId") Long propertyId
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Property not found with ID: " + propertyId);
            }

            // 🗃️ Save binary video data to DB
            Video video = new Video();
            video.setProperty(propertyOpt.get());
            video.setVideoData(file.getBytes()); // store raw bytes

            Video savedVideo = videoRepository.save(video);
            return ResponseEntity.ok(savedVideo);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error reading video file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving video: " + e.getMessage());
        }
    }

    // ▶️ FETCH video by ID (plays directly)
    @GetMapping(value = "/{id}", produces = "video/mp4")
    public ResponseEntity<byte[]> getVideoById(@PathVariable Long id) {
        Optional<Video> videoOpt = videoRepository.findById(id);

        if (videoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Video video = videoOpt.get();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(video.getVideoData());
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

    // 🔍 GET video by Property ID
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<Video> getVideoByProperty(@PathVariable Long propertyId) {
        Optional<Video> videoOpt = videoRepository.findByProperty_PropertyId(propertyId);
        return videoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
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
