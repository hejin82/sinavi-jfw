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

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、{@link CountSqlResolver} のデフォルト実装です。
 * ページング検索用SQLのIDに<code>::count</code>をサフィックスとして追加しら文字列をトータル件数取得SQLのIDとします。
 * </p>
 * <p>
 * プレフィックスとサフィックスは任意に変更可能です。
 * 例えば、ページング検索用SQLのIDが<code>listFooWithPaginating</code>の場合に
 * <code>count(listFooWithPaginating)</code>をトータル件数取得SQLのIDとしたい場合は、
 * 次のように設定します。
 * <pre class="brush:xml">
 * &lt;bean id="jxSqlSessionFactoryBuilder"
 *   class="jp.co.ctc_g.jfw.core.jdbc.JxSqlSessionFactoryBuilder"&gt;
 *   &lt;property name="countSqlResolver" ref="myCountSqlResolver" /&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="myCountSqlResolver"
 *   class=" jp.co.ctc_g.jfw.core.jdbc.DefaultCountSqlResolver"&gt;
 *   &lt;property name="prefix" value="count(" /&gt;
 *   &lt;property name="suffix" value=")" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see CountSqlResolver
 */
public class DefaultCountSqlResolver implements CountSqlResolver {

    /**
     * デフォルトのプレフィックスです。デフォルト値は空文字です。
     */
    public static final String DEFAULT_PREFIX = "";

    /**
     * デフォルトのサフィックスです。デフォルト値は<code>::count</code>です。
     */
    public static final String DEFAULT_SUFFIX = "::count";

    private String prefix = DEFAULT_PREFIX;
    private String suffix = DEFAULT_SUFFIX;

    /**
     * デフォルトコンストラクタです。
     */
    public DefaultCountSqlResolver() {}

    /**
     *  {@inheritDoc}
     */
    @Override
    public String resolve(String statement, Object parameter) {
        return new StringBuilder()
                .append(prefix)
                .append(statement)
                .append(suffix)
                .toString();
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
        this.prefix = Args.proper(prefix, "");
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
        this.suffix = Args.proper(suffix, "");
    }
}
