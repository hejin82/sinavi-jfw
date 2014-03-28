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

package jp.co.ctc_g.jfw.core.util.porter;

import java.util.ArrayList;
import java.util.List;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、{@link Porter}処理中の{@link PortablePair}を
 * 取捨選択する{@link ManipulationFilter}です。
 * このフィルタを利用した場合、
 * 特定の名前(キー)を持つ{@link PortablePair}を選択的に{@link DestinationStrategy}
 * に伝えないようにすることができます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see Porter
 * @see PortablePair
 * @see ManipulationFilter
 * @see SourceStrategy
 * @see DestinationStrategy
 */
public class PortablePairSelector implements ManipulationFilter {

    private static final int IGNORE_SIZE = 2;

    private List<String> ignores;

    /**
     * このクラスのインスタンスを生成します。
     */
    public PortablePairSelector() {
        ignores = new ArrayList<String>(IGNORE_SIZE);
    }

    /**
     * このメソッドが起動されても、{@link #ignore(String)}で追加された無視定義はクリアされません。
     * @param source データ入力元オブジェクト
     * @param destination データ出力先オブジェクト
     * @see ManipulationFilter#initialize(Object, Object)
     */
    public void initialize(Object source, Object destination) {
    }

    /**
     * 引数に指定されたペアのキー(名前)が無視定義に含まれていた場合、
     * <code>null</code>を返却することで、直ちに現在の{@link Porter#process()}ループを1段階進めます。
     * これにより、{@link DestinationStrategy}までペアは到達できなくなります。
     * @param sourcePair ペアのキー(名前)
     * @return ペア無視する場合は<code>null</code>、無視しない場合は引数を返却
     */
    public PortablePair manipulate(PortablePair sourcePair) {
        if (ignores.contains(sourcePair.getKey())) return null;
        return sourcePair;
    }

    /**
     * 無視定義に指定された名前(ペアのキー)を追加します。
     * @param portablePairKey 無視するペア({@link PortablePair})のキー
     * @return このインスタンス
     */
    public PortablePairSelector ignore(String portablePairKey) {
        Args.checkNotBlank(portablePairKey);
        ignores.add(portablePairKey);
        return this;
    }

    /**
     * 設定されている無視定義を消去します。
     */
    public void clear() {
        ignores = new ArrayList<String>(IGNORE_SIZE);
    }

}
