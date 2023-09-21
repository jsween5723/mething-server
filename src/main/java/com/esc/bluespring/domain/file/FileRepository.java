package com.esc.bluespring.domain.file;

import com.esc.bluespring.domain.file.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetadata, Long> {

  void deleteByUrl(String url);
  FileMetadata findByUrl(String url);
}
