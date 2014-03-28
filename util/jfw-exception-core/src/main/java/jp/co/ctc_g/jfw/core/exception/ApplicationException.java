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

import java.util.Map;

/**
 * <p>
 * このクラスは、J-Frameworkのアプリケーション例外の基底クラスです。
 * </p>
 * @see AbstractException
 */
public class ApplicationException extends AbstractException {

    private static final long serialVersionUID = 2332843159182165114L;

    /**
     * エラーコードとメッセージ、原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public ApplicationException(String code, Map<String, ?> args, Throwable cause) {

        super(code, args, cause);
    }

    /**
     * エラーコードとメッセージの置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     */
    public ApplicationException(String code, Map<String, ?> args) {

        super(code, args);
    }

    /**
     * エラーコードと原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public ApplicationException(String code, Throwable cause) {

        super(code, cause);
    }

    /**
     * エラーコードのみを指定するコンストラクタです。
     * @param code エラーコード
     */
    public ApplicationException(String code) {

        super(code);
    }

}
