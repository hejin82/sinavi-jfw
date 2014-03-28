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
 * このクラスは、{@link PaginationEnableMatcher} のデフォルト実装です。
 * ページング検索用SQLのIDが<code>WithPaginating</code>で終了している場合、ページング対象であると判定します。
 * </p>
 * <p>
 * プレフィックスとサフィックスは任意に変更可能です。
 * 例えば、ページング検索用SQLのIDを<code>page(listFoo)</code>というように、
 * page()で囲むような書式に変更したい場合には次のように設定します。
 * <pre class="brush:xml">
 * &lt;bean id="jxSqlSessionFactoryBuilder"
 *   class="jp.co.ctc_g.jfw.core.jdbc.JxSqlSessionFactoryBuilder"&gt;
 *   &lt;property name="paginationEnableMatcher" ref="myPaginationEnableMatcher" /&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="myPaginationEnableMatcher"
 *   class=" jp.co.ctc_g.jfw.core.jdbc.DefaultPaginationEnableMatcher"&gt;
 *   &lt;property name="prefix" value="page(" /&gt;
 *   &lt;property name="suffix" value=")" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSessionFactoryBuilder
 * @see PaginationEnableMatcher
 */
public class DefaultPaginationEnableMatcher implements PaginationEnableMatcher {

    /**
     * デフォルトのプレフィックスです。デフォルト値は空文字です。
     */
    public static final String DEFAULT_PREFIX = "";
    
    /**
     * デフォルトのサフィックスです。デフォルト値は<code>WithPaginating</code>です。
     */
    public static final String DEFAULT_SUFFIX = "WithPaginating";

    private String prefix = DEFAULT_PREFIX;
    private String suffix = DEFAULT_SUFFIX;

    /**
     * デフォルトコンストラクタです。
     */
    public DefaultPaginationEnableMatcher() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(String statement, Object parameter) {
        return statement.startsWith(prefix)
                && statement.endsWith(suffix);
    }

    /**
     * プレフィックスを返却します。
     * @return プレフィックス
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * プレフィックスを設定します。
     * @param prefix プレフィックス
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * サフィックスを返却します。
     * @return サフィックス
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * サフィックスを設定します。
     * @param suffix サフィックス
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
