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
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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

        //分页
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());

        //排序。其实之前的那个实现也行。
        //PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.getDescending() ? Sort.Direction.DESC : Sort.Direction.ASC, StringUtils.isBlank(request.getSortBy()) ? "id" : request.getSortBy()));
        String sortBy = request.getSortBy();
        Boolean descending = request.getDescending();
        builder.withSort( SortBuilders.fieldSort( StringUtils.isBlank(sortBy) ?  "id" : sortBy ).order( descending ? SortOrder.DESC : SortOrder.ASC ) );

        builder.withPageable(pageRequest);
        builder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","image", "createdTime","price"}, null));

        SearchHits<Goods> searchHits = this.elasticsearchRestTemplate.search(builder.build(), Goods.class);
        SearchPage<Goods> searchPage = SearchHitSupport.searchPageFor(searchHits, pageRequest);  //convert searchHits to a SearchPage

        List<Goods> goodsList = searchPage.getContent().stream().map(goodsSearchHit -> {
            return goodsSearchHit.getContent();
        }).collect(Collectors.toList());

        return new PageResult<Goods>(searchPage.getTotalElements(), (long)searchPage.getTotalPages(), goodsList );

    }
}
