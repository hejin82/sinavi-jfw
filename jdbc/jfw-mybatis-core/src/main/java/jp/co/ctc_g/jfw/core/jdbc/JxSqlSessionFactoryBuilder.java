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

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * <p>
 * このクラスは、J-Frameworkにより拡張された{@link SqlSessionFactoryBuilder}です。
 * {@link JxSqlSessionFactory}に対して{@link PaginationEnableMatcher}、
 * {@link CountSqlResolver}、{@link PaginatedResultHandler}を設定します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JxSqlSession
 * @see JxSqlSessionFactory
 * @see PaginatedResultHandler
 * @see PaginationEnableMatcher
 * @see CountSqlResolver
 */
public class JxSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    private PaginationEnableMatcher paginationEnableMatcher = new DefaultPaginationEnableMatcher();
    private CountSqlResolver countSqlResolver = new DefaultCountSqlResolver();
    private PaginatedResultHandler paginatedResultHandler = new PartialListResultHandler();

    /**
     * デフォルトコンストラクタです。
     */
    public JxSqlSessionFactoryBuilder() {}

    /**
     * {@link SqlSessionFactory}を構築します。
     * {@inheritDoc}
     */
    @Override
    public SqlSessionFactory build(Configuration config) {

        SqlSessionFactory delegate = super.build(config);
        JxSqlSessionFactory factory = new JxSqlSessionFactory(delegate);
        factory.setPaginationEnableMatcher(paginationEnableMatcher);
        factory.setCountSqlResolver(countSqlResolver);
        factory.setPaginatedResultHandler(paginatedResultHandler);
        return factory;
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
    public void setPaginationEnableMatcher(
            PaginationEnableMatcher paginationEnableMatcher) {
        this.paginationEnableMatcher = paginationEnableMatcher;
    }

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
    public void setCountSqlResolver(CountSqlResolver countSqlResolver) {
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
    public void setPaginatedResultHandler(
            PaginatedResultHandler paginatedResultHandler) {
        this.paginatedResultHandler = paginatedResultHandler;
    }
}
