package com.penglecode.xmodule.java8.examples.newfeatures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 十分相似与jodatime这个类库
 * 
 * @author 	pengpeng
 * @date   		2017年1月16日 上午10:32:21
 * @version 	1.0
 */
public class LocalDateTimeExample {

	public static void main(String[] args) {
		System.out.println(ZoneId.getAvailableZoneIds());
		ZoneId currentZone = ZoneId.systemDefault();
		System.out.println(currentZone);
		
		LocalDateTime currentDateTime0 = LocalDateTime.ofInstant(new Date().toInstant(), currentZone);
		System.out.println(currentDateTime0);
		
		LocalDateTime currentDateTime1 = LocalDateTime.now();
		System.out.println(currentDateTime1);
		currentDateTime1 = currentDateTime1.minusDays(10);
		System.out.println(currentDateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		LocalDate currentDate1 = currentDateTime1.toLocalDate();
		System.out.println(currentDate1);
		LocalDate currentDate2 = LocalDate.now();
		System.out.println(currentDate2);
		System.out.println(currentDate1.equals(currentDate2));
		
		LocalTime currentTime1 = currentDateTime1.toLocalTime();
		System.out.println(currentTime1);
		LocalTime currentTime2 = LocalTime.now();
		System.out.println(currentTime2);
		
		LocalDateTime dateTime1 = LocalDateTime.parse("2017-01-12T10:30:07.123");
		System.out.println(dateTime1);
		LocalDateTime dateTime2 = LocalDateTime.parse("2017-01-12 10:30:07.123", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		System.out.println(dateTime2);
		
		System.out.println(LocalDate.now());
	}

}
