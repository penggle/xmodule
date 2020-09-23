package com.penglecode.xmodule.master4j.spring.transaction.programmatic;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 8:39
 */
public class TransactionTemplateFactory {

    private final PlatformTransactionManager platformTransactionManager;

    public TransactionTemplateFactory(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    public TransactionTemplate createTransactionTemplate(int propagation) {
        return createTransactionTemplate(propagation, false);
    }

    public TransactionTemplate createTransactionTemplate(int propagation, boolean readOnly) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(propagation);
        transactionTemplate.setReadOnly(readOnly);
        return transactionTemplate;
    }

}
