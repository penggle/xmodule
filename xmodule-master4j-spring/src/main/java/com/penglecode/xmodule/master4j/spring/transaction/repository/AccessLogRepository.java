package com.penglecode.xmodule.master4j.spring.transaction.repository;

import com.penglecode.xmodule.master4j.spring.transaction.model.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 8:24
 */
@Repository
public class AccessLogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void recordAccessLog1(AccessLog accessLog) {
        String sql = "INSERT INTO t_access_log(log_id, log_title, invoke_method, create_time) VALUES (?, ?, ?, ?)";
        int effectRows = jdbcTemplate.update(sql, new Object[] {accessLog.getLogId(), accessLog.getLogTitle(), accessLog.getInvokeMethod(), accessLog.getCreateTime()});
        //fireException(effectRows);
    }

    public void recordAccessLog2(AccessLog accessLog) {
        String sql = "INSERT INTO t_access_log(log_id, log_title, invoke_method, create_time) VALUES (?, ?, ?, ?)";
        int effectRows = jdbcTemplate.update(sql, new Object[] {accessLog.getLogId(), accessLog.getLogTitle(), accessLog.getInvokeMethod(), accessLog.getCreateTime()});
        //fireException(effectRows);
    }

    protected void fireException(int effectRows) {
        if(effectRows == 1) {
            throw new IllegalStateException("Intentional Exception!");
        }
    }

}
