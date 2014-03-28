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

package jp.co.ctc_g.jse.core.rest.springmvc.client.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、REST通信時のレスポンスデータをログに出力します。
 * </p>
 * <p>
 * 本来であれば、{@link org.springframework.http.client.ClientHttpRequestInterceptor}で出力するべきですが、
 * ClientHttpRequestInterceptorでレスポンスデータを取得するとそれ以降の処理で
 * レスポンスデータを読み込めなくなります。
 * そのため、レスポンスデータをEntityにマッピング後に
 * マッピングされた値をログに出力します。
 * 以下に設定例を示します。
 * <pre class="brush:java">
 * &lt;aop:config&gt;
 *  &lt;aop:pointcut id="restClientPointcut" expression="execution(* jp.co.ctc_g.jse..*Dao.*(..))" /&gt;
 *  &lt;aop:advisor pointcut-ref="restClientPointcut" advice-ref="responseLoggingInterceptor" /&gt;
 * &lt;/aop:config&gt;
 * &lt;bean id="responseLoggingInterceptor" class="jp.co.ctc_g.jse.core.rest.springmvc.client.interceptor.ResponseLoggingInterceptor" /&gt;
 * </pre>
 * </p>
 * @see RequestLoggingInterceptor
 * @see MethodInterceptor
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class ResponseLoggingInterceptor implements MethodInterceptor {

    private static final Logger L = LoggerFactory.getLogger(ResponseLoggingInterceptor.class);

    /**
     * デフォルトコンストラクタです。
     */
    public ResponseLoggingInterceptor() {}

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (L.isDebugEnabled()) {
            if (result != null) L.debug("< Response Body: {}", result.toString());
            else L.debug("< Response Body: レスポンスデータがNULLです。");
        }
        return result;
    }

}
