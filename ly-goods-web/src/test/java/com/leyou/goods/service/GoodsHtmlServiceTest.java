package com.leyou.goods.service;


import com.leyou.goods.LyGoodsWeb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyGoodsWeb.class)
public class GoodsHtmlServiceTest {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @Autowired
    private GoodsService goodsService;

    @Test
    public void saveHtml() {
        goodsHtmlService.saveHtml(243L);
        System.out.println();
    }
}