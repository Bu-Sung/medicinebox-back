package com.klp.medicinebox.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klp.medicinebox.dto.DrugDTO;
import com.klp.medicinebox.dto.ShapeDTO;
import com.klp.medicinebox.entity.DrugEntity;
import com.klp.medicinebox.entity.ShapeEntity;
import com.klp.medicinebox.repository.DrugRepository;
import com.klp.medicinebox.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;
    private final ShapeRepository shapeRepository;

    @Value("${medicineShape.folder}")
    private String medicineShapeFolderPath;

    @Autowired
    private ServletContext ctx;
    
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
    public List<ShapeDTO> getDrugFromShape(ShapeDTO shapeDTO) {
        
       List<ShapeDTO> shapeDTOs = new ArrayList<>();
        // 1. 검색창에 입력된 모양으로 검색(shapeEntity)
        
        if (shapeDTO.getForm() != null && shapeDTO.getForm().equals("정제")) {
            shapeDTO.setForm("정");
        }

        if (shapeDTO.getFrontLine() != null && shapeDTO.getFrontLine().equals("없음")) {
            shapeDTO.setFrontLine("");
        }

        List<ShapeEntity> shapeEntities = shapeRepository.findSeqByShape(shapeDTO.getShape(), shapeDTO.getForm(), shapeDTO.getFrontLine(), shapeDTO.getFrontColor(), shapeDTO.getBackColor(), shapeDTO.getFrontPrint(), shapeDTO.getBackPrint());

        // 2. 검색한 결과들의 제품 기준 코드, 이름, 사진 반환 (shapeDTO)
        for (ShapeEntity shapeEntity : shapeEntities) {

                ShapeDTO searchResult = new ShapeDTO();
                searchResult.setSeq(shapeEntity.getSeq());
                searchResult.setForm(shapeEntity.getForm());
                searchResult.setImage(shapeEntity.getImage());
                searchResult.setFrontPrint(shapeEntity.getFrontPrint());
                searchResult.setBackPrint(shapeEntity.getBackPrint());
                searchResult.setShape(shapeEntity.getShape());
                searchResult.setFrontColor(shapeEntity.getFrontColor());
                searchResult.setBackColor(shapeEntity.getBackColor());
                searchResult.setFrontLine(shapeEntity.getFrontLine());
                searchResult.setBackLine(shapeEntity.getBackLine()); 
                searchResult.setName(shapeEntity.getName());

                shapeDTOs.add(searchResult);
            }

        return shapeDTOs;
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
        List<DrugDTO> drugDTOS = searchDrugList(1, seq);
        
        // 2. 해당 제품의 정보를 반환
        if(drugDTOS.isEmpty()) {
            drugDTOS = searchDrugList2(1, seq);
            if(drugDTOS.isEmpty()) {
                return null;
            }
        } 
        return drugDTOS.get(0);
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
        DrugEntity drugEntity = DrugEntity.builder()
                .uid(uid)
                .seq(drugDTO.getSeq())
                .entpName(drugDTO.getEntpName())
                .name(drugDTO.getName())
                .efcy(drugDTO.getEfcy())
                .use(drugDTO.getUse())
                .atpnWarn(drugDTO.getAtpnWarn())
                .atpn(drugDTO.getAtpn())
                .intrc(drugDTO.getIntrc())
                .se(drugDTO.getSe())
                .diposit(drugDTO.getDiposit())
                .image(drugDTO.getImage())
                .count(drugDTO.getCount())
                .buyDate(LocalDate.parse(drugDTO.getBuyDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .expirationDate(LocalDate.parse(drugDTO.getExpirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .registerDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        // 2. 제품 등록 성공 여부를 반환
        return drugRepository.save(drugEntity) != null;
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
        List<DrugEntity> drugEntities = null;
        
        if(filter.equals("등록순")) {  // 등록순 
            drugEntities = drugRepository.findByUidOrderByRegisterDate(uid);
        } else if(filter.equals("최신순")) {  // 최신순  
            drugEntities = drugRepository.findByUidOrderByUpdateDate(uid);
        }
        
        // 2. 받아온 리스트를 DTO 리스트로 변경해서 반환
        for (DrugEntity drugEntity : drugEntities) {
            
            drugDTOS.add(DrugDTO.builder()
                    .pid(drugEntity.getPid())
                    .seq(drugEntity.getSeq())
                    .entpName(drugEntity.getEntpName())
                    .name(drugEntity.getName())
                    .efcy(drugEntity.getEfcy())
                    .use(drugEntity.getUse())
                    .atpnWarn(drugEntity.getAtpnWarn())
                    .atpn(drugEntity.getAtpn())
                    .intrc(drugEntity.getIntrc())
                    .se(drugEntity.getSe())
                    .diposit(drugEntity.getDiposit())
                    .image(drugEntity.getImage())
                    .count(drugEntity.getCount())
                    .buyDate(drugEntity.getBuyDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .expirationDate(drugEntity.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .registerDate(drugEntity.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .updateDate(drugEntity.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build());
        }

        return drugDTOS;
    }

    
    /**
     * 선택한 보유 제품의 정보를 반환
     *
     * @param pid 보유 제품 고유번호 
     * @param uid 사용자 아이디
     * @return 선택된 제품의 정보
     * @author 박채빈
     *
     */
    public DrugDTO getMyDrugInfo(Long pid) {
        // 1. 제품 코드를 이용하여 약품 검색
        // 2. 해당 제품의 정보(DrugEntity랑 PossessionEntity를 join)를 반환
        DrugEntity drugEntity = drugRepository.findByPid(pid);

        DrugDTO drugDTO = DrugDTO.builder()
            .pid(drugEntity.getPid())
            .seq(drugEntity.getSeq())
            .entpName(drugEntity.getEntpName())
            .name(drugEntity.getName())
            .efcy(drugEntity.getEfcy())
            .use(drugEntity.getUse())
            .atpnWarn(drugEntity.getAtpnWarn())
            .atpn(drugEntity.getAtpn())
            .intrc(drugEntity.getIntrc())
            .se(drugEntity.getSe())
            .diposit(drugEntity.getDiposit())
            .image(drugEntity.getImage())
            .count(drugEntity.getCount())
            .buyDate(drugEntity.getBuyDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .expirationDate(drugEntity.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .registerDate(drugEntity.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .updateDate(drugEntity.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .build();
        
        return drugDTO;
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
    public boolean updateMyDrug(DrugDTO drugDTO) {
        // 1. 제품 정보를 통해 제품을 등록(updateDate를 현재 시각으로 변경)
        DrugEntity drugEntity = drugRepository.findByPid(drugDTO.getPid());
            
        if (drugEntity != null) {
            drugEntity.setCount(drugDTO.getCount());
            drugEntity.setBuyDate(LocalDate.parse(drugDTO.getBuyDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            drugEntity.setExpirationDate(LocalDate.parse(drugDTO.getExpirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            drugEntity.setUpdateDate(LocalDateTime.now());

            // 2. 제품 등록 성공 여부를 반환   
            return drugRepository.save(drugEntity) != null;
        }
        return false;
    }

    
    /**
     * 선택한 제품을 자신의 제품 리스트에서 제거하는 함수
     *
     * @param pid 보유 제품 고유번호 
     * @param uid 사용자 아이디
     * @return 등록 성공 여부
     * @author 박채빈
     *
     */
    public boolean deleteMyDrug(Long pid) {
        // 1. 제품 정보를 통해 제품을 삭제
        DrugEntity drugEntity = drugRepository.findByPid(pid);
        // 2. 제품 등록 성공 여부를 반환
        if (drugEntity != null) {
            drugRepository.delete(drugEntity);
            return true;
        }
        return false;
    }
    

    
    /**
     * medicine shape 엑셀 파일 데이터 DB에 저장 
     * @return 
     */
    public boolean addMedicineShape() {
        String directoryPath = ctx.getRealPath(medicineShapeFolderPath);
        List<ShapeEntity> shapeEntities = new ArrayList<>();

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            for (File file : files) {

                try (InputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {
                    Sheet sheet = workbook.getSheetAt(0);

                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) {
                            // 헤더 값은 넣지 않음 
                            continue;
                        }

                        ShapeEntity shapeEntity = new ShapeEntity();
                        shapeEntity.setSeq(getCellValue(row.getCell(0))); 
                        shapeEntity.setName(getCellValue(row.getCell(1)));
                        shapeEntity.setFrontPrint(getCellValue(row.getCell(6)));
                        shapeEntity.setBackPrint(getCellValue(row.getCell(7)));
                        shapeEntity.setShape(getCellValue(row.getCell(8)));
                        shapeEntity.setFrontColor(getCellValue(row.getCell(9)));
                        shapeEntity.setBackColor(getCellValue(row.getCell(10)));
                        shapeEntity.setFrontLine(getCellValue(row.getCell(11)));
                        shapeEntity.setBackLine(getCellValue(row.getCell(12)));
                        shapeEntity.setForm(getCellValue(row.getCell(4)));
                        shapeEntity.setImage(getCellValue(row.getCell(5)));

                        shapeEntities.add(shapeEntity);
                    }
                } catch (Exception e) {
                    // 엑셀 파일 읽기 오류 처리
                    e.printStackTrace();
                }
            }

            shapeRepository.saveAll(shapeEntities);

            return true;
        }

        return false;
    }

    // cell 값 가져오기 
    private String getCellValue(Cell cell) {
        if (cell != null) {
            return cell.getStringCellValue();
        }
        return "";
    }

    

    /**
     * 제품 검색 2 (searchDrugList 함수 검색 결과 없을 경우) 
     * 
     * @param type 1 : 제품 기준 코드 검색, 2 : 제품 이름 검색 
     * @param search 입력값 
     * @return 검색된 리스트 반환 
     */
    public List<DrugDTO> searchDrugList2(int type, String search) {
        List<DrugDTO> drugDTOS = new ArrayList<>();
        
        // type이 1, 2가 아닌 경우 빈 리스트 반환
        if (type != 1 && type != 2) {
            return drugDTOS;
        }
    
        try {
            // URL
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnDtlInq03");
            // Service Key
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=sDtf81qz1ObM5WdrhmQaFgaa9zJ1%2BlOhV1%2B%2FAoKwlPHXITobuVPCQicwzsOSKp7UR%2BQyctLfoGDQ8aMjVjM%2FxQ%3D%3D");
            // 페이지번호
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            // 한 페이지 결과 수
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));

            if (type == 1) {
                // 품목기준코드
                urlBuilder.append("&" + URLEncoder.encode("item_seq", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            } else if (type == 2) {
                // 제품명
                urlBuilder.append("&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8"));
            }

            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            // 응답데이터 형식(xml/json) Default:xml

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
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
                            .seq(item.get("ITEM_SEQ").asText())
                            .entpName(item.get("ENTP_NAME").asText())
                            .name(item.get("ITEM_NAME").asText())
                            .efcy(extractText(item.get("EE_DOC_DATA").asText()))
                            .use(extractText(item.get("UD_DOC_DATA").asText()))
                            .diposit(item.get("STORAGE_METHOD").asText())
                            .image(shapeRepository.findImageBySeq(item.get("ITEM_SEQ").asText()))
                            .build();

                    if (item.get("ETC_OTC_CODE").asText().equals("전문의약품")) {
                        drugDTO.setAtpnWarn(extractSelectText(item.get("NB_DOC_DATA").asText(), "1. 다음 환자에는 투여하지 말 것.") + "\n" + extractSelectText(item.get("NB_DOC_DATA").asText(), "2. 다음 환자에는 신중히 투여할 것."));
                        drugDTO.setAtpn(extractSelectText(item.get("NB_DOC_DATA").asText(), "4. 일반적 주의"));
                        drugDTO.setIntrc(extractSelectText(item.get("NB_DOC_DATA").asText(), "5. 상호작용"));
                        drugDTO.setSe(extractSelectText(item.get("NB_DOC_DATA").asText(), "3. 이상반응"));
                    } 
//                    else if (item.get("ETC_OTC_CODE").asText().equals("일반의약품")) {
//                        drugDTO.setDrugFile(item.get("INSERT_FILE").asText());
//                    }

                    drugDTOS.add(drugDTO);
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(DrugService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return drugDTOS;
    }

     
    /**
     * ARTICLE 태그의 title과 PARAGRAPH 태그의 텍스트 추출
     * 
     * @param input
     * @return 추출한 텍스트 
     */
    public String extractText(String input) {

        Document doc = Jsoup.parse(input);
        // SECTION 태그 선택
        Elements sections = doc.select("SECTION");

        StringBuilder extractedText = new StringBuilder();

        // ARTICLE 태그의 title과 PARAGRAPH 태그의 텍스트 추출
        for (Element section : sections) {
            Elements articles = section.select("ARTICLE");
            for (Element article : articles) {
                String articleTitle = article.attr("title");

                // ARTICLE의 title 추가
                if (!articleTitle.isEmpty()) {
                    extractedText.append(articleTitle);
                }

                // PARAGRAPH 태그 텍스트 추가
                Elements paragraphs = article.select("PARAGRAPH");
                for (Element paragraph : paragraphs) {
                    // tbody 태그 제외 
                    if (!paragraph.toString().contains("<tbody>")) {
                        extractedText.append(paragraph.text()).append("\n");
                    }
                }
                extractedText.append("\n");
            }
        }

        return extractedText.toString().trim();
    }

    
    /**
     * articleTitle인 해당 ARTICLE 태그의 title과 PARAGRAPH 태그의 텍스트 추출
     * 
     * @param input
     * @param articleTitle 추출할 ARTICLE 태그의 title
     * @return 추출한 텍스트 
     */
    public String extractSelectText(String input, String articleTitle) {

        Document doc = Jsoup.parse(input);
        // DOC 태그 선택
        Element docElement = doc.selectFirst("DOC");

        StringBuilder extractedText = new StringBuilder();

        // title이 articleTitle과 일치하는 ARTICLE 태그 
        Elements articleElements = docElement.select("ARTICLE[title='" + articleTitle + "']");

        for (Element articleElement : articleElements) {
            // articleTitle 추가
            extractedText.append(articleTitle).append("\n");

            // ARTICLE 내의 PARAGRAPH 태그들의 텍스트 추가
            Elements paragraphs = articleElement.select("PARAGRAPH");
            for (Element paragraph : paragraphs) {
                // tbody 태그 제외 
                if (!paragraph.toString().contains("<tbody>")) {
                    extractedText.append(paragraph.text()).append("\n");
                }
            }
            extractedText.append("\n");
        }

        return extractedText.toString().trim();
    }

         
    
    
    /**
     * 약 수량 부족, 유통기한 임박 약 리스트
     *
     * @param uid
     * @param type 1: 수량 부족, 2 : 유통기한 임박, 3 : 유통기한 지남
     * @return
     */
    public List<DrugDTO> getDrugListByType(String uid, int type) {
        List<DrugDTO> drugDTOS = new ArrayList<>();

        List<DrugEntity> drugEntities = drugRepository.findByUid(uid);

        LocalDate term = LocalDate.now().plusDays(8);

        for (DrugEntity drugEntity : drugEntities) {

            // 수량 부족 (2개이하) 
            if (type == 1 && drugEntity.getCount() <= 2) {
                listBuild(drugDTOS, drugEntity);
            }

            // 유통기한 임박 (7일) 
            if (type == 2 && drugEntity.getCount() > 0
                    && !drugEntity.getExpirationDate().isBefore(LocalDate.now()) && drugEntity.getExpirationDate().isBefore(term)) {
                listBuild(drugDTOS, drugEntity);
            }

            // 유통기한 지남 
            if (type == 3 && drugEntity.getCount() > 0 && drugEntity.getExpirationDate().isBefore(LocalDate.now())) {
                listBuild(drugDTOS, drugEntity);
            }
        }

        return drugDTOS;
    }

    public void listBuild(List<DrugDTO> drugDTOS, DrugEntity drugEntity) {
        drugDTOS.add(DrugDTO.builder()
                .pid(drugEntity.getPid())
                .seq(drugEntity.getSeq())
                .entpName(drugEntity.getEntpName())
                .name(drugEntity.getName())
                .efcy(drugEntity.getEfcy())
                .use(drugEntity.getUse())
                .atpnWarn(drugEntity.getAtpnWarn())
                .atpn(drugEntity.getAtpn())
                .intrc(drugEntity.getIntrc())
                .se(drugEntity.getSe())
                .diposit(drugEntity.getDiposit())
                .image(drugEntity.getImage())
                .count(drugEntity.getCount())
                .buyDate(drugEntity.getBuyDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .expirationDate(drugEntity.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .registerDate(drugEntity.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .updateDate(drugEntity.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build());
    }



    
}
