package com.penglecode.xmodule.common.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 构造java树形数据的抽象基类
 * @param <I>
 * @param <T>
 * @author 	pengpeng
 * @date   		2017年4月11日 下午2:00:47
 * @version 	1.0
 */
public abstract class AbstractXTreeBuilder<I, T extends Comparable<T>> implements XTreeBuilder<I, T> {

	private Integer defaultRootLevel = 0;
	
	public AbstractXTreeBuilder() {
		super();
	}

	public AbstractXTreeBuilder(Integer defaultRootLevel) {
		super();
		this.defaultRootLevel = defaultRootLevel;
	}

	public Integer getDefaultRootLevel() {
		return defaultRootLevel;
	}

	public void setDefaultRootLevel(Integer defaultRootLevel) {
		this.defaultRootLevel = defaultRootLevel;
	}

	public List<T> buildObjectTree(List<I> rootTreeNodeIds, List<T> allTreeNodeList) {
		Assert.notEmpty(rootTreeNodeIds, "Parameter 'rootTreeNodeIds' can not be empty!");
		Assert.notEmpty(allTreeNodeList, "Parameter 'allTreeNodeList' can not be empty!");
		List<T> rootLevelTreeNodeList = new ArrayList<T>();
		for(I rootTreeNodeId : rootTreeNodeIds) {
			T rootTreeNode = getTreeNodeById(rootTreeNodeId, allTreeNodeList);
			if(rootTreeNode != null){
				rootLevelTreeNodeList.add(rootTreeNode);
			}else{
				List<T> rootChildNodeList = getDirectChildNodeList(rootTreeNodeId, allTreeNodeList);
				if(!CollectionUtils.isEmpty(rootChildNodeList)) {
					rootLevelTreeNodeList.addAll(rootChildNodeList);
				}
			}
		}
		if(!CollectionUtils.isEmpty(rootLevelTreeNodeList)){
			for(T rootLevelTreeNode : rootLevelTreeNodeList){
				recurisiveLoadChilds(rootLevelTreeNode, allTreeNodeList, getDefaultRootLevel());
			}
		}
		return rootLevelTreeNodeList == null ? new ArrayList<T>() : rootLevelTreeNodeList;
	}

	public <R> List<R> buildObjectTree(List<I> rootTreeNodeIds, List<T> allTreeNodeList, TreeNodeConverter<T, R> treeNodeConverter) {
		Assert.notEmpty(rootTreeNodeIds, "Parameter 'rootTreeNodeIds' can not be empty!");
		Assert.notEmpty(allTreeNodeList, "Parameter 'allTreeNodeList' can not be empty!");
		Assert.notNull(treeNodeConverter, "Parameter 'treeNodeConverter' can not be null!");
		
		List<T> rootLevelTreeNodeList = buildObjectTree(rootTreeNodeIds, allTreeNodeList);
		List<R> resultTreeNodeList = new ArrayList<R>();
		if(!CollectionUtils.isEmpty(rootLevelTreeNodeList)){
			for(T rootLevelTreeNode : rootLevelTreeNodeList){
				R resultTreeNode = treeNodeConverter.convertTreeNode(rootLevelTreeNode);
				resultTreeNodeList.add(resultTreeNode);
				recurisiveConvertTreeNode(rootLevelTreeNode, resultTreeNode, treeNodeConverter);
			}
		}
		return resultTreeNodeList;
	}

	@Override
	public <R> List<R> buildObjectTree(List<T> allObjectTreeList, TreeNodeConverter<T, R> treeNodeConverter) {
		Assert.notEmpty(allObjectTreeList, "Parameter 'allObjectTreeList' can not be empty!");
		Assert.notNull(treeNodeConverter, "Parameter 'treeNodeConverter' can not be null!");
		
		List<R> resultTreeNodeList = new ArrayList<R>();
		if(!CollectionUtils.isEmpty(allObjectTreeList)){
			for(T rootLevelTreeNode : allObjectTreeList){
				R resultTreeNode = treeNodeConverter.convertTreeNode(rootLevelTreeNode);
				resultTreeNodeList.add(resultTreeNode);
				recurisiveConvertTreeNode(rootLevelTreeNode, resultTreeNode, treeNodeConverter);
			}
		}
		return resultTreeNodeList;
	}

