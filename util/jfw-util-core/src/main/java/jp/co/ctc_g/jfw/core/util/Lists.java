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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.CoreInternals;

/**
 * <p>
 * このクラスは、{@link List}に関するユーティリティを提供します。
 * </p>
 * <h4>クラスコンフィグオーバライド</h4>
 * <p>
 * 以下の{@link Config クラスコンフィグオーバライド}用のキーが公開されています。
 * </p>
 * <table class="property_file_override_info">
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>型</th>
 *    <th>効果</th>
 *    <th>デフォルト</th>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>general_implementation</td>
 *    <td>java.lang.String</td>
 *    <td>明示的に指定されない場合の{@link List}実装クラスの完全修飾クラス名を指定します。</td>
 *    <td>
 *     {@link ArrayList java.util.ArrayList}
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@SuppressWarnings("unchecked")
public final class Lists {

    /**
     * 空の{@link PartialList}のインスタンスです。
     * 総称性を有効にしてこのインスタンスを取得たい場合には、
     * {@link #emptyPartialList()}を利用してください。
     */
    @SuppressWarnings("rawtypes")
    public static final PartialList EMPTY_PARTIAL_LIST = new EmptyPartialList();

    /**
     * 明示的に指定されない場合の{@link List}実装クラスです。
     */
    protected static final Class<?> LIST_IMPL;

    static {
        Config c = CoreInternals.getConfig(Lists.class);
        String impl = c.find("general_implementation", ArrayList.class.getName());
        LIST_IMPL = (Class<?>) Classes.forName(impl);
    }

    private Lists() {
    }


    /**
     * 指定された可変長引数をリストにまとめて返却します。
     * @param <T> 返却されるリストの型
     * @param elements リストにまとめられる要素群
     * @return 指定された可変長引数をまとめたリスト
     */
    public static <T> List<T> gen(T... elements) {
        if (elements == null || elements.length == 0) return Collections.<T>emptyList();
        List<T> list = (List<T>) Reflects.make(LIST_IMPL);
        for (T t : elements) {
            list.add(t);
        }
        return list;
    }

    /**
     * 指定されたサイズのリストを返却します。
     * リストは{@link GenCall}の戻り値で満たされています。
     * @param <T> 返却されるリストの型
     * @param size 返却されるリストのサイズ
     * @param generator リスト要素生成オブジェクト
     * @return {@link GenCall}の戻り値で満たされたリスト
     */
    public static <T> List<T> gen(int size, GenCall<T> generator) {
        return (List<T>) gen(size, (Class<? extends List<T>>) LIST_IMPL, generator);
    }

    /**
     * 指定されたサイズのリストを返却します。
     * リストは{@link GenCall}の戻り値で満たされています。
     * @param <T> 返却されるリストの型
     * @param <U> コンテナ型
     * @param size 返却されるリストのサイズ
     * @param container {@link List}実装クラス
     * @param generator リスト要素生成オブジェクト
     * @return {@link GenCall}の戻り値で満たされたリスト
     */
    public static <T, U extends List<T>> U gen(int size, Class<U> container, GenCall<T> generator) {
        if (size < 1 || generator == null) return (U) Collections.<T>emptyList();
        U results = (U) Reflects.make(container);
        for (int i = 0; i < size; i++) {
            T result = generator.gen(i, size);
            if (result != null) results.add(result);
        }
        return results;
    }

    /**
     * 指定されたリストから要素を取り出し、指定されたコールバッククラスを呼び出します。
     * @param <T> 繰り返し対象の型
     * @param elements 繰り返すリスト
     * @param callback 繰り返し時のコールバック
     * @return elements引数
     * @see EachCall
     */
    public static <T> List<T> each(List<T> elements, EachCall<T> callback) {
        if (elements == null || callback == null || elements.size() == 0)
            return Collections.<T>emptyList();
        int index = 0;
        int total = elements.size();
        for (T t : elements) {
            callback.each(t, index++, total);
        }
        return elements;
    }

    /**
     * 指定されたリストから要素を取り出し、
     * 指定されたコールバッククラスが返却したオブジェクトをまとめて返却します。
     * コールバッククラスが{@code null}を返却した場合、
     * それは処理結果の配列には含まれません。
     * よって、指定されたリストよりも、結果のリストの方がサイズが小さくなる可能性があります。
     * @param <T> 繰り返し対象の型
     * @param elements 繰り返す要素
     * @param callback 繰り返し時のコールバック
     * @return コールバック結果をまとめたリスト
     * @see CollectCall
     */
    public static <T> List<T> collect(List<T> elements, CollectCall<T> callback) {
        if (elements == null || callback == null || elements.size() == 0)
            return Collections.<T>emptyList();
        int index = 0;
        int total = elements.size();
        List<T> results = Reflects.make(elements.getClass());
        for (T t : elements) {
            T result = callback.collect(t, index++, total);
            if (result != null) results.add(result);
        }
        return results;
    }

    /**
     * 指定されたリストから要素を取り出し、
     * コールバックの結果に従ってグループ分けします。
     * コールバッククラスが{@code null}を返却した場合、
     * 処理中のオブジェクトは処理結果のマップには含まれません。
     * @param <T> グループ化する要素の型
     * @param elements グループ化する要素
     * @param callback 繰り返し時のコールバック
     * @return グループ化された要素が格納されたマップ
     */
    public static <T> Map<String, List<T>> group(List<T> elements, GroupCall<T> callback) {
        if (elements == null || callback == null || elements.size() == 0)
            return Collections.<String, List<T>>emptyMap();
        int index = 0;
        int total = elements.size();
        Map<String, List<T>> grouped = new HashMap<String, List<T>>();
        for (T t : elements) {
            String key = callback.group(t, index++, total);
            if (key != null) {
                List<T> g = grouped.get(key);
                if (g != null) {
                    g.add(t);
                } else {
                    g = Reflects.make(elements.getClass());
                    g.add(t);
                    grouped.put(key, g);
                }
            }
        }
        return grouped;
    }

    /**
     * 指定されたリストが{@code null}あるいは空要素かどうかを判定します。
     * @param <T> 検査対象のリスト要素の型
     * @param suspect 検査対象のリスト
     * @return 指定されたリストが{@code null}あるいは空要素の場合true
     */
    public static <T> boolean isEmpty(List<T> suspect) {
        return suspect == null || suspect.isEmpty();
    }

    /**
     * 空の{@link PartialList}を、総称性を有効に保ったまま返却します。
     * @param <T> 返却される空の{@link PartialList}が保持するべき要素型
     * @return 空の{@link PartialList}
     */
    public static <T> PartialList<T> emptyPartialList() {
        return (PartialList<T>) EMPTY_PARTIAL_LIST;
    }

    private static class EmptyPartialList<T> extends PartialList<T> {

        private static final long serialVersionUID = -6814725955681747486L;

        public EmptyPartialList() {
            super(Collections.<T>unmodifiableList(Collections.<T>emptyList()));
        }

        private Object readResolve() {
            // シングルトンでお願いします
            return EMPTY_PARTIAL_LIST;
        }
    }
}
