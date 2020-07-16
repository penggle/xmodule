package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.sun.management.OperatingSystemMXBean;

/**
 * JVM参数示例：
 * 
 * -XX:+PrintCommandLineFlags	 : 在Java程序运行的开始打印所有设置的JVM参数(包括用户手动设置或者JVM自动设置的)
 * -Xms (即-XX:InitialHeapSize)	 : 初始化堆内存的大小，如果程序未显示指定该参数，则在JDK1.8中默认为本机总物理内存的1/64
 * -Xmx (即-XX:MaxHeapSize)		 : 最大堆内存的大小，如果程序未显示指定该参数，则在JDK1.8中默认为本机总物理内存的1/4
 * -XX:+PrintFlagsInitial		 : 表示打印出所有XX选项的默认值
 * -XX:+PrintFlagsFinal			 : 表示打印出XX选项在运行程序时生效的值
 * @author 	pengpeng
 * @date 	2020年6月15日 上午10:38:31
 */
@SuppressWarnings("restriction")
public class VMArgumentsExample {

	public static void main(String[] args) {
		System.out.println("hello world!");
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long totalPhysical = osmxb.getTotalPhysicalMemorySize();
		System.out.println("本机总物理内存大小为：" + totalPhysical);
		System.out.println("JDK1.8默认的-XX:InitialHeapSize大小为本机总物理内存的1/64：" + (totalPhysical / 64));
		System.out.println("JDK1.8默认的-XX:MaxHeapSize大小为本机总物理内存的1/4：" + (totalPhysical / 4));
		
		System.out.println("==============================VM Args==============================");
		List<String> vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
		vmArgs.forEach(System.out::println);
	}

}
