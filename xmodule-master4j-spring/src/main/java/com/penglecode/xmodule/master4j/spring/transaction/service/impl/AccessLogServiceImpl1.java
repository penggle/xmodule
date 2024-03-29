package com.penglecode.xmodule.master4j.spring.transaction.service.impl;

import com.penglecode.xmodule.master4j.spring.transaction.model.AccessLog;
import com.penglecode.xmodule.master4j.spring.transaction.repository.AccessLogRepository;
import com.penglecode.xmodule.master4j.spring.transaction.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:18
 */
@Service("accessLogService1")
public class AccessLogServiceImpl1 implements AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void recordAccessLog1(AccessLog accessLog) {
        accessLogRepository.recordAccessLog1(accessLog);
        /**
         * 不通过抛出业务异常来回滚，而直接通过标记rollbackOnly=true来强制回滚当前事务；PROPAGATION_REQUIRES_NEW时，子事务回滚不会影响父事务.
         * 与下面不同的是，这个会导致UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
         * 这是因为在当前事务传播语义下，一旦某个事务标记rollback-only为true那么将会导致global-rollback-only也为true，所以会抛出异常
         * 但是此异常并不是导致事物回滚的原因，事务是主动回滚的，而不是依靠此UnexpectedRollbackException被动回滚的，这点需要明白。
         */
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public void recordAccessLog2(AccessLog accessLog) {
        accessLogRepository.recordAccessLog2(accessLog);
        /**
         * 不通过抛出业务异常来回滚，而直接通过标记rollbackOnly=true来强制回滚当前事务；PROPAGATION_REQUIRES_NEW时，子事务回滚不会影响父事务.
         * 与上面不同的是，这个不会导致UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
         * 这是因为当前事务是处于独立的事务中的
         */
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

}
