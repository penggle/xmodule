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
 * 原因分析：
 * 产生 java.lang.OutOfMemoryError: Java heap space 错误的原因, 很多时候, 就类似于将 XXL 号的对象,往 S 号的 Java heap space 里面塞。其实清楚了原因, 就很容易解决对不对? 只要增加堆内存的大小, 程序就能正常运行. 另外还有一些比较复杂的情况, 主要是由代码问题导致的:
 *
 * 1、超出预期的访问量/数据量。 应用系统设计时,一般是有 “容量” 定义的, 部署这么多机器, 用来处理一定量的数据/业务。 如果访问量突然飙升, 超过预期的阈值, 类似于时间坐标系中针尖形状的图谱, 那么在峰值所在的时间段, 程序很可能就会卡死、并触发 java.lang.OutOfMemoryError: Java heap space 错误。
 *
 * 2、内存泄露(Memory leak). 这也是一种经常出现的情形。由于代码中的某些错误, 导致系统占用的内存越来越多. 如果某个方法/某段代码存在内存泄漏的, 每执行一次, 就会（有更多的垃圾对象）占用更多的内存. 随着运行时间的推移, 泄漏的对象耗光了堆中的所有内存, 那么 java.lang.OutOfMemoryError: Java heap space 错误就爆发了。
 *
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author 	pengpeng
 * @date 	2020年6月4日 下午4:14:05
 */
public class JavaHeapSpaceOOMExample {

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
		JavaHeapSpaceOOMExample example = new JavaHeapSpaceOOMExample();
		example.collectRuntimeSamples();
	}
	
}
