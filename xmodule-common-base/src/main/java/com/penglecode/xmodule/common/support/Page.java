package com.penglecode.xmodule.common.support;

/**
 * 通用分页Page对象
 * 
 * @author 	pengpeng
 * @date	2018年10月10日 下午2:14:46
 */
public class Page implements DtoModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	private Integer currentPage = 1;
	
	/**
	 * 每页显示多少条
	 */
	private Integer pageSize = 10;
	
	/**
	 * 查询总记录数
	 */
	private Integer totalRowCount = 0;
	
	/**
	 * 可分多少页
	 */
	private Integer totalPageCount = 0;
	
	Page() {
		super();
	}

	Page(Integer currentPage, Integer pageSize) {
		super();
		if(currentPage != null && currentPage > 0){
			this.currentPage = currentPage;
		}
		if(pageSize != null && pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	Page(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		this(currentPage, pageSize);
		if(totalRowCount != null){
			this.totalRowCount = totalRowCount;
		}
	}

	public static Page of(Integer currentPage, Integer pageSize) {
		return new Page(currentPage, pageSize);
	}
	
	public static Page of(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		return new Page(currentPage, pageSize, totalRowCount);
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
		getTotalPageCount(); //计算totalPageCount
	}

	public Integer getTotalPageCount() {
		if(totalRowCount <= 0){
			totalPageCount = 0;
		}else{
			totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
		}
		return totalPageCount;
	}

	public Integer getOffset() {
		return (currentPage - 1) * pageSize;
	}
	
	public Integer getLimit() {
		return pageSize;
	}
	
	public String toString() {
		return "Pager [currentPage=" + currentPage + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + "]";
	}

}
