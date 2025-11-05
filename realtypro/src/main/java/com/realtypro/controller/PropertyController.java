package com.realtypro.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    // ➕ Create Property with Images & Video
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(
            @RequestParam Long agentId,
            @RequestParam Long managerId,
            @RequestParam Integer footfall,
            @RequestParam Integer snackSpend,
            @RequestParam String propertyType,
            @RequestParam String storeModel,
            @RequestParam Integer storeSize,
            @RequestParam Integer storeDimensionsL,
            @RequestParam Integer storeDimensionsW,
            @RequestParam String roadFacing,
            @RequestParam String entryDirection,
            @RequestParam String cornerPiece,
            @RequestParam String cornerSide,
            @RequestParam String storePosition,
            @RequestParam Integer shutterL,
            @RequestParam Integer shutterW,
            @RequestParam Integer frontOffset,
            @RequestParam String setback,
            @RequestParam String floor,
            @RequestParam Integer rentalValue,
            @RequestParam String parkingAvailability,
            @RequestParam Integer twoWCapacity,
            @RequestParam Integer fourWCapacity,
            @RequestParam Boolean ownerContacted,
            @RequestParam Boolean washroom,
            @RequestParam Boolean electricity,
            @RequestParam Boolean generator,
            @RequestParam String buildingAge,
            @RequestParam Boolean waterSupply,
            @RequestParam String buildingCondition,
            @RequestParam String landmark,
            @RequestParam String aboutProperty,
            @RequestParam String neighbourhoodFacilities,
            @RequestParam String locationAdvantages,
            @RequestParam String status,
            @RequestParam(required = false, value = "property_photos") List<MultipartFile> propertyPhotos,
            @RequestParam(required = false, value = "parking_photos") List<MultipartFile> parkingPhotos,
            @RequestParam(required = false, value = "video") MultipartFile video
    ) throws IOException {

        try {
            // Validate file limits
            if (propertyPhotos != null && propertyPhotos.size() > 5) {
                return ResponseEntity.badRequest().body("Maximum 5 property photos allowed");
            }
            if (parkingPhotos != null && parkingPhotos.size() > 5) {
                return ResponseEntity.badRequest().body("Maximum 5 parking photos allowed");
            }

            // Validate agent and manager
            User agent = userRepository.findById(agentId)
                    .orElseThrow(() -> new RuntimeException("Agent not found"));
            if (agent.getRole() != Role.AGENT)
                return ResponseEntity.badRequest().body("User is not an AGENT");

            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            if (manager.getRole() != Role.MANAGER)
                return ResponseEntity.badRequest().body("User is not a MANAGER");

            // Create property
            Property property = new Property();
            property.setAgent(agent);
            property.setManager(manager);
            property.setFootfall(footfall);
            property.setSnackSpend(snackSpend);
            property.setPropertyType(propertyType);
            property.setStoreModel(storeModel);
            property.setStoreSize(storeSize);
            property.setStoreDimensionsL(storeDimensionsL);
            property.setStoreDimensionsW(storeDimensionsW);
            property.setRoadFacing(roadFacing);
            property.setEntryDirection(entryDirection);
            property.setCornerPiece(cornerPiece);
            property.setCornerSide(cornerSide);
            property.setStorePosition(storePosition);
            property.setShutterL(shutterL);
            property.setShutterW(shutterW);
            property.setFrontOffset(frontOffset);
            property.setSetback(setback);
            property.setFloor(floor);
            property.setRentalValue(rentalValue);
            property.setParkingAvailability(parkingAvailability);
            property.setTwoWCapacity(twoWCapacity);
            property.setFourWCapacity(fourWCapacity);
            property.setOwnerContacted(ownerContacted);
            property.setWashroom(washroom);
            property.setElectricity(electricity);
            property.setGenerator(generator);
            property.setBuildingAge(buildingAge);
            property.setWaterSupply(waterSupply);
            property.setBuildingCondition(buildingCondition);
            property.setLandmark(landmark);
            property.setAboutProperty(aboutProperty);
            property.setNeighbourhoodFacilities(neighbourhoodFacilities);
            property.setLocationAdvantages(locationAdvantages);
            property.setStatus(status);

            propertyRepository.save(property);

            // Save property photos
            if (propertyPhotos != null) {
                for (MultipartFile file : propertyPhotos) {
                    Image img = new Image();
                    img.setProperty(property);
                    img.setImageGroup("property_photos");
                    img.setImageUrl(saveFile(file));
                    imageRepository.save(img);
                }
            }

            // Save parking photos
            if (parkingPhotos != null) {
                for (MultipartFile file : parkingPhotos) {
                    Image img = new Image();
                    img.setProperty(property);
                    img.setImageGroup("parking_photos");
                    img.setImageUrl(saveFile(file));
                    imageRepository.save(img);
                }
            }

            // Save video (max 1)
            if (video != null) {
                Video vid = new Video();
                vid.setProperty(property);
                vid.setVideoUrl(saveFile(video));
                videoRepository.save(vid);
            }

            return ResponseEntity.ok(property);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating property: " + e.getMessage());
        }
    }

    // Helper to save files locally
    private String saveFile(MultipartFile file) throws IOException {
        String folder = "uploads/";
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(folder + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return "/uploads/" + filename;
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

    // 🧭 Get properties by Agent
    @GetMapping("/agent/{userId}")
    public ResponseEntity<List<Property>> getPropertiesByAgent(@PathVariable Long userId) {
        List<Property> properties = propertyRepository.findByAgentUserId(userId);
        return properties.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(properties);
    }

    // 🧭 Get properties by Manager
    @GetMapping("/manager/{userId}")
    public ResponseEntity<List<Property>> getPropertiesByManager(@PathVariable Long userId) {
        List<Property> properties = propertyRepository.findByManagerUserId(userId);
        return properties.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(properties);
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
}
