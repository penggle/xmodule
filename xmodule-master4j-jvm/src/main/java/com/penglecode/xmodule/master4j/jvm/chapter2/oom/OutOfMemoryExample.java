package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.penglecode.xmodule.master4j.jvm.common.model.RuntimeSample;
import com.penglecode.xmodule.master4j.jvm.common.service.RuntimeSampleService;

/**
 * Java {@link #OutOfMemoryError}异常模拟示例及问题定位
 * 
 * 增加-XX:+PrintCommandLineFlags参数可以在程序运行开头打印当前JVM的所有参数设置信息
 * 
 * 随着程序的运行，Runtime.totalMemory()会慢慢变大，因为JVM会向操作系统动态申请更多的内存(挖内存)，当totalMemory触及(接近)Runtime.maxMemory()时，抛出OutOfMemoryError。
 * 
 * 1、在未显式增加-Xmx(即-XX:MaxHeapSize)参数指定最大堆内存时，Eclipse -> Run As -> Java Application，通过-XX:+PrintCommandLineFlags参数可见出现了默认的-XX:MaxHeapSize=4264857600 (应该是Eclipse添加的该参数)，因此出现OOM错误可能需要更长的时间。
 * 2、显式增加-Xmx20m参数指定最大堆内存时，Eclipse -> Run As -> Java Application，通过-XX:+PrintCommandLineFlags参数可见出现了-XX:MaxHeapSize=20971520 (即20MB)，因此出现OOM错误的时间更短一些。
 * 3、增加-XX:+HeapDumpOnOutOfMemoryError选项，即在发生OOM时dump出堆的快照。快照文件将在工程的根目录下生产。
 * 4、使用Eclipse Memory Analyzer插件对.hprof格式的Dump文件进行分析，具体怎么使用这个工具请自行搜索，例如：https://blog.csdn.net/haolyj98/article/details/78361867
 * 
 * @author 	pengpeng
 * @date 	2020年6月4日 下午4:14:05
 */
public class OutOfMemoryExample {

	private final List<RuntimeSample> memoryStates = new ArrayList<>();
	
	private final RuntimeSampleService memorySamplingService = new RuntimeSampleService();
	
	public RuntimeSample collectRuntimeSample() throws Exception {
		RuntimeSample sample = memorySamplingService.collectRuntimeSample();
		Path attachmentPath = Paths.get(getClass().getResource("attachment.jpg").toURI());
		sample.setAttachment(Files.readAllBytes(attachmentPath));
		return sample;
	}
	
	public List<RuntimeSample> collectRuntimeSamples() throws Exception {
		int count = 0;
		while(true) {
			RuntimeSample sample = collectRuntimeSample();
			memoryStates.add(sample);
			System.out.println("【" + ++count + "】" + sample);
		}
	}
	
	public static void main(String[] args) throws Exception {
		OutOfMemoryExample example = new OutOfMemoryExample();
		example.collectRuntimeSamples();
	}
	
}
