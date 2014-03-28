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

package jp.co.ctc_g.jfw.paginate;

import jp.co.ctc_g.jfw.core.util.Lists;
import jp.co.ctc_g.jfw.core.util.PartialList;

/**
 * <p>
 * このクラスは、{@link PartialList}のいくつかのメソッドを関数タグライブラリから利用するためのユーティリティアダプタです。
 * 通常、このクラスを利用する必要はありません。{@link PartialList}の同名のメソッドを直接コールしてください。
 * このクラスはアプリケーションサーバで発生する、関数タグライブラリの問題を回避するために利用されています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Partials {

    private Partials() {
    }

    /**
     * 背後にある全体集合の要素数を返却します。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return 背後にある全体集合の要素数
     */
    public static <E> Integer getElementCount(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getElementCount();
    }

    /**
     * この部分集合の全体集合に対するインデクス(1..N)を返却します。
     * このコンテナが要素を保持していない場合など現在の部分インデクスが不明確な場合、
     * このメソッドが0を返却する場合があります。
     * とはいえ、要素数が空であるかどうかは、{@link jp.co.ctc_g.jfw.core.util.Lists#isEmpty(java.util.List)}を利用するべきであり、
     * 要素数の確認のためにこのメソッドを呼び出すべきではありません。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return この部分集合の全体集合に対するインデクス(1..N)
     */
    public static <E> Integer getPartIndex(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getPartIndex();
    }

    /**
     * 背後にある全体集合を構成する部分集合の総数を返却します。
     * もしelementCountPerPartプロパティが5でelementCountが9である場合、partCountは2となります。
     * つまり、全ての要素を充分に収容可能なように算出されます。
     * このコンテナが要素を保持していない場合、このメソッドが0を返却します。
     * とはいえ、要素数が空であるかどうかは、{@link jp.co.ctc_g.jfw.core.util.Lists#isEmpty(java.util.List)}を利用するべきであり、
     * 要素数の確認のためにこのメソッドを呼び出すべきではありません。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return 背後にある全体集合を構成する部分集合の総数
     */
    public static <E> Integer getPartCount(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getPartCount();
    }

    /**
     * 1つの部分集合に含まれる要素数を返却します。
     * このメソッドは正の整数のみを返却し、負の数や0を返却することはありません。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return 1つの部分集合に含まれる要素数
     */
    public static <E> Integer getElementCountPerPart(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getElementCountPerPart();
    }

    /**
     * この部分集合が保持する先頭要素の全体集合における位置を返却します。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return この部分集合が保持する先頭要素の全体集合における位置
     */
    public static <E> Integer getElementBeginIndex(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getElementBeginIndex();
    }

    /**
     * この部分集合が保持する先頭要素の全体集合における位置を返却します。
     * @param <E> 要素
     * @param partials 要素の一覧
     * @return この部分集合が保持する先頭要素の全体集合における位置
     */
    public static <E> Integer getElementEndIndex(PartialList<E> partials) {
        if (Lists.isEmpty(partials)) return 0;
        return partials.getElementEndIndex();
    }
}
