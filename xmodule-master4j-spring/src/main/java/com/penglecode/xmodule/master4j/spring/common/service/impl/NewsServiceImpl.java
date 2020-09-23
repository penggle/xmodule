package com.penglecode.xmodule.master4j.spring.common.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.master4j.spring.common.model.News;
import com.penglecode.xmodule.master4j.spring.common.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 23:00
 */
public class NewsServiceImpl implements NewsService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

    private RestTemplate restTemplate;

    @Override
    public List<News> getNewsListByPage(int page) {
        String newsApiUrl = "https://api.apiopen.top/getTencentNews?page={page}";
        ResponseEntity<String> response = restTemplate.exchange(newsApiUrl, HttpMethod.GET, HttpEntity.EMPTY, String.class, Collections.singletonMap("page", page));
        Assert.isTrue(response.getStatusCode().is2xxSuccessful(), "获取数据出错!");
        String json = response.getBody();
        return JsonUtils.json2Object(json, (objectMapper, rootNode) -> {
            List<News> newsList = new ArrayList<>();
            JsonNode jsonNode = rootNode.get("data");
            if(jsonNode.isArray()) {
                ArrayNode dataNodes = (ArrayNode) jsonNode;
                for(Iterator<JsonNode> it = dataNodes.elements(); it.hasNext();) {
                    JsonNode itemNode = it.next();
                    News news = new News();
                    news.setId(JsonUtils.getString(itemNode,"id"));
                    news.setTitle(JsonUtils.getString(itemNode,"title"));
                    news.setUrl(JsonUtils.getString(itemNode,"vurl"));
                    news.setImg(JsonUtils.getString(itemNode,"img"));
                    news.setCategory1(JsonUtils.getString(itemNode,"category1_chn"));
                    news.setCategory2(JsonUtils.getString(itemNode,"category2_chn"));
                    news.setIntro(JsonUtils.getString(itemNode,"intro"));
                    news.setTags(JsonUtils.getString(itemNode,"tags"));
                    news.setSource(JsonUtils.getString(itemNode,"source"));
                    news.setPublishTime(JsonUtils.getString(itemNode,"publish_time"));
                    newsList.add(news);
                }
            }
            return newsList;
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(restTemplate == null) {
            OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
            requestFactory.setConnectTimeout(6000);
            requestFactory.setReadTimeout(60000);
            requestFactory.setWriteTimeout(60000);
            restTemplate = new RestTemplate(requestFactory);
            LOGGER.info(">>> init restTemplate: {}", restTemplate);
        }
    }

}
