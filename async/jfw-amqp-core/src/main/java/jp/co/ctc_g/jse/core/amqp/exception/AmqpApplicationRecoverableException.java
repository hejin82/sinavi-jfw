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
 * このクラスは、アプリケーションが回復可能な例外を表現するクラスです。
 * 回復可能であるとは、システムの運用管理者などのシステム外部からの介入を必要とせず、
 * 自律的に正常稼働状態に遷移することができること指します。
 * メッセージ受信側の処理をアプリケーション的にリトライさせたい場合にこの例外をスローします。
 * また、アプリケーションに回復できない場合は{@link jp.co.ctc_g.jse.core.amqp.exception.AmqpApplicationUnrecoverableException}を利用してください。
 * </p>
 * <p>
 * この例外が発生した場合はフレームワーク内でリトライ処理を行い、自動で回復を試みます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class AmqpApplicationRecoverableException extends AbstractAmqpException {

    private static final long serialVersionUID = -3789379857104200316L;

    private static final String ROUTING_KEY;
    static {
        Config c = AmqpInternals.getConfig(AmqpApplicationRecoverableException.class);
        ROUTING_KEY = c.find("routing_key");
    }

    /**
     * エラーコードのみを指定するコンストラクタです。
     * @param code エラーコード
     */
    public AmqpApplicationRecoverableException(String code) {
        super(code, ROUTING_KEY);
    }

    /**
     * エラーコードと原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public AmqpApplicationRecoverableException(String code, Throwable cause) {
        super(code, ROUTING_KEY, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public AmqpApplicationRecoverableException(String code, Map<String, ?> args, Throwable cause) {
        super(code, args, ROUTING_KEY, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     */
    public AmqpApplicationRecoverableException(String code, Map<String, ?> args) {
        super(code, args, ROUTING_KEY);
    }

    /**
     * エラーコードとルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     */
    public AmqpApplicationRecoverableException(String code, String routingKey) {
        super(code, routingKey);
    }

    /**
     * エラーコードとルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AmqpApplicationRecoverableException(String code, String routingKey, Throwable cause) {
        super(code, routingKey, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、ルーティングキー、原因となった例外を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     * @param cause 原因となった例外
     */
    public AmqpApplicationRecoverableException(String code, Map<String, ?> args, String routingKey, Throwable cause) {
        super(code, args, routingKey, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列、ルーティングキーを指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param routingKey ルーティングキー
     */
    public AmqpApplicationRecoverableException(String code, Map<String, ?> args, String routingKey) {
        super(code, args, routingKey);
    }

}
