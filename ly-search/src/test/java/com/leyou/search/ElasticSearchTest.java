/**
 * @Author jzfeng
 * @Date 5/9/21-7:52 PM
 */

package com.leyou.search;

import com.leyou.LySearchService;
import com.leyou.search.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class ElasticSearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testCreateIndex() {

        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(Goods.class);
        if (!indexOps.exists()) {
            indexOps.create();
            System.out.println("索引库创建成功!");
        } else {
            System.out.println("索引库已存在!");
        }

    }
}
