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

import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * このクラスは、クラス操作をサポートするためユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Classes {

    private static final Log L = LogFactory.getLog(Classes.class);

    private static final ResourceBundle R = InternalMessages.getBundle(Classes.class);

    private Classes() {
    }

    /**
     * 推奨されるクラスローダを返却します。
     * このメソッドは、コンテキストクラスローダの取得を試みますが、
     * それができない場合、自身をロードしたクラスローダを利用します。
     * @return 推奨されるクラスローダ
     */
    public static ClassLoader preferredClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException e) {
            // コンテキストクラスローダの取得権限がない場合は自身のクラスローダを利用しますので、
            // この例外は無視します(ログにも記録する必要はありません)。
        }
        if (loader == null) {
            loader = Classes.class.getClassLoader();
        }
        return loader;
    }

    /**
     * 指定された文字列で表現されるクラスをロードします。
     * クラスローダには、{@link #preferredClassLoader()}の結果が利用されます。
     * @param <T> ロードするクラスの型
     * @param className クラスの名前
     * @return クラス
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(String className) {
        return (Class<T>) forName(className, preferredClassLoader());
    }

    /**
     * 指定された文字列で表現されるクラスをロードします。
     * @param <T> ロードするクラスの型
     * @param className クラスの名前
     * @param loader 利用するクラスローダ
     * @return クラス
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(String className, ClassLoader loader) {
        Args.checkNotBlank(className);
        loader = loader == null ? preferredClassLoader() : loader;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, loader);
        } catch (ClassNotFoundException e) {
            if (L.isDebugEnabled()) {
                String message = Strings.substitute(R.getString("D-UTIL#0001"), Maps.hash("class", className));
                L.debug(message);
            }
        }
        return (Class<T>) clazz;
    }

    /**
     * 継承関係にあるクラスとクラスの間がどれほど「離れているか」を計算します。
     * 換言すると、クラス階層における継承回数です。
     * 例えば、以下の通りです。
     * <pre class="brush:java">
     * Classes.howFar(Object.class, Object.class); // 0
     * Classes.howFar(String.class, Object.class); // 1
     * Classes.howFar(JPanel.class, Object.class); // 4
     * Classes.howFar(JPanel.class, JComponent.class); // 1
     * </pre>
     * もし、クラスに関連がない場合は、<code>-1</code>が返却されます。
     * @param from 起点となるクラス
     * @param to より上位のクラス
     * @return クラス間の距離
     */
    public static int howFar(Class<?> from, Class<?> to) {
        Args.checkNotNull(from);
        Args.checkNotNull(to);
        if (from == to) return 0;
        if (!to.isAssignableFrom(from)) return -1;
        int distance = 0;
        Class<?> hierarchyExplorer = from;
        while (hierarchyExplorer != null && hierarchyExplorer != to) {
            hierarchyExplorer = hierarchyExplorer.getSuperclass();
            if (hierarchyExplorer != null) {
                distance++;
            } else {
                // 型互換が保障されているので、この処理は通りません
                assert false;
            }
        }
        return distance;
    }

}
