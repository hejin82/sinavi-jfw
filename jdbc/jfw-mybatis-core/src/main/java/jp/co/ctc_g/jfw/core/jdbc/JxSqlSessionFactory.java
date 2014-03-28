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

import java.sql.Connection;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * <p>
 * このクラスは、J-Frameworkによよる {@link SqlSessionFactory} の実装クラスです。
 * {@link JxSqlSession}を生成するためのファクトリとして機能します。 
 * </p>
 * <p>
 * このクラスは{@link JxSqlSessionFactoryBuilder}により生成されるため、
 * 当該クラスを使用している場合にはアプリケーションで独自にインスタンスを生成する必要はありません。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSession
 * @see JxSqlSessionFactoryBuilder
 */
public class JxSqlSessionFactory implements SqlSessionFactory {

    private SqlSessionFactory delegate;

    private PaginationEnableMatcher paginationEnableMatcher;
    private CountSqlResolver countSqlResolver;
    private PaginatedResultHandler paginatedResultHandler;

    /**
     * デフォルトコンストラクタです。
     */
    public JxSqlSessionFactory() {}

    /**
     * 指定された{@link SqlSessionFactory}を内部に保持するこのクラスのインスタンスを生成します。
     * @param delegate 内部に保持されるファクトリ
     */
    public JxSqlSessionFactory(SqlSessionFactory delegate) {
        this.delegate = delegate;
    }
    
    /**
     * 指定された{@link SqlSession}を内部に保持する{@link JxSqlSession}のインスタンスを作成ます。
     * @param session 内部に保持されるセッション
     * @return セッションラッパー
     */
    protected SqlSession wrap(SqlSession session) {
        JxSqlSession jxsession = new JxSqlSession(session);
        jxsession.setPaginationEnableMatcher(paginationEnableMatcher);
        jxsession.setCountSqlResolver(countSqlResolver);
        jxsession.setPaginatedResultHandler(paginatedResultHandler);
        return jxsession;
    }

    // -----------------------------------------------------------------------------------

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession() {
        return wrap(delegate.openSession());
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param autoCommit オートコミットの設定
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(boolean autoCommit) {
        return wrap(delegate.openSession(autoCommit));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param connection コネクション
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(Connection connection) {
        return wrap(delegate.openSession(connection));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param level トランザクションの分離レベル
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(TransactionIsolationLevel level) {
        return wrap(delegate.openSession(level));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param execType 実行タイプ
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(ExecutorType execType) {
        return wrap(delegate.openSession(execType));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param execType 実行タイプ
     * @param autoCommit オートコミット
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        return wrap(delegate.openSession(execType, autoCommit));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param execType 実行タイプ
     * @param level トランザクションの分離レベル
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(ExecutorType execType,
            TransactionIsolationLevel level) {
        return wrap(delegate.openSession(execType, level));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @param execType 実行タイプ
     * @param connection コネクション
     * @return SqlSessionのインスタンス
     */
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        return wrap(delegate.openSession(execType, connection));
    }

    /**
     * このメソッドは委譲先ファクトリが返却したSqlSessionをラップして返却します。
     * @return 設定情報
     */
    public Configuration getConfiguration() {
        return delegate.getConfiguration();
    }

    // -----------------------------------------------------------------------------------

    /**
     * {@link CountSqlResolver}を返却します。
     * @return {@link CountSqlResolver}
     */
    public CountSqlResolver getCountSqlResolver() {
        return countSqlResolver;
    }

    /**
     * {@link CountSqlResolver}を設定します。
     * @param countSqlResolver {@link CountSqlResolver}
     */
    public void setCountSqlResolver(
            CountSqlResolver countSqlResolver) {
        this.countSqlResolver = countSqlResolver;
    }

    /**
     * {@link PaginatedResultHandler}を返却します。
     * @return {@link PaginatedResultHandler}
     */
    public PaginatedResultHandler getPaginatedResultHandler() {
        return paginatedResultHandler;
    }

    /**
     * {@link PaginatedResultHandler}を設定します。
     * @param paginatedResultHandler {@link PaginatedResultHandler}
     */
    public void setPaginatedResultHandler(PaginatedResultHandler paginatedResultHandler) {
        this.paginatedResultHandler = paginatedResultHandler;
    }

    /**
     * {@link PaginationEnableMatcher}を返却します。
     * @return {@link PaginationEnableMatcher}
     */
    public PaginationEnableMatcher getPaginationEnableMatcher() {
        return paginationEnableMatcher;
    }

    /**
     *  {@link PaginationEnableMatcher}を設定します。
     * @param paginationEnableMatcher  {@link PaginationEnableMatcher}
     */
    public void setPaginationEnableMatcher(PaginationEnableMatcher paginationEnableMatcher) {
        this.paginationEnableMatcher = paginationEnableMatcher;
    }
}
