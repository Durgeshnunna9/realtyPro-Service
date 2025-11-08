package com.realtypro.service;

import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.VideoRepository;
import com.realtypro.schema.Property;
import com.realtypro.schema.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ✅ CREATE Video
    public Video uploadVideo(Long propertyId, MultipartFile file) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Video video = new Video();
        video.setProperty(property);
        video.setVideoData(file.getBytes());

        return videoRepository.save(video);
    }

    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }

    public Optional<Video> getVideoByProperty(Long propertyId) {
        return videoRepository.findByProperty_PropertyId(propertyId);
    }

    public boolean deleteVideo(Long id) {
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
