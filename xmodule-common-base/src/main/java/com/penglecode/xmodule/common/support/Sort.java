package com.penglecode.xmodule.common.support;

import java.util.Arrays;
import java.util.List;

/**
 * 通用排序对象
 * 
 * @author 	pengpeng
 * @date	2018年10月10日 下午2:14:29
 */
public class Sort implements DtoModel {

	private static final long serialVersionUID = 1L;

	private List<Order> orders;
	
	Sort() {
		super();
	}
	
	Sort(List<Order> orders) {
		super();
		this.orders = orders;
	}
	
	public static Sort by(Order... orders) {
		return new Sort(Arrays.asList(orders));
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public Order first() {
		if(orders != null && orders.size() > 0) {
			return orders.get(0);
		}
		return null;
	}
	
	public static class Order {
		
		public static final String DIRECTION_ASC = "asc";

	    public static final String DIRECTION_DESC = "desc";
		
		private String property;
		
		private String direction;

		Order() {
			super();
		}
		
		Order(String property, String direction) {
			super();
			if(direction != null) {
				direction = direction.toLowerCase();
				direction = DIRECTION_DESC.equals(direction) ? DIRECTION_DESC : DIRECTION_ASC;
			} else {
				direction = DIRECTION_ASC;
			}
			this.property = property;
			this.direction = direction;
		}
		
		public static Order by(String property, String direction) {
			return new Order(property, direction);
		}
		
		public static Order asc(String property) {
			return new Order(property, DIRECTION_ASC);
		}
		
		public static Order desc(String property) {
			return new Order(property, DIRECTION_DESC);
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}
		
		/**
		 * Used by SpringMVC @RequestParam and JAX-RS @QueryParam
		 * @param order
		 * @return
		 */
		public static Order valueOf(String order) {
			if(order != null) {
				String[] orders = order.trim().split(":");
				String prop = null, dir = null;
				if(orders.length == 1) {
					prop = orders[0] == null ? null : orders[0].trim();
					if(prop != null && prop.length() > 0) {
						return Order.asc(prop);
					}
				} else if (orders.length == 2) {
					prop = orders[0] == null ? null : orders[0].trim();
					dir = orders[1] == null ? null : orders[1].trim();
					if(prop != null && prop.length() > 0) {
						return Order.by(prop, dir);
					}
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return property + ":" + direction;
		}
		
	}
	
	@Override
	public String toString() {
		return "Sort " + orders + "";
	}

}
