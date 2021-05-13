package com.leyou.search.service;

import com.leyou.LySearchService;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Test
    public void testSearch() {
        SearchRequest req = SearchRequest.builder().key("手机").page(0).size(50).sortBy("price").descending(false).build();
        SearchResult searchResult = this.searchService.search(req);
        System.out.println(searchResult);

    }
}