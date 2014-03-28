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

package jp.co.ctc_g.jse.core.amqp.exception;

import java.util.Map;

import jp.co.ctc_g.jfw.core.exception.AbstractException;

/**
 * <p>
 * このクラスは、AMQPの例外の基底クラスです。
 * </p>
 * <p>
 * このクラスは、AMQP例外の基底クラスで、以下の情報を保持します。
 * </p>
 * <ol>
 *  <li>ルーティングキー：例外に応じて配送先のキューを決定するためのルーティングキーです。</li>
 * </ol>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public abstract class AbstractAmqpException extends AbstractException {

    private static final long serialVersionUID = 7372367461221820949L;

    private final String routingKey;

    /**
     * エラーコードとルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     */
    public AbstractAmqpException(String code, String routingKey) {
        super(code);
        this.routingKey = routingKey;
    }
    
    /**
     * エラーコードとメッセージの置換文字列、ルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     */
    public AbstractAmqpException(String code, Map<String, ?> args, String routingKey) {
        super(code, args);
        this.routingKey = routingKey;
    }

    /**
     * エラーコードとルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AbstractAmqpException(String code, String routingKey, Throwable cause) {
        super(code, cause);
        this.routingKey = routingKey;
    }
    
    /**
     * エラーコードとメッセージの置換文字列、ルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AbstractAmqpException(String code, Map<String, ?> args, String routingKey, Throwable cause) {
        super(code, args, cause);
        this.routingKey = routingKey;
    }

    /**
     * ルーティングキーを取得します。
     * @return ルーティングキー
     */
    public String getRoutingKey() {
        return routingKey;
    }
    
}
