package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorExample2 {

	protected static List<DeviceAccessConfig> getAllDeviceAccessConfigList() {
		Random random = new Random();
		List<String> protocols = Arrays.asList("MODBUS_RTU", "MODBUS_TCP", "PLC", "OPCUA");
		List<DeviceAccessConfig> dataList = new ArrayList<DeviceAccessConfig>();
		DeviceAccessConfig config = null;
		for(int i = 0; i < 10; i++) {
			config = new DeviceAccessConfig();
			int n = i % 4;
			config.setDeviceName("测试设备" + n);
			config.setProtocol(protocols.get(random.nextInt(protocols.size())));
			config.setEndpoint("设备地址" + n);
			
			List<ReadAccessData> readAccessDatas = new ArrayList<ReadAccessData>();
			int reads = random.nextInt(10);
			ReadAccessData data = null;
			for(int j = 0; j < reads; j++) {
				data = new ReadAccessData();
				data.setDataName("dataName" + n + "" + j);
				data.setDataType(String.class.getName());
				data.setAttributes(new HashMap<String,Object>());
				readAccessDatas.add(data);
			}
			config.setReadAccessDatas(readAccessDatas);
			dataList.add(config);
		}
		return dataList;
	}
	
	public static void collectorTest1() {
		List<DeviceAccessConfig> dataList = getAllDeviceAccessConfigList();
		String deviceName = "测试设备1";
		
		Map<DeviceAccessConfig, List<String>> result = dataList.stream().filter(c -> c.getDeviceName().equals(deviceName))
				.collect(Collector.of(HashMap<DeviceAccessConfig, List<String>>::new, (r, e) -> {
					r.put(e, e.getReadAccessDatas().stream().map(ReadAccessData::getDataName).collect(Collectors.toList()));
					System.out.println(">>> e = " + e.getDeviceName() + ", " + e.getReadAccessDatas().stream().map(ReadAccessData::getDataName).collect(Collectors.toList()));
				}, (r1, r2) -> {
					r1.putAll(r2);
					System.out.println(">>> r1 = " + r1 + ", r2 = " + r2);
					return r1;
				}));
		System.out.println(result);
	}
	
	public static void main(String[] args) {
		collectorTest1();
	}

	public static class DeviceAccessConfig {
		
		private String deviceName;
		
		private String endpoint;
		
		private String protocol;
		
		private List<ReadAccessData> readAccessDatas;

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public List<ReadAccessData> getReadAccessDatas() {
			return readAccessDatas;
		}

		public void setReadAccessDatas(List<ReadAccessData> readAccessDatas) {
			this.readAccessDatas = readAccessDatas;
		}
		
	}
	
	public static class ReadAccessData {
		
		private String dataName;
		
		private String dataType;
		
		private Map<String,Object> attributes;

		public String getDataName() {
			return dataName;
		}

		public void setDataName(String dataName) {
			this.dataName = dataName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}
		
	}
	
}
