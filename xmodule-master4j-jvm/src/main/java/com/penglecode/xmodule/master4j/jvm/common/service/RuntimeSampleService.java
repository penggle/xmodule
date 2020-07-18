package com.penglecode.xmodule.master4j.jvm.common.service;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.master4j.jvm.common.model.RuntimeSample;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

@Service
public class RuntimeSampleService {

	private static final int MB_OF_BYTES = 1024 * 1024;

	public RuntimeSample collectRuntimeSample() {
		RuntimeSample sample = new RuntimeSample();
		Runtime runtime = Runtime.getRuntime();
		
		sample.setOsName(System.getProperty("os.name"));
		sample.setOsArch(System.getProperty("os.arch"));
		sample.setOsVersion(System.getProperty("os.version"));
		
		sample.setVmName(System.getProperty("java.vm.name"));
		sample.setVmVersion(System.getProperty("java.vm.version"));
		sample.setVmVendor(System.getProperty("java.vm.vendor"));
		sample.setVmInfo(System.getProperty("java.vm.info"));
		
		sample.setCoreSize(runtime.availableProcessors());
		sample.setMaxVmMemory((int) (runtime.maxMemory() / MB_OF_BYTES));
		sample.setTotalVmMemory((int) (runtime.totalMemory() / MB_OF_BYTES));
		sample.setFreeVmMemory((int) (runtime.freeMemory() / MB_OF_BYTES));
		sample.setUsedVmMemory(sample.getTotalVmMemory() - sample.getFreeVmMemory());
		
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		sample.setTotalPhyMemory((int) (osmxb.getTotalPhysicalMemorySize() / MB_OF_BYTES));
		sample.setFreePhyMemory((int) (osmxb.getFreePhysicalMemorySize() / MB_OF_BYTES));
		sample.setUsedPhyMemory(sample.getTotalPhyMemory() - sample.getFreePhyMemory());
		
		sample.setJavaVendor(System.getProperty("java.vendor"));
		sample.setJavaVersion(System.getProperty("java.version"));
		
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> vmArgs = runtimeMxBean.getInputArguments();
		if(!CollectionUtils.isEmpty(vmArgs)) {
			sample.setVmArgs(String.join(";", vmArgs));
		}
		return sample;
	}
	
}
