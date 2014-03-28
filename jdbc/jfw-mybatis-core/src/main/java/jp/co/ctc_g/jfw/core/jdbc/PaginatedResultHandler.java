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

import java.util.List;

import jp.co.ctc_g.jfw.paginate.Paginatable;

import org.apache.ibatis.session.RowBounds;

/**
 * <p>
 * このインタフェースは、ページングされている検索結果を加工する機能を提供します。
 * {@link PartialListResultHandler デフォルト}では、{@link jp.co.ctc_g.jfw.core.util.PartialList}に加工されます。
 * </p>
 * <p>
 * このインタフェースを実装したクラスは主として {@link JxSqlSession} から利用されますが、
 * 設定は {@link JxSqlSessionFactoryBuilder} に対して行ないます。
 * <pre class="brush:xml">
 * &lt;bean id="jxSqlSessionFactoryBuilder"
 *   class="jp.co.ctc_g.jfw.core.jdbc.JxSqlSessionFactoryBuilder"&gt;
 *   &lt;property name="paginatedResultHandler" ref="myPaginatedResultHandler" /&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id="myPaginatedResultHandler"
 *   class="my.custom.application.MyPaginatedResultHandler" /&gt;
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSession
 * @see JxSqlSessionFactory
 * @see JxSqlSessionFactoryBuilder
 */
public interface PaginatedResultHandler {

    /**
     * {@link Paginatable} によりページングされた結果を加工します。
     * @param statement 実行されたSQLのID
     * @param parameter 検索パラメータ
     * @param total トータル件数
     * @param result 検索結果
     * @return 加工された検索結果
     * @param <E> 検索結果の型
     */
    <E> List<E> createPaginatedResult(String statement, Paginatable parameter, Integer total, List<E> result);
    
    /**
     * {@link RowBounds} によりページングされた結果を加工します。
     * @param statement 実行されたSQLのID
     * @param parameter 検索パラメータ
     * @param rowBounds ページング境界
     * @param total トータル件数
     * @param result 検索結果
     * @return 加工された検索結果
     * @param <E> 検索結果の型
     */
    <E> List<E> createPaginatedResult(String statement, Object parameter, RowBounds rowBounds, Integer total, List<E> result);
}
