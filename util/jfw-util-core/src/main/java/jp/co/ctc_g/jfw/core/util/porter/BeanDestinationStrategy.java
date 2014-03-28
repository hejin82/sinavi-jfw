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

import jp.co.ctc_g.jfw.core.util.Beans;

/**
 * <p>
 * このクラスは、Javaビーンに対して{@link PortablePair}を出力する{@link DestinationStrategy}です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see SourceStrategy
 * @see DestinationStrategy
 * @see Porter
 * @see PortablePair
 */
public class BeanDestinationStrategy implements DestinationStrategy {

    private Object destination;

    /**
     * このクラスのインスタンスを生成します。
     */
    public BeanDestinationStrategy() {
    }

    /**
     * 指定されたdestinationオブジェクトに対してデータ出力するように、このインスタンスを初期化します。
     * @param src データ入力元オブジェクト
     * @param dest データ出力先オブジェクト
     * @see DestinationStrategy#initialize(Object, Object)
     */
    public void initialize(Object src, Object dest) {
        this.destination = dest;
    }

    /**
     * {@link #initialize(Object, Object)}で受け取ったdestinationオブジェクトに対して、データを出力します。
     * @param assignablePair データ出力先に保存する必要のあるペア
     * @see DestinationStrategy#assign(PortablePair)
     */
    public void assign(PortablePair assignablePair) {
        if ("class".equals(assignablePair.getKey())) return;
        Beans.writePropertyValueNamed(
                assignablePair.getKey().toString(),
                destination,
                assignablePair.getValue());
    }

    /**
     * データ出力先オブジェクトを返却します。
     * これは、{@link #initialize(Object, Object)}で受け取ったdestinationオブジェクトそのものです。
     * @return データ出力先オブジェクト
     * @see DestinationStrategy#complete()
     */
    public Object complete() {
        return destination;
    }

}
