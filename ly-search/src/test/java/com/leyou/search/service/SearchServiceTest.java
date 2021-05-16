package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.LySearchService;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Test
    public void testSearchWithoutFilters() {
        SearchRequest req = SearchRequest.builder().key("手机").page(0).size(50).sortBy("price").descending(false).build();
        SearchResult searchResult = this.searchService.search(req);
        System.out.println(searchResult);
        Assert.assertTrue(searchResult.getTotal() > 180 && searchResult.getTotalPage() > 2);
    }


    @Test
    public void testSearchWithFilters() {
        Map<String,String> filters = new HashedMap();
        filters.put("CPU核数","八核");
        filters.put("机身颜色", "黑色");
        filters.put("主屏幕尺寸（英寸）","5.5英寸");
        SearchRequest req = SearchRequest.builder().key("小米").page(0).size(10).sortBy("price").descending(false).filters(filters).build();
        SearchResult searchResult = this.searchService.search(req);
        System.out.println("总共：" + searchResult.getTotal() + "条记录；总共" + searchResult.getTotalPage() + "页!");
        searchResult.getItems().forEach(System.out::println);

        Assert.assertTrue(searchResult.getSpecs().size() == 10 );
        Assert.assertTrue(searchResult.getCategories().size() == 1 && searchResult.getCategories().get(0).getId() == 76 );

        try {
            String request = new ObjectMapper().writeValueAsString(req);
            System.out.println(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}