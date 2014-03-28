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

package jp.co.ctc_g.jfw.profill;

import jp.co.ctc_g.jfw.core.internal.InternalException;

/**
 * <p>
 * この例外は、{@link Profill} の状態を変更できないことを示す場合に発生します。
 * 主として、{@link Profill#inThreadSafeMode} が <code>true</code> であるにもかかわらず、
 * {@link Profill#addFillingProvider(FillingProvider)} や 
 * {@link Profill#setTryToInstantiateIfNestedPropertyIsNull(boolean)}
 *  を呼び出した場合に発生します。
 * </p>
 * @see Profill
 */
public class CannotModifyProfillException extends InternalException {

    private static final long serialVersionUID = 1L;

    /**
     * このクラスのインスタンスを生成します。
     * @param code 例外コード
     * @param thrower 例外を発生させたクラス
     */
    public CannotModifyProfillException(String code, Class<?> thrower) {
        super(thrower, code);
    }
}
