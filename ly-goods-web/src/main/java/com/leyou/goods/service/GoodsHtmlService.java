/**
 * @Author jzfeng
 * @Date 5/16/21-9:28 AM
 */

package com.leyou.goods.service;

import com.leyou.goods.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlService {

    private static final int MAX_NUM_OF_THREADS = 10;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 需要做的事情抽取成一个函数，然后放到线程中异步执行；
     * @param spuId
     * @param objectMap
     */
    private void saveHtml(Long spuId, Map<String, Object> objectMap) {
        if(objectMap != null && objectMap.size() > 0) {
            PrintWriter writer = null;

            try {
                File file = new File("/Users/jzfeng/Documents/git/leyou/ly-goods-web/pages/" + spuId + ".html");
                writer = new PrintWriter(file);

                Context context = new Context();
                context.setVariables(objectMap);

                templateEngine.process("item", context, writer);
            } catch (Exception e) {
                System.out.println("页面静态化"+spuId+"时出错：");
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    public void asyncSaveHtml(Long spuId, Map<String, Object> objectMap) {
            ThreadUtils.execute(() ->{saveHtml(spuId, objectMap);});
    }

}
