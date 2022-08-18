package com.kk.hotel;

import com.alibaba.fastjson.JSON;
import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.HotelDoc;
import com.kk.hotel.service.HotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HotelDocTest {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private HotelService hotelService;

    @BeforeEach
    public void createRestHighLevelClient() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.56.1:9200")));
    }

    //@Test
    public void testCreateDoc() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        IndexRequest indexRequest = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        indexRequest.source(JSON.toJSONString(hotelDoc), XContentType.JSON);//内容
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    //@Test
    public void testDeleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("hotel").id(String.valueOf(61083L));
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    //@Test
    public void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("hotel").id(String.valueOf(61083L));
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String responseSourceAsString = getResponse.getSourceAsString();//获取内容字符串 ---> 转换为对象
        HotelDoc hotelDoc = JSON.parseObject(responseSourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    //@Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("hotel", "61083");
        updateRequest.doc("price", "99999", "starName", "四钻");
        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    @Test
    public void testBulkDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        Long[] hotelIds = {56977L, 60922L, 395799L, 636080L, 2011785622L};
        for (int i = 0; i < hotelIds.length; i++) {
            Hotel hotel = hotelService.getById(hotelIds[i]);
            HotelDoc hotelDoc = new HotelDoc(hotel);
            bulkRequest.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @AfterEach
    public void CloseRestHighLevelClient() throws IOException {
        if (this.restHighLevelClient != null) restHighLevelClient.close();
    }
}
