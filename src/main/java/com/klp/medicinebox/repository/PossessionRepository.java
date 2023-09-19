package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.DrugEntity;
import com.klp.medicinebox.entity.PossessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PossessionRepository extends JpaRepository<PossessionEntity, Long> {
}
