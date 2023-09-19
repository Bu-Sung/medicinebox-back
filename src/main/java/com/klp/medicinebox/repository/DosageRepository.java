package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.DosageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DosageRepository extends JpaRepository<DosageEntity, Long> {
}
