package com.realtypro.repository;

import com.realtypro.schema.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // Derived query to find all images belonging to a given property
    List<Image> findByProperty_PropertyId(Long propertyId);
    List<Image> findByProperty_PropertyIdAndImageGroup(Long propertyId, String imageGroup);
}
