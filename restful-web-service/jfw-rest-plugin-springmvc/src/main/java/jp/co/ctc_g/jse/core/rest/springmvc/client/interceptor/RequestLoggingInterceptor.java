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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.InternalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * <p>
 * このクラスは、REST通信時にそのリクエスト情報(ヘッダ・データ)とレスポンス情報(ヘッダ)をログに出力します。
 * レスポンスデータはこのインタセプタで読込を行うと以後の処理でレスポンスデータを取得できなくなるため、
 * Entityにレスポンスデータをマッピング後に
 * {@link jp.co.ctc_g.jse.core.rest.springmvc.client.interceptor.ResponseLoggingInterceptor}で出力します。
 * </p>
 * <p>
 * この例外ハンドラを利用するには、{@link org.springframework.web.client.RestTemplate#setInterceptors(List)}に登録する必要があります。
 * 以下に設定例を示します。
 * <pre class="brush:java">
 * &lt;bean id="requestLoggingInterceptor" class="jp.co.ctc_g.jse.core.rest.springmvc.client.interceptor.RequestLoggingInterceptor"/&gt;
 * &lt;bean id="template" class="org.springframework.web.client.RestTemplate"&gt;
 *  &lt;property name="interceptors"&gt; &lt;!-- interceptors に RequestLoggingInterceptorを設定 --&gt;
 *   &lt;list&gt;
 *    &lt;ref bean="requestLoggingInterceptor" /&gt; 
 *   &lt;/list&gt;
 *  &lt;/property&gt;
 *  &lt;!-- MessageConverterや例外ハンドラは省略 --&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * </p>
 * @see ClientHttpRequestInterceptor
 * @see ResponseLoggingInterceptor
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger L = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final String LS = System.getProperty("line.separator");
    private static final String REQUEST_PREFIX = "> ";
    private static final String RESPONSE_PREFIX = "< ";
    private static final String DELIMITER = ": ";

    /**
     * コンストラクタです。
     */
    public RequestLoggingInterceptor() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        dumpRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        dumpResponse(response);
        return response;
    }

    /**
     * リクエスト情報をログ出力します。
     * @param request HTTPリクエスト情報
     * @param body ボディ情報
     */
    protected void dumpRequest(HttpRequest request, byte[] body) {
        if (L.isDebugEnabled()) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Request").append(LS);
                sb.append(REQUEST_PREFIX).append(request.getMethod() + " " + request.getURI().toASCIIString()).append(LS);
                dumpHttpHeader(sb, REQUEST_PREFIX, request.getHeaders());
                sb.append(REQUEST_PREFIX).append(new String(body, "UTF-8")).append(LS);
                L.debug(sb.toString());
            } catch(UnsupportedEncodingException e) {
                throw new InternalException(RequestLoggingInterceptor.class, "E-REST-SPRINGMVC-INTERCEPTOR#0001");
            }
        }
    }

    /**
     * レスポンス情報をログ出力します。
     * @param response HTTPレスポンス
     * @throws IOException I/O例外
     */
    protected void dumpResponse(ClientHttpResponse response) throws IOException {
        if (L.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Response").append(LS);
            sb.append(RESPONSE_PREFIX).append(response.getStatusCode().toString() + " " + response.getStatusText())
                .append(LS);
            dumpHttpHeader(sb, RESPONSE_PREFIX, response.getHeaders());
            L.debug(sb.toString());
        }
    }

    private void dumpHttpHeader(StringBuilder sb, final String prefix, HttpHeaders headers) {
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            String key = e.getKey();
            List<String> value = e.getValue();
            if (value.size() == 1) {
                sb.append(prefix).append(key).append(DELIMITER).append(value.get(0)).append(LS);
            } else {
                StringBuilder b = new StringBuilder();
                boolean add = false;
                for (String s : value) {
                    if (add) b.append(',');
                    add = true;
                    b.append(s);
                }
                sb.append(prefix).append(key).append(DELIMITER).append(b.toString()).append(LS);
            }
        }
    }

}