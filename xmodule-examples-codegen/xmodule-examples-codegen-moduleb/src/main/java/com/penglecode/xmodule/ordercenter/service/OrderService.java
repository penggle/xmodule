package com.penglecode.xmodule.ordercenter.service;

import java.util.List;


import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.ordercenter.model.Order;

/**
 * 订单服务
 * 
 * @author 	AutoGenerator
 * @date	2020年08月04日 下午 20:40:12
 */
public interface OrderService {

	/**
	 * 创建订单
	 * @param parameter
	 */
	public void createOrder(Order parameter);
	
	/**
	 * 根据ID更新订单
	 * @param parameter
	 */
	public void updateOrder(Order parameter);
	
	/**
	 * 根据ID删除订单
	 * @param orderId
	 */
	public void deleteOrderById(Long orderId);
	
	/**
	 * 根据ID获取订单
	 * @param orderId
	 * @return
	 */
	public Order getOrderById(Long orderId);
	
	/**
	 * 根据条件查询订单列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<Order> getOrderListByPage(Order condition, Page page, Sort sort);
	
	/**
	 * 获取所有订单列表
	 * @return
	 */
	public List<Order> getAllOrderList();
	
}