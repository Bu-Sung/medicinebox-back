package com.klp.medicinebox.service;


import com.klp.medicinebox.dto.PillResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface MainService {

    String md5sum(byte[] bytea) throws Exception;

    boolean upload(byte[] bytea, String filePath);

    String execute(String command);

    List<PillResult> convert(String jsonData) throws IOException;

    boolean save(String content, String filePath);
    
}
