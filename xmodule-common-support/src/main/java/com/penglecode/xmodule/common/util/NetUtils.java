package com.penglecode.xmodule.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
/**
 * 获取ip地址、mac地址等信息的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:59
 * @version  	1.0
 */
public class NetUtils {

	/**
	 * 获取客户端的ip地址
	 * @param request
	 * @return
	 */
	public static String getRemoteIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		// ipAddress = request.getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}

		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.split(".").length > 3) { // "***.***.***.***"
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	
	/**
	 * 获取服务器的ip地址:端口号
	 * @param request
	 * @return
	 */
	public static String getLocalIpAddr(HttpServletRequest request) {
		return request.getLocalAddr() + ":" + request.getLocalPort();
	}
	
	/**
	 * 根据ip地址获取mac地址
	 * @param ip
	 * @return
	 */
	public String getMACAddress(String ip) {  
        String str = "";  
        String macAddress = "";  
        try {  
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);  
            InputStreamReader ir = new InputStreamReader(p.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            for (int i = 1; i < 100; i++) {  
                str = input.readLine();  
                if (str != null) {  
                    if (str.indexOf("MAC Address") > 1) {  
                        macAddress = str.substring(  
                                str.indexOf("MAC Address") + 14, str.length());  
                        break;  
                    }  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return macAddress;  
    }
	
	/**
	 * 根据条件查找本机地址
	 * @param onlySiteLocalAddress	- isSiteLocalAddress()指10|172|192网段的地址
	 * @param preferredNetworks		- 优先选用的网段地址正则表达式列表
	 * @return
	 */
	public static InetAddress getLocalAddress(boolean onlySiteLocalAddress, List<String> preferredNetworks) {
	    try {
	    	if(onlySiteLocalAddress || !CollectionUtils.isEmpty(preferredNetworks)) { //存在过滤条件
	    		// 遍历所有的网络接口
		        for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
		            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
		            // 在所有的接口下再遍历IP
		            for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
		                InetAddress candidateAddress = (InetAddress) inetAddrs.nextElement();
		                if (!candidateAddress.isLoopbackAddress()) {// 排除loopback类型地址
		                	if (onlySiteLocalAddress && !candidateAddress.isSiteLocalAddress()) { // 要求是site-local地址，但是不是
		                		continue;
		                	}
		                	if(!CollectionUtils.isEmpty(preferredNetworks)) { // 匹配要求的网段
		                		final String hostAddress = candidateAddress.getHostAddress();
		                		boolean matched = false;
		                		for(String regex : preferredNetworks) {
		                			matched = hostAddress.matches(regex) || hostAddress.startsWith(regex);
		                			if (matched) {
		    	        				break;
		    	        			}
		                		}
		                		if(!matched) {
		                			continue;
		                		}
		                	}
		                	return candidateAddress;
		                }
		            }
		        }
	    	}
	        return InetAddress.getLocalHost(); // 默认的
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static void main(String[] args) throws Exception {
		InetAddress address = null;
		address = getLocalAddress(false, null);
		System.out.println(address.getHostAddress() + " , " + address.getHostName());
		
		address = getLocalAddress(true, null);
		System.out.println(address.getHostAddress() + " , " + address.getHostName());
		
		address = getLocalAddress(false, Arrays.asList("172.16"));
		System.out.println(address.getHostAddress() + " , " + address.getHostName());
		
		address = getLocalAddress(true, Arrays.asList("172.16"));
		System.out.println(address.getHostAddress() + " , " + address.getHostName());
	}

}