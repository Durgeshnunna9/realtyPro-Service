package com.realtypro.repository;

import com.realtypro.schema.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByProperty_PropertyId(Long propertyId);
}

