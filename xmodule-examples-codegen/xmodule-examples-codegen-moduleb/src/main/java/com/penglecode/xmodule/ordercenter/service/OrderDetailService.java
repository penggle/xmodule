package com.penglecode.xmodule.ordercenter.service;

import java.util.List;


import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.ordercenter.model.OrderDetail;

/**
 * 订单明细服务
 * 
 * @author 	AutoGenerator
 * @date	2020年04月22日 下午 15:50:15
 */
public interface OrderDetailService {

	/**
	 * 创建订单明细
	 * @param parameter
	 */
	public void createOrderDetail(OrderDetail parameter);
	
	/**
	 * 根据ID更新订单明细
	 * @param parameter
	 */
	public void updateOrderDetail(OrderDetail parameter);
	
	/**
	 * 根据ID删除订单明细
	 * @param id
	 */
	public void deleteOrderDetailById(OrderDetail.PrimaryKey id);
	
	/**
	 * 根据ID获取订单明细
	 * @param id
	 * @return
	 */
	public OrderDetail getOrderDetailById(OrderDetail.PrimaryKey id);
	
	/**
	 * 根据条件查询订单明细列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<OrderDetail> getOrderDetailListByPage(OrderDetail condition, Page page, Sort sort);
	
	/**
	 * 获取所有订单明细列表
	 * @return
	 */
	public List<OrderDetail> getAllOrderDetailList();
	
}