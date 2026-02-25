package com.theology.semantic.infrastructure.adapters.persistence.repository;

import com.theology.semantic.infrastructure.adapters.persistence.entity.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataDocumentoRepository extends JpaRepository<DocumentoEntity, UUID> {
}
