/**
 * @Author jzfeng
 * @Date 5/11/21-9:05 AM
 */

package com.leyou.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Param;
import com.leyou.item.pojo.Spu;
import com.leyou.search.GoodsRepository;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        if(StringUtils.isBlank(key)) {
            return null;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //?????????????????????????????????
        QueryBuilder query = this.buildBasicQueryWithFilters(request);
        queryBuilder.withQuery(query);

        //??????
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());

        //?????????
        String sortBy = request.getSortBy();
        Boolean descending = request.getDescending();

        queryBuilder.withSort( SortBuilders.fieldSort( StringUtils.isBlank(sortBy) ?  "id" : sortBy ).order( descending ? SortOrder.DESC : SortOrder.ASC ) );
        queryBuilder.withPageable(pageRequest);
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","image", "specs" /*, "createdTime","price" */}, null));

        //????????????
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms("categories").field("cid3"));

        //?????????????????????????????????
        SearchHits<Goods> searchHits = this.elasticsearchRestTemplate.search(queryBuilder.build(), Goods.class);
        SearchPage<Goods> searchPage = SearchHitSupport.searchPageFor(searchHits, pageRequest);  //convert searchHits to a SearchPage
                 //-- ??????goodsList
        List<Goods> goodsList = searchPage.getContent().stream().map(searchHit -> {
            return searchHit.getContent();
        }).collect(Collectors.toList());

                //-- ??????Aggregation???????????????????????????
        List<Brand> brands = getBrandAggregationResult((Terms)searchHits.getAggregations().get("brands"));
        List<Category> categories = getCategoryAggregationResult((Terms)searchHits.getAggregations().get("categories"));

        //????????????????????????????????????????????????????????????????????????specs??????;
        // {"size":1,"query":{"bool":{"must":[{"match":{"all":"??????"}}],"filter":[{"term":{"specs.CPU??????.keyword":"??????"}}]}},"aggs":{"CPU??????":{"terms":{"size":200,"field":"specs.CPU??????.keyword"}},"???????????????????????????":{"terms":{"size":200,"field":"specs.???????????????????????????.keyword"}},"?????????":{"terms":{"size":200,"field":"specs.?????????.keyword"}},"CPU??????":{"terms":{"size":200,"field":"specs.CPU??????.keyword"}},"????????????":{"terms":{"size":200,"field":"specs.????????????.keyword"}}}}
        //??????????????????????????????key??????????????????
        Map<String,Object> specs = new HashMap<>();
        if(!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            specs = getAggregatedSpecsForCategory(  query, categories.get(0).getId() );
        }

        return new SearchResult(searchPage.getTotalElements(), (long)searchPage.getTotalPages(), goodsList, brands, categories,specs);

    }

    /**
     *
     * @param query  ??????search???query??????
     * @param cid  ????????????es search?????????categoryId
     * @return ???????????????????????????specs
     */
    private Map<String, Object> getAggregatedSpecsForCategory(QueryBuilder query, Long cid) {

        Map<String, Object> specs = new HashMap<>();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(query);
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null)); //?????????Items???????????????????????????

        //??????cid??????params??????????????????param?????????k?????????v???options??????????????????????????????????????????????????????;
        List<Param> params = this.specificationClient.querySpecsByCid(cid);
        if(!CollectionUtils.isEmpty(params)) {
            params.forEach(param -> {
                if(param.isSearchable()) {
                    String k = param.getK().trim();
                    queryBuilder.addAggregation(AggregationBuilders.terms(k).field("specs." + k + ".keyword").size(500));
                }
            });
        }

        SearchHits<Goods> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), Goods.class);

        //??????aggregation??????
        searchHits.getAggregations().asList().stream()
                .map( aggregation -> {return (Terms) aggregation; } )
                .forEach( terms -> {
                    List<String> strs = new ArrayList<>();
                    terms.getBuckets().forEach(
                            bucket -> {
                                String key = bucket.getKeyAsString();
                                if(!StringUtils.isBlank(key) && !key.equalsIgnoreCase("null")){
                                    strs.add(key);
                                }
                            }
                    );
                    specs.put(terms.getName(), strs);
                });

        return specs;
    }


    /**
     *
     * @param request
     * @return
     *
     * Sample ES Query:
       GET goods/_search {"query":{"bool":{"must":[{"match":{"all":"??????"}}],"filter":[{"term":{"specs.CPU??????.keyword":"??????"}},{"term":{"specs.????????????.keyword":"??????"}},{"term":{"specs.???????????????????????????.keyword":"5.5??????"}}]}}}
     */
    private QueryBuilder buildBasicQueryWithFilters(SearchRequest request) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));

        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();

        Map<String, String> filters = request.getFilters();
        if( filters!= null && !filters.isEmpty() ) {
            filters.entrySet().forEach(
                    entry -> {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if( !key.equalsIgnoreCase("cid3") && !key.equalsIgnoreCase("brandId")) {
                            key = "specs." + key + ".keyword";
                        }
                        filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    }
            );
            queryBuilder.filter(filterQueryBuilder);
        }

        return queryBuilder;
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


    public void createIndex(Long spuId) {
        Spu spu = this.goodsClient.querySpuBySpuId(spuId);
        Goods goods = this.indexService.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        this.goodsRepository.deleteById(spuId);
    }
}
