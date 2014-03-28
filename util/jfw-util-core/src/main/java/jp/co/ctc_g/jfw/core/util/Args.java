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

package jp.co.ctc_g.jfw.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.InternalException;

/**
 * <p>
 * このクラスは、引数に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Args {

    private static String[] ignoredCallers = Arrays.gen(Args.class.getName());

    private Args() {
    }

    /**
     * このメソッドを呼び出したメソッドの、呼び出し元クラス名とメソッド名を取得します。
     * 例えば、Fooクラスのbarメソッドがこのメソッドを呼び出したとすると、
     * 結果は、Fooクラスのbarメソッドを呼び出したクラスの名前とメソッドの名前です。
     * つまり、現在のメソッドがどのクラスのどのメソッドによって起動されたかを知ることができます。
     * メソッド名とクラス名は .（ドット記号）で接続されています（スタックとレースと同じ表記方法です）。
     * @return このメソッドの呼び出し元クラス名とメソッド名
     */
    public static String caller() {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        int callerIndex = Integer.MIN_VALUE;
        for (int i = 1; i < elements.length; i++) {
            String c = elements[i].getClassName();
            if (Arrays.contain(ignoredCallers, c) == -1) {
                callerIndex = i;
                break;
            }
        }
        return Strings.join(
                elements[callerIndex].getClassName(),
                ".",
                elements[callerIndex].getMethodName());
    }

    /**
     * このメソッドは、現在処理中のメソッドが指定されたクラスを経由して呼び出されたかどうかを判定します。
     * 例えば、
     * <pre class="brush:java">
     * class Foo {
     *     public static void bar() {
     *         Buz.qux();
     *     }
     * }
     *
     * class Buz {
     *     public static void qux() {
     *         assert Args.calledBy(Foo.class);
     *         OK();
     *     }
     * }
     *
     * Foo.bar();
     * </pre>
     * ということです。これにより、
     * 自分が誰に呼び出されたかによって処理を変えることもできるようになります（それが優れたアイディアかどうかはおまかせします）。
     * また、直前の呼び出し元だけでなく、このメソッドはコールスタックを無限に遡って、
     * 呼び出し元に含まれているかどうかを判定します。
     * よって、大抵のWebアプリケーションでは、
     * <pre class="brush:java">
     * Args.calledBy(Thread.class); // true
     * </pre>
     * となります。
     * @param caller 呼び出されたクラス
     * @return 指定されたクラスが呼び出し元クラスに含まれていた場合はtrue
     */
    public static boolean calledBy(Class<?> caller) {
        return calledBy(caller, Integer.MAX_VALUE);
    }

    /**
     * このメソッドは、現在処理中のメソッドが指定されたクラスを経由して呼び出されたかどうかを判定します。
     * 例えば、
     * <pre class="brush:java">
     * class Foo {
     *     public static void bar() {
     *         Buz.qux();
     *     }
     * }
     *
     * class Buz {
     *     public static void qux() {
     *         assert Args.calledBy(Foo.class);
     *         OK();
     *     }
     * }
     *
     * Foo.bar();
     * </pre>
     * ということです。これにより、
     * 自分が誰に呼び出されたかによって処理を変えることもできるようになります（それが優れたアイディアかどうかはおまかせします）。
     * また、直前の呼び出し元だけでなく、このメソッドはコールスタックを指定された深さ分だけ遡って、
     * 呼び出し元に含まれているかどうかを判定します。
     * @param caller 呼び出し元に含まれているかどうかを判定したいクラス
     * @param depth コールスタックを遡る深さ
     * @return 指定されたクラスが呼び出し元クラスに含まれていた場合はtrue
     */
    public static boolean calledBy(Class<?> caller, int depth) {
        Args.checkNotNull(caller);
        Args.checkTrue(depth > -1);
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        boolean found = false;
        String callerName = caller.getName();
        for (int i = 0; i < elements.length && i < depth; i++) {
            if (found = elements[i].getClassName().equals(callerName)) break;
        }
        return found;
    }

    /**
     * 第1引数が{@code null}の場合は第2引数を返却し、
     * それ以外の場合は第1引数を返却するメソッドです。
     * @param <T> 入出力型
     * @param returnIfNotNull {@code null}でない場合に返却されるオブジェクト
     * @param alternative 第1引数が{@code null}の場合に返却されるオブジェクト
     * @return 第1引数、あるいは第2引数
     */
    public static <T> T proper(T returnIfNotNull, T alternative) {
        if (returnIfNotNull == null) {
            return alternative;
        } else {
            return returnIfNotNull;
        }
    }

    // アーギュメント検証 -----------------------------------------------------------

    /**
     * 引数が{@code true}であることを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0001）
     */
    public static void checkTrue(boolean suspect) {
        checkTrue(suspect, "E-UTIL#0001");
    }

    /**
     * 引数が{@code true}であることを表明します。
     * @param suspect 検査対象
     * @param message 例外メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0001）
     */
    public static void checkTrue(boolean suspect, Object message) {
        if (!suspect) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}でないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0002）
     */
    public static void checkNotNull(Object suspect) {
        checkNotNull(suspect, "E-UTIL#0002");
    }

    /**
     * 引数が{@code null}でないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0002）
     */
    public static void checkNotNull(Object suspect, Object message) {
        if (suspect == null) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}あるいは空の配列でないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0003）
     */
    public static void checkNotEmpty(Object[] suspect) {
        checkNotEmpty(suspect, "E-UTIL#0003");
    }

    /**
     * 引数が{@code null}あるいは空の配列でないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0003）
     */
    public static void checkNotEmpty(Object[] suspect, Object message) {
        checkNotNull(suspect, message);
        if (suspect == null || suspect.length == 0) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}あるいは{@link Strings#isEmpty(CharSequence) 空の文字列}でないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0006）
     */
    public static void checkNotEmpty(CharSequence suspect) {
        checkNotEmpty(suspect, "E-UTIL#0006");
    }

    /**
     * 引数が{@code null}あるいは{@link Strings#isEmpty(CharSequence) 空の文字列}でないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0006）
     */
    public static void checkNotEmpty(CharSequence suspect, Object message) {
        checkNotNull(suspect, message);
        if (Strings.isEmpty(suspect)) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}あるいは空のコレクションでないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0004）
     */
    public static void checkNotEmpty(Collection<?> suspect) {
        checkNotEmpty(suspect, "E-UTIL#0004");
    }

    /**
     * 引数が{@code null}あるいは空のコレクションでないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0004）
     */
    public static void checkNotEmpty(Collection<?> suspect, Object message) {
        checkNotNull(suspect, message);
        if (suspect == null || suspect.isEmpty()) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}あるいは空のマップでないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0005）
     */
    public static void checkNotEmpty(Map<?, ?> suspect) {
        checkNotEmpty(suspect, "E-UTIL#0005");
    }

    /**
     * 引数が{@code null}あるいは空のマップでないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0005）
     */
    public static void checkNotEmpty(Map<?, ?> suspect, Object message) {
        checkNotNull(suspect, message);
        if (suspect.isEmpty()) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が{@code null}あるいは{@link Strings#isEmpty(CharSequence) ブランク文字列}でないことを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0007）
     */
    public static void checkNotBlank(CharSequence suspect) {
        checkNotBlank(suspect, "E-UTIL#0007");
    }

    /**
     * 引数が{@code null}あるいは{@link Strings#isBlank(CharSequence) ブランク文字列}でないことを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0007）
     */
    public static void checkNotBlank(CharSequence suspect, Object message) {
        if (Strings.isBlank(suspect)) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が自然数（1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ... n）であることを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0008）
     */
    public static void checkPositive(int suspect) {
        checkPositive(suspect, "E-UTIL#0008");
    }

    /**
     * 引数が自然数、つまり（1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ... n）であることを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0008）
     */
    public static void checkPositive(int suspect, Object message) {
        if (suspect <= 0) {
            raise(message, suspect);
        }
    }

    /**
     * 引数が0または自然数、つまり（0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ... n）であることを表明します。
     * @param suspect 検査対象
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0034）
     */
    public static void checkPositiveOrZero(int suspect) {
        checkPositiveOrZero(suspect, "E-UTIL#0034");
    }

    /**
     * 引数が0または自然数、つまり（0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ... n）であることを表明します。
     * @param suspect 検査対象
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0034）
     */
    public static void checkPositiveOrZero(int suspect, Object message) {
        if (suspect < 0) {
            raise(message, suspect);
        }
    }

    /**
     * 指定されたオブジェクトが指定された型に代入可能であることを表明します。
     * @param suspect 検査対象
     * @param assignable 対象の型
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0009）
     */
    public static void checkAssignable(Object suspect, Class<?> assignable) {
        checkAssignable(suspect, assignable, "E-UTIL#0009");
    }

    /**
     * 指定されたオブジェクトが指定された型に代入可能であることを表明します。
     * @param suspect 検査対象
     * @param assignable 対象の型
     * @param message メッセージあるいはエラーID
     * @throws InternalException 検証の結果不正な場合（E-UTIL#0009）
     */
    public static void checkAssignable(Object suspect, Class<?> assignable, Object message) {
        if (!assignable.isAssignableFrom(suspect.getClass())) {
            Map<String, String> replace = new HashMap<String, String>(3);
            replace.put("super", assignable.toString());
            raise(message, suspect, replace);
        }
    }

    private static void raise(Object message, Object arg) {
        raise(message, arg, null);
    }

    private static void raise(Object message, Object arg, Map<String, String> replace) {
        if (replace == null) {
            replace = new HashMap<String, String>(2);
        }
        replace.put("arg", Args.proper(arg, "null").toString());
        replace.put("caller", caller());
        throw new InternalException(Args.class, message.toString(), replace);
    }
}
