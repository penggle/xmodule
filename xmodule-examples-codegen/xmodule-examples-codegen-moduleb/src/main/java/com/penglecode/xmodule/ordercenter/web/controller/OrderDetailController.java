package com.penglecode.xmodule.ordercenter.web.controller;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;
import com.penglecode.xmodule.ordercenter.model.OrderDetail;
import com.penglecode.xmodule.ordercenter.service.OrderDetailService;

/**
 * 订单明细Controller
 * 
 * @author 	AutoGenerator
 * @date	2020年04月22日 下午 17:18:57
 */
@RestController
public class OrderDetailController extends HttpApiResourceSupport{

	@Resource(name="orderDetailService")
	private OrderDetailService orderDetailService;
	
	/**
	 * 根据条件查询订单明细列表(分页、排序)
	 * @param condition		- 查询参数
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	@GetMapping(value="/api/ordercenter/order/detail/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public PageResult<List<OrderDetail>> getOrderDetailListByPage(OrderDetail condition, Page page, Sort sort) {
		List<OrderDetail> dataList = orderDetailService.getOrderDetailListByPage(condition, page, sort);
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}
	
	/**
	 * 创建订单明细
	 * @param parameter		- 创建参数
	 * @return
	 */
	@PostMapping(value="/api/ordercenter/order/detail/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> createOrderDetail(@RequestBody OrderDetail parameter) {
		orderDetailService.createOrderDetail(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新订单明细
	 * @param parameter		- 更新参数
	 * @return
	 */
	@PutMapping(value="/api/ordercenter/order/detail/update", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> updateOrderDetail(@RequestBody OrderDetail parameter) {
		orderDetailService.updateOrderDetail(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据订单明细ID获取订单明细详情
	 * @param id		- 订单明细ID
	 * @return
	 */
	@GetMapping(value="/api/ordercenter/order/detail/{orderId}/{productId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<OrderDetail> getOrderDetailById(OrderDetail.PrimaryKey id) {
		OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
		return Result.success().message("OK").data(orderDetail).build();
	}
	
	/**
	 * 根据订单明细ID删除订单明细
	 * @param id		- 订单明细ID
	 * @return
	 */
	@DeleteMapping(value="/api/ordercenter/order/detail/{orderId}/{productId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> deleteOrderDetailById(OrderDetail.PrimaryKey id) {
		orderDetailService.deleteOrderDetailById(id);
		return Result.success().message("删除成功!").build();
	}
	
}