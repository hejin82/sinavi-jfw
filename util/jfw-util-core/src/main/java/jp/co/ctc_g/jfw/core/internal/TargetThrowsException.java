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

import java.util.Map;

/**
 * <p>
 * この例外クラスは、リフレクトされたメソッドを実行した際に対象のメソッドが例外を投げた場合に発生します。
 * </p>
 * <p>
 * この例外クラスは、JavaSEにおける{@link java.lang.reflect.InvocationTargetException}と同様の意味付けがなされています。
 * とはいえ、2つの点で異なっています。まず1つ目は、この例外クラスは実行時例外であり例外の検証を強制しないことです。
 * これにより、対象の例外を検証する必要がない場合には、不要なtry-catchを省略できます。
 * 2つ目は、この例外クラスの特性上、原因クラスを与えることなくインスタンスを生成することを抑止していることです。
 * よって、{@link TargetThrowsException#getCause()}は必ず意味のある原因例外を返却するように設計されています。
 * </p>
 * <p>
 * この例外クラスはJ-Frameworkの内部例外設計基本クラスである{@link InternalException}のサブクラスです。
 * この例外クラスを捕捉する場合には、{@link InternalException}よりも先に捕捉する必要があります。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see java.lang.reflect.InvocationTargetException
 * @see jp.co.ctc_g.jfw.core.util.Reflects
 */
public class TargetThrowsException extends InternalException {

    private static final long serialVersionUID = -4869357961887980784L;

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * @param thrower この例外の作成者
     * @param code エラーコード
     * @param args パラメータ
     * @param cause 原因
     */
    public TargetThrowsException(Class<?> thrower, String code, Map<String, ?> args, Throwable cause) {
        super(thrower, code, args, cause);
    }

    /**
     * 指定された引数を利用してこのクラスのインスタンスを生成します。
     * @param thrower この例外の作成者
     * @param code エラーコード
     * @param cause 原因
     */
    public TargetThrowsException(Class<?> thrower, String code, Throwable cause) {
        super(thrower, code, cause);
    }
}
