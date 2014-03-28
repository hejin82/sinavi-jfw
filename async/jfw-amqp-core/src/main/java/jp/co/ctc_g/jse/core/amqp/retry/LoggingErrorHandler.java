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

package jp.co.ctc_g.jse.core.amqp.retry;

import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、メッセージ受信側で例外が発生した場合にログを出力する機能を提供します。
 * </p>
 * <p>
 * リトライ例外であれば警告レベルで回復不可能な例外であればエラーレベルでログを出力します。
 * ログに出力するフォーマットを変更する場合は{@link AbstractAmqpErrorHandler}を継承し、
 * {@link AbstractAmqpErrorHandler#warn(Throwable)}と{@link AbstractAmqpErrorHandler#error(Throwable)}のメソッドを
 * オーバライドしてください。
 * </p>
 * @see AbstractAmqpErrorHandler
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class LoggingErrorHandler extends AbstractAmqpErrorHandler {

    private static final Logger L = LoggerFactory.getLogger(LoggingErrorHandler.class);
    private static final ResourceBundle R = InternalMessages.getBundle(LoggingErrorHandler.class);

    /**
     * デフォルトコンストラクタです。
     */
    public LoggingErrorHandler() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void warn(Throwable t) {
        L.warn(R.getString("W-AMQP-RETRY#0001"), t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void error(Throwable t) {
        L.error(R.getString("E-AMQP-RETRY#0003"), t);
    }

}
