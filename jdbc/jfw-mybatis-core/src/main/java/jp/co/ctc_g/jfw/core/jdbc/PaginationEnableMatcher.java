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
 * このインタフェースは、検索系SQLがページングされるかどうかを判定するためのインタフェースです。
 * </p>
 * <p>
 * {@link DefaultPaginationEnableMatcher デフォルト}では、SqlMap.xmlに定義されるSQLのIDの末尾に <code>WithPaginating</code>が付加されている場合、ページング対象となります。
 * 例えば、<code>listFooWithPaginating</code>のIDを持つSQLを実行した結果はページングされます。
 * </p>
 * <p>
 * このインタフェースを実装したクラスは主として {@link JxSqlSession} から利用されますが、
 * 設定は {@link JxSqlSessionFactoryBuilder} に対して行ないます。
 * </p>
 * <pre class="brush:xml">
 * &lt;bean id="jxSqlSessionFactoryBuilder"
 *   class="jp.co.ctc_g.jfw.core.jdbc.JxSqlSessionFactoryBuilder"&gt;
 *   &lt;property name="paginationEnableMatcher" ref="myPaginationEnableMatcher" /&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="myPaginationEnableMatcher"
 *   class="my.custom.application.MyPaginationEnableMatcher" /&gt;
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSession
 * @see JxSqlSessionFactoryBuilder
 * @see DefaultPaginationEnableMatcher
 */
public interface PaginationEnableMatcher {

    /**
     * 検索系SQLがページングされるかどうかを判定します
     * @param statement 判定対象のSQLのID
     * @param parameter SQLを実行する際のパラメータ
     * @return 検索系SQLがページングされる場合は<code>true</code>
     */
    boolean match(String statement, Object parameter);
}
