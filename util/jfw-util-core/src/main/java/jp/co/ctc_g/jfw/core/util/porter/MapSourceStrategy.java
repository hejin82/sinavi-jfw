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

import java.util.Iterator;
import java.util.Map;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、データ入力元オブジェクトを{@link Map}とした場合の{@link SourceStrategy}です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see SourceStrategy
 * @see MapToBeanPorter
 */
public class MapSourceStrategy implements SourceStrategy {

    private Map<?, ?> source;
    private Iterator<?> keyIterator;

    /**
     * デフォルトコンストラクタです。
     */
    public MapSourceStrategy() {}

    /**
     * 指定されたデータ入力元に対して、このインスタンスを初期化します。
     * @param src データ入力元オブジェクト
     * @param dest データ出力先オブジェクト
     * @see SourceStrategy#initialize(Object, Object)
     */
    public void initialize(Object src, Object dest) {
        Args.checkTrue(src instanceof Map);
        this.source = (Map<?, ?>) src;
        this.keyIterator = this.source.keySet().iterator();
    }

    /**
     * 次のペアがあるかどうかを返却します。
     * @return 次のペアが存在する場合にtrue
     * @see SourceStrategy#hasNext()
     */
    public boolean hasNext() {
        return keyIterator.hasNext();
    }

    /**
     * 次のペアを取得します。
     * @return {@link PortablePair}
     * @see SourceStrategy#nextPair()
     */
    public PortablePair nextPair() {
        Object key = keyIterator.next();
        PortablePair pair = new PortablePair(key, source.get(key));
        return pair;
    }

}
