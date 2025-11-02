package com.realtypro.service;

import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.VideoRepository;
import com.realtypro.schema.Property;
import com.realtypro.schema.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE Video
    public Video createVideo(Video video) {
        if (video.getProperty() == null || video.getProperty().getPropertyId() == null) {
            throw new IllegalArgumentException("Property reference is required");
        }

        Optional<Property> propertyOpt = propertyRepository.findById(video.getProperty().getPropertyId());
        if (propertyOpt.isEmpty()) {
            throw new IllegalArgumentException("Property not found with ID: " + video.getProperty().getPropertyId());
        }

        video.setProperty(propertyOpt.get());
        return videoRepository.save(video);
    }

    // ✅ GET all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // ✅ GET video by Property ID
    public Video getVideoByProperty(Long propertyId) {
        return videoRepository.findByPropertyPropertyId(propertyId);
    }

    // ✅ UPDATE video
    public Optional<Video> updateVideo(Long id, Video updatedVideo) {
        return videoRepository.findById(id).map(existing -> {
            existing.setVideoUrl(updatedVideo.getVideoUrl());

            // Update property if changed
            if (updatedVideo.getProperty() != null && updatedVideo.getProperty().getPropertyId() != null) {
                propertyRepository.findById(updatedVideo.getProperty().getPropertyId())
                        .ifPresent(existing::setProperty);
            }

            return videoRepository.save(existing);
        });
    }

    // ✅ DELETE video
    public boolean deleteVideo(Long id) {
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
