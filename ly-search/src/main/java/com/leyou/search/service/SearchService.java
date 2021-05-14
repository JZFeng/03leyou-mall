/**
 * @Author jzfeng
 * @Date 5/11/21-9:05 AM
 */

package com.leyou.search.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Param;
import com.leyou.search.GoodsRepository;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.leyou.search.utils.SearchUtils.generateSpecsFromParamList;

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

    @Autowired
    private SpecificationClient specificationClient;

    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        if(StringUtils.isBlank(key)) {
            return null;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //分页
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());

        //排序。其实之前的那个实现也行。
        //PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.getDescending() ? Sort.Direction.DESC : Sort.Direction.ASC, StringUtils.isBlank(request.getSortBy()) ? "id" : request.getSortBy()));
        String sortBy = request.getSortBy();
        Boolean descending = request.getDescending();

        queryBuilder.withSort( SortBuilders.fieldSort( StringUtils.isBlank(sortBy) ?  "id" : sortBy ).order( descending ? SortOrder.DESC : SortOrder.ASC ) );
        queryBuilder.withPageable(pageRequest);
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle","image", "specs" /*, "createdTime","price" */}, null));

        //条件过滤
        QueryBuilder query = this.buildBasicQueryWithFilters(request);
        queryBuilder.withQuery(query);

        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms("categories").field("cid3"));

        //查询，以及查询结果处理
        SearchHits<Goods> searchHits = this.elasticsearchRestTemplate.search(queryBuilder.build(), Goods.class);
        SearchPage<Goods> searchPage = SearchHitSupport.searchPageFor(searchHits, pageRequest);  //convert searchHits to a SearchPage
                 //-- 提取goodsList
        List<Goods> goodsList = searchPage.getContent().stream().map(searchHit -> {
            return searchHit.getContent();
        }).collect(Collectors.toList());


                //-- 处理Aggregation
        List<Brand> brands = getBrandAggregationResult((Terms)searchHits.getAggregations().get("brands"));
        List<Category> categories = getCategoryAggregationResult((Terms)searchHits.getAggregations().get("categories"));

        Map<String,Object> specs = new HashMap<>();
        if(categories.size() == 1 ) {
            List<Param> params = this.specificationClient.querySpecsByCid(categories.get(0).getId());
            specs = generateSpecsFromParamList(params);
        }

        return new SearchResult(searchPage.getTotalElements(), (long)searchPage.getTotalPages(), goodsList, brands, categories,specs);

    }


    /**
     *
     * @param request
     * @return
     *
     * Sample ES Query:
     * GET goods/_search
     * {
     *   "query": {
     *     "bool": {
     *       "must": [
     *         {
     *           "match": {
     *             "all": "小米"
     *           }
     *         }
     *       ],
     *       "filter": [
     *         {
     *           "term": {
     *             "specs.CPU核数.keyword": "八核"
     *           }
     *         },
     *         {
     *           "term": {
     *             "specs.机身颜色.keyword": "黑色"
     *           }
     *         },
     *         {
     *           "term": {
     *             "specs.主屏幕尺寸（英寸）.keyword": "5.5英寸"
     *           }
     *         }
     *       ]
     *     }
     *   }
     * }
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


}
