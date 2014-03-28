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

package jp.co.ctc_g.jse.core.rest.springmvc.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * このクラスは、{@link org.springframework.web.client.RestOperations}で外部システムとHTTP通信を行う場合にプロキシサーバを経由する設定を行います。
 * </p>
 * <p>
 * SpringMVCのデフォルトでは{@link HttpComponentsClientHttpRequestFactory}を利用してプロキシの設定を行います。
 * この設定を行うためにはかなり複雑な設定が必要となります。(Springの設定ファイルに50行程度の設定が必要)
 * </p>
 * <p>
 * そのため、このクラスはプロキシ設定を簡素化するために提供しています。
 * このクラスを利用したプロキシを設定するには最低限プロキシのホスト名とポート番号のみとなります。
 * 以下が設定例です。
 * <pre>
 * &lt;bean id="httpClientFactory" class="jp.co.ctc_g.jse.core.framework.ProxyClientHttpRequestFactory"&gt;
 *    &lt;property name="proxyHost" value="proxy.xx.xx" /&gt;
 *    &lt;property name="proxyPort" value="8080" /&gt;
 *    &lt;property name="targetPort" value="8080" /&gt;
 * &lt;/bean&lt;
 * </pre>
 * この設定を{@link org.springframework.web.client.RestTemplate#setRequestFactory(org.springframework.http.client.ClientHttpRequestFactory)}にインジェクションすることで、
 * プロキシを経由したHTTP通信を実現することができます。
 * インジェクションする場合は以下のように設定します。
 * <pre>
 * &lt;bean id="proxyRestTemplate" class="org.springframework.web.client.RestTemplate"&gt;
 *    &lt;property name="requestFactory" ref="httpClientFactory" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * <p>
 * なお、プロキシサーバのユーザ認証が必要な場合はユーザID・パスワードを設定してください。
 * これは主にテスト環境で利用することを想定しています。
 * <pre>
 * &lt;bean id="httpClientFactory" class="jp.co.ctc_g.jse.core.framework.ProxyClientHttpRequestFactory"&gt;
 *    &lt;property name="proxyHost" value="proxy.xx.xx" /&gt;
 *    &lt;property name="proxyPort" value="8080" /&gt;
 *    &lt;property name="authentication" value="true" /&gt;
 *    &lt;property name="username" value="foo" /&gt;
 *    &lt;property name="password" value="bar" /&gt;
 * &lt;/bean&lt;
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class ProxyClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory implements InitializingBean {

    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

    private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

    private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

    private int maxTotal = DEFAULT_MAX_TOTAL_CONNECTIONS;

    private int defaultMaxPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    private int readTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;

    private String proxyHost;

    private String proxyPort;

    private boolean authentication = false;

    private String username;

    private String password;

    /**
     * コンストラクタです。
     */
    public ProxyClientHttpRequestFactory() {}

    /**
     * プロパティの設定が終了したあとにHttpClientのインスタンスを生成し、プロキシの設定を行います。
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(proxyHost, "プロキシホスト(proxyHost)は必須です。");
        Assert.notNull(proxyPort, "プロキシポート番号(proxyPort)は必須です。");

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        HttpClientBuilder builder = HttpClients.custom();
        builder.setConnectionManager(connectionManager);
        if (authentication) {
            Assert.notNull(username, "ユーザ認証がtrueに設定された場合、ユーザ名(username)は必須です。");
            Assert.notNull(password, "ユーザ認証がtrueに設定された場合、パスワード(password)は必須です。");
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(new HttpHost(proxyHost, Integer.parseInt(proxyPort)));
            builder.setRoutePlanner(routePlanner);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxyHost, Integer.parseInt(proxyPort)), new UsernamePasswordCredentials(username, password));
            builder.setDefaultCredentialsProvider(credsProvider);
        }
        builder.setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout(readTimeout).build());
        CloseableHttpClient client = builder.build();
        setHttpClient(client);
    }

    /**
     * プロキシホストを設定します。
     * @param proxyHost プロキシホスト
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * プロキシのポート番号を設定します。
     * @param proxyPort ポート番号
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * ユーザ認証が必要かどうかを設定します。
     * デフォルト値はfalseです。
     * また、ユーザ認証が必要な場合はユーザ名とパスワードが必須となります。
     * @param authentication ユーザ認証が必要な場合はtrue/必要ない場合はfalse
     */
    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    /**
     * ユーザ名を設定します。
     * @param username ユーザ名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * パスワードを設定します。
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 最大コネクション数を設定します。
     * デフォルトは100です。
     * @param maxTotal 最大コネクション数
     */
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    /**
     * ルートごとのデフォルトの最大値を設定します。
     * デフォルトは5です。
     * @param defaultMaxPerRoute ルートごとのデフォルト最大値
     */
    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    /**
     * 読込のタイムアウトを設定します。
     * デフォルトは1分です。
     * なお、設定はミリ秒となります。(1分の場合：60*1000=60000)
     * @param readTimeout コネクションのタイムアウト値
     */
    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
}
