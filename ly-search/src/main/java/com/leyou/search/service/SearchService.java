/**
 * @Author jzfeng
 * @Date 5/11/21-9:05 AM
 */

package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.GoodsRepository;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if(StringUtils.isBlank(key)) {
            return null;
        }

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.getDescending() ? Sort.Direction.DESC : Sort.Direction.ASC, StringUtils.isBlank(request.getSortBy()) ? "id" : request.getSortBy()));
//        PageRequest pageRequest = PageRequest.of(0, 20);
        builder.withPageable(pageRequest);
        builder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","image"}, null));

        SearchHits<Goods> searchHits = this.elasticsearchRestTemplate.search(builder.build(), Goods.class);
        SearchPage<Goods> searchPage = SearchHitSupport.searchPageFor(searchHits, pageRequest);  //convert searchHits to a SearchPage

        List<Goods> goodsList = searchPage.getContent().stream().map(goodsSearchHit -> {
            return goodsSearchHit.getContent();
        }).collect(Collectors.toList());

        return new PageResult<Goods>(searchPage.getTotalElements(), (long)searchPage.getTotalPages(), goodsList );

    }
}
