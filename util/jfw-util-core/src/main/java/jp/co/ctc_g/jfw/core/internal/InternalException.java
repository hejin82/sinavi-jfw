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

package jp.co.ctc_g.jfw.core.internal;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;

import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * この例外クラスは、J-Frameworkの内部で例外が発生したことを表現する例外クラスです。
 * 全ての例外に例外コードが設定されなければなりません。
 * また、例外コードに対応するメッセージは、フレームワーク内部のものであっても、
 * ローカライズ可能でなければなりません。
 * </p>
 * <h4>コンストラクタのClass引数</h4>
 * <p>
 * 例外コード及びメッセージローカライズの方針を容易に実現するために、
 * このクラスはコンストラクタにて例外の呼び出し元を識別するClassクラスを受け取ります。
 * このClassクラスは、{@link InternalMessages}と連携して動作します。
 * </p>
 * @see RuntimeException
 */
public class InternalException extends RuntimeException {

    private static final long serialVersionUID = -7212994373365288556L;

    protected final String code;

    protected final Date date;

    protected final String id;

    protected final String message;

    /**
     * 例外発生クラスとエラーコードを指定するコンストラクタです。
     * @param thrower 例外発生クラス
     * @param code エラーコード
     */
    public InternalException(Class<?> thrower, String code) {

        super();
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(thrower, code, Collections.<String, String> emptyMap());
    }

    /**
     * 例外発生クラスとエラーコード、置換文字列を指定するコンストラクタです。
     * @param thrower 例外発生クラス
     * @param code エラーコード
     * @param args 置換文字列
     */
    public InternalException(Class<?> thrower, String code, Map<String, ?> args) {

        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(thrower, code, args);
    }

    /**
     * 例外発生クラスとエラーコード、メッセージ、原因となった例外の置換文字列を指定するコンストラクタです。
     * @param thrower 例外発生クラス
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public InternalException(Class<?> thrower, String code, Map<String, ?> args, Throwable cause) {

        super(cause);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(thrower, code, args);
    }

    /**
     * 例外発生クラスとエラーコード、原因となった例外の置換文字列を指定するコンストラクタです。
     * @param thrower 例外発生クラス
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public InternalException(Class<?> thrower, String code, Throwable cause) {

        super(cause);
        this.id = generateId();
        this.code = code;
        this.date = new Date();
        this.message = generateMessage(thrower, code, Collections.<String, String> emptyMap());
    }

    /**
     * エラーメッセージのみを指定するコンストラクタです。
     * @param message エラーメッセージ
     */
    private InternalException(String message) {

        this.id = generateId();
        this.code = message;
        this.date = new Date();
        this.message = message;
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
     * エラーメッセージを例外発生クラスと同一のパッケージに格納されているリソースから取得します。
     * @param thrower 例外発生クラス
     * @param c エラーコード
     * @param args 置換文字列
     * @return エラーメッセージ
     */
    protected String generateMessage(Class<?> thrower, String c, Map<String, ?> args) {

        try {
            ResourceBundle bundle = InternalMessages.getBundle(thrower);
            return Strings.substitute(bundle.getString(c), args);
        } catch (MissingResourceException e) {
            throw new InternalException(c);
        }
    }

}
