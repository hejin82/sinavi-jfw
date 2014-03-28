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

package jp.co.ctc_g.jfw.core.exception;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import jp.co.ctc_g.jfw.core.resource.Rs;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * このクラスは、J-Frameworkの例外の基底クラスです。
 * </p>
 * <h4>概要</h4>
 * <p>
 * このクラスは、J-Frameworkの例外の基底クラスで、以下の情報を保持します。
 * </p>
 * <ol>
 * <li>エラーID:例外を一意に特定するためのIDです。(ランダムなIDを生成します。)</li>
 * <li>エラーコード：例外発生時のエラーコードです。(例：E#0001)</li>
 * <li>エラー発生日時：例外発生時の時刻です。</li>
 * <li>エラーメッセージ：エラーコードに紐づくエラーメッセージです。</li>
 * </ol>
 * <h4>カスタマイズ</h4>
 * <p>
 * エラーIDの生成アルゴリズムをデフォルトのものから変更したい場合は、
 * この基底クラスを継承して{@link AbstractException#generateId()}をオーバライドしてください。
 * </p>
 * <pre>
 * public class AppException extends AbstractException {
 *     &#064;Override
 *     protected void generateId() {
 *         // 生成アルゴリズムを実装
 *     }
 * }
 * </pre>
 * @see Runtime
 */
public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = -8268763724165016948L;

    protected final String id;

    protected final String code;

    protected final Date date;

    protected final String message;

    /**
     * エラーコードのみを指定するコンストラクタです。
     * @param code エラーコード
     */
    public AbstractException(String code) {

        super(code);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(code, Collections.<String, String> emptyMap());
    }

    /**
     * エラーコードとメッセージの置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     */
    public AbstractException(String code, Map<String, ?> args) {

        super(code);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(code, args);
    }

    /**
     * エラーコードとメッセージ、原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public AbstractException(String code, Map<String, ?> args, Throwable cause) {

        super(code, cause);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(code, args);
    }

    /**
     * エラーコードと原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public AbstractException(String code, Throwable cause) {

        super(code, cause);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(code, Collections.<String, String> emptyMap());
    }

    /**
     * エラーコードを取得します。
     * @return エラーコード
     */
    public String getCode() {

        return code;
    }

    /**
     * エラーの発生日時を取得します。
     * @return エラー発生日時
     */
    public Date getDate() {

        return date;
    }

    /**
     * エラーIDを取得します。
     * @return エラーID
     */
    public String getId() {

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {

        return message;
    }

    /**
     * エラーID(ランダムなID)を生成します。
     * @return エラーID
     */
    protected String generateId() {

        return UUID.randomUUID().toString();
    }

    /**
     * エラーメッセージをリソースファイルから取得します。
     * @param c エラーコード
     * @param as 置換文字列
     * @return エラーメッセージ
     */
    protected String generateMessage(String c, Map<String, ?> as) {

        return Strings.substitute(Rs.find(c), as);
    }

}
