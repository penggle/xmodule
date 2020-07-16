package com.penglecode.xmodule.master4j.jvm.chapter3.gc;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import com.penglecode.xmodule.master4j.jvm.common.model.RuntimeSample;
import com.penglecode.xmodule.master4j.jvm.common.service.RuntimeSampleService;

/**
 * 测试自JDK9以后出现的统一日志框架
 * 
 * 1、查看GC基本信息，JDK9之前使用-XX:+PrintGC，JDK9开始使用-Xlog:gc：
 * 		VM Args：-Xmx20M -Xlog:gc
 * 		
 * 		[0.030s][info][gc] Using G1
 * 		[2.028s][info][gc] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 9M->1M(20M) 5.554ms
 * 		[2.134s][info][gc] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 9M->1M(20M) 8.990ms
 * 		[2.229s][info][gc] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 10M->2M(20M) 7.383ms
 * 		[2.331s][info][gc] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 12M->2M(20M) 5.668ms
 * 		[2.412s][info][gc] GC(4) Pause Young (Normal) (G1 Evacuation Pause) 12M->2M(20M) 2.766ms
 * 		[2.489s][info][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 12M->2M(20M) 4.558ms
 * 		[2.563s][info][gc] GC(6) Pause Young (Normal) (G1 Evacuation Pause) 12M->2M(20M) 2.872ms
 * 
 * 2、查看GC详细信息，JDK9之前使用-XX:+PrintGCDetails，JDK9开始使用-Xlog:gc*：
 * 		VM Args：-Xmx20M -Xlog:gc* 		(如果把日志级别调整到Debug或者Trace将获得更多信息，本例子中并没有)
 * 
 * 		[0.031s][info][gc,heap] Heap region size: 1M
 * 		[0.034s][info][gc     ] Using G1
 * 		[0.034s][info][gc,heap,coops] Heap address: 0x00000000fec00000, size: 20 MB, Compressed Oops mode: 32-bit
 * 		[1.905s][info][gc,start     ] GC(0) Pause Young (Normal) (G1 Evacuation Pause)
 * 		[1.905s][info][gc,task      ] GC(0) Using 2 workers of 8 for evacuation
 * 		[1.913s][info][gc,phases    ] GC(0)   Pre Evacuate Collection Set: 0.0ms
 * 		[1.913s][info][gc,phases    ] GC(0)   Evacuate Collection Set: 6.8ms
 * 		[1.913s][info][gc,phases    ] GC(0)   Post Evacuate Collection Set: 0.5ms
 * 		[1.913s][info][gc,phases    ] GC(0)   Other: 0.2ms
 * 		[1.913s][info][gc,heap      ] GC(0) Eden regions: 9->0(8)
 * 		[1.913s][info][gc,heap      ] GC(0) Survivor regions: 0->2(2)
 * 		[1.913s][info][gc,heap      ] GC(0) Old regions: 0->0
 * 		[1.913s][info][gc,heap      ] GC(0) Humongous regions: 0->0
 * 		[1.913s][info][gc,metaspace ] GC(0) Metaspace: 6774K->6774K(1056768K)
 * 		[1.913s][info][gc           ] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 9M->1M(20M) 7.634ms
 * 		[1.913s][info][gc,cpu       ] GC(0) User=0.00s Sys=0.00s Real=0.01s
 * 		[2.015s][info][gc,start     ] GC(1) Pause Young (Normal) (G1 Evacuation Pause)
 * 		[2.015s][info][gc,task      ] GC(1) Using 2 workers of 8 for evacuation
 * 		[2.022s][info][gc,phases    ] GC(1)   Pre Evacuate Collection Set: 0.0ms
 * 		[2.022s][info][gc,phases    ] GC(1)   Evacuate Collection Set: 6.0ms
 * 		[2.022s][info][gc,phases    ] GC(1)   Post Evacuate Collection Set: 0.7ms
 * 		[2.022s][info][gc,phases    ] GC(1)   Other: 0.1ms
 * 		[2.022s][info][gc,heap      ] GC(1) Eden regions: 8->0(9)
 * 		[2.022s][info][gc,heap      ] GC(1) Survivor regions: 2->1(2)
 * 		[2.022s][info][gc,heap      ] GC(1) Old regions: 0->1
 * 		[2.022s][info][gc,heap      ] GC(1) Humongous regions: 0->0
 * 		[2.022s][info][gc,metaspace ] GC(1) Metaspace: 6775K->6775K(1056768K)
 * 		[2.022s][info][gc           ] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 9M->1M(20M) 6.979ms
 * 		[2.022s][info][gc,cpu       ] GC(1) User=0.00s Sys=0.00s Real=0.01s
 * 
 * 3、查看GC前后的堆、方法区可用容量变化，在JDK9之前使用XX:+PrintHeapAtGC，JDK9开始使用-Xlog:gc+heap=debug
 * 		VM Args：-Xmx20M -Xlog:gc+heap=debug
 * 
 * 		[0.027s][info][gc,heap] Heap region size: 1M
 * 		[0.027s][debug][gc,heap] Minimum heap 8388608  Initial heap 20971520  Maximum heap 20971520
 * 		[1.956s][debug][gc,heap] GC(0) Heap before GC invocations=0 (full 0): garbage-first heap   total 20480K, used 9216K [0x00000000fec00000, 0x0000000100000000)
 * 		[1.956s][debug][gc,heap] GC(0)   region size 1024K, 9 young (9216K), 0 survivors (0K)
 * 		[1.956s][debug][gc,heap] GC(0)  Metaspace       used 6772K, capacity 6888K, committed 7168K, reserved 1056768K
 * 		[1.956s][debug][gc,heap] GC(0)   class space    used 608K, capacity 652K, committed 768K, reserved 1048576K
 * 		[1.963s][info ][gc,heap] GC(0) Eden regions: 9->0(8)
 * 		[1.963s][info ][gc,heap] GC(0) Survivor regions: 0->2(2)
 * 		[1.963s][info ][gc,heap] GC(0) Old regions: 0->0
 * 		[1.963s][info ][gc,heap] GC(0) Humongous regions: 0->0
 * 		[1.963s][debug][gc,heap] GC(0) Heap after GC invocations=1 (full 0): garbage-first heap   total 20480K, used 1744K [0x00000000fec00000, 0x0000000100000000)
 * 		[1.963s][debug][gc,heap] GC(0)   region size 1024K, 2 young (2048K), 2 survivors (2048K)
 * 		[1.963s][debug][gc,heap] GC(0)  Metaspace       used 6772K, capacity 6888K, committed 7168K, reserved 1056768K
 * 		[1.963s][debug][gc,heap] GC(0)   class space    used 608K, capacity 652K, committed 768K, reserved 1048576K
 * 		[2.066s][debug][gc,heap] GC(1) Heap before GC invocations=1 (full 0): garbage-first heap   total 20480K, used 9936K [0x00000000fec00000, 0x0000000100000000)
 * 		[2.066s][debug][gc,heap] GC(1)   region size 1024K, 10 young (10240K), 2 survivors (2048K)
 * 		[2.066s][debug][gc,heap] GC(1)  Metaspace       used 6774K, capacity 6888K, committed 7168K, reserved 1056768K
 * 		[2.066s][debug][gc,heap] GC(1)   class space    used 608K, capacity 652K, committed 768K, reserved 1048576K
 * 		[2.072s][info ][gc,heap] GC(1) Eden regions: 8->0(9)
 * 		[2.072s][info ][gc,heap] GC(1) Survivor regions: 2->1(2)
 * 		[2.072s][info ][gc,heap] GC(1) Old regions: 0->1
 * 		[2.072s][info ][gc,heap] GC(1) Humongous regions: 0->0
 * 		[2.072s][debug][gc,heap] GC(1) Heap after GC invocations=2 (full 0): garbage-first heap   total 20480K, used 2029K [0x00000000fec00000, 0x0000000100000000)
 * 		[2.072s][debug][gc,heap] GC(1)   region size 1024K, 1 young (1024K), 1 survivors (1024K)
 * 		[2.072s][debug][gc,heap] GC(1)  Metaspace       used 6774K, capacity 6888K, committed 7168K, reserved 1056768K
 * 		[2.072s][debug][gc,heap] GC(1)   class space    used 608K, capacity 652K, committed 768K, reserved 1048576K
 * 		
 *	4、查看GC过程中用户线程并发时间以及停顿的时间，在JDK9之前使用-XX:+PrintGCApplicationConcurrentTime以-XX:+PrintGCApplicationStoppedTime，JDK9开始使用-Xlog:safepoint:
 *		VM Args：-Xmx20M -Xlog:safepoint
 *		
 *		[0.151s][info][safepoint] Entering safepoint region: EnableBiasedLocking
 *		[0.151s][info][safepoint] Leaving safepoint region
 *		[0.151s][info][safepoint] Total time for which application threads were stopped: 0.0002627 seconds, Stopping threads took: 0.0000538 seconds
 *		[0.163s][info][safepoint] Application time: 0.0115640 seconds
 *		[0.163s][info][safepoint] Entering safepoint region: RevokeBias
 *		[0.163s][info][safepoint] Leaving safepoint region
 *		[0.163s][info][safepoint] Total time for which application threads were stopped: 0.0001633 seconds, Stopping threads took: 0.0000275 seconds
 *		[1.163s][info][safepoint] Application time: 1.0005282 seconds
 *		[1.163s][info][safepoint] Entering safepoint region: Cleanup
 *		[1.164s][info][safepoint] Leaving safepoint region
 *		[1.164s][info][safepoint] Total time for which application threads were stopped: 0.0001195 seconds, Stopping threads took: 0.0000113 seconds
 *		[2.029s][info][safepoint] Application time: 0.8651060 seconds
 *		[2.029s][info][safepoint] Entering safepoint region: G1CollectForAllocation
 *		[2.037s][info][safepoint] Leaving safepoint region
 *		[2.037s][info][safepoint] Total time for which application threads were stopped: 0.0082999 seconds, Stopping threads took: 0.0000177 seconds
 *		[2.037s][info][safepoint] Application time: 0.0001746 seconds
 *		[2.037s][info][safepoint] Entering safepoint region: RevokeBias
 *		[2.037s][info][safepoint] Leaving safepoint region
 *		[2.037s][info][safepoint] Total time for which application threads were stopped: 0.0001640 seconds, Stopping threads took: 0.0000893 seconds
 *		[2.140s][info][safepoint] Application time: 0.1026310 seconds
 *		[2.140s][info][safepoint] Entering safepoint region: G1CollectForAllocation
 *		[2.146s][info][safepoint] Leaving safepoint region
 *		[2.146s][info][safepoint] Total time for which application threads were stopped: 0.0061848 seconds, Stopping threads took: 0.0000140 seconds
 *		[2.235s][info][safepoint] Application time: 0.0886255 seconds
 *		[2.235s][info][safepoint] Entering safepoint region: G1CollectForAllocation
 *		[2.241s][info][safepoint] Leaving safepoint region
 *		[2.241s][info][safepoint] Total time for which application threads were stopped: 0.0061654 seconds, Stopping threads took: 0.0000279 seconds
 *		[2.356s][info][safepoint] Application time: 0.1151419 seconds
 *
 *  5、查看收集器Ergonomics机制（自动设置堆空间各分代区域大小、收集目标等内容，从Parallel收集器开始支持）自动调节的相关信息。在JDK9之前使用-XX:+PrintAdaptiveSizePolicy，JDK9开始使用Xlog:gc+ergo*=trace ：
 *  	VM Args：-Xmx20M -Xlog:gc+ergo*=trace
 *  
 *  6、查看熬过收集后剩余对象的年龄分布信息，在JDK9前使用XX:+PrintTenuringDistribution，JDK9开始使用-Xlog:gc+age=trace：
 *  	VM Args：-Xmx20M -Xlog:gc+ergo*=trace
 *  
 * 		[1.933s][debug][gc,age] GC(0) Desired survivor size 1048576 bytes, new threshold 15 (max threshold 15)
 * 		[1.939s][trace][gc,age] GC(0) Age table with threshold 15 (max threshold 15)
 * 		[1.939s][trace][gc,age] GC(0) - age   1:    1774424 bytes,    1774424 total
 * 		[2.040s][debug][gc,age] GC(1) Desired survivor size 1048576 bytes, new threshold 1 (max threshold 15)
 * 		[2.048s][trace][gc,age] GC(1) Age table with threshold 1 (max threshold 15)
 * 		[2.048s][trace][gc,age] GC(1) - age   1:     912424 bytes,     912424 total
 * 		[2.148s][debug][gc,age] GC(2) Desired survivor size 1048576 bytes, new threshold 15 (max threshold 15)
 * 		[2.151s][trace][gc,age] GC(2) Age table with threshold 15 (max threshold 15)
 * 		[2.151s][trace][gc,age] GC(2) - age   1:     940656 bytes,     940656 total
 * 		[2.151s][trace][gc,age] GC(2) - age   2:      69560 bytes,    1010216 total
 * 		[2.253s][debug][gc,age] GC(3) Desired survivor size 1048576 bytes, new threshold 15 (max threshold 15)
 * 		[2.259s][trace][gc,age] GC(3) Age table with threshold 15 (max threshold 15)
 * 		[2.259s][trace][gc,age] GC(3) - age   1:    1084368 bytes,    1084368 total
 * 		[2.259s][trace][gc,age] GC(3) - age   2:       3144 bytes,    1087512 total
 * 		[2.259s][trace][gc,age] GC(3) - age   3:      69464 bytes,    1156976 total
 * 		[2.347s][debug][gc,age] GC(4) Desired survivor size 1048576 bytes, new threshold 1 (max threshold 15)
 * 		[2.350s][trace][gc,age] GC(4) Age table with threshold 1 (max threshold 15)
 * 		[2.350s][trace][gc,age] GC(4) - age   1:    1121072 bytes,    1121072 total
 * 		[2.425s][debug][gc,age] GC(5) Desired survivor size 1048576 bytes, new threshold 1 (max threshold 15)
 * 		[2.429s][trace][gc,age] GC(5) Age table with threshold 1 (max threshold 15)
 * 		[2.429s][trace][gc,age] GC(5) - age   1:    1130920 bytes,    1130920 total
 *  
 * @author 	pengpeng
 * @date 	2020年7月6日 上午10:54:32
 */
public class XlogExample {
	
	private final RuntimeSampleService memorySamplingService = new RuntimeSampleService();
	
	public void tryGc() {
		Map<String,Object> samples = new WeakHashMap<>();
		while(true) {
			RuntimeSample sample = memorySamplingService.collectRuntimeSample();
			samples.put(UUID.randomUUID().toString(), sample);
		}
	}
	
	protected static void checkJavaVersion() {
		String javaVersion = System.getProperty("java.version");
		int majorVersion = Integer.valueOf(javaVersion.split(".")[0]);
		if(majorVersion < 9) {
			throw new IllegalStateException("本例必须使用JDK9及以上版本运行!");
		}
	}
	
	public static void main(String[] args) {
		checkJavaVersion();
		new XlogExample().tryGc();
	}

}
