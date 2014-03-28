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

package jp.co.ctc_g.jfw.core.jdbc;

/**
 * <p>
 * このインタフェースは、ページングで検索する際のトータル件数を取得するためのSQL（のID）を解決するためのインタフェースです。
 * </p>
 * <p>
 * {@link DefaultCountSqlResolver デフォルト}では、SqlMap.xmlに定義されるSQLのIDに <code>::count</code>を付加したIDがトータル件数を取得するSQLです。
 * 例えば、<code>listFooWithPaginating</code>というIDに対しては<code>listFooWithPaginating::count</code>となります。
 * </p>
 * <p>
 * このインタフェースを実装したクラスは主として {@link JxSqlSession} から利用されますが、
 * 設定は {@link JxSqlSessionFactoryBuilder} に対して行ないます。
 * </p>
 * <pre class="brush:xml">
 * &lt;bean id="jxSqlSessionFactoryBuilder"
 *   class="jp.co.ctc_g.jfw.core.jdbc.JxSqlSessionFactoryBuilder"&gt;
 *   &lt;property name="countSqlResolver" ref="myCountSqlResolver" /&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="myCountSqlResolver"
 *   class="my.custom.application.MyCountSqlResolver" /&gt;
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSession
 * @see JxSqlSessionFactoryBuilder
 * @see DefaultCountSqlResolver
 */
public interface CountSqlResolver {
    
    /**
     * ページングで検索する際のトータル件数を取得するためのSQLのIDを解決します
     * @param statement ページングでの検索に利用するSQLのID
     * @param parameter 検索に利用するパラメータ
     * @return トータル件数を取得するためのSQLのID
     */
    String resolve(String statement, Object parameter);
}
