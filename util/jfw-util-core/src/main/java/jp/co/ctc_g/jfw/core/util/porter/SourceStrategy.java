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

/**
 * <p>
 * データ入力元オブジェクトから、移送可能データを抽出するクラスが実装すべきインタフェースです。
 * このクラスは、データ入力元オブジェクトからデータを取得し、
 * {@link PortablePair}オブジェクトを生成し{@link Porter}に返却します。
 * これにより、データ入力元について詳しく知っているのはこのクラスのみでよく、
 * {@link Porter}と共に協調動作する他のオブジェクト群はデータ入力元について無関心であることができます。
 * </p>
 * <p>
 * このクラスのAPIから容易にわかるように、
 * このクラスのインスタンスは複数スレッドから同時アクセスに対して安全ではありません。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see Porter
 * @see PortablePair
 * @see DestinationStrategy
 */
public interface SourceStrategy {

    /**
     * 初期化します。
     * このメソッドが呼ばれるタイミングは、
     * このオブジェクトが所属する{@link Porter}の
     * {@link Porter#initialize() Porter#initialize()}が呼ばれた時です。
     * @param source データ入力元オブジェクト
     * @param destination データ出力先オブジェクト
     * @see Porter
     * @see Porter#initialize()
     */
    void initialize(Object source, Object destination);

    /**
     * 次に取得可能なデータが存在するかどうかを返却します。
     * このメソッドは{@link Porter}の{@link Porter#transport() Porter#transport()メソッド}から呼ばれます。
     * @return 次に取得可能なデータが存在するかどうか。存在する場合はtrue、存在しない場合はfalse
     */
    boolean hasNext();

    /**
     * 次のデータを返却します。
     * データは{@link PortablePair}クラスのインスタンスとして返却されます。
     * このメソッドがnullを返却した場合、
     * {@link Porter}は直ちにこのインスタンスに対して{@link #hasNext()}呼び出しを行ないます。
     * ですので、多くの場合、このメソッドがnullを返却するのであれば、{@link #hasNext()}
     * 呼び出しにおいてあらかじめfalseを返却できるでしょう。
     * このメソッドがnullを返却することによって再び{@link #hasNext()}を呼び出すのは、
     * 非常に特殊な要件に対応するためですので、
     * たいてい、気にする必要はありません。
     * また、{@link #hasNext()}がtrueを返し続け、このメソッドがnullを返し続けた場合、
     * 無限にループする可能性がありますので、
     * 実装者は充分に注意してください。
     * @return 次の移送値が格納された{@link PortablePair}クラスのインスタンス。
     *         nullを返却した場合は直ちに{@link #hasNext()}が呼び出される。
     */
    PortablePair nextPair();
}
