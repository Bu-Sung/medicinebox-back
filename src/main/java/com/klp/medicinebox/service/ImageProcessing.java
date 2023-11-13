package com.klp.medicinebox.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klp.medicinebox.dto.PillResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessing {
    private static Logger logger = LoggerFactory.getLogger(ImageProcessing.class);

    /* 파일 업로드 기능 */
    public static boolean upload(byte[] bytea, String target) {
        try {
            File file = new File(target);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytea);
            fos.close();

        } catch (IOException e) {
            logger.error("IOException");
            return false;
        }
        return true;
    }

    /* Base64 해시정보 추출 */
    public static String md5sum(byte[] bytea) throws Exception {
        String result;
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] digest = m.digest(bytea);
        result = new BigInteger(1, digest).toString(16);
        return result;
    }

    /* 문자열 추출 json -> list */
    public static List<PillResult> convert(String jsonString) throws IOException {
        List<PillResult> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("convert = " + jsonString);
        result = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(ArrayList.class, PillResult.class));

        return result;
    }

    /* 명령어 실행 */
    public static String execute(String command) {
        String result = null;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                result = line;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("execute.command = " + result);
        return result;
    }

    public static boolean save(String content, String target) {
        byte[] bytea = content.getBytes();
        try {
            File file = new File(target);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytea);
            fos.close();

        } catch (IOException e) {
            logger.error("IO Exception");
            return false;
        }
        return true;
    }
}
