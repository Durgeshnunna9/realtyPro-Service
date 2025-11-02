package com.realtypro.schema;

import jakarta.persistence.*;

@Entity
@Table(name="properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @ManyToOne
    @JoinColumn(name="agent_id", nullable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name="manager", nullable = false)
    private Manager manager;

    @Column(name="footfall", nullable = false)
    private int footfall;

    @Column(name="snack_spend", nullable = false)
    private int snackSpend;

    @Column(name="property_type", nullable = false)
    private String propertyType;

    @Column(name = "store_model", columnDefinition = "TEXT")
    private String storeModel;

    @Column(name = "store_size")
    private Integer storeSize;

    @Column(name = "store_dimensions_l")
    private Integer storeDimensionsL;

    @Column(name = "store_dimensions_w")
    private Integer storeDimensionsW;

    @Column(name = "road_facing", columnDefinition = "TEXT")
    private String roadFacing;

    @Column(name = "entry_direction", columnDefinition = "TEXT")
    private String entryDirection;

    @Column(name = "corner_piece", columnDefinition = "TEXT")
    private String cornerPiece;

    @Column(name = "corner_side", columnDefinition = "TEXT")
    private String cornerSide;

    @Column(name = "store_position", columnDefinition = "TEXT")
    private String storePosition;

    @Column(name = "shutter_l")
    private Integer shutterL;

    @Column(name = "shutter_w")
    private Integer shutterW;

    @Column(name = "front_offset")
    private Integer frontOffset;

    @Column(name = "setback", columnDefinition = "TEXT")
    private String setback;

    @Column(name = "floor", columnDefinition = "TEXT")
    private String floor;

    @Column(name = "rental_value", columnDefinition = "TEXT")
    private String rentalValue;

    @Column(name = "parking_availability", columnDefinition = "TEXT")
    private String parkingAvailability;

    @Column(name = "two_w_capacity", columnDefinition = "TEXT")
    private String twoWCapacity;

    @Column(name = "four_w_capacity", columnDefinition = "TEXT")
    private String fourWCapacity;

    @Column(name = "owner_contacted", columnDefinition = "TEXT")
    private String ownerContacted;

    @Column(name = "washroom", columnDefinition = "TEXT")
    private String washroom;

    @Column(name = "electricity", columnDefinition = "TEXT")
    private String electricity;

    @Column(name = "generator", columnDefinition = "TEXT")
    private String generator;

    @Column(name = "building_age", columnDefinition = "TEXT")
    private String buildingAge;

    @Column(name = "water_supply", columnDefinition = "TEXT")
    private String waterSupply;

    @Column(name = "building_condition", columnDefinition = "TEXT")
    private String buildingCondition;

    @Column(name = "landmark", columnDefinition = "TEXT")
    private String landmark;

    @Column(name = "about_property", columnDefinition = "TEXT")
    private String aboutProperty;

    @Column(name = "neighbourhood_facilities", columnDefinition = "TEXT")
    private String neighbourhoodFacilities;

    @Column(name = "location_advantages", columnDefinition = "TEXT")
    private String locationAdvantages;

    @Column(name="status", nullable = false)
    private String status;

    // Constructors
    public Property() {}

    // Getters and Setters
    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public int getFootfall() {
        return footfall;
    }

    public void setFootfall(int footfall) {
        this.footfall = footfall;
    }

    public int getSnackSpend() {
        return snackSpend;
    }

    public void setSnackSpend(int snackSpend) {
        this.snackSpend = snackSpend;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getStoreModel() { return storeModel; }
    public void setStoreModel(String storeModel) { this.storeModel = storeModel; }

    public Integer getStoreSize() { return storeSize; }
    public void setStoreSize(Integer storeSize) { this.storeSize = storeSize; }

    public Integer getStoreDimensionsL() { return storeDimensionsL; }
    public void setStoreDimensionsL(Integer storeDimensionsL) { this.storeDimensionsL = storeDimensionsL; }

    public Integer getStoreDimensionsW() { return storeDimensionsW; }
    public void setStoreDimensionsW(Integer storeDimensionsW) { this.storeDimensionsW = storeDimensionsW; }

    public String getRoadFacing() { return roadFacing; }
    public void setRoadFacing(String roadFacing) { this.roadFacing = roadFacing; }

    public String getEntryDirection() { return entryDirection; }
    public void setEntryDirection(String entryDirection) { this.entryDirection = entryDirection; }

    public String getCornerPiece() { return cornerPiece; }
    public void setCornerPiece(String cornerPiece) { this.cornerPiece = cornerPiece; }

    public String getCornerSide() { return cornerSide; }
    public void setCornerSide(String cornerSide) { this.cornerSide = cornerSide; }

    public String getStorePosition() { return storePosition; }
    public void setStorePosition(String storePosition) { this.storePosition = storePosition; }

    public Integer getShutterL() { return shutterL; }
    public void setShutterL(Integer shutterL) { this.shutterL = shutterL; }

    public Integer getShutterW() { return shutterW; }
    public void setShutterW(Integer shutterW) { this.shutterW = shutterW; }

    public Integer getFrontOffset() { return frontOffset; }
    public void setFrontOffset(Integer frontOffset) { this.frontOffset = frontOffset; }

    public String getSetback() { return setback; }
    public void setSetback(String setback) { this.setback = setback; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getRentalValue() { return rentalValue; }
    public void setRentalValue(String rentalValue) { this.rentalValue = rentalValue; }

    public String getParkingAvailability() { return parkingAvailability; }
    public void setParkingAvailability(String parkingAvailability) { this.parkingAvailability = parkingAvailability; }

    public String getTwoWCapacity() { return twoWCapacity; }
    public void setTwoWCapacity(String twoWCapacity) { this.twoWCapacity = twoWCapacity; }

    public String getFourWCapacity() { return fourWCapacity; }
    public void setFourWCapacity(String fourWCapacity) { this.fourWCapacity = fourWCapacity; }

    public String getOwnerContacted() { return ownerContacted; }
    public void setOwnerContacted(String ownerContacted) { this.ownerContacted = ownerContacted; }

    public String getWashroom() { return washroom; }
    public void setWashroom(String washroom) { this.washroom = washroom; }

    public String getElectricity() { return electricity; }
    public void setElectricity(String electricity) { this.electricity = electricity; }

    public String getGenerator() { return generator; }
    public void setGenerator(String generator) { this.generator = generator; }

    public String getBuildingAge() { return buildingAge; }
    public void setBuildingAge(String buildingAge) { this.buildingAge = buildingAge; }

    public String getWaterSupply() { return waterSupply; }
    public void setWaterSupply(String waterSupply) { this.waterSupply = waterSupply; }

    public String getBuildingCondition() { return buildingCondition; }
    public void setBuildingCondition(String buildingCondition) { this.buildingCondition = buildingCondition; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getAboutProperty() { return aboutProperty; }
    public void setAboutProperty(String aboutProperty) { this.aboutProperty = aboutProperty; }

    public String getNeighbourhoodFacilities() { return neighbourhoodFacilities; }
    public void setNeighbourhoodFacilities(String neighbourhoodFacilities) { this.neighbourhoodFacilities = neighbourhoodFacilities; }

    public String getLocationAdvantages() { return locationAdvantages; }
    public void setLocationAdvantages(String locationAdvantages) { this.locationAdvantages = locationAdvantages; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
