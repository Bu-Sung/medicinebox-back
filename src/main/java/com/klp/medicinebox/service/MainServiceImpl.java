package com.klp.medicinebox.service;

import com.klp.medicinebox.dto.PillResult;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MainServiceImpl implements MainService {
    
    @Autowired
    private ServletContext ctx;
    
    @Override
    public String md5sum(byte[] bytea) throws Exception {
        return ImageProcessing.md5sum(bytea);
    }

    @Override
    public boolean upload(byte[] bytea, String filePath) {
        return ImageProcessing.upload(bytea, ctx.getRealPath(filePath));
    }

    @Override
    public String execute(String command) {
        return ImageProcessing.execute(command);
    }

    @Override
    public List<PillResult> convert(String jsonString) throws IOException {
        return ImageProcessing.convert(jsonString);
    }

    @Override
    public boolean save(String content, String filePath) {
        return ImageProcessing.save(content, ctx.getRealPath(filePath));
    }

 
}
