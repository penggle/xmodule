package com.penglecode.xmodule.master4j.spring.common.service;

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
public interface NewsService {

    public List<News> getNewsListByPage(int page);

}
