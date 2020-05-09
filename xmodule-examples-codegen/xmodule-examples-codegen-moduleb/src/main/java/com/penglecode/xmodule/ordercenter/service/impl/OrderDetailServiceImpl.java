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
import com.penglecode.xmodule.ordercenter.mapper.OrderDetailMapper;
import com.penglecode.xmodule.ordercenter.model.OrderDetail;
import com.penglecode.xmodule.ordercenter.service.OrderDetailService;

@Service("orderDetailService")
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createOrderDetail(OrderDetail parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		orderDetailMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateOrderDetail(OrderDetail parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		Map<String, Object> paramMap = parameter.mapBuilder()
												.withFreightAmount()
												.withQuantity()
												.withProductId()
												.withProductName()
												.withProductUrl()
												.withUnitPrice()
												.withOrderId()
												.withSubTotalAmount()
												.build();
		orderDetailMapper.updateModelById(parameter.ofPrimaryKey(), paramMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteOrderDetailById(OrderDetail.PrimaryKey id) {
		ValidationAssert.notNull(id, "id不能为空");
		orderDetailMapper.deleteModelById(id);
	}

	@Override
	public OrderDetail getOrderDetailById(OrderDetail.PrimaryKey id) {
		return ModelDecodeUtils.decodeModel(orderDetailMapper.selectModelById(id));
	}

	@Override
	public List<OrderDetail> getOrderDetailListByPage(OrderDetail condition, Page page, Sort sort) {
		List<OrderDetail> dataList = ModelDecodeUtils.decodeModel(orderDetailMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(orderDetailMapper.selectModelPageCountByExample(condition));
		return dataList;
	}

	@Override
	public List<OrderDetail> getAllOrderDetailList() {
		return ModelDecodeUtils.decodeModel(orderDetailMapper.selectAllModelList());
	}

}