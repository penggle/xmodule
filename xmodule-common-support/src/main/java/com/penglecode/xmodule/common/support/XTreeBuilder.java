package com.penglecode.xmodule.common.support;

import java.util.List;

/**
 * java树形数据构造器
 * 
 * @param <I>
 * @param <T>
 * @author 	pengpeng
 * @date   		2017年3月29日 下午3:09:25
 * @version 	1.0
 */
public interface XTreeBuilder<I,T extends Comparable<T>> {
	
	/**
	 * 根据根treeNodeId及全部TreeNode列表来渲染一颗具有父子关系的对象树
	 * @param rootTreeNodeIds
	 * @param allTreeNodeList
	 * @return
	 */
	public List<T> buildObjectTree(List<I> rootTreeNodeIds, List<T> allTreeNodeList);
	
	/**
	 * 根据根treeNodeId及全部TreeNode列表来渲染一颗具有父子关系的对象树,同时提供对树节点数据类型的转换
	 * @param rootTreeNodeIds
	 * @param allTreeNodeList
	 * @param treeNodeBuilder
	 * @return
	 */
	public <R> List<R> buildObjectTree(List<I> rootTreeNodeIds, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeConverter);
	
	/**
	 * 根据根treeNodeId及全部TreeNode列表来渲染一颗具有父子关系的对象树,同时提供对树节点数据类型的转换
	 * @param allObjectTreeList		- 该allObjectTreeList是通过buildObjectTree(List<I> rootTreeNodeIds, List<T> allTreeNodeList)方法返回的结果
	 * @param treeNodeBuilder
	 * @return
	 */
	public <R> List<R> buildObjectTree(List<T> allObjectTreeList, TreeNodeConverter<T,R> treeNodeConverter);
	
	public List<T> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList);
	
	public <R> List<R> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeConverter);
	
}
