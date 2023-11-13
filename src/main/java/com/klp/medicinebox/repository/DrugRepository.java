package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.DrugEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<DrugEntity, Long> {
    
    @Query(value = "SELECT * FROM drug WHERE user_id = :uid ORDER BY possession_register ASC", nativeQuery = true)
    List<DrugEntity> findByUidOrderByRegisterDate(@Param("uid") String uid);
            
    @Query(value = "SELECT * FROM drug WHERE user_id = :uid ORDER BY possession_update DESC", nativeQuery = true)
    List<DrugEntity> findByUidOrderByUpdateDate(@Param("uid") String uid);
    
    @Query(value = "SELECT * FROM drug WHERE user_id = :uid AND drug_seq = :seq", nativeQuery = true)
    DrugEntity findByUidAndSeq(@Param("seq") String seq,@Param("uid") String uid);
    
    @Query(value = "SELECT * FROM drug WHERE user_id = :uid AND possession_id = :pid", nativeQuery = true)
    DrugEntity findByUidAndPid(Long pid, String uid);
    
    DrugEntity findByPid(Long pid);
    
    List<DrugEntity> findByUid(String uid);
}
