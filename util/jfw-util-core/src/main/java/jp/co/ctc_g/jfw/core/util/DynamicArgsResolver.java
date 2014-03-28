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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * このクラスは、引数が不明な場合に、それらの引数に対応したインスタンスを解決するためのクラスです。
 * </p>
 * <p>
 * 例として、以下のようなクラスがあるとします。
 * </p>
 * <pre>
 * import java.util.Date;
 * public class DynamicArgsVictim {
 *
 *     public void needToResolve(String message, Date today) {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * メソッドneedToResolveの引数にはStringとDateが宣言されていますが、
 * このメソッドを呼び出す側はneedToResolveというメソッド名しか知らず、
 * 引数に何が指定されているかは知りません。
 * このような場合、このクラスを利用すると、
 * いくつかの候補の中から自動的に適切な引数を解決してくれます。
 * </p>
 * <pre>
 * DynamicArgsResolver resolver = new DynamicArgsResolver();
 * resolver.candidate(String.class, "メッセージ");
 * resolver.candidate(Date.class, new Date());
 * resolver.candidate(Object.class, new Object());
 * resolver.candidate(Integer.class, 10);
 * Method needToResolve =
 *         Reflects.findMethodNamed("needToResolve", DynamicArgsVictim.class);
 * Object[] arguments = resolver.resolve(needToResolve);
 * Reflects.invoke(needToResolve, new DynamicArgsVictim(), arguments);
 * </pre>
 * <p>
 * みなさんの多くは、このクラスを利用する機会には恵まれないかもしれません。
 * 稀に、特にプロジェクト毎の共通処理などを作成する方には、このクラスがお役にたつかもしれません。
 * </p>
 * <p>
 * このクラスは複数スレッドからのアクセスに対して安全ではありません。
 * ただし、全ての引数候補を設定済みで、追加変更の可能性がないと保証される場合に限り、
 * {@link #resolve(Method)}メソッド、{@link #find(Class)}メソッド、{@link #invoke(Method, Object)}は
 * 複数スレッドからの並行アクセスに対しても動作が保証されます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class DynamicArgsResolver {

    private Map<String, Object> candidates;

    /**
     * このクラスのインスタンスを作成します。
     */
    public DynamicArgsResolver() {
        candidates = new HashMap<String, Object>(10);
    }

    /**
     * 引数候補を追加します。このメソッドは、
     * <pre>
     * candidate(candidate.getClass(), candidate);
     * </pre>
     * と同じ意味です。
     * @param candidate 引数候補として設定するオブジェクト
     * @return 引数候補が設定されたこのインスタンス
     */
    public DynamicArgsResolver candidate(Object candidate) {
        Args.checkNotNull(candidate);
        candidate(candidate.getClass(), candidate);
        return this;
    }

    /**
     * 引数候補を指定された型に関連付けて、追加します。
     * 例えば以下のようなメソッドの引数を解決するとします。
     * <pre>
     * public class DynaArgsVictim {
     *     public void method(CharSequence argToResolve) {
     *         ...
     *     }
     * }
     * </pre>
     * この場合、CharSequenceはインタフェースなので、直接的なインスタンスは生成できません。
     * そこで、
     * <pre>
     * Method method = Reflects.findMethodNamed("method", DynaArgsVictim.class);
     * DynamicArgsResolver resolver = new DynamicArgsResolver();
     * resolver.candidate(CharSequence.class, "メッセージ");
     * Reflects.invoke(method, new DynaArgsVictim(), resolver.resolve(method));
     * </pre>
     * このように記述することで、文字列&quot;メッセージ&quot;が引数に渡されます。
     * 第1引数の型と第2引数のインスタンスの関連は検証しませんが、
     * 一般的には、第1引数の型として第2引数のインスタンスが見なせるような関係であるべきです。
     * このメソッドは第1引数にnullは認めませんが、第2引数はnullを許容します。
     * @param clazz 解決時に参照される引数候補の型
     * @param candidate 引数候補として設定するオブジェクト
     * @return 引数候補が設定されたこのインスタンス
     */
    public DynamicArgsResolver candidate(Class<?> clazz, Object candidate) {
        Args.checkNotNull(clazz);
        this.candidates.put(clazz.getName(), candidate);
        return this;
    }

    /**
     * 引数で指定されたオブジェクトを、引数候補として追加します。
     * このメソッドは、引数で渡されたオブジェクトに対して
     * {@link #candidate(Object)}を呼び出すことと等価です。
     * @param cds 引数候補として設定するオブジェクト
     * @return 引数候補が設定されたこのインスタンス
     */
    public DynamicArgsResolver candidates(Object... cds) {
        Args.checkNotEmpty(cds);
        for (Object c : cds) {
            candidate(c);
        }
        return this;
    }

    /**
     * 指定されたメソッドに対して、現在設定されている引数候補を用いて引数に渡すオブジェクトを解決します。
     * もし解決されなかった引数がある場合、nullが設定されています。
     * また、返却される配列の大きさは、メソッドが受け取る引数の長さと一致します。
     * これは全てが解決できない場合でも、全てnullの配列を返却します。
     * このメソッドは、もちろん、引数のメソッドオブジェクトがnullであることを認めません。
     * @param method 引数を解決する必要のあるメソッドオブジェクト
     * @return 解決されたこのメソッドに適した引数オブジェクト
     */
    public Object[] resolve(Method method) {
        Args.checkNotNull(method);
        Class<?>[] argTypes = method.getParameterTypes();
        Object[] resolved = new Object[argTypes.length];
        int index = 0;
        for (Class<?> argType : argTypes) {
            resolved[index++] = candidates.get(argType.getName());
        }
        return resolved;
    }

    /**
     * 指定されたメソッドに対して、
     * 現在設定されている引数候補を用いて引数に渡すオブジェクトを解決し、
     * メソッドを第2引数で指定されたインスタンスを利用して起動します。
     * 引数オブジェクトの解決方法は{@link #resolve(Method)}に準じます。
     * このメソッドは、引数のメソッドオブジェクトがnullであることを認めませんが、
     * 第2引数のインスタンスはnullであることを許容します。
     * それは、静的メソッド呼び出しの場合に利用されるでしょう。
     * @param method 引数を解決する必要のあるメソッドオブジェクト
     * @param instance このメソッドを保持するインスタンス
     * @return メソッドの実行結果
     */
    public Object invoke(Method method, Object instance) {
        Args.checkNotNull(method);
        Args.checkNotNull(instance);
        Object[] resolved = resolve(method);
        return Reflects.invoke(method, instance, resolved);
    }

    /**
     * 指定されたクラスで識別される、現在設定されている引数候補を取得します。
     * 見つからない場合は、nullを返却します。
     * 引数で指定されたクラスがnullであることは認められません。
     * @param clazz 引数候補を識別するためのクラス
     * @return 指定されたクラスに対応する引数候補
     */
    public Object find(Class<?> clazz) {
        Args.checkNotNull(clazz);
        return candidates.get(clazz.getName());
    }
}
