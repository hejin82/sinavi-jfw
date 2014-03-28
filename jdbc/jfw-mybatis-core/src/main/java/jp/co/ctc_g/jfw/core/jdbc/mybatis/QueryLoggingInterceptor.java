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

package jp.co.ctc_g.jfw.core.jdbc.mybatis;

import java.sql.Connection;
import java.util.Properties;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Reflects;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * <p>
 * SQLロギング機構を起動するためのMyBatis専用インターセプタです。
 * MyBatis内部のクエリ実行要求をインターセプトし、SQLのロギング機構を起動します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Intercepts({@Signature(type=StatementHandler.class, method="prepare", args={Connection.class})})
public class QueryLoggingInterceptor implements Interceptor, BeanFactoryAware, InitializingBean {

    private QueryLogger queryLogger;

    private static final String CONVERTER_CLASS_KEY = "CONVERTER_CLASS";

    private BeanFactory beanFactory;

    private boolean normalize = true;

    /**
     * デフォルトコンストラクタです。
     */
    public QueryLoggingInterceptor() {}

    /**
     * {@inheritDoc}
     * <p>
     * {@code StatementHandler}へのクエリ実行要求をインターセプトし、 SQLのロギング機構を起動します。
     * </p>
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        QueryInformation queryInformation = new QueryInformation((StatementHandler)invocation.getTarget());
        Object result;
        queryLogger.log(queryInformation);
        result = invocation.proceed();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperties(Properties properties) {
        initialize(properties);
    }

    /**
     * 正規化するかどうかを設定します。
     * @param normalize 正規化するかどうか
     */
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    private void initialize(Properties properties) {
        String convertorClass = properties.getProperty(CONVERTER_CLASS_KEY);

        if (convertorClass != null) {
            LiteralConvertorRegistory registory = LiteralConvertorRegistory.getInstance();
            for (String c : convertorClass.split(",")) {
                registory.regist(createConvertor(c.trim()));
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private LiteralConvertor createConvertor(String convertorClass) {
        try {
            return (LiteralConvertor)Reflects.make(Class.forName(convertorClass), (Object[])null);
        } catch (ClassNotFoundException e) {
            throw new InternalException(QueryLoggingInterceptor.class, "E-JDBC-MYBATIS#0001", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.queryLogger = new QueryLogger(normalize);
        LoggingStrategy strategy;
        try {
            strategy = this.beanFactory.getBean(LoggingStrategy.class);
        } catch (NoSuchBeanDefinitionException e) {
            strategy = new DefaultLoggingStrategy();
        }
        this.queryLogger.setLoggingStrategy(strategy);
    }
}
