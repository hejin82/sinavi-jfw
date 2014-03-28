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

import java.sql.Timestamp;

/**
 * <p>
 * このインタフェースは、トランザクション開始時刻を生成するジェネレータです。
 * このインタフェースは {@link TransactionTimeInterceptor} から利用されます。
 * </p>
 * <p>
 * デフォルトではトランザクション開始時刻は、アプリケーションサーバローカルな時刻です。
 * これはクラスタリングされている場合などで問題になる可能性があります。
 * その場合には、このインタフェースを利用してください。
 * <pre class="brush:java">
 * public class MyCustomGenerator implements TransationTimeGenerator {
 * 
 *     &#64;Autowired
 *     protected MyApplicationMapper mapper;
 * 
 *     public Timestamp generate() {
 *         return mapper.getNewTransactionTimestamp();
 *     }
 * }
 * </pre>
 * 上記例は、MyApplicationMapper がデータベースにアクセスして、
 * アプリケーションサーバに依存しないトランザクション開始時間を発行しているイメージです。
 * これを下記のようにして {@link TransactionTimeInterceptor} に設定します。
 * <pre class="brush:xml">
 * &lt;bean id="transactionTimeAdvice" class="jp.co.ctc_g.jfw.profill.util.TransactionTimeInterceptor"&gt;
 *   &lt;property name="generator" ref="transationTimeGenerator" /&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id="transationTimeGenerator" class="foo.bar.MyCustuomGenerator" /&gt;
 * </pre>
 * </p>
 * @see TransactionTimeInterceptor
 * @see TransactionTimeKeeper
 * @see UpdateStampProvider
 */
public interface TransactionTimeGenerator {

    /**
     * トランザクション開始時刻を生成します。
     * @return トランザクション開始時刻
     */
    Timestamp  generate();
    
    /**
     * このクラスは、アプリケーションサーバローカルなトランザクション開始時刻を生成するジェネレータです。
     * {@link TransactionTimeInterceptor} はデフォルトではこのクラスを利用します。
     */
    class Local implements TransactionTimeGenerator {
        
        /**
         * アプリケーションサーバローカルなトランザクション開始時刻を生成します。
         * @return アプリケーションサーバローカルなトランザクション開始時刻
         */
        public Timestamp generate() {
            return new Timestamp(System.currentTimeMillis());
        }
    }
}
