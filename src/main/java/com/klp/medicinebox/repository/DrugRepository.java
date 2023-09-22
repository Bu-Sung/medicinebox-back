package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.DrugEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<DrugEntity, Long> {
}
