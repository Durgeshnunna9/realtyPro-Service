package com.realtypro.repository;

import com.realtypro.schema.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByPropertyPropertyId(Long propertyId);
}

