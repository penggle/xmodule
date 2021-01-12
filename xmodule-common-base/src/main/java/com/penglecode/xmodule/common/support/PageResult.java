package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.GlobalConstants;

/**
 * 通用返回结果类(针对分页)
 * 
 * @param <T>
 * @author  pengpeng
 * @date 	 2015年5月7日 上午9:53:27
 * @version 1.0
 */
public class PageResult<T> extends Result<T> {

	private static final long serialVersionUID = 1L;
	
	/** 当存在分页查询时此值为总记录数 */
	private int totalRowCount;

	PageResult() {}
	
	PageResult(Result<T> result, int totalRowCount) {
		super(result);
		this.totalRowCount = totalRowCount;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	protected void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	public static PageBuilder success() {
		return new PageBuilder(Boolean.TRUE, GlobalConstants.RESULT_CODE_SUCCESS, "OK");
	}
	
	public static PageBuilder failure() {
		return new PageBuilder(Boolean.FALSE, GlobalConstants.RESULT_CODE_FAILURE, "Internal Server Error");
	}
	
	@Override
	public String toString() {
		return "PageResult [success=" + isSuccess() + ", code=" + getCode() + ", message="
				+ getMessage() + ", data=" + getData() + ", totalRowCount=" + getTotalRowCount() + "]";
	}

	public static class PageBuilder extends Builder {
		
		private int totalRowCount = 0;
		
		PageBuilder(boolean success, int code, String message) {
			super(success, code, message);
		}
		
		public PageBuilder totalRowCount(int totalRowCount) {
			this.totalRowCount = totalRowCount;
			return this;
		}
		
		@Override
		public PageBuilder code(int code) {
			return (PageBuilder) super.code(code);
		}
		
		@Override
		public PageBuilder message(String message) {
			return (PageBuilder) super.message(message);
		}
		
		@Override
		public PageBuilder data(Object data) {
			return (PageBuilder) super.data(data);
		}
		
		@Override
		public <T> PageResult<T> build() {
			return new PageResult<>(super.build(), totalRowCount);
		}
		
	}
	
}
