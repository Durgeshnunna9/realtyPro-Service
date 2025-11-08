package com.realtypro.controller;

import com.realtypro.dto.PropertyDTO;
import com.realtypro.dto.PropertyResponseDTO;
import com.realtypro.repository.UserRepository;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.ImageRepository;
import com.realtypro.repository.VideoRepository;
import com.realtypro.schema.Property;
import com.realtypro.schema.User;
import com.realtypro.schema.Image;
import com.realtypro.schema.Video;
import com.realtypro.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private VideoRepository videoRepository;

    // ➕ Create Property with JSON
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(@RequestBody PropertyDTO request) {
        try {
            // Validate agent and manager
            User agent = userRepository.findById(request.getAgent().getUserId())
                    .orElseThrow(() -> new RuntimeException("Agent not found"));
            if (agent.getRole() != Role.AGENT)
                return ResponseEntity.badRequest().body("User is not an AGENT");

            User manager = userRepository.findById(request.getManager().getUserId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            if (manager.getRole() != Role.MANAGER)
                return ResponseEntity.badRequest().body("User is not a MANAGER");

            // Create property
            Property property = new Property();
            property.setAgent(agent);
            property.setManager(manager);
            property.setFootfall(request.getFootfall());
            property.setSnackSpend(request.getSnackSpend());
            property.setPropertyType(request.getPropertyType());
            property.setStoreModel(request.getStoreModel());
            property.setStoreSize(request.getStoreSize());
            property.setStoreDimensionsL(request.getStoreDimensionsL());
            property.setStoreDimensionsW(request.getStoreDimensionsW());
            property.setRoadFacing(request.getRoadFacing());
            property.setEntryDirection(request.getEntryDirection());
            property.setCornerPiece(request.getCornerPiece());
            property.setCornerSide(request.getCornerSide());
            property.setStorePosition(request.getStorePosition());
            property.setShutterL(request.getShutterL());
            property.setShutterW(request.getShutterW());
            property.setFrontOffset(request.getFrontOffset());
            property.setSetback(request.getSetback());
            property.setFloor(request.getFloor());
            property.setRentalValue(request.getRentalValue());
            property.setParkingAvailability(request.getParkingAvailability());
            property.setTwoWCapacity(request.getTwoWCapacity());
            property.setFourWCapacity(request.getFourWCapacity());
            property.setOwnerContacted(request.getOwnerContacted());
            property.setWashroom(request.getWashroom());
            property.setElectricity(request.getElectricity());
            property.setGenerator(request.getGenerator());
            property.setBuildingAge(request.getBuildingAge());
            property.setWaterSupply(request.getWaterSupply());
            property.setBuildingCondition(request.getBuildingCondition());
            property.setLandmark(request.getLandmark());
            property.setAboutProperty(request.getAboutProperty());

            // Handle list fields - convert to comma-separated strings
            if (request.getNeighbourhoodFacilities() != null && !request.getNeighbourhoodFacilities().isEmpty()) {
                property.setNeighbourhoodFacilities(String.join(", ", request.getNeighbourhoodFacilities()));
            } else {
                property.setNeighbourhoodFacilities("");
            }

            if (request.getLocationAdvantages() != null && !request.getLocationAdvantages().isEmpty()) {
                property.setLocationAdvantages(String.join(", ", request.getLocationAdvantages()));
            } else {
                property.setLocationAdvantages("");
            }

            property.setStatus(request.getStatus());

            propertyRepository.save(property);

            return ResponseEntity.ok(property);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error creating property: " + e.getMessage());
        }
    }
    // 📋 Get all properties
    @GetMapping("/all")
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(properties);
    }

    // 🔍 Get property by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // ✏️ Update property
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {
        Optional<Property> existingOpt = propertyRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        Property existing = existingOpt.get();

        // ✅ Update only non-null fields
        if (updatedProperty.getFootfall() != null) existing.setFootfall(updatedProperty.getFootfall());
        if (updatedProperty.getSnackSpend() != null) existing.setSnackSpend(updatedProperty.getSnackSpend());
        if (updatedProperty.getPropertyType() != null) existing.setPropertyType(updatedProperty.getPropertyType());
        if (updatedProperty.getStatus() != null) existing.setStatus(updatedProperty.getStatus());
        if (updatedProperty.getStoreModel() != null) existing.setStoreModel(updatedProperty.getStoreModel());
        if (updatedProperty.getStoreSize() != null) existing.setStoreSize(updatedProperty.getStoreSize());
        if (updatedProperty.getStoreDimensionsL() != null) existing.setStoreDimensionsL(updatedProperty.getStoreDimensionsL());
        if (updatedProperty.getStoreDimensionsW() != null) existing.setStoreDimensionsW(updatedProperty.getStoreDimensionsW());
        if (updatedProperty.getRoadFacing() != null) existing.setRoadFacing(updatedProperty.getRoadFacing());
        if (updatedProperty.getEntryDirection() != null) existing.setEntryDirection(updatedProperty.getEntryDirection());
        if (updatedProperty.getCornerPiece() != null) existing.setCornerPiece(updatedProperty.getCornerPiece());
        if (updatedProperty.getCornerSide() != null) existing.setCornerSide(updatedProperty.getCornerSide());
        if (updatedProperty.getShutterL() != null) existing.setShutterL(updatedProperty.getShutterL());
        if (updatedProperty.getShutterW() != null) existing.setShutterW(updatedProperty.getShutterW());
        if (updatedProperty.getStorePosition() != null) existing.setStorePosition(updatedProperty.getStorePosition());
        if (updatedProperty.getFrontOffset() != null) existing.setFrontOffset(updatedProperty.getFrontOffset());
        if (updatedProperty.getSetback() != null) existing.setSetback(updatedProperty.getSetback());
        if (updatedProperty.getFloor() != null) existing.setFloor(updatedProperty.getFloor());
        if (updatedProperty.getRentalValue() != null) existing.setRentalValue(updatedProperty.getRentalValue());
        if (updatedProperty.getParkingAvailability() != null) existing.setParkingAvailability(updatedProperty.getParkingAvailability());
        if (updatedProperty.getTwoWCapacity() != null) existing.setTwoWCapacity(updatedProperty.getTwoWCapacity());
        if (updatedProperty.getFourWCapacity() != null) existing.setFourWCapacity(updatedProperty.getFourWCapacity());
        if (updatedProperty.getOwnerContacted() != null) existing.setOwnerContacted(updatedProperty.getOwnerContacted());
        if (updatedProperty.getWashroom() != null) existing.setWashroom(updatedProperty.getWashroom());
        if (updatedProperty.getElectricity() != null) existing.setElectricity(updatedProperty.getElectricity());
        if (updatedProperty.getGenerator() != null) existing.setGenerator(updatedProperty.getGenerator());
        if (updatedProperty.getBuildingAge() != null) existing.setBuildingAge(updatedProperty.getBuildingAge());
        if (updatedProperty.getWaterSupply() != null) existing.setWaterSupply(updatedProperty.getWaterSupply());
        if (updatedProperty.getBuildingCondition() != null) existing.setBuildingCondition(updatedProperty.getBuildingCondition());
        if (updatedProperty.getLandmark() != null) existing.setLandmark(updatedProperty.getLandmark());
        if (updatedProperty.getAboutProperty() != null) existing.setAboutProperty(updatedProperty.getAboutProperty());
        if (updatedProperty.getNeighbourhoodFacilities() != null)
            existing.setNeighbourhoodFacilities(updatedProperty.getNeighbourhoodFacilities());
        if (updatedProperty.getLocationAdvantages() != null)
            existing.setLocationAdvantages(updatedProperty.getLocationAdvantages());

        // Update linked users
        if (updatedProperty.getAgent() != null && updatedProperty.getAgent().getUserId() != null)
            userRepository.findById(updatedProperty.getAgent().getUserId()).ifPresent(existing::setAgent);

        if (updatedProperty.getManager() != null && updatedProperty.getManager().getUserId() != null)
            userRepository.findById(updatedProperty.getManager().getUserId()).ifPresent(existing::setManager);

        Property saved = propertyRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ Delete property
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) return ResponseEntity.notFound().build();
        propertyRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Property deleted successfully", "propertyId", id));
    }

    // ============================================
    // 🧭 GET PROPERTIES BY USER ROLE (with Base64 encoded media)
    // ============================================

    // Get properties by Agent
    @GetMapping("/agent/{userId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<PropertyResponseDTO>> getPropertiesByAgent(@PathVariable Long userId) {
        List<Property> properties = propertyRepository.findByAgentUserId(userId);

        List<PropertyResponseDTO> propertyResponses = properties.stream().map(property -> {
            List<Image> images = imageRepository.findByProperty_PropertyId(property.getPropertyId());

            // ✅ Get the Video from Optional
            Optional<Video> videoOptional = videoRepository.findByProperty_PropertyId(property.getPropertyId());
            Video video = videoOptional.orElse(null);  // Extract Video or null

            // Convert byte[] to Base64 URLs grouped by imageGroup
            Map<String, List<String>> imageMap = images.stream()
                    .collect(Collectors.groupingBy(
                            Image::getImageGroup,
                            Collectors.mapping(img -> convertToBase64Image(img.getImageData()),
                                    Collectors.toList())
                    ));

            // ✅ Convert video byte[] to Base64 URL
            String videoUrl = (video != null && video.getVideoData() != null)
                    ? convertToBase64Video(video.getVideoData())
                    : "";

            return new PropertyResponseDTO(property, imageMap, videoUrl);
        }).toList();

        return ResponseEntity.ok(propertyResponses);
    }

    // Get properties by Manager
    @GetMapping("/manager/{userId}")
    public ResponseEntity<List<PropertyResponseDTO>> getPropertiesByManager(@PathVariable Long userId) {
        List<Property> properties = propertyRepository.findByManagerUserId(userId);

        if (properties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        List<PropertyResponseDTO> responseList = properties.stream().map(property -> {
            Long propertyId = property.getPropertyId();

            List<Image> images = imageRepository.findByProperty_PropertyId(propertyId);
            Map<String, List<String>> groupedImages = images.stream()
                    .collect(Collectors.groupingBy(
                            Image::getImageGroup,
                            Collectors.mapping(img -> convertToBase64Image(img.getImageData()),
                                    Collectors.toList())
                    ));

            Optional<Video> videoOptional = videoRepository.findByProperty_PropertyId(propertyId);
            Video video = videoOptional.orElse(null);

            String videoUrl = (video != null && video.getVideoData() != null)
                    ? convertToBase64Video(video.getVideoData())
                    : "";

            return new PropertyResponseDTO(property, groupedImages, videoUrl);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // ============================================
    // 🖼️ IMAGE UPLOAD ENDPOINTS
    // ============================================

    // Upload property images (max 5)
    @PostMapping("/{propertyId}/images/property")
    public ResponseEntity<?> uploadPropertyImages(
            @PathVariable Long propertyId,
            @RequestParam("images") List<MultipartFile> images
    ) {
        try {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            if (images.size() > 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Maximum 5 property photos allowed"));
            }

            for (MultipartFile file : images) {
                Image img = new Image();
                img.setProperty(property);
                img.setImageGroup("property_photos");
                img.setImageData(file.getBytes());
                imageRepository.save(img);
            }

            return ResponseEntity.ok(Map.of("message", "Property images uploaded successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error uploading property images: " + e.getMessage()));
        }
    }

    // Upload images by group
    @PostMapping("/{propertyId}/images/{imageGroup}")
    public ResponseEntity<?> uploadImagesByGroup(
            @PathVariable Long propertyId,
            @PathVariable String imageGroup,
            @RequestParam("images") List<MultipartFile> images
    ) {
        try {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            for (MultipartFile file : images) {
                Image img = new Image();
                img.setProperty(property);
                img.setImageGroup(imageGroup);
                img.setImageData(file.getBytes());
                imageRepository.save(img);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Images uploaded successfully for group: " + imageGroup,
                    "count", images.size()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error uploading images: " + e.getMessage()));
        }
    }

    // Get image by ID (returns actual image)
    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // or detect from data
                    return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ============================================
    // 🎥 VIDEO UPLOAD ENDPOINT
    // ============================================

    @PostMapping("/{propertyId}/video")
    public ResponseEntity<?> uploadVideo(
            @PathVariable Long propertyId,
            @RequestParam("video") MultipartFile video) {

        // ✅ Check if a video already exists for this property
        Optional<Video> existingVideoOptional = videoRepository.findByProperty_PropertyId(propertyId);

        if (existingVideoOptional.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "A video is already uploaded for this property. Use PUT to update."));
        }

        try {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            Video vid = new Video();
            vid.setProperty(property);
            vid.setVideoData(video.getBytes());
            videoRepository.save(vid);

            return ResponseEntity.ok(Map.of(
                    "message", "Video uploaded successfully",
                    "videoId", vid.getVideoId()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload video: " + e.getMessage()));
        }
    }

    // Update/Replace video
    @PutMapping("/{propertyId}/video")
    public ResponseEntity<?> updateVideo(
            @PathVariable Long propertyId,
            @RequestParam("video") MultipartFile video) {

        try {
            // Step 1: Get the Optional
            Optional<Video> existingVideoOptional = videoRepository.findByProperty_PropertyId(propertyId);

            // Step 2: Check if Optional is empty
            if (existingVideoOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No video found for this property. Use POST to create."));
            }

            // Step 3: Extract the actual Video object from Optional
            Video existingVideo = existingVideoOptional.get();

            // Step 4: Now you can call Video methods
            existingVideo.setVideoData(video.getBytes());
            videoRepository.save(existingVideo);

            return ResponseEntity.ok(Map.of(
                    "message", "Video updated successfully",
                    "videoId", existingVideo.getVideoId()  // ✅ Now this works!
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update video: " + e.getMessage()));
        }
    }

    // Get video by property ID (returns actual video)
    @GetMapping("/{propertyId}/video")
    public ResponseEntity<byte[]> getVideo(@PathVariable Long propertyId) {
        // ✅ Extract Video or return null
        Video video = videoRepository.findByProperty_PropertyId(propertyId)
                .orElse(null);

        if (video == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        headers.setContentLength(video.getVideoData().length);

        return new ResponseEntity<>(video.getVideoData(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{propertyId}/video")
    public ResponseEntity<?> deleteVideo(@PathVariable Long propertyId) {
        try {
            // ✅ Extract Video or return null
            Video video = videoRepository.findByProperty_PropertyId(propertyId)
                    .orElse(null);

            if (video == null) {
                return ResponseEntity.notFound().build();
            }

            videoRepository.delete(video);
            return ResponseEntity.ok(Map.of("message", "Video deleted successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete video: " + e.getMessage()));
        }
    }

    // ============================================
    // 🗑️ DELETE MEDIA
    // ============================================

    // Delete specific image
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    imageRepository.delete(image);
                    return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete all images of a specific group for a property
    @DeleteMapping("/{propertyId}/images/{imageGroup}")
    public ResponseEntity<?> deleteImagesByGroup(
            @PathVariable Long propertyId,
            @PathVariable String imageGroup) {

        List<Image> images = imageRepository.findByProperty_PropertyIdAndImageGroup(propertyId, imageGroup);

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        imageRepository.deleteAll(images);
        return ResponseEntity.ok(Map.of(
                "message", "Images deleted successfully",
                "count", images.size()
        ));
    }

    // ============================================
    // 🛠️ HELPER METHODS
    // ============================================

    private String convertToBase64Image(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return "";
        }
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
    }

    private String convertToBase64Video(byte[] videoData) {
        if (videoData == null || videoData.length == 0) {
            return "";
        }
        return "data:video/mp4;base64," + Base64.getEncoder().encodeToString(videoData);
    }
}
