package com.realtypro.controller;

import com.realtypro.schema.Property;
import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // ➕ Create Property
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(@RequestBody Property property) {
        try {
            // Validate and attach Agent
            if (property.getAgent() == null || property.getAgent().getAgentId() == null) {
                return ResponseEntity.badRequest().body("Agent reference is required");
            }
            Optional<Agent> agentOpt = agentRepository.findById(property.getAgent().getAgentId());
            if (agentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Agent not found with ID: " + property.getAgent().getAgentId());
            }

            // Validate and attach Manager
            if (property.getManager() == null || property.getManager().getManagerId() == null) {
                return ResponseEntity.badRequest().body("Manager reference is required");
            }
            Optional<Manager> managerOpt = managerRepository.findById(property.getManager().getManagerId());
            if (managerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Manager not found with ID: " + property.getManager().getManagerId());
            }

            property.setAgent(agentOpt.get());
            property.setManager(managerOpt.get());

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
        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(properties);
    }

    // 🔍 Get property by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);
        return propertyOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get Property by AgentId
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<Property>> getPropertiesByAgent(@PathVariable Long agentId) {
        List<Property> properties = propertyRepository.findByAgentAgentId(agentId);

        if (properties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        return ResponseEntity.ok(properties);
    }

    // Get latest properties by AgentId (sorted DESC)
    @GetMapping("/agent/{agentId}/latest")
    public ResponseEntity<List<Property>> getLatestPropertiesByAgent(@PathVariable Long agentId) {
        List<Property> properties = propertyRepository.findByAgentAgentIdOrderByPropertyIdDesc(agentId);

        if (properties.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(properties);
    }

    // ✏️ Update property
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {
        Optional<Property> existingOpt = propertyRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property existing = existingOpt.get();

        // Update only fields that are passed (basic example)
        existing.setFootfall(updatedProperty.getFootfall());
        existing.setSnackSpend(updatedProperty.getSnackSpend());
        existing.setPropertyType(updatedProperty.getPropertyType());
        existing.setStatus(updatedProperty.getStatus());
        existing.setStoreModel(updatedProperty.getStoreModel());
        existing.setStoreSize(updatedProperty.getStoreSize());
        existing.setRoadFacing(updatedProperty.getRoadFacing());
        existing.setEntryDirection(updatedProperty.getEntryDirection());
        existing.setCornerPiece(updatedProperty.getCornerPiece());
        existing.setCornerSide(updatedProperty.getCornerSide());
        existing.setStorePosition(updatedProperty.getStorePosition());
        existing.setFrontOffset(updatedProperty.getFrontOffset());
        existing.setSetback(updatedProperty.getSetback());
        existing.setFloor(updatedProperty.getFloor());
        existing.setRentalValue(updatedProperty.getRentalValue());
        existing.setParkingAvailability(updatedProperty.getParkingAvailability());
        existing.setTwoWCapacity(updatedProperty.getTwoWCapacity());
        existing.setFourWCapacity(updatedProperty.getFourWCapacity());
        existing.setOwnerContacted(updatedProperty.getOwnerContacted());
        existing.setWashroom(updatedProperty.getWashroom());
        existing.setElectricity(updatedProperty.getElectricity());
        existing.setGenerator(updatedProperty.getGenerator());
        existing.setBuildingAge(updatedProperty.getBuildingAge());
        existing.setWaterSupply(updatedProperty.getWaterSupply());
        existing.setBuildingCondition(updatedProperty.getBuildingCondition());
        existing.setLandmark(updatedProperty.getLandmark());
        existing.setAboutProperty(updatedProperty.getAboutProperty());
        existing.setNeighbourhoodFacilities(updatedProperty.getNeighbourhoodFacilities());
        existing.setLocationAdvantages(updatedProperty.getLocationAdvantages());

        // Update linked agent/manager (optional)
        if (updatedProperty.getAgent() != null && updatedProperty.getAgent().getAgentId() != null) {
            agentRepository.findById(updatedProperty.getAgent().getAgentId())
                    .ifPresent(existing::setAgent);
        }

        if (updatedProperty.getManager() != null && updatedProperty.getManager().getManagerId() != null) {
            managerRepository.findById(updatedProperty.getManager().getManagerId())
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
        return ResponseEntity.ok("Property deleted successfully");
    }
}
