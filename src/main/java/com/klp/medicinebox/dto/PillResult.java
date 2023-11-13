package com.klp.medicinebox.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PillResult {

 
    @JsonIgnore
    private Long index;         // 테이블 인덱스

    @JsonIgnore

    private long piIndex;

    private short rank;

    private String code;

    private Double accuracy;

    private String image_link;

    private String name;

    private String cls_name;

    private String href;

}
