package com.youyi.infra.tx;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2024/12/29
 */
public class MoreTxFeature {

    /**
     * 在当前事务执行成功之后执行 action
     *
     * @param action 需要执行的操作
     */
    public static void doAfterTxCompletion(Runnable action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new DoTransactionCompletion(action));
        }
    }
}
