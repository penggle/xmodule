package com.penglecode.xmodule.master4j.spring.aop.aspect.service;

import com.penglecode.xmodule.master4j.spring.common.model.News;

import java.util.List;

/**
 * News新闻服务
 *
 * https://api.apiopen.top/api.html
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 22:38
 */
public interface NewsService extends RemoteService {

    public List<News> getNewsListByPage(int page);

    public void publishNews(News news);

}
