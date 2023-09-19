package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.ShapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShapeRepository extends JpaRepository<ShapeEntity, String> {
}
