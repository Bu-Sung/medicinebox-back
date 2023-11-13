package com.klp.medicinebox.service;

import com.klp.medicinebox.dto.DosageDTO;
import com.klp.medicinebox.dto.MixtureDTO;
import com.klp.medicinebox.entity.DosageEntity;
import com.klp.medicinebox.entity.DrugEntity;
import com.klp.medicinebox.repository.DosageRepository;
import com.klp.medicinebox.repository.DrugRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
@RequiredArgsConstructor
public class DosageService {

    private final DosageRepository dosageRepository;
    private final DrugRepository drugRepository;

    /**
     * 자신의 복약 기록을 가져오는 함수
     * @param uid 사용자 아이디
     * @return 사용자 복약 리스트 정보
     * @author 박채빈
     *
     */
    public List<DosageDTO> getDosageList(String uid){
        List<DosageDTO> dosageDTOS = new ArrayList<>();
        // 1. 아이디에 맞는 복용 목록 들고오기
        List<DosageEntity> dosageEntities = dosageRepository.findByUid(uid);
        // 2. DTO로 변환해서 리스트로 반환
        for (DosageEntity dosageEntity : dosageEntities) {
            DrugEntity drugEntity = drugRepository.findByPid(dosageEntity.getPid());
            
            dosageDTOS.add(DosageDTO.builder()
                    .did(dosageEntity.getDid())
                    .userName(dosageEntity.getName())
                    .drugName(dosageEntity.getDrugName())
                    .pid(dosageEntity.getPid())
                    .seq(dosageEntity.getSeq())
                    .date(dosageEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                    .count(dosageEntity.getCount())
                    .build());
            
            }
        return dosageDTOS;
    }
  

    /**
     * 복약 정보를 가져오는 함수(보여줄께 딱히 없어서 삭제 고려 중 가장 나중에 구현)
     * @param did 복약 아이디
     * @return 복약 정보 DTO
     * @author 박채빈
     *
     */
    public DosageDTO getDosageInfo(Long did, String uid){
        // 1. 복용 목록에 맞는 아이디 정보 불러와서 반환
        DosageEntity dosageEntity = dosageRepository.findByDid(did);
        
        DrugEntity drugEntity = drugRepository.findByPid(dosageEntity.getPid());
        
        DosageDTO dosageDTO = DosageDTO.builder()
                .did(dosageEntity.getDid())
                .userName(dosageEntity.getName())
                .drugName(dosageEntity.getDrugName())
                .pid(dosageEntity.getPid())
                .seq(dosageEntity.getSeq())
                .date(dosageEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                .count(dosageEntity.getCount())
                .build();
        
        return dosageDTO;
    }

    
    /**
     * 복약 정보를 기록하는 함수
     * @param dosageDTO 복약 정보
     * @param uid 사용자 아이디
     * @return 저장의 결과
     * @author 박채빈
     *
     */
    public boolean addDosage(DosageDTO dosageDTO, String uid){
        // 1. 복용 정보와 아이디를 사용해 Entity로 변형
        DosageEntity dosageEntity = DosageEntity.builder()
                .uid(uid)
                .name(dosageDTO.getUserName())
                .pid(dosageDTO.getPid())
                .seq(dosageDTO.getSeq())
                .date(LocalDateTime.parse(dosageDTO.getDate()))
                .count(dosageDTO.getCount()) 
                .drugName(dosageDTO.getDrugName())
                .build();
        
        
        
        // 복용시 drug 약 갯수 차감 
        if(dosageRepository.save(dosageEntity) != null) {
            DrugEntity drugEntity = drugRepository.findByPid(dosageDTO.getPid());
            if (drugEntity != null) {
                drugEntity.setCount(drugEntity.getCount() - dosageDTO.getCount());
                try{
                    drugRepository.save(drugEntity);
                } catch(Exception e) {
                    return false;
                }
            }
        }   
        
        // 2. 저장 후 결과 반환
        return dosageRepository.save(dosageEntity) != null;
    }
    

    
    /**
     * 입력값에 대한 병용 금기 리스트 검색 
     * 
     * @param type 1 : 제품 기준 코드 검색, 2 : 제품 이름 검색 
     * @param search  
     * @return 검색된 리스트 반환 
     */
    public List<MixtureDTO> searchProhibitList(int type, String search) {
        List<MixtureDTO> mixtureDTOS = new ArrayList<>();
        
        try {
            // URL 생성
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1471000/DURPrdlstInfoService03/getUsjntTabooInfoList03");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=sDtf81qz1ObM5WdrhmQaFgaa9zJ1%2BlOhV1%2B%2FAoKwlPHXITobuVPCQicwzsOSKp7UR%2BQyctLfoGDQ8aMjVjM%2FxQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));

            if (type == 1) {  // 제품 기준 코드로 검색  
                urlBuilder.append("&" + URLEncoder.encode("itemSeq", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            } else if (type == 2) {  // 제품 이름으로 검색 
                urlBuilder.append("&" + URLEncoder.encode("itemName", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            }

            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));

            // HTTP 요청 및 응답 처리
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder xmlResponse = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                xmlResponse.append(line);
            }
            rd.close();
            conn.disconnect();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlResponse.toString()));
            Document document = builder.parse(is);

            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Element itemElement = (Element) itemList.item(i);
                MixtureDTO mixtureDTO = new MixtureDTO();

                mixtureDTO.setDrugSeq(itemElement.getElementsByTagName("ITEM_SEQ").item(0).getTextContent());
                mixtureDTO.setDrugName(itemElement.getElementsByTagName("ITEM_NAME").item(0).getTextContent());
                mixtureDTO.setDrugEntp(itemElement.getElementsByTagName("ENTP_NAME").item(0).getTextContent());
                mixtureDTO.setMixtureSeq(itemElement.getElementsByTagName("MIXTURE_ITEM_SEQ").item(0).getTextContent());
                mixtureDTO.setMixtureName(itemElement.getElementsByTagName("MIXTURE_ITEM_NAME").item(0).getTextContent());
                mixtureDTO.setMixtureEntp(itemElement.getElementsByTagName("MIXTURE_ENTP_NAME").item(0).getTextContent());
                mixtureDTO.setProhibitContent(itemElement.getElementsByTagName("PROHBT_CONTENT").item(0).getTextContent());

                mixtureDTOS.add(mixtureDTO); 
            }

        } catch (Exception ex) {
            Logger.getLogger(DosageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mixtureDTOS; 
    }
    

    /**
     * 해당 날짜의 복용한 복약 리스트 
     * 
     * @param uid 사용자 아이디 
     * @return 
     */
    public List<DosageDTO> getTodaysDosageList(DosageDTO dosageDTO, String uid){
        List<DosageDTO> dosageDTOS = new ArrayList<>();

        List<DosageEntity> dosageEntities = dosageRepository.findByUid(uid);

        for (DosageEntity dosageEntity : dosageEntities) {
            DrugEntity drugEntity = drugRepository.findByPid(dosageEntity.getPid());
            String dosageDate = dosageEntity.getDate().toLocalDate().toString(); // 복용한 날짜

            // 해당 날짜의 복용한 기록만 가져오기
            if (dosageDate.equals(dosageDTO.getDate().substring(0, 10))) {
                dosageDTOS.add(DosageDTO.builder()
                        .did(dosageEntity.getDid())
                        .userName(dosageEntity.getName())
                        .drugName(drugEntity.getName())
                        .pid(dosageEntity.getPid())
                        .seq(dosageEntity.getSeq())
                        .date(dosageEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                        .count(dosageEntity.getCount())
                        .build());
            }
        }

        return dosageDTOS;
    }

    
    /**
     * 해당 날짜(dosageDTO.date)의 복용한 약들과 현재 복용할 약에 대해 병용 금기인 약품이 존재하는지 확인 
     * 
     * @param dosageDTO 복약 정보
     * @param uid 사용자 아이디 
     * @return 병용 금기 리스트 
     */
    public List<MixtureDTO> getProhibitList(DosageDTO dosageDTO, String uid) {

        // 현재 복용할 약에 대한 병용 금기 리스트 
        List<MixtureDTO> prohibitList = searchProhibitList(1, dosageDTO.getSeq());

        if (prohibitList == null || prohibitList.isEmpty()) {  // 병용 금기 리스트가 없는 경우 
            return new ArrayList<>();  // 빈 목록 반환
        } else {  // 병용 금기 리스트가 있는 경우 
            List<DosageDTO> todaysDosageDTOS = getTodaysDosageList(dosageDTO, uid);  // 해당 날짜의 복용한 약 리스트 
            List<MixtureDTO> mixtureDTOS = new ArrayList<>();

            // 오늘 복용한 약 리스트 중 병용 금기 리스트에 해당하는 제품이 있는지 확인 
            for (DosageDTO todayDosageDTO : todaysDosageDTOS) {
                for (MixtureDTO mixtureDTO : prohibitList) {
                    if (mixtureDTO.getMixtureSeq().equals(todayDosageDTO.getSeq())) {
                        mixtureDTO.setDid(todayDosageDTO.getDid());
                        mixtureDTOS.add(mixtureDTO);
                    }
                }
            }

            return mixtureDTOS;
        }
    }

    
}
