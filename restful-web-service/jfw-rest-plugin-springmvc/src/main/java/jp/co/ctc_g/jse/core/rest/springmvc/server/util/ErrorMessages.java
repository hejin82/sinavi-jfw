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

package jp.co.ctc_g.jse.core.rest.springmvc.server.util;

import jp.co.ctc_g.jse.core.rest.entity.ErrorMessage;
import jp.co.ctc_g.jse.core.rest.entity.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * このクラスは、例外が発生した際に例外情報を呼び出し元に返すドメインを構築するユーティリティです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class ErrorMessages {

    private final ErrorMessage error;
    private final List<ValidationMessage> validation = new LinkedList<ValidationMessage>();

    /**
     * このユーティリティのインスタンスを生成します。
     * @return ユーティリティ
     */
    public static ErrorMessages create() {
        return new ErrorMessages();
    }

    /**
     * このユーティリティのインスタンスを生成します。
     * 引数で指定されたステータスコードで内部で保持している例外情報のドメインにステータスコードを設定します。
     * @param status HTTPステータス
     * @return ユーティリティ
     */
    public static ErrorMessages create(HttpStatus status) {
        return new ErrorMessages(status);
    }

    /**
     * HTTPステータスを設定します。
     * @param status HTTPステータス
     * @return ユーティリティ
     */
    public ErrorMessages status(HttpStatus status) {
        error.setStatus(status.value());
        error.setMessage(status.getReasonPhrase());
        return this;
    }

    /**
     * ランダムUUIDを設定します。
     * @return ユーティリティ
     */
    public ErrorMessages id() {
        error.setId(UUID.randomUUID().toString());
        return this;
    }

    /**
     * 指定されたIDを設定します。
     * @param id ID
     * @return ユーティリティ
     */
    public ErrorMessages id(String id) {
        error.setId(id);
        return this;
    }

    /**
     * HTTPステータスコードを設定します。
     * @param status HTTPステータスコード
     * @return ユーティリティ
     */
    public ErrorMessages status(int status) {
        error.setStatus(status);
        return this;
    }

    /**
     * エラーメッセージを設定します。
     * @param message メッセージ
     * @return ユーティリティ
     */
    public ErrorMessages message(String message) {
        error.setMessage(message);
        return this;
    }

    /**
     * エラーコードを設定します。
     * @param code エラーコード
     * @return ユーティリティ
     */
    public ErrorMessages code(String code) {
        error.setCode(code);
        return this;
    }

    /**
     * 指定されたプロパティ名、メッセージをバリデーションメッセージに追加します。
     * @param propertyName プロパティ名
     * @param message メッセージ
     * @return ユーティリティ
     */
    public ErrorMessages bind(String propertyName, String message) {
        validation.add(new ValidationMessage(propertyName, message));
        return this;
    }

    /**
     * 指定されたバインディング結果をバリデーションメッセージに追加します。
     * @param result バインディング結果
     * @return ユーティリティ
     */
    public ErrorMessages bind(BindingResult result) {
        if (result != null && result.hasFieldErrors()) {
            for (FieldError field : result.getFieldErrors()) {
                validation.add(new ValidationMessage(field.getObjectName() + "." + field.getField(),
                                                     field.getDefaultMessage()));
            }
        }
        return this;
    }

    /**
     * エラーメッセージを取得します。
     * @return {@link ErrorMessage} オブジェクト
     */
    public ErrorMessage get() {
        if (!validation.isEmpty()) error.setValidationMessages(validation);
        return error;
    }

    private ErrorMessages() {
        this.error = new ErrorMessage();
    }

    private ErrorMessages(HttpStatus status) {
        this.error = new ErrorMessage();
        this.error.setStatus(status.value());
        this.error.setMessage(status.getReasonPhrase());
    }

}
