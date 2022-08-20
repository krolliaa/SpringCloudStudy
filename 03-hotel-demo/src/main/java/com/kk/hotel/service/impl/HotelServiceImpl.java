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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            if (city != null && !"".equals(city)){
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
            //System.out.println(searchRequest.toString());
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
            hotelDocList.add(hotelDoc);
        }
        pageResult.setHotels(hotelDocList);
        return pageResult;
    }
}
