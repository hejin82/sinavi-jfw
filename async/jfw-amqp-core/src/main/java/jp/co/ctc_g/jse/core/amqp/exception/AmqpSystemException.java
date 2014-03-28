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

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jse.core.amqp.internal.AmqpInternals;

/**
 * <p>
 * このクラスは、メッセージ受信側の処理中にシステムが回復不可能な例外を表現するクラスです。
 * 回復不可能であるとは、システムの運用管理者などのシステム外部からの介入がなければ、
 * 正常稼働を継続することができない状態を指します。
 * システムが回復できない場合にこの例外クラスを利用してください。
 * </p>
 */
public class AmqpSystemException extends AbstractAmqpException {

    private static final long serialVersionUID = -3789379857104200316L;

    private static final String ROUTING_KEY;
    static {
        Config c = AmqpInternals.getConfig(AmqpSystemException.class);
        ROUTING_KEY = c.find("routing_key");
    }

    /**
     * エラーコードのみを指定するコンストラクタです。
     * @param code エラーコード
     */
    public AmqpSystemException(String code) {
        super(code, ROUTING_KEY);
    }

    /**
     * エラーコードと原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public AmqpSystemException(String code, Throwable cause) {
        super(code, ROUTING_KEY, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public AmqpSystemException(String code, Map<String, ?> args, Throwable cause) {
        super(code, args, ROUTING_KEY, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     */
    public AmqpSystemException(String code, Map<String, ?> args) {
        super(code, args, ROUTING_KEY);
    }

    /**
     * エラーコードとルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     */
    public AmqpSystemException(String code, String routingKey) {
        super(code, routingKey);
    }

    /**
     * エラーコードとルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AmqpSystemException(String code, String routingKey, Throwable cause) {
        super(code, routingKey, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、ルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AmqpSystemException(String code, Map<String, ?> args, String routingKey, Throwable cause) {
        super(code, args, routingKey, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、ルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     */
    public AmqpSystemException(String code, Map<String, ?> args, String routingKey) {
        super(code, args, routingKey);
    }
}
