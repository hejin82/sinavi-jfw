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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * このクラスは、{@link Map}に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Maps {

    private Maps() {
    }

    /**
     * {@link HashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> hash(K key, V value) {
        return createFluentMap(new HashMap<K, V>()).map(key, value);
    }

    /**
     * {@link HashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param initialCapacity 初期容量
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> hash(int initialCapacity, K key, V value) {
        return createFluentMap(new HashMap<K, V>(initialCapacity)).map(key, value);
    }

    /**
     * {@link HashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param initialCapacity 初期容量
     * @param loadFactor ロードファクタ
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> hash(
            int initialCapacity,
            float loadFactor,
            K key,
            V value) {
        return createFluentMap(new HashMap<K, V>(initialCapacity, loadFactor)).map(key, value);
    }

    /**
     * {@link HashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param m コピー元マップ
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> hash(Map<? extends K, ? extends V> m) {
        return createFluentMap(new HashMap<K, V>(m));
    }

    /**
     * {@link LinkedHashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> linkedHash(K key, V value) {
        return createFluentMap(new LinkedHashMap<K, V>()).map(key, value);
    }

    /**
     * {@link LinkedHashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param initialCapacity 初期容量
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> linkedHash(int initialCapacity, K key, V value) {
        return createFluentMap(new LinkedHashMap<K, V>(initialCapacity)).map(key, value);
    }

    /**
     * {@link LinkedHashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param initialCapacity 初期容量
     * @param loadFactor ロードファクタ
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> linkedHash(
            int initialCapacity,
            float loadFactor,
            K key,
            V value) {
        return createFluentMap(new LinkedHashMap<K, V>(
                initialCapacity, loadFactor)).map(key, value);
    }

    /**
     * {@link LinkedHashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param initialCapacity 初期容量
     * @param loadFactor ロードファクタ
     * @param accessOrder アクセスオーダ
     * @param key マップキー
     * @param value マップキーに対応する値
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> linkedHash(
            int initialCapacity,
            float loadFactor,
            boolean accessOrder,
            K key,
            V value) {
        return createFluentMap(new LinkedHashMap<K, V>(
                initialCapacity, loadFactor, accessOrder)).map(key, value);
    }

    /**
     * {@link LinkedHashMap}を内部に持っている{@link FluentMap}を構築します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param m コピー元マップ
     * @return {@link FluentMap}
     */
    public static <K, V> FluentMap<K, V> linkedHash(Map<? extends K, ? extends V> m) {
        return createFluentMap(new LinkedHashMap<K, V>(m));
    }

    /**
     * 指定されたマップが{@code null}あるいは空要素かどうかを判定します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param suspect 検査対象のマップ
     * @return 指定されたマップが{@code null}あるいは空要素の場合true
     */
    public static <K, V> boolean isEmpty(Map<K, V> suspect) {
        return suspect == null || suspect.isEmpty();
    }

    /**
     * 指定されたマップが{@code null}あるいは空要素<strong>ではない</strong>かどうかを判定します。
     * @param <K> マップキーの型
     * @param <V> マップキーに対応する値の型
     * @param suspect 検査対象のマップ
     * @return 指定されたマップが{@code null}あるいは空要素<strong>ではない</strong>場合true
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> suspect) {
        return !isEmpty(suspect);
    }

    private static <K, V> FluentMap<K, V> createFluentMap(Map<K, V> internal) {
        return new FluentMap<K, V>(internal);
    }

    /**
     * <p>
     * このクラスは、マップをメソッドチェーンにより構築する機能を提供します。
     * </p>
     * <pre class="brush:java">
     * Map<String, String> m = Maps.hash("foo", "bar").map("buz", "qux");
     * </pre>
     * @param <K> 内部のマップ実装のキーの型
     * @param <V> キーに対応する値の型
     * @see Maps
     */
    public static class FluentMap<K, V> implements Map<K, V> {

        private Map<K, V> internal;

        /**
         * このクラスのインスタンスを生成します。
         * この場合、内部では{@link HashMap}を利用します。
         */
        public FluentMap() {
            internal = new HashMap<K, V>();
        }

        /**
         * 指定されたマップを利用して、
         * このクラスのインスタンスを生成します。
         * @param internal マップインスタンス
         */
        public FluentMap(Map<K, V> internal) {
            this.internal = internal;
        }

        /**
         * 内部のマップ実装に対して、{@link Map#put(Object, Object)}します。
         * このメソッドは、自身を返却するためメソッドチェーンをつなげることができます。
         * @param key マップキー
         * @param value キーに対応する値
         * @return このインスタンス
         */
        public FluentMap<K, V> map(K key, V value) {
            put(key, value);
            return this;
        }

        // --------------------------------------------------------------------------------

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#clear()
         */
        public void clear() {
            internal.clear();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#containsKey(Object)
         */
        public boolean containsKey(Object key) {
            return internal.containsKey(key);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#containsValue(Object)
         */
        public boolean containsValue(Object value) {
            return internal.containsValue(value);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#entrySet()
         */
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            return internal.entrySet();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#equals(Object)
         */
        public boolean equals(Object o) {
            return internal.equals(o);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#get(Object)
         */
        public V get(Object key) {
            return internal.get(key);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#hashCode()
         */
        public int hashCode() {
            return internal.hashCode();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#isEmpty()
         */
        public boolean isEmpty() {
            return internal.isEmpty();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#keySet()
         */
        public Set<K> keySet() {
            return internal.keySet();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#put(Object, Object)
         */
        public V put(K key, V value) {
            return internal.put(key, value);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#putAll(Map)
         */
        public void putAll(Map<? extends K, ? extends V> t) {
            internal.putAll(t);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#remove(Object)
         */
        public V remove(Object key) {
            return internal.remove(key);
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#size()
         */
        public int size() {
            return internal.size();
        }

        /**
         * {@inheritDoc}
         * 内部のマップ実装へ委譲します。
         * @see Map#values()
         */
        public Collection<V> values() {
            return internal.values();
        }

        // --------------------------------------------------------------------------------

    }
}
