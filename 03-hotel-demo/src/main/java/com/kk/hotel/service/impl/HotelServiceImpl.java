package com.kk.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.hotel.mapper.HotelMapper;
import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.HotelDoc;
import com.kk.hotel.pojo.PageResult;
import com.kk.hotel.pojo.RequestParam;
import com.kk.hotel.service.HotelService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public PageResult search(RequestParam requestParam) {
        try {
            //实现全文检索
            int page = requestParam.getPage();
            int size = requestParam.getSize();
            SearchRequest searchRequest = new SearchRequest("hotel");
            //按条件过滤信息
            handlerFilter(requestParam, searchRequest);
            searchRequest.source().from((page - 1) * size).size(size);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return handleSearchResult(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<String>> filters(RequestParam requestParam) {
        try {
            SearchRequest searchRequest = new SearchRequest("hotel");
            handlerFilter(requestParam, searchRequest);
            searchRequest.source().aggregation(AggregationBuilders.terms("brand_aggregation").field("brand"));
            searchRequest.source().aggregation(AggregationBuilders.terms("city_aggregation").field("city"));
            searchRequest.source().aggregation(AggregationBuilders.terms("starName_aggregation").field("starName"));
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms brandAggregation = aggregations.get("brand_aggregation");
            List<? extends Terms.Bucket> brandBuckets = brandAggregation.getBuckets();
            List<String> brandList = getList(brandBuckets);
            Terms cityAggregation = aggregations.get("city_aggregation");
            List<? extends Terms.Bucket> cityBuckets = cityAggregation.getBuckets();
            List<String> cityList = getList(cityBuckets);
            Terms starNameAggregation = aggregations.get("starName_aggregation");
            List<? extends Terms.Bucket> starNameBuckets = starNameAggregation.getBuckets();
            List<String> starNameList = getList(starNameBuckets);
            Map<String, List<String>> map = new HashMap<>();
            map.put("city", cityList);
            map.put("brand", brandList);
            map.put("starName", starNameList);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getList(List<? extends Terms.Bucket> buckets) {
        List<String> keywordList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            keywordList.add(keyAsString);
        }
        return keywordList;
    }

    //过滤
    public void handlerFilter(RequestParam requestParam, SearchRequest searchRequest) {
        if (requestParam != null) {
            String key = requestParam.getKey();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (key == null || "".equals(key))
                searchRequest.source().query(boolQuery.must(QueryBuilders.matchAllQuery()));
            else searchRequest.source().query(boolQuery.must(QueryBuilders.matchQuery("name", key)));
            String city = requestParam.getCity();
            String brand = requestParam.getBrand();
            String starName = requestParam.getStarName();
            Integer minPrice = requestParam.getMinPrice();
            Integer maxPrice = requestParam.getMaxPrice();
            String location = requestParam.getLocation();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            if (city != null && !"".equals(city)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("city", city)));
            }
            if (brand != null && !"".equals(brand)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("brand", brand)));
            }
            if (starName != null && !"".equals(starName)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("starName", starName)));
            }
            if (minPrice != null && maxPrice != null) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice).gte(minPrice)));
            }
            if (location != null && !"".equals(location)) {
                searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
            }
            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQuery, new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("isAD", true), ScoreFunctionBuilders.weightFactorFunction(10))
            });
            searchRequest.source().query(functionScoreQuery);
        }
    }

    //封装处理请求的函数
    public PageResult handleSearchResult(SearchResponse searchResponse) {
        PageResult pageResult = new PageResult();
        pageResult.setTotal(searchResponse.getHits().getTotalHits().value);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<HotelDoc> hotelDocList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            if (hit.getSortValues().length > 0) hotelDoc.setDistance(hit.getSortValues()[0]);
            hotelDocList.add(hotelDoc);
        }
        pageResult.setHotels(hotelDocList);
        return pageResult;
    }
}
