package com.realtypro.controller;

import com.realtypro.repository.UserRepository;
import com.realtypro.schema.Property;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.schema.User;
import com.realtypro.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    // ➕ Create Property
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(@RequestBody Property property) {
        try {
            // Validate and attach Agent
            if (property.getUser() == null || property.getUser().getUserId() == null) {
                return ResponseEntity.badRequest().body("Agent reference is required");
            }

            User agent = userRepository.findById(property.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + property.getUser().getUserId()));

            if (agent.getRole() != Role.AGENT) {
                return ResponseEntity.badRequest().body("User with ID " + agent.getUserId() + " is not an AGENT");
            }

            // Validate and attach Manager
            if (property.getManager() == null || property.getManager().getUserId() == null) {
                return ResponseEntity.badRequest().body("Manager reference is required");
            }

            User manager = userRepository.findById(property.getManager().getUserId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + property.getManager().getUserId()));

            if (manager.getRole() != Role.MANAGER) {
                return ResponseEntity.badRequest().body("User with ID " + manager.getUserId() + " is not a MANAGER");
            }

            // Set validated users
            property.setUser(agent);
            property.setManager(manager);

            Property savedProperty = propertyRepository.save(property);
            return ResponseEntity.ok(savedProperty);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving property: " + e.getMessage());
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

    // 🧭 Get properties by Agent (User)
    @GetMapping("/agent/{userId}")
    public ResponseEntity<List<Property>> getPropertiesByAgent(@PathVariable Long userId) {
        List<Property> properties = propertyRepository.findByAgentUserId(userId);
        return properties.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(properties);
    }

    // 🧭 Get properties by Manager (User)
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
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property existing = existingOpt.get();

        // ✅ Update only non-null fields from updatedProperty
        if (updatedProperty.getFootfall() != 0) existing.setFootfall(updatedProperty.getFootfall());
        if (updatedProperty.getSnackSpend() != 0) existing.setSnackSpend(updatedProperty.getSnackSpend());
        if (updatedProperty.getPropertyType() != null) existing.setPropertyType(updatedProperty.getPropertyType());
        if (updatedProperty.getStatus() != null) existing.setStatus(updatedProperty.getStatus());
        if (updatedProperty.getStoreModel() != null) existing.setStoreModel(updatedProperty.getStoreModel());
        if (updatedProperty.getStoreSize() != 0) existing.setStoreSize(updatedProperty.getStoreSize());
        if (updatedProperty.getStoreDimensionsL() != 0) existing.setStoreDimensionsL(updatedProperty.getStoreDimensionsL());
        if (updatedProperty.getStoreDimensionsW() != 0) existing.setStoreDimensionsW(updatedProperty.getStoreDimensionsW());
        if (updatedProperty.getRoadFacing() != null) existing.setRoadFacing(updatedProperty.getRoadFacing());
        if (updatedProperty.getEntryDirection() != null) existing.setEntryDirection(updatedProperty.getEntryDirection());
        if (updatedProperty.getCornerPiece() != null) existing.setCornerPiece(updatedProperty.getCornerPiece());
        if (updatedProperty.getCornerSide() != null) existing.setCornerSide(updatedProperty.getCornerSide());
        if (updatedProperty.getShutterL() != 0) existing.setShutterL(updatedProperty.getShutterL());
        if (updatedProperty.getShutterW() != 0) existing.setShutterW(updatedProperty.getShutterW());
        if (updatedProperty.getStorePosition() != null) existing.setStorePosition(updatedProperty.getStorePosition());
        if (updatedProperty.getFrontOffset() != 0) existing.setFrontOffset(updatedProperty.getFrontOffset());
        if (updatedProperty.getSetback() != null) existing.setSetback(updatedProperty.getSetback());
        if (updatedProperty.getFloor() != null) existing.setFloor(updatedProperty.getFloor());
        if (updatedProperty.getRentalValue() != 0) existing.setRentalValue(updatedProperty.getRentalValue());
        if (updatedProperty.getParkingAvailability() != null) existing.setParkingAvailability(updatedProperty.getParkingAvailability());
        if (updatedProperty.getTwoWCapacity() != 0) existing.setTwoWCapacity(updatedProperty.getTwoWCapacity());
        if (updatedProperty.getFourWCapacity() != 0) existing.setFourWCapacity(updatedProperty.getFourWCapacity());
        if (updatedProperty.getOwnerContacted() != null) existing.setOwnerContacted(updatedProperty.getOwnerContacted());
        if (updatedProperty.getWashroom() != null) existing.setWashroom(updatedProperty.getWashroom());
        if (updatedProperty.getElectricity() != null) existing.setElectricity(updatedProperty.getElectricity());
        if (updatedProperty.getGenerator() != null) existing.setGenerator(updatedProperty.getGenerator());
        if (updatedProperty.getBuildingAge() != null) existing.setBuildingAge(updatedProperty.getBuildingAge());
        if (updatedProperty.getWaterSupply() != null) existing.setWaterSupply(updatedProperty.getWaterSupply());
        if (updatedProperty.getBuildingCondition() != null) existing.setBuildingCondition(updatedProperty.getBuildingCondition());
        if (updatedProperty.getLandmark() != null) existing.setLandmark(updatedProperty.getLandmark());
        if (updatedProperty.getAboutProperty() != null) existing.setAboutProperty(updatedProperty.getAboutProperty());
        if (updatedProperty.getNeighbourhoodFacilities() != null) existing.setNeighbourhoodFacilities(updatedProperty.getNeighbourhoodFacilities());
        if (updatedProperty.getLocationAdvantages() != null) existing.setLocationAdvantages(updatedProperty.getLocationAdvantages());

        // ✅ Update linked users (agent & manager)
        if (updatedProperty.getUser() != null && updatedProperty.getUser().getUserId() != null) {
            userRepository.findById(updatedProperty.getUser().getUserId())
                    .ifPresent(existing::setUser);
        }

        if (updatedProperty.getManager() != null && updatedProperty.getManager().getUserId() != null) {
            userRepository.findById(updatedProperty.getManager().getUserId())
                    .ifPresent(existing::setManager);
        }

        Property saved = propertyRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ❌ Delete property
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        propertyRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Property deleted successfully", "propertyId", id));
    }
}
