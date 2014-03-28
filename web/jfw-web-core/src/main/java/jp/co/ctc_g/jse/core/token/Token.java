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

package jp.co.ctc_g.jse.core.token;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.ctc_g.jse.core.framework.Controllers;

/**
 * <p>
 * この注釈は、リクエストトークンに関する制御を実現します。
 * </p>
 * <h4>リクエストトークン</h4>
 * <p>
 * リクエストトークンは、ワンタイムトークンやトランザクショントークンなど様々に呼ばれますが、
 * <storng>リクエスト毎に割り振られるID</strong>のことです。
 * リクエストトークンを利用すると、リクエストの順序が重要であるような画面遷移を安全に行なうことができます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Token {

    /**
     * リクエストトークンを保存するかどうかを設定します。
     * リクエストトークンを保存する場合はtrue
     */
    boolean save() default false;

    /**
     * リクエストトークンをチェックするかどうかを設定します。
     * リクエストトークンをチェックする場合はtrue
     */
    boolean check() default false;

    /**
     * リクエストトークンをチェック後リセットするかどうかを設定します。
     * リクエストトークンをリセットする場合はtrue
     */
    boolean reset() default true;

    /**
     * トークンを保存するスコープを指定します。
     * トークンを保存するスコープ
     */
    String scope() default Controllers.SCOPE_SESSION;

}
