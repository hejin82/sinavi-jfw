/*
 * Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.ctc_g.jfw.profill.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * このクラスは、{@link TransactionTimeKeeper} にトランザクション開始時刻を設定するインターセプタです。
 * {@link UpdateStampProvider#isTransactionAware()} が <code>true</code> である場合に必要です。
 * 設定方法は通常の {@link MethodInterceptor} と変わりません。
 * <pre class="brush:xml">
 * &lt;aop:config&gt;
 *   &lt;aop:pointcut id="servicePointcut" expression="execution(* * ..*Service.*(..))" /&gt;
 *   &lt;aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionAdvice" /&gt;
 *   &lt;aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionTimeAdvice" /&gt;
 * &lt;/aop:config&gt;
 * 
 * &lt;tx:advice id="transactionAdvice" transaction-manager="transactionManager"&gt;
 *   ...
 * &lt;/tx:advice&gt;
 * 
 * &lt;bean id="transactionTimeAdvice" class="jp.co.ctc_g.jfw.profill.util.TransactionTimeInterceptor" /&gt;
 * </pre>
 * <p>
 * @see UpdateStampProvider
 * @see TransactionTimeKeeper
 */
public class TransactionTimeInterceptor implements MethodInterceptor, InitializingBean {

    /**
     * トランザクション時間を生成するジェネレータです。
     */
    private TransactionTimeGenerator generator;

    /**
     * デフォルトコンストラクタです。
     */
    public TransactionTimeInterceptor() {}

    /**
     * {@inheritDoc}
     * <p>
     * メソッド開始時に {@link TransactionTimeKeeper#beginTransaction()} を、
     * メソッド終了時に {@link TransactionTimeKeeper#endTransaction()} をコールします。
     * </p>
     * @see TransactionTimeKeeper
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionTimeKeeper.beginTransaction(generator.generate());
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            TransactionTimeKeeper.endTransaction();
        }
        return result;
    }

    /**
     * トランザクション時間を生成するジェネレータを返却します。
     * @return トランザクション時間を生成するジェネレータ
     */
    public TransactionTimeGenerator getGenerator() {
        return generator;
    }

    /**
     * トランザクション時間を生成するジェネレータを設定します。
     * @param generator トランザクション時間を生成するジェネレータ
     */
    public void setGenerator(TransactionTimeGenerator generator) {
        this.generator = generator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (generator == null) generator = new TransactionTimeGenerator.Local();
    }
}
