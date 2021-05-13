/**
 * @Author jzfeng
 * @Date 5/11/21-9:05 AM
 */

package com.leyou.search.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.search.GoodsRepository;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    public SearchResult search(SearchRequest request) {
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
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","image" /*, "createdTime","price" */}, null));

        //聚合
        builder.addAggregation(AggregationBuilders.terms("brands").field("brandId"));
        builder.addAggregation(AggregationBuilders.terms("categories").field("cid3"));

        SearchHits<Goods> searchHits = this.elasticsearchRestTemplate.search(builder.build(), Goods.class);
        SearchPage<Goods> searchPage = SearchHitSupport.searchPageFor(searchHits, pageRequest);  //convert searchHits to a SearchPage

        //取出List<Goods>
        List<Goods> goodsList = searchPage.getContent().stream().map(goodsSearchHit -> {
            return goodsSearchHit.getContent();
        }).collect(Collectors.toList());

        //处理Aggregation
        List<Brand> brands = getBrandAggregationResult((Terms)searchHits.getAggregations().get("brands"));
        List<Category> categories = getCategoryAggregationResult((Terms)searchHits.getAggregations().get("categories"));

        return new SearchResult(searchPage.getTotalElements(), (long)searchPage.getTotalPages(), goodsList, brands, categories,null);
    }

    private List<Category> getCategoryAggregationResult(Terms agg_categories) {
        try {
            List<Long> categoryIds = agg_categories.getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsNumber().longValue();
            }).collect(Collectors.toList());

            List<Category> categories = this.categoryClient.queryCategoryByIds(categoryIds);

            return categories;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Brand> getBrandAggregationResult(Terms agg_brands) {
        try {
            List<Long> brandIds = agg_brands.getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsNumber().longValue();
            }).collect(Collectors.toList());

            List<Brand> brands = this.brandClient.queryBrandByIds(brandIds);

            return brands;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
