package com.kk.hotel;

import com.alibaba.fastjson.JSON;
import com.kk.hotel.pojo.HotelDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.job.results.Bucket;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.WeightFactorFunction;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class HotelSearchTest {

    private RestHighLevelClient restHighLevelClient;

    @BeforeEach
    public void init() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.56.1:9200")));
    }

    //@Test
    public void testSearchDoc() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.matchAllQuery()).from(0).size(200);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocMatch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.matchQuery("name", "如家")).from(0).size(100);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocMultiMatch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.multiMatchQuery("君悦", "name", "all")).from(0).size(200);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocTerm() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.termQuery("brand", "君悦"));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocRange() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.rangeQuery("price").gte(2500).lte(3000));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocBool() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name", "如家汉庭希尔顿"))
                        .mustNot(QueryBuilders.rangeQuery("price").lte(300))
                        //GeoDistance.ARC精确查询 || GeoDistance.PLANE模糊查询
                        .filter(QueryBuilders.geoDistanceQuery("location").distance(3, DistanceUnit.KILOMETERS).point(22.53D, 114.06D).geoDistance(GeoDistance.ARC)))
                .from(0)
                .size(200);
        searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint(22.53D, 114.06D)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            double distance = new BigDecimal(String.valueOf(hit.getSortValues()[0])).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("距离目的地大约" + distance + "公里 ---> " + hotelDoc);
        }
    }

    //@Test
    public void testSearchDocSortAndPage() throws IOException {
        int page = 1, size = 10;
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().sort("price", SortOrder.ASC);
        searchRequest.source().from((page - 1) * size).size(size);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocHighLight() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.matchQuery("name", "汉庭"))
                .highlighter(new HighlightBuilder().field("name").requireFieldMatch(true));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            Map<String, HighlightField> highLightFieldMap = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highLightFieldMap)) {
                HighlightField highlightField = highLightFieldMap.get("name");
                if (highlightField != null) {
                    String name = highlightField.getFragments()[0].string();
                    hotelDoc.setName(name);
                }
            }
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocGeo() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.geoDistanceQuery("location").distance(5, DistanceUnit.KILOMETERS).point(new GeoPoint("22.53,114.06")));
        searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint("22.53,114.06")).order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //@Test
    public void testSearchDocFunctionScore() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().query(QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery(),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("brand", "汉庭"), ScoreFunctionBuilders.weightFactorFunction(10))
                }).boostMode(CombineFunction.SUM));
        searchRequest.source().from(0).size(200);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    @Test
    public void testSearchDocAggregation() throws IOException {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().aggregation(AggregationBuilders.terms("brand_aggregation").field("brand").size(20));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms brandAggregation = aggregations.get("brand_aggregation");
        List<? extends Terms.Bucket> buckets = brandAggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println(keyAsString);
        }
    }

    @AfterEach
    public void destroy() throws IOException {
        if (this.restHighLevelClient != null) this.restHighLevelClient.close();
    }
}
