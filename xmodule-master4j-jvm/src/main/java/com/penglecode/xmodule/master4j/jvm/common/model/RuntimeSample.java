package com.penglecode.xmodule.master4j.jvm.common.model;

/**
 * 运行时采样信息
 * 
 * @author 	pengpeng
 * @date 	2020年7月8日 上午8:44:47
 */
public class RuntimeSample {

	/**
	 * 操作系统版本
	 */
	private String osName;
	
	/**
	 * 操作系统架构
	 */
	private String osArch;
	
	/**
	 * 操作系统版本
	 */
	private String osVersion;
	
	/**
	 * 核数
	 */
	private Integer coreSize;
	
	/**
	 * 总物理内存
	 */
	private Integer totalPhyMemory;
	
	/**
	 * 空闲物理内存
	 */
	private Integer freePhyMemory;
	
	/**
	 * 已用物理内存
	 */
	private Integer	 usedPhyMemory;
	
	/**
	 * VM提供商
	 */
	private String vmVendor;
	
	/**
	 * VM名称
	 */
	private String vmName;
	
	/**
	 * VM版本
	 */
	private String vmVersion;
	
	/**
	 * VM信息
	 */
	private String vmInfo;
	
	/**
	 * VM参数
	 */
	private String vmArgs;
	
	/**
	 * 最大JVM内存(java虚拟机能构从操作系统那里挖到的最大的内存)
	 */
	private Integer maxVmMemory;
	
	/**
	 * 总JVM内存(java虚拟机现在已经从操作系统那里挖过来的内存大小)
	 */
	private Integer totalVmMemory;
	
	/**
	 * 空闲JVM内存
	 */
	private Integer freeVmMemory;
	
	/**
	 * 已用JVM内存
	 */
	private Integer	 usedVmMemory;
	
	/**
	 * JAVA提供商
	 */
	private String javaVendor;
	
	/**
	 * JAVA版本
	 */
	private String javaVersion;
	
	private Object attachment;

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public Integer getCoreSize() {
		return coreSize;
	}

	public void setCoreSize(Integer coreSize) {
		this.coreSize = coreSize;
	}

	public Integer getTotalPhyMemory() {
		return totalPhyMemory;
	}

	public void setTotalPhyMemory(Integer totalPhyMemory) {
		this.totalPhyMemory = totalPhyMemory;
	}

	public Integer getFreePhyMemory() {
		return freePhyMemory;
	}

	public void setFreePhyMemory(Integer freePhyMemory) {
		this.freePhyMemory = freePhyMemory;
	}

	public Integer getUsedPhyMemory() {
		return usedPhyMemory;
	}

	public void setUsedPhyMemory(Integer usedPhyMemory) {
		this.usedPhyMemory = usedPhyMemory;
	}

	public String getVmVendor() {
		return vmVendor;
	}

	public void setVmVendor(String vmVendor) {
		this.vmVendor = vmVendor;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public String getVmInfo() {
		return vmInfo;
	}

	public void setVmInfo(String vmInfo) {
		this.vmInfo = vmInfo;
	}

	public String getVmArgs() {
		return vmArgs;
	}

	public void setVmArgs(String vmArgs) {
		this.vmArgs = vmArgs;
	}

	public Integer getMaxVmMemory() {
		return maxVmMemory;
	}

	public void setMaxVmMemory(Integer maxVmMemory) {
		this.maxVmMemory = maxVmMemory;
	}

	public Integer getTotalVmMemory() {
		return totalVmMemory;
	}

	public void setTotalVmMemory(Integer totalVmMemory) {
		this.totalVmMemory = totalVmMemory;
	}

	public Integer getFreeVmMemory() {
		return freeVmMemory;
	}

	public void setFreeVmMemory(Integer freeVmMemory) {
		this.freeVmMemory = freeVmMemory;
	}

	public Integer getUsedVmMemory() {
		return usedVmMemory;
	}

	public void setUsedVmMemory(Integer usedVmMemory) {
		this.usedVmMemory = usedVmMemory;
	}

	public String getJavaVendor() {
		return javaVendor;
	}

	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "RuntimeSample [osName=" + osName + ", osArch=" + osArch + ", osVersion=" + osVersion + ", coreSize="
				+ coreSize + ", totalPhyMemory=" + totalPhyMemory + ", freePhyMemory=" + freePhyMemory
				+ ", usedPhyMemory=" + usedPhyMemory + ", vmVendor=" + vmVendor + ", vmName=" + vmName + ", vmVersion="
				+ vmVersion + ", vmInfo=" + vmInfo + ", maxVmMemory=" + maxVmMemory + ", totalVmMemory=" + totalVmMemory
				+ ", freeVmMemory=" + freeVmMemory + ", usedVmMemory=" + usedVmMemory + ", javaVendor=" + javaVendor
				+ ", javaVersion=" + javaVersion + "]";
	}
	
}
