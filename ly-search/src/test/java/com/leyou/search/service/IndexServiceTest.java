package com.leyou.search.service;

import com.leyou.LySearchService;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Spu;
import com.leyou.search.GoodsRepository;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)

public class IndexServiceTest {

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testBuildGoods() {

        Long spuId = 243L;
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        Goods goods = indexService.buildGoods(spu);
        System.out.println(goods);
        this.goodsRepository.save(goods);
    }

    @Test
    public void loadData(){
        //创建index
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(Goods.class);
        if (!indexOps.exists() ) {
            System.out.println("Index创建成功!");
            indexOps.create();
        } else {
            System.out.println("Index已创建！");
        }

        //分页查询，创建Goods记录
        int page = 1;
        int rows = 20;
        int size = 0;

        do {
            PageResult<SpuBo> result = goodsClient.querySpuByPage(page, rows, null, true);
            List<SpuBo> spus = result.getItems();
            size = spus.size();

            List<Goods> goodsList = new ArrayList<>();
            spus.forEach( spu -> {  goodsList.add(this.indexService.buildGoods(spu));});
            this.goodsRepository.saveAll(goodsList);
            page++;

        } while (size == 20); //最后一页就停了。

    }

}