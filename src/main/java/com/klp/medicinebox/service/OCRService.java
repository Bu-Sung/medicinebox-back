/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.klp.medicinebox.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.klp.medicinebox.dto.DrugDTO;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author qntjd
 */
@Service
@RequiredArgsConstructor
public class OCRService {

    private final ChatgptService chatgptService;
    private final DrugService drugService;

    public String getOCRText(File file) {
        String apiURL = "https://b30mw5y8ca.apigw.ntruss.com/custom/v1/26112/30f6a27d00757141b086cefa6e4462b66f43cdd1060d281468c4cc56f5b7eceb/general";
        String secretKey = "d09ka3ZtR2ZXQ1NWbW1qaWl5TWtDbldrTnlVSGR0Qm4=";

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            writeMultiPart(wr, postParams, file, boundary);
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return getInferTexts(response.toString());
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    public String getInferTexts(String jsonString) {
        String inferTexts = "";
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray imagesArray = jsonObject.getJSONArray("images");

        for (int i = 0; i < imagesArray.length(); i++) {
            JSONObject imageObject = imagesArray.getJSONObject(i);
            JSONArray fieldsArray = imageObject.getJSONArray("fields");

            for (int j = 0; j < fieldsArray.length(); j++) {
                JSONObject fieldObject = fieldsArray.getJSONObject(j);
                String inferText = fieldObject.getString("inferText");
                inferTexts += inferText + ",";
            }
        }

        return inferTexts;
    }

    public String[] getOpenAPIResult(String text) {
        String[] result = null;
        try {
            String message = String.format("[%s] 이 문자들을을 보고 의약품 제품 이름같은 항목만 알려줘 대답의 형식은 ,를 구분자로 대답만 해줘", text);
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer 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");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"model\" : \"gpt-3.5-turbo\", \"messages\" :[{\"role\" : \"user\", \"content\" : \" " + message + "\"}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException ex) {
                Logger.getLogger(OCRService.class.getName()).log(Level.SEVERE, null, ex);
            }

            int code = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String json = response.toString();
            JsonElement jsonElement = new JsonParser().parse(json);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("choices");
            JsonObject messageObject = jsonArray.get(0).getAsJsonObject().getAsJsonObject("message");
            String content = messageObject.get("content").getAsString();
            result = content.split(",");
            conn.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(OCRService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<DrugDTO> getOCRResultDrug(File file) {
        String ocrText = getOCRText(file);
        String[] gptText = getOpenAPIResult(ocrText);
        List<DrugDTO> resultDTOS = new ArrayList();
        List<DrugDTO> newDTOS = new ArrayList();
        for (String tmp : gptText) {
            
            newDTOS = drugService.searchDrugList(2, tmp.replace(" ", ""));
            // resultDTOS의 seq 값을 모두 가져옵니다.
            Set<String> resultSeqs = resultDTOS.stream()
                    .map(DrugDTO::getSeq)
                    .collect(Collectors.toSet());

            // newDTOS에서 resultSeqs에 없는 seq 값을 가진 항목만 필터링합니다.
            List<DrugDTO> uniqueNewDTOS = newDTOS.stream()
                    .filter(newDTO -> !resultSeqs.contains(newDTO.getSeq()))
                    .collect(Collectors.toList());

            // 필터링된 항목들을 resultDTOS에 추가합니다.
            resultDTOS.addAll(uniqueNewDTOS);
        }
        return resultDTOS;
    }
}
