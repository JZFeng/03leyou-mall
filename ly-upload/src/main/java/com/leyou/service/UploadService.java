/**
 * @Author jzfeng
 * @Date 4/20/21-12:01 AM
 */

package com.leyou.service;

import com.leyou.controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String upload(MultipartFile file) {
        //验证文件类型
        String type = file.getContentType();
        if(!suffixes.contains(type)) {
            logger.info("上传失败，文件类型不匹配：{}" + type) ;
            return null;
        }

        try {
            //验证文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }

            File dir = new File("/Users/jzfeng/Desktop/uploads");
            if(!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(dir, file.getOriginalFilename()));
            String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();

            return url;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }
}
