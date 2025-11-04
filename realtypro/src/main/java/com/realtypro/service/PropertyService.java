package com.realtypro.service;

import com.realtypro.repository.UserRepository;
import com.realtypro.schema.User;
import com.realtypro.schema.Property;
import com.realtypro.repository.PropertyRepository;
import com.realtypro.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    // ➕ CREATE property
    public Property createProperty(Property property) {
        if (property.getUser() == null || property.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User reference is required");
        }

        // Fetch user from DB
        User user = userRepository.findById(property.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + property.getUser().getUserId()));

        // Validate role
        if (user.getRole() != Role.AGENT && user.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("User must be either an AGENT or a MANAGER");
        }

        property.setUser(user);

        return propertyRepository.save(property);
    }

    // 📋 GET all properties
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // 🔍 GET property by ID
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));
    }

    // ✏️ UPDATE property
    public Property updateProperty(Long id, Property updatedProperty) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        // Update basic fields
        existing.setFootfall(updatedProperty.getFootfall());
        existing.setSnackSpend(updatedProperty.getSnackSpend());
        existing.setPropertyType(updatedProperty.getPropertyType());
        existing.setStatus(updatedProperty.getStatus());
        existing.setStoreModel(updatedProperty.getStoreModel());
        existing.setStoreSize(updatedProperty.getStoreSize());
        existing.setStoreDimensionsL(updatedProperty.getStoreDimensionsL());
        existing.setStoreDimensionsW(updatedProperty.getStoreDimensionsW());
        existing.setRoadFacing(updatedProperty.getRoadFacing());
        existing.setEntryDirection(updatedProperty.getEntryDirection());
        existing.setCornerPiece(updatedProperty.getCornerPiece());
        existing.setCornerSide(updatedProperty.getCornerSide());
        existing.setStorePosition(updatedProperty.getStorePosition());
        existing.setShutterL(updatedProperty.getShutterL());
        existing.setShutterW(updatedProperty.getShutterW());
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

        // ✅ Update linked user (instead of agent/manager)
        if (updatedProperty.getUser() != null && updatedProperty.getUser().getUserId() != null) {
            User user = userRepository.findById(updatedProperty.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found with ID: " + updatedProperty.getUser().getUserId()));

            if (user.getRole() != Role.AGENT && user.getRole() != Role.MANAGER) {
                throw new IllegalArgumentException("User must be either an AGENT or a MANAGER");
            }

            existing.setUser(user);
        }

        return propertyRepository.save(existing);
    }

    // ❌ DELETE property
    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new IllegalArgumentException("Property not found with ID: " + id);
        }
        propertyRepository.deleteById(id);
    }
}
