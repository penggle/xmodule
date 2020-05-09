package com.penglecode.xmodule.ordercenter.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.ordercenter.mapper.OrderMapper;
import com.penglecode.xmodule.ordercenter.model.Order;
import com.penglecode.xmodule.ordercenter.service.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createOrder(Order parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		orderMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateOrder(Order parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		Map<String, Object> paramMap = parameter.mapBuilder()
												.withFreightAmount()
												.withOrderId()
												.withRemark()
												.withPaymentType()
												.withTotalAmount()
												.withStatus()
												.withCustomerId()
												.withOrderTime()
												.build();
		orderMapper.updateModelById(parameter.getOrderId(), paramMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteOrderById(Long orderId) {
		ValidationAssert.notNull(orderId, "orderId不能为空");
		orderMapper.deleteModelById(orderId);
	}

	@Override
	public Order getOrderById(Long orderId) {
		return ModelDecodeUtils.decodeModel(orderMapper.selectModelById(orderId));
	}

	@Override
	public List<Order> getOrderListByPage(Order condition, Page page, Sort sort) {
		List<Order> dataList = ModelDecodeUtils.decodeModel(orderMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(orderMapper.selectModelPageCountByExample(condition));
		return dataList;
	}

	@Override
	public List<Order> getAllOrderList() {
		return ModelDecodeUtils.decodeModel(orderMapper.selectAllModelList());
	}

}