	public <R> void recurisiveConvertTreeNode(T currentTargetTreeNode, R currentResultTreeNode, TreeNodeConverter<T, R> treeNodeConverter) {
		if(currentTargetTreeNode != null && currentResultTreeNode != null){
			List<T> targetTreeNodeChildList = getSubTreeNodeList(currentTargetTreeNode);
			if(!CollectionUtils.isEmpty(targetTreeNodeChildList)){
				List<R> resultTreeNodeChildList = new ArrayList<R>();
				for(T targetTreeNode : targetTreeNodeChildList){
					R resultTreeNode = treeNodeConverter.convertTreeNode(targetTreeNode);
					resultTreeNodeChildList.add(resultTreeNode);
					recurisiveConvertTreeNode(targetTreeNode, resultTreeNode, treeNodeConverter);
				}
				treeNodeConverter.setSubTreeNodeList(currentResultTreeNode, resultTreeNodeChildList);
			}
		}
	}
	
	/**
	 * 递归构加载对象树
	 * 
	 * @param currentTreeNode
	 * @param allTreeNodeList
	 */
	public void recurisiveLoadChilds(T currentTreeNode, List<T> allTreeNodeList, int level) {
		if(currentTreeNode != null && !CollectionUtils.isEmpty(allTreeNodeList)){
			setTreeNodeLevel(currentTreeNode, level);
			
			attachTreeNodePath(currentTreeNode, allTreeNodeList);
			
			List<T> directChildList = getDirectChildNodeList(getTreeNodeId(currentTreeNode), allTreeNodeList);
			if(!CollectionUtils.isEmpty(directChildList)){
				Collections.sort(directChildList);
			}
			setSubTreeNodeList(currentTreeNode, directChildList);
			if(!CollectionUtils.isEmpty(directChildList)){
				for(T childTreeNode : directChildList){
					recurisiveLoadChilds(childTreeNode, allTreeNodeList, level + 1);
				}
			}
		}
	}
	
	protected void attachTreeNodePath(T currentTreeNode, List<T> allTreeNodeList) {
		String parentPath = "";
		I parentTreeNodeId = getParentTreeNodeId(currentTreeNode);
		if(parentTreeNodeId != null){
			T parentTreeNode = getTreeNodeById(parentTreeNodeId, allTreeNodeList);
			if(parentTreeNode != null){
				parentPath = StringUtils.defaultIfEmpty(getTreeNodePath(parentTreeNode), "");
			}
		}
		String currentPath = parentPath + "/" + getTreeNodeId(currentTreeNode);
		setTreeNodePath(currentTreeNode, currentPath);
	}
	
	/**
	 * 根据treeNodeId获取TreeNode对象
	 * @param treeNodeId
	 * @param allTreeNodeList
	 * @return
	 */
	protected T getTreeNodeById(I treeNodeId, List<T> allTreeNodeList) {
		if(CollectionUtils.isEmpty(allTreeNodeList) || ObjectUtils.isEmpty(treeNodeId)){
			return null;
		}
		for(T treeNode : allTreeNodeList){
			if(treeNode != null && treeNodeId.equals(getTreeNodeId(treeNode))){
				return treeNode;
			}
		}
		return null;
	}
	
	/**
	 * 根据parentTreeNodeId获取直接子节点
	 * @param parentTreeNodeId
	 * @param allTreeNodeList
	 * @return
	 */
	public List<T> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList) {
		List<T> childList = new ArrayList<T>();
		if(CollectionUtils.isEmpty(allTreeNodeList) || ObjectUtils.isEmpty(parentTreeNodeId)){
			return childList;
		}
		for(T treeNode : allTreeNodeList){
			if(treeNode != null && parentTreeNodeId.equals(getParentTreeNodeId(treeNode))){
				childList.add(treeNode);
			}
		}
		return childList;
	}
	
	public <R> List<R> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList, TreeNodeConverter<T, R> treeNodeBuilder) {
		List<R> childList = new ArrayList<R>();
		if(CollectionUtils.isEmpty(allTreeNodeList) || ObjectUtils.isEmpty(parentTreeNodeId) || treeNodeBuilder == null){
			return childList;
		}
		for(T treeNode : allTreeNodeList){
			if(treeNode != null && parentTreeNodeId.equals(getParentTreeNodeId(treeNode))){
				childList.add(treeNodeBuilder.convertTreeNode(treeNode));
			}
		}
		return childList;
	}
	
	protected abstract I getParentTreeNodeId(T treeNode);
	
	protected abstract I getTreeNodeId(T treeNode);
	
	protected abstract void setSubTreeNodeList(T treeNode, List<T> directChildList);
	
	protected abstract List<T> getSubTreeNodeList(T treeNode);
	
	protected abstract void setTreeNodeLevel(T treeNode, Integer level);
	
	protected abstract void setTreeNodePath(T treeNode, String path);
	
	protected abstract String getTreeNodePath(T treeNode);
	
}
