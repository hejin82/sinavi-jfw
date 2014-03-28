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
 * このクラスは、アプリケーションが回復不可能な例外を表現するクラスです。
 * 回復不可能であるとは、システムの運用管理者などのシステム外部からの介入がなければ、
 * 正常稼働を継続することができない状態を指します。
 * アプリケーションが回復できない場合にこの例外クラスを利用してください。
 * また、アプリケーションが回復できる場合は{@link ApplicationRecoverableException}を利用してください。
 * </p>
 * @see ApplicationException
 * @see ApplicationRecoverableException
 */
public class ApplicationUnrecoverableException extends ApplicationException {

    private static final long serialVersionUID = -6703718368224759610L;

    /**
     * エラーコードのみを指定するコンストラクタです。
     * @param code エラーコード
     */
    public ApplicationUnrecoverableException(String code) {

        super(code);
    }

    /**
     * エラーコードとメッセージの置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     */
    public ApplicationUnrecoverableException(String code, Map<String, ?> args) {

        super(code, args);
    }

    /**
     * エラーコードと原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param cause 原因となった例外
     */
    public ApplicationUnrecoverableException(String code, Throwable cause) {

        super(code, cause);
    }

    /**
     * エラーコードとメッセージ、原因となった例外の置換文字列を指定するコンストラクタです。
     * @param code エラーコード
     * @param args 置換文字列
     * @param cause 原因となった例外
     */
    public ApplicationUnrecoverableException(String code, Throwable cause, Map<String, ?> args) {

        super(code, args, cause);
    }

}
