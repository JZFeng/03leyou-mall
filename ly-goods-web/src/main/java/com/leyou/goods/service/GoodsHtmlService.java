/**
 * @Author jzfeng
 * @Date 5/16/21-9:28 AM
 */

package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GoodsHtmlService {

    private static final int MAX_NUM_OF_THREADS = 10;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_NUM_OF_THREADS);

    public void saveHtml(Long spuId) {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                PrintWriter writer = null;

                try {
                    File file = new File("/Users/jzfeng/Documents/git/leyou/ly-goods-web/pages/" + spuId + ".html");
                    writer = new PrintWriter(file);

                    Map<String, Object> objectMap = goodsService.loadModel(spuId);
                    Context context = new Context();
                    context.setVariables(objectMap);

                    templateEngine.process("item", context, writer);

                } catch (Exception e) {
                    System.out.println("页面静态化出错：{}，"+ e + spuId);
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        });
    }

}
