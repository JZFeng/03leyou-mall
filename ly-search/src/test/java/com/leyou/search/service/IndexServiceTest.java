package com.leyou.search.service;

import com.leyou.LySearchService;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)

public class IndexServiceTest {

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsClient goodsClient;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void buildGoods() {
        Long spuId = 243L;
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        Goods goods = indexService.buildGoods(spu);
        System.out.println(goods);

    }
}