package com.leyou.goods.service;


import com.leyou.goods.LyGoodsWeb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyGoodsWeb.class)
public class GoodsHtmlServiceTest {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @Autowired
    private GoodsService goodsService;

    @Test
    public void generatePages() {
        for (long i = 1; i < 250 ; i++) {
            Map<String, Object> objectMap = goodsService.loadModel(i);
            goodsHtmlService.asyncSaveHtml( i, objectMap );
        }
    }

    @Test
    public void saveHtmlInvalidSpuId() {
        Long spuId = 1L;
        Map<String, Object> objectMap = goodsService.loadModel(spuId);
        goodsHtmlService.asyncSaveHtml( spuId, objectMap );
    }

    @Test
    public void saveHtmlValidSpuId() {
        Long spuId = 243L;
        Map<String, Object> objectMap = goodsService.loadModel(spuId);
        goodsHtmlService.asyncSaveHtml( spuId, objectMap );
    }
}