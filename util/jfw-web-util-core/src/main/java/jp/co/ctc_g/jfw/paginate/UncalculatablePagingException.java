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

package jp.co.ctc_g.jfw.paginate;

import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.InternalException;

/**
 * <p>
 * このクラスは、ページング処理時に不正な値が設定されたときに発生する例外です。
 * </p>
 */
public class UncalculatablePagingException extends InternalException {

    private static final long serialVersionUID = 1L;
    
    /**
     * コンストラクタです。
     * @param code エラーコード
     * @param thrower 例外発生クラス
     * @param args エラーメッセージ置換文字列
     */
    public UncalculatablePagingException(
            String code,
            Class<?> thrower,
            Map<String, String> args) {
        super(thrower, code, args);
    }
}
