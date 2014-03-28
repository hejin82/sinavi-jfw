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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>
 * このクラスは、配列に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Arrays {

    private Arrays() {
    }

    /**
     * 指定された可変長引数を配列にまとめて返却します。
     * @param <T> 返却される配列の型
     * @param elements 配列にまとめられる要素群
     * @return 指定された可変長引数をまとめた配列
     */
    public static <T> T[] gen(T... elements) {
        return elements;
    }

    /**
     * 指定されたサイズの配列を返却します。
     * 配列は{@link GenCall}の戻り値で満たされています。
     * @param <T> 返却される配列の型
     * @param size 返却される配列のサイズ
     * @param generator 配列生成オブジェクト
     * @return {@link GenCall}の戻り値で満たされた配列
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] gen(int size, GenCall<T> generator) {
        if (size < 1 || generator == null) return null;
        List<T> results = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            T result = generator.gen(i, size);
            if (result != null) results.add(result);
        }
        if (results.size() > 0) {
            T t = results.get(0);
            T[] zero = (T[]) Array.newInstance(t.getClass(), 0);
            return (T[]) results.toArray(zero);
        } else {
            // 言語仕様の限界なのでサイズ0の配列ではなくnullを返却します。
            // よいアイディアがある方は教えてください。
            return null;
        }
    }

    /**
     * 指定された配列に、指定されたオブジェクトが含まれているかどうかを探索します。
     * この探索は先頭から末尾に向かって線形に実行されるため、探索時間は最小1、最大n、平均でn/2となります。
     * また、オブジェクト通しの「等しさ」は{@link Object#equals(Object)}により検証されます
     * （検証対象要素が{@code null}の場合、は == を利用します）。
     * @param <T> 検証対象の型
     * @param t 検証対象の配列
     * @param suspect 検索対象要素
     * @return 等しい要素のインデクス、見つからない場合は<code>-1</code>
     */
    public static <T> int contain(T[] t, T suspect) {
        int index = -1;
        if (t == null) return index;
        if (suspect == null) {
            for (int i = 0; i < t.length; i++) if (t[i] == null) index = i;
        } else {
            for (int i = 0; i < t.length; i++) if (suspect.equals(t[i])) index = i;
        }
        return index;
    }

    /**
     * 指定された配列を反復可能な{@link Iterator}を返却します。
     * @param <T> 反復される型
     * @param elements 生成されるイテレータにより反復される要素
     * @return イテレータ
     */
    public static <T> Iterator<T> iterator(T... elements) {
        return new ArrayIterator<T>(elements);
    }

    /**
     * 第1引数に指定された配列の末尾に、残りの引数を追加します。
     * この配列の結合は浅いコピーにより行なわれます。
     * つまり、2つの配列のサイズの和のサイズを持った新しい要素が作成され、
     * そこに既存の要素が追加されることになります。
     * もし、former、あるいはlatterのどちらかが{@code null}であったり、サイズがからの場合、
     * 同様に浅くコピーされた配列が返却されます。つまり、下記は正しい表明です。
     * この挙動は、空要素をマージするという意味になります。
     * <pre class="brush:java">
     * String[] before = {"1", "2", "3"};
     * Stringp[] after = Arrays.merge(before);
     * assert before != after;
     * </pre>
     * @param <T> 配列要素の型
     * @param former この配列の後ろにlatterを追加
     * @param latter formerの後ろに追加される要素
     * @return 配列
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] merge(final T[] former, final T... latter) {
        if ((former == null || former.length == 0) && (latter == null || latter.length == 0))
            return null;
        if (former == null || former.length == 0) return latter.clone();
        if (latter == null || latter.length == 0) return former.clone();
        T[] merged = (T[]) Array.newInstance(
                former.getClass().getComponentType(),
                former.length + latter.length);
        System.arraycopy(former, 0, merged, 0, former.length);
        System.arraycopy(latter, 0, merged, former.length, latter.length);
        return merged;
    }

    /**
     * 指定された配列から要素を取り出し、指定されたコールバッククラスを呼び出します。
     * @param <T> 繰り返し対象の型
     * @param elements 繰り返す要素
     * @param callback 繰り返し時のコールバック
     * @return elements引数
     * @see EachCall
     */
    public static <T> T[] each(T[] elements, EachCall<T> callback) {
        if (elements == null || callback == null || elements.length == 0)
            return elements;
        int index = 0;
        int total = elements.length;
        for (T t : elements) {
            callback.each(t, index++, total);
        }
        return elements;
    }

    /**
     * 指定された配列から要素を取り出し、
     * 指定されたコールバッククラスが返却したオブジェクトをまとめて返却します。
     * コールバッククラスが{@code null}を返却した場合、
     * それは処理結果の配列には含まれません。
     * よって、指定された配列よりも、結果の配列の方がサイズが小さくなる可能性があります。
     * @param <T> 繰り返し対象の型
     * @param elements 繰り返す要素
     * @param callback 繰り返し時のコールバック
     * @return コールバック結果をまとめた配列
     * @see CollectCall
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] collect(T[] elements, CollectCall<T> callback) {
        if (elements == null || callback == null || elements.length == 0)
            return elements;
        int index = 0;
        int total = elements.length;
        List<T> results = new ArrayList<T>(total);
        for (T t : elements) {
            T result = callback.collect(t, index++, total);
            if (result != null) results.add(result);
        }
        T[] zero = (T[]) Array.newInstance(elements.getClass().getComponentType(), 0);
        return (T[]) results.toArray(zero);
    }

    /**
     * 指定された配列から要素を取り出し、
     * コールバックの結果に従ってグループ分けします。
     * コールバッククラスが{@code null}を返却した場合、
     * 処理中のオブジェクトは処理結果のマップには含まれません。
     * @param <T> グループ化する要素の型
     * @param elements グループ化する要素
     * @param callback 繰り返し時のコールバック
     * @return グループ化された要素が格納されたマップ
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T[]> group(T[] elements, GroupCall<T> callback) {
        if (elements == null || callback == null || elements.length == 0)
            return Collections.<String, T[]>emptyMap();
        int index = 0;
        int total = elements.length;
        Map<String, List<T>> grouped = new HashMap<String, List<T>>();
        for (T t : elements) {
            String key = callback.group(t, index++, total);
            if (key != null) {
                List<T> g = grouped.get(key);
                if (g != null) {
                    g.add(t);
                } else {
                    g = new ArrayList<T>();
                    g.add(t);
                    grouped.put(key, g);
                }
            }
        }
        Map<String, T[]> results = new HashMap<String, T[]>(grouped.size());
        T[] zero = (T[]) Array.newInstance(elements.getClass().getComponentType(), 0);
        for (String key : grouped.keySet()) {
            List<T> g = grouped.get(key);
            results.put(key, g.toArray(zero));
        }
        return results;
    }

    /**
     * 指定された配列が{@code null}あるいは空要素かどうかを判定します。
     * @param <T> 検査対象の配列要素の型
     * @param suspect 検査対象の配列
     * @return 指定された配列が{@code null}あるいは空要素の場合true
     */
    public static <T> boolean isEmpty(T[] suspect) {
        return suspect == null || suspect.length == 0;
    }

    /**
     * 指定された配列が{@code null}あるいは空要素<strong>ではない</strong>かどうかを判定します。
     * @param <T> 検査対象の配列要素の型
     * @param suspect 検査対象の配列
     * @return 指定された配列が{@code null}あるいは空要素<strong>ではない</strong>場合true
     */
    public static <T> boolean isNotEmpty(T[] suspect) {
        return !isEmpty(suspect);
    }

    /**
     * 指定された配列を、指定された位置からスライスします。
     * @param <T> スライス対象の配列要素の型
     * @param slicee スライス対象の配列
     * @param start スライスの開始位置
     * @return スライスされた配列
     */
    public static <T> T[] slice(T[] slicee, int start) {
        Args.checkNotNull(slicee);
        if (slicee.length == 0) return slicee;
        Args.checkPositiveOrZero(start);
        return slice(slicee, start, slicee.length - start);
    }

    /**
     * 指定された配列を、指定された位置から、指定された要素分だけスライスします。
     * @param <T> スライス対象の配列要素の型
     * @param slicee スライス対象の配列
     * @param start スライスの開始位置
     * @param length スライスする長さ
     * @return スライスされた配列
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] slice(T[] slicee, int start, int length) {
        Args.checkNotNull(slicee);
        if (slicee.length == 0) return slicee;
        Args.checkPositiveOrZero(start);
        Args.checkPositiveOrZero(length);
        T[] newer = (T[]) Array.newInstance(slicee.getClass().getComponentType(), length);
        for (int newIndex = 0, index = start; newIndex < length; newIndex++, index++) {
            newer[newIndex] = slicee[index];
        }
        return newer;
    }

    /**
     * {@link Iterator}インタフェースの実装クラスです。
     * @param <T> 要素の型
     */
    public static class ArrayIterator<T> implements Iterator<T> {

        private int current;
        private T[] elements;

        /**
         * コンストラクタです。
         * @param elements 要素
         */
        public ArrayIterator(T[] elements) {
            this.elements = elements;
        }
        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return elements == null ?
                    false : current < elements.length;
        }
        /**
         * {@inheritDoc}
         */
        public T next() {
            if (hasNext()) {
                T element = elements[current++];
                return element;
            } else {
                throw new NoSuchElementException();
            }
        }
        /**
         * {@inheritDoc}
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
