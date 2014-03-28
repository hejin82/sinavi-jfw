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

import jp.co.ctc_g.jfw.core.consts.LogLevel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * このクラスは、SQLのログ出力に関するデフォルトの実装を提供します。
 * このクラスにより提供されるログ出力処理の実装は、{@link QueryLoggingInterceptor}を設定する
 * だけで有効になります。
 * <p>
 * <h3>デフォルトのログ出力処理</h3>
 * <p>
 * デフォルトでは、Commons logging APIを利用したログ出力実装が提供されます。<br>
 * なお、カテゴリ、ログレベルのデフォルト値は以下の値として設定されます。
 * <ul>
 *   <li>カテゴリ：{@link QueryLogger} のFQCN </li>
 *   <li>ログレベル：INFO</li>
 * </ul>
 * これを変更するには、以下の設定を行う必要があります。
 * </p>
 * <pre>
 * &lt;bean id=&quot;defaultLoggingStrategy&quot; class=&quot;jp.co.ctc_g.jfw.core.jdbc.mybatis.DefaultLoggingStrategy&quot;&gt;
 *     &lt;constructor-arg index=&quot;0&quot;&gt;
 *         &lt;value&gt;カテゴリ&lt;/value&gt;
 *     &lt;/constructor-arg&gt;
 *     &lt;constructor-arg index=&quot;1&quot;&gt;
 *         &lt;value&gt;ログレベル&lt;/value&gt;
 *     &lt;/constructor-arg&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * ログレベルは、commons-logging に従い、次の6つが指定可能です。
 * <ul>
 *     <li>TRACE</li>
 *     <li>DEBUG</li>
 *     <li>INFO</li>
 *     <li>WARN</li>
 *     <li>ERROR</li>
 *     <li>FATEAL</li>
 * </ul>
 * </p>
 * <p>
 * なお、J-Frameworkでは、ロギング実装としてlog4jを使用するためlog4jの設定を行う必要があります。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class DefaultLoggingStrategy implements LoggingStrategy {

    protected Log log;

    protected LogLevel logLevel;

    private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.INFO;

    private static final Class<?> DEFAULT_CATEGORY = QueryLogger.class;

    /**
     * デフォルト・コンストラクタ
     */
    public DefaultLoggingStrategy() {
        setDefaultCategory();
        setDefaultLogLevel();
    }

    /**
     * コンストラクタ
     * @param logLevelString ログレベル文字列
     */
    public DefaultLoggingStrategy(String logLevelString) {
        this(null, logLevelString);
    }

    /**
     * コンストラクタ
     * @param category カテゴリ文字列
     * 指定が行われなかった場合、{@link QueryLogger}のFQCNが使用されます。
     * {@code null}が設定された場合、
     * @param logLevelString ログレベル文字列
     * {@code commons-loggin}に基づき、TRACE / DEBUG / INFO / WARN / ERROR / FATAL が指定可能。
     * {@code null}が設定された場合、または、サポートされないログレベルが指定された場合はデフォルトログレベル{@code INFO}が設定されます。
     */
    public DefaultLoggingStrategy(String category, String logLevelString) {
        if (category != null) {
            log = LogFactory.getLog(category);
        } else {
            setDefaultCategory();
        }
        if (logLevelString != null) {
            LogLevel selected = LogLevel.getLogLevel(logLevelString);
            if (selected != LogLevel.UNSUPPORTED) {
                this.logLevel = selected;
            } else {
                setDefaultLogLevel();
            }
        } else {
            setDefaultLogLevel();
        }
    }

    private void setDefaultCategory() {
        log = LogFactory.getLog(DEFAULT_CATEGORY);
    }

    private void setDefaultLogLevel() {
        this.logLevel = DEFAULT_LOG_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    public void log(String sql) {

        switch(this.logLevel) {
            case TRACE:
                if (log.isTraceEnabled()) {
                    log.trace(sql);
                }
                break;
            case DEBUG:
                if (log.isDebugEnabled()) {
                    log.debug(sql);
                }
                break;
            case WARN:
                if (log.isWarnEnabled()) {
                    log.warn(sql);
                }
                break;
            case ERROR:
                if (log.isErrorEnabled()) {
                    log.error(sql);
                }
                break;
            case FATAL:
                if (log.isFatalEnabled()) {
                    log.fatal(sql);
                }
                break;
            case INFO:
            default:
                if (log.isInfoEnabled()) {
                    log.info(sql);
                }
                break;
        }
    }
}
