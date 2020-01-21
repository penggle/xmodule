package com.penglecode.xmodule.common.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * 基于Twitter/snowflake 算法的分布式全局id生成器
 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加)) 
 * 
 * 整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午9:24:42
 * @version  	1.0
 */
public class TwitterSnowflakeIdGenerator implements IdGenerator<Long> {

	private final static long twepoch = 1288834974657L;
	// 机器标识位数
	private final static long workerIdBits = 5L;
	// 数据中心标识位数
	private final static long datacenterIdBits = 5L;
	// 机器ID最大值
	private final static long MAX_WORKER_ID = -1L ^ (-1L << workerIdBits);
	// 数据中心ID最大值
	private final static long MAX_DATA_CENTER_ID = -1L ^ (-1L << datacenterIdBits);
	// 毫秒内自增位
	private final static long sequenceBits = 12L;
	// 机器ID偏左移12位
	private final static long workerIdShift = sequenceBits;
	// 数据中心ID左移17位
	private final static long datacenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒左移22位
	private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

	private static long lastTimestamp = -1L;

	private long sequence = 0L;
	private final long datacenterId;
	private final long workerId;

	/**
	 * @param datacenterId	- 数据中心id,范围[0,31],比如用户中心为0,订单中心为1等等
	 * @param workerId		- 机器ID,范围[0,31],比如用户中心有10台机器做集群,则依次分配0-9
	 */
	public TwitterSnowflakeIdGenerator(long datacenterId, long workerId) {
		Assert.isTrue(datacenterId <= MAX_DATA_CENTER_ID && datacenterId >= 0, "datacenter Id can't be greater than %d or less than 0");
		Assert.isTrue(workerId <= MAX_WORKER_ID && workerId >= 0, "worker Id can't be greater than %d or less than 0");
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized Long nextId() {
		return getNextId();
	}
	
	public synchronized List<Long> nextIds(int batchSize) {
		Assert.isTrue(batchSize > 1, "worker Id can't be less than 1");
		List<Long> list = new ArrayList<Long>((int)(batchSize * 1.5));
		for(int i = 0; i < batchSize; i++){
			list.add(getNextId());
		}
		return list;
	}
	
	protected long getNextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new KeyGeneratorException("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
		}

		if (lastTimestamp == timestamp) {
			// 当前毫秒内，则+1
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				// 当前毫秒内计数满了，则等待下一秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		// ID偏移组合生成最终的ID，并返回ID
		long nextId = ((timestamp - twepoch) << timestampLeftShift)
				| (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;

		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

}