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

import java.util.Map;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、データ出力先オブジェクトを{@link Map}とした場合の{@link DestinationStrategy}です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see DestinationStrategy
 * @see BeanToMapPorter
 */
public class MapDestinationStrategy implements DestinationStrategy {

    private Map<Object, Object> destination;

    /**
     * このクラスのインスタンスを生成します。
     */
    public MapDestinationStrategy() {}

    /**
     * 指定されたデータ出力先オブジェクトに対して、このインスタンスを初期化します。
     * @param src データ入力元オブジェクト
     * @param dest データ出力先オブジェクト
     * @see DestinationStrategy#initialize(Object, Object)
     */
    @SuppressWarnings("unchecked")
    public void initialize(Object src, Object dest) {
        Args.checkAssignable(dest, Map.class);
        this.destination = (Map<Object, Object>) dest;
    }

    /**
     * ポータブルペアをデータ出力先オブジェクトに保存します。
     * @param assignablePair 現在処理中のペア
     * @see DestinationStrategy#assign(PortablePair)
     */
    public void assign(PortablePair assignablePair) {
        this.destination.put(assignablePair.getKey(), assignablePair.getValue());
    }

    /**
     * データ出力先オブジェクトを返却します。
     * @return このインスタンス
     * @see DestinationStrategy#complete()
     */
    public Object complete() {
        return destination;
    }

}
