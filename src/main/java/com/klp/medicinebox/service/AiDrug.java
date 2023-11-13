/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.klp.medicinebox.service;

import com.klp.medicinebox.dto.Metadata;
import com.klp.medicinebox.dto.PillResult;
import com.klp.medicinebox.dto.RequestJson;
import com.klp.medicinebox.dto.ResponseJson;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author qntjd
 */
@Service
public class AiDrug {
        
    @Autowired
    private ServletContext ctx;
    
    @Autowired
    private MainService mainService;
    
    @Value("${aiDrug.folder}")
    private String aiUploadFolderPath;
    
    public List<String> getAiDrugName(RequestJson request){
        StopWatch stopWatch = new StopWatch();
        ResponseJson<PillResult> reponse = new ResponseJson<>();
        List<PillResult> result = null;
        int i;

        String shape = request.getShape();
        List<MultipartFile> image = request.getImage();
        List image_hash = request.getImage_hash();

        List file_path = new ArrayList();
        StringBuilder fileName;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();

        // 파라미터 검사
        shape = shape.trim().toLowerCase();

        if (!(shape.equals("circle")
                || shape.equals("ellipse")
                || shape.equals("diamond")
                || shape.equals("triangle")
                || shape.equals("rectangle")
                || shape.equals("square")
                || shape.equals("pentagon")
                || shape.equals("hexagon")
                || shape.equals("octagon")
                || shape.equals("etc"))) {
            
            return null;
        }
        i = 0;
        for (MultipartFile multipartFile : image) {
            stopWatch.start();

            fileName = new StringBuilder();
            fileName.append(image_hash.get(0));
            fileName.append("_");
            fileName.append(shape);
            fileName.append("_");
            fileName.append(dateFormat.format(now));
            fileName.append("_");
            fileName.append(i + 1);
            fileName.append(".jpg");

            i++;
            /* 이미지 숫자 증가 */

            file_path.add(fileName.toString());
            /* 메타데이터 파일 경로 저장 */

 /* 이미지 파일 업로드 */
            try {
                if (mainService.upload(multipartFile.getBytes(), aiUploadFolderPath + fileName.toString()) == false) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
            stopWatch.stop();
        }

        // 메타데이터 파일 생성
        // 1. 메타데이터 추출
        // 2. 메타데이터 저장
        stopWatch.start();
        Metadata metadata = new Metadata();
        metadata.setShape(shape);
        if (request.getB_text().length() != 0) {
            metadata.setB_text(request.getB_text());
        }

        if (request.getF_text().length() != 0) {
            metadata.setF_text(request.getF_text());
        }

        if (request.getDrug_code() != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            i = 0;
            for (Object s : request.getDrug_code()) {
                if (i != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(s.toString());
                i++;
            }
            stringBuffer.append("]");
            metadata.setDrug_code(stringBuffer.toString());
        }

        fileName = new StringBuilder();
        fileName.append(image_hash.get(0));
        fileName.append("_");
        fileName.append(shape);
        fileName.append("_");
        fileName.append(dateFormat.format(now));
        fileName.append(".txt");

        file_path.add(fileName.toString());
        /* 메타데이터 파일 경로 저장 */

        if (mainService.save(metadata.toString(), aiUploadFolderPath + fileName.toString()) == false) {
            return null;
        }
        stopWatch.stop();

        // 명령어 실행
        // 1. 명령어 실행
        // 2. 명령어 결과 반환
        stopWatch.start();
        StringBuffer command = new StringBuffer();
        String jsonData;
        command.append("python");
        command.append(" ");
        command.append("C:/Users/qntjd/Desktop/newfill/drugfill_predict_source_code/predict/PillMain.py");
        command.append(" ");
        command.append(ctx.getRealPath(aiUploadFolderPath + file_path.get(0)).replace("\\", "/"));
        System.out.println(ctx.getRealPath(aiUploadFolderPath + file_path.get(0)).replace("\\", "/"));
        command.append(" ");
        command.append(ctx.getRealPath(aiUploadFolderPath + file_path.get(1)).replace("\\", "/"));
        System.out.println(ctx.getRealPath(aiUploadFolderPath + file_path.get(0)).replace("\\", "/"));
        command.append(" ");
        command.append(ctx.getRealPath(aiUploadFolderPath + file_path.get(2)).replace("\\", "/"));
        System.out.println(ctx.getRealPath(aiUploadFolderPath + file_path.get(0)).replace("\\", "/"));
        jsonData = mainService.execute(command.toString());
        stopWatch.stop();
        stopWatch.start();
        try {
            result = mainService.convert(jsonData);
        } catch (IOException | NullPointerException e) {
            System.out.println("JSON 형 변환을 실패하였습니다. command= " + command.toString() + ",command_result=" + jsonData);
        }
        stopWatch.stop();



        HashMap<String, String> map = new HashMap<>();
        map.put("41344", "토바스트정10mg"); //1
        map.put("41327", "동성라베프라졸정20mg");//2
        map.put("41225", "티뮤즈연질캡슐");//3
        map.put("41207", "브이타민정");//4
        map.put("41172", "레비에필정1000mg");//5
        map.put("41170", "레비에필정250mg");//6
        map.put("41169", "라자렉트정");//7
        map.put("41107", "아나콕스캡슐100mg");//8
        map.put("41097", "경동파니틴정20mg");//9
        map.put("40991", "세바코에이치씨티정10/40/12.5mg");//10
        map.put("40990", "세바코에이치씨티정5/40/12.5mg");//11
        map.put("40953", "카나가바로틴캡슐300mg");//12
        map.put("40949", "아펜탈CR서방정");//13
        map.put("40837", "듀오스타정40/5m");//14
        map.put("40792", "디아셀캡슐50mg");//15
        map.put("40767", "게스타렌투엑스정");//16
        map.put("40720", "뉴글리아정");//17
        map.put("40122", "유니카민정");//18
        map.put("39916", "유니테리드정5mg");//19
        map.put("37990", "멀티큐텐플러스정");//20
        map.put("34342", "웰러드연질캡슐");//21
        map.put("29002", "오피큐탄연질캡슐");//22
        
        List<String> drugNameList = new ArrayList();
        for (PillResult p : result) {
            if (map.containsKey(p.getCode())) {
                drugNameList.add(map.get(p.getCode()));
            }
        }
        return drugNameList; 
    }
    
    
}
