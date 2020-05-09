package com.penglecode.xmodule.ordercenter.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;
import com.penglecode.xmodule.ordercenter.model.Order;
import com.penglecode.xmodule.ordercenter.service.OrderService;

/**
 * 订单Controller
 * 
 * @author 	pengpeng
 * @date 	2020年4月22日 下午2:14:32
 */
@RestController
public class OrderController extends HttpApiResourceSupport {

	@Resource(name="orderService")
	private OrderService orderService;
	
	/**
	 * 根据条件查询订单列表(分页、排序)
	 * @param condition		- 查询参数
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	@GetMapping(value="/api/order/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public PageResult<List<Order>> getOrderListByPage(Order condition, Page page, Sort sort) {
		List<Order> dataList = orderService.getOrderListByPage(condition, page, sort);
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}
	
	/**
	 * 创建订单
	 * @param parameter		- 创建参数
	 * @return
	 */
	@PostMapping(value="/api/order/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> createOrder(@RequestBody Order parameter) {
		orderService.createOrder(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新订单
	 * @param parameter		- 更新参数
	 * @return
	 */
	@PutMapping(value="/api/order/update", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> updateOrder(@RequestBody Order parameter) {
		orderService.updateOrder(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据订单ID获取订单详情
	 * @param orderId		- 订单ID
	 * @return
	 */
	@GetMapping(value="/api/order/{orderId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Order> getOrderById(@PathVariable("orderId") Long orderId) {
		Order order = orderService.getOrderById(orderId);
		return Result.success().message("OK").data(order).build();
	}
	
	/**
	 * 根据订单ID删除订单
	 * @param orderId		- 订单ID
	 * @return
	 */
	@DeleteMapping(value="/api/order/{orderId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> deleteOrderById(@PathVariable("orderId") Long orderId) {
		orderService.deleteOrderById(orderId);
		return Result.success().message("删除成功!").build();
	}
	
}
