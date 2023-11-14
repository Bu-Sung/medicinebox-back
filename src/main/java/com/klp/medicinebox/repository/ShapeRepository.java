package com.klp.medicinebox.repository;

import com.klp.medicinebox.entity.ShapeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShapeRepository extends JpaRepository<ShapeEntity, String> {


     @Query(value = "SELECT * FROM shape WHERE "
        + "drug_shape LIKE %:shape% AND "
        + "drug_form LIKE %:form% AND "
        + "((:frontLine IS NULL) OR (drug_frontline = :frontLine OR drug_backline = :frontLine)) AND"
        + "(((:frontColor IS NULL) OR (drug_frontcolor LIKE %:frontColor% OR drug_backcolor LIKE %:frontColor%)) AND"
        + " ((:backColor IS NULL) OR (drug_frontcolor LIKE %:backColor% OR drug_backcolor LIKE %:backColor%))) AND"
        + "(((:frontPrint IS NULL) OR (drug_frontprint LIKE %:frontPrint% OR drug_backprint LIKE %:frontPrint%)) AND "
        + " ((:backPrint IS NULL) OR (drug_frontprint LIKE %:backPrint% OR drug_backprint LIKE %:backPrint%)))",
        nativeQuery = true)
    List<ShapeEntity> findSeqByShape(String shape, String form, String frontLine, String frontColor, String backColor, String frontPrint, String backPrint);
    
    @Query(value = "SELECT drug_image FROM shape WHERE drug_seq = :seq", nativeQuery = true)
    String findImageBySeq(String seq);


}
