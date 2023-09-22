package com.klp.medicinebox.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.dto.ShapeDTO;
import com.klp.medicinebox.entity.DrugEntity;
import com.klp.medicinebox.repository.DrugRepository;
import com.klp.medicinebox.repository.PossessionRepository;
import com.klp.medicinebox.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;
    private final ShapeRepository shapeRepository;
    private final PossessionRepository possessionRepository;

    /**
     * 제품 검색을 위한 함수
     *
     * @param type 검색 방법에 대한 타입(1: 제품코드, 2: 제품명, 3: 효능관련)
     * @param search 검색한 제품 명
     * @return 검색된 리스트를 반환
     * @author 박채빈
     *
     */
    public List<DrugDTO> searchDrugList(int type, String search) {
        List<DrugDTO> drugDTOS = new ArrayList<>();
        try {
            /*URL*/
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList");
            /*Service Key*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=Luco9rwVuxP3RyPaO%2BYc09eiRfSRf%2B260CwkIJfvChXaraDw6TkGMGAO2XeHGX%2FIzhlKjnDgLx60xGKd2UYh1Q%3D%3D");
            /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
            
            if (type == 1) {
                /* 품목기준코드 */
                urlBuilder.append("&" + URLEncoder.encode("itemSeq", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            } else if (type == 2) {/* 제품명 */
                urlBuilder.append("&" + URLEncoder.encode("itemName", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            } else if (type == 3) {/* 효능 */
                urlBuilder.append("&" + URLEncoder.encode("efcyQesitm", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
                /*이 약의 효능은 무엇입니까?*/
            }
            
            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            /*응답데이터 형식(xml/json) Default:xml*/
            
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            
            String jsonResponse = sb.toString();
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            
            JsonNode bodyNode = rootNode.path("body");
            if (!bodyNode.isMissingNode()) {
                JsonNode itemsArray = bodyNode.path("items");
                
                for (JsonNode item : itemsArray) {
                    DrugDTO drugDTO = DrugDTO.builder()
                            .seq(item.get("itemSeq").asText())
                            .entpName(item.get("entpName").asText())
                            .name(item.get("itemName").asText())
                            .efcy(item.get("efcyQesitm").asText())
                            .use(item.get("useMethodQesitm").asText())
                            .atpn(item.get("atpnQesitm").asText())
                            .atpnWarn(item.get("atpnWarnQesitm").asText())
                            .intrc(item.get("intrcQesitm").asText())
                            .se(item.get("seQesitm").asText())
                            .diposit(item.get("depositMethodQesitm").asText())
                            .image(item.get("itemImage").asText())
                            .build();
                    drugDTOS.add(drugDTO);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DrugService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return drugDTOS;
    }

    /**
     * 약품의 모양으로 검색
     *
     * @param shapeDTO 검색할 모양 입력 정보
     * @return 관련 약품 리스트
     * @author 박채빈
     *
     */

    public List<DrugDTO> getDrugFromShape(ShapeDTO shapeDTO){
        List<DrugDTO> drugDTOS = new ArrayList<>();
        // 1. 검색창에 입력된 모양으로 검색(shapeEntity)
        // 2. 검색한 결과(DrugEntity)를 DTO List로 변환하여 반환
        return drugDTOS;
    }

    /**
     * 선택한 제품의 정보를 반환
     *
     * @param seq 선택한 제품의 제품 코드
     * @return 선택된 제품의 정보
     * @author 박채빈
     *
     */
    public DrugDTO getDrugInfo(String seq) {
        // 1. 제품 코드를 이용하여 약품 검색
        // 2. 해당 제품의 정보를 반환
        return null;
    }

    /**
     * 선택한 제품을 자신의 제품 리스트에 추가하는 함수
     *
     * @param drugDTO 제품 코드, 수량, 구매 일자, 사용 기한 입력 정보
     * @param uid 사용자 아이디
     * @return 등록 성공 여부
     * @author 박채빈
     *
     */
    public boolean addMyDrug(DrugDTO drugDTO, String uid) {
        // 1. 제품 정보를 통해 제품을 등록
        // 2. 제품 등록 성공 여부를 반환
        return false;
    }

    /**
     * 자신이 등록한 제품의 리스트를 가져오는 함수
     *
     * @param uid 사용자 아이디
     * @param filter 목록 정렬 필터
     * @return 요청한 아이디에 따른 등록 제품 리스트를 반환
     */
    public List<DrugDTO> getMyDrugList(String uid, String filter) {
        List<DrugDTO> drugDTOS = new ArrayList<>();
        // 1. 아이디에 해당하는 약품 목록(PossessionEntity)을 필터에 따라서(등록 순 - 최초 등록 registerDate순, 최신순 - 정보가 새로 등록된 updateDate순)으로 가져오기
        // 2. 받아온 리스트를 DTO 리스트로 변경해서 반환
        return drugDTOS;
    }

    /**
     * 선택한 보유 제품의 정보를 반환
     *
     * @param seq 선택한 제품의 제품 코드
     * @param uid 사용자 아이디
     * @return 선택된 제품의 정보
     * @author 박채빈
     *
     */
    public DrugDTO getMyDrugInfo(String seq, String uid) {
        // 1. 제품 코드를 이용하여 약품 검색
        // 2. 해당 제품의 정보(DrugEntity랑 PossessionEntity를 join)를 반환
        return null;
    }

    /**
     * 선택한 보유 제품의 정보를 변경하는 함수
     *
     * @param drugDTO 제품 코드, 수량, 구매 일자, 사용 기한 입력 정보
     * @param uid 사용자 아이디
     * @return 수정 성공 여부
     * @author 박채빈
     *
     */
    public boolean updateMyDrug(DrugDTO drugDTO, String uid) {
        // 1. 제품 정보를 통해 제품을 등록(updateDate를 현재 시각으로 변경)
        // 2. 제품 등록 성공 여부를 반환
        return false;
    }

    /**
     * 선택한 제품을 자신의 제품 리스트에서 제거하는 함수
     *
     * @param seq 제품 코드
     * @param uid 사용자 아이디
     * @return 등록 성공 여부
     * @author 박채빈
     *
     */
    public boolean deleteMyDrug(String seq, String uid) {
        // 1. 제품 정보를 통해 제품을 삭제
        // 2. 제품 등록 성공 여부를 반환
        return false;
    }

}
