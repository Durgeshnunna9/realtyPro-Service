package com.realtypro.utilities;

public enum PropertyStatus {
    PENDING("Pending"),
    SITE_VISIT_DONE("Site Visit Done"),
    OWNER_DISCUSSION_COMPLETED("Owner Discussion Completed"),
    PENDING_REVIEW("Pending Review"),
    TERMS_NOT_AGREED("Terms Not Agreed"),
    DEPOSIT_HIGH("Deposit High"),
    PROPERTY_ON_LEASE("Property on Lease"),
    PROPERTY_ON_RENT("Property on Rent"),
    PROPERTY_ON_SALE("Property on Sale"),
    REJECTED("Rejected");

    private final String displayName;

    PropertyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
