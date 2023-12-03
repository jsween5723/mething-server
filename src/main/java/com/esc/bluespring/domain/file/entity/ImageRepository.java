package com.esc.bluespring.domain.file.entity;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    Image findByUrl(String url);

}