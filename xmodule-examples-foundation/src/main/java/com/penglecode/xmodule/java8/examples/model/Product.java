package com.penglecode.xmodule.java8.examples.model;

public class Product {

	private Long productId;
	
	private String productName;
	
	private Double unitPrice;
	
	private Integer inventory;
	
	private String productType;
	
	private String inventoryDesc;

	public Product() {
		super();
	}

	public Product(Long productId, String productName, Double unitPrice, Integer inventory, String productType) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.inventory = inventory;
		this.productType = productType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getInventoryDesc() {
		return inventoryDesc;
	}

	public void setInventoryDesc(String inventoryDesc) {
		this.inventoryDesc = inventoryDesc;
	}

	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", unitPrice=" + unitPrice
				+ ", inventory=" + inventory + ", productType=" + productType + ", inventoryDesc=" + inventoryDesc
				+ "]";
	}
	
}
