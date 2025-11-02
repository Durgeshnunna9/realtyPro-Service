package com.realtypro.service;

import com.realtypro.schema.Agent;
import com.realtypro.schema.Manager;
import com.realtypro.schema.Property;
import com.realtypro.repository.AgentRepository;
import com.realtypro.repository.ManagerRepository;
import com.realtypro.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // ➕ CREATE property
    public Property createProperty(Property property) {
        if (property.getAgent() == null || property.getAgent().getAgentId() == null) {
            throw new IllegalArgumentException("Agent reference is required");
        }

        if (property.getManager() == null || property.getManager().getManagerId() == null) {
            throw new IllegalArgumentException("Manager reference is required");
        }

        Agent agent = agentRepository.findById(property.getAgent().getAgentId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Agent not found with ID: " + property.getAgent().getAgentId()));

        Manager manager = managerRepository.findById(property.getManager().getManagerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Manager not found with ID: " + property.getManager().getManagerId()));

        property.setAgent(agent);
        property.setManager(manager);

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

        // Update linked agent
        if (updatedProperty.getAgent() != null && updatedProperty.getAgent().getAgentId() != null) {
            Agent agent = agentRepository.findById(updatedProperty.getAgent().getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Agent not found with ID: " + updatedProperty.getAgent().getAgentId()));
            existing.setAgent(agent);
        }

        // Update linked manager
        if (updatedProperty.getManager() != null && updatedProperty.getManager().getManagerId() != null) {
            Manager manager = managerRepository.findById(updatedProperty.getManager().getManagerId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Manager not found with ID: " + updatedProperty.getManager().getManagerId()));
            existing.setManager(manager);
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
