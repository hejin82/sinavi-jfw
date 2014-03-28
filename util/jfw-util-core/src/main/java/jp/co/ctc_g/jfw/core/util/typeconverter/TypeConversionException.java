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

package jp.co.ctc_g.jfw.core.util.typeconverter;

import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.InternalException;

/**
 * <p>
 * このクラスは、型変換時に発生する例外を表現します。
 * </p>
 * 
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class TypeConversionException extends InternalException {

    private static final long serialVersionUID = 5414940284463051398L;

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * 
     * @param code
     *            エラーコード
     * @param thrower
     *            この例外の作成者
     * @param args
     *            パラメータ
     */
    public TypeConversionException(String code, Class<?> thrower, Map<String, String> args) {
        super(thrower, code, args);
    }

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * 
     * @param code
     *            エラーコード
     * @param thrower
     *            この例外の作成者
     * @param cause
     *            原因
     * @param args
     *            パラメータ
     */
    public TypeConversionException(String code, Class<?> thrower, Throwable cause, Map<String, String> args) {
        super(thrower, code, args, cause);
    }

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * 
     * @param code
     *            エラーコード
     * @param thrower
     *            この例外の作成者
     * @param cause
     *            原因
     */
    public TypeConversionException(String code, Class<?> thrower, Throwable cause) {
        super(thrower, code, cause);
    }

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * 
     * @param code
     *            エラーコード
     * @param thrower
     *            この例外の作成者
     */
    public TypeConversionException(String code, Class<?> thrower) {
        super(thrower, code);
    }
}
