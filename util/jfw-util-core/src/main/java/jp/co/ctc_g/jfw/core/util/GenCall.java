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

/**
 * <p>
 * このインタフェースは、{@link Arrays#gen(int, GenCall)}や
 * {@link Lists#gen(int, GenCall)}などのメソッドからのコールバックを受けるためのインタフェースです。
 * </p>
 * @param <T> 配列やリスト内の要素の型
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface GenCall<T> {

    /**
     * 配列やリスト内の要素を生成するためのジェネレータメソッドです。
     * このメソッドが{@code null}を返却した場合、
     * コールバック元メソッドは結果を無視します（配列やリストに含めません）。
     * @param index 現在のループインデクス
     * @param total トータル要素数
     * @return 配列やリストに含める要素
     */
    T gen(int index, int total);
}
