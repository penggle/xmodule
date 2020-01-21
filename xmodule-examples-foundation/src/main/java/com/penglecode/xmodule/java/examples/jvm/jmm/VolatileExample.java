package com.penglecode.xmodule.java.examples.jvm.jmm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * volatile 变量的使用和理解
 * 
 * @author 	pengpeng
 * @date	2017年10月27日 下午5:00:58
 */
public class VolatileExample {

	private Connection connection; 
	
	private volatile boolean initialized = false;
	
	public void init(){
		try {
			/**
			 * 根据java内存模型的原则,volatile变量的不能参与指令重排序,
			 * 并且volatile变量周围的非volatile变量也不能参与指令重排序
			 * 因此以下两句语句在多处理器环境下将保持和表象语义上的一致性：即两句是顺序执行的不存在重排序
			 */
			connection = DriverManager.getConnection(" jdbc:mysql://localhost:3306/db_test", "test", "123456");
			initialized = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		if(initialized){ //绝对能保证connection初始化完成了
			return connection;
		}
		throw new IllegalStateException("Connection's initialization has not completed!");
	}
	
}
