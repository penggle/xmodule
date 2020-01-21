package com.penglecode.xmodule.common.support;

import java.util.List;

/**
 * TreeNode转换器
 * @param <T>
 * @param <R>
 * @author 	pengpeng
 * @date   		2017年3月29日 下午2:48:48
 * @version 	1.0
 */
public interface TreeNodeConverter<T,R> {

	public R convertTreeNode(T targetTreeNode);
	
	public void setSubTreeNodeList(R resultTreeNode, List<R> subTreeNodeList);
	
}
