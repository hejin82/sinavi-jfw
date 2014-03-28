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

package jp.co.ctc_g.jse.core.util.web.beans;


/**
 * <p>
 * このクラスはリクエスト・パラメータのBeanへのバインディング、および、入力値検証に関するエラーを
 * {@code J-Framework} が内部的に処理する際に利用する例外クラスです。<br/>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class PropertyEditingException extends RuntimeException {

    private static final long serialVersionUID = 2617010182858913101L;

    private String code;
    
    private String message;

    /**
     * デフォルトコンストラクタです。
     */
    public PropertyEditingException() {}

    /**
     * コンストラクタです。
     * @param code メッセージコード
     */
    public PropertyEditingException(String code) {
        this(code, null);
    }

    /**
     * コンストラクタです。
     * @param code メッセージコード
     * @param argMap 置換文字列
     */
    public PropertyEditingException(String code, String message) {
        super(code);
        this.code = code;
        this.message = message;
    }

    /**
     * メッセージコードを返却します。
     * @return メッセージコード
     */
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
