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
 * データ出力先オブジェクトへ、移送可能データを出力するクラスが実装すべきインタフェースです。
 * このクラスは、{@link PortablePair}オブジェクトを受け取り、
 * データ出力先オブジェクトへデータを保存します。
 * これにより、データ出力先について詳しく知っているのはこのクラスのみでよく、
 * {@link Porter}と共に協調動作する他のオブジェクト群はデータ出力先について無関心であることができます。
 * </p>
 * <p>
 * このクラスのインスタンスは複数スレッドから同時アクセスに対して安全ではありません。
 * </p>
 * @see Porter
 * @see PortablePair
 * @see SourceStrategy
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface DestinationStrategy {

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
     * 指定されたペアをデータ出力先に保存します。
     * このメソッドは、{@link Porter#transport()}にてフィルタを通過したペアに対して呼び出されます。
     * @param assignablePair データ出力先に保存する必要のあるペア
     */
    void assign(PortablePair assignablePair);

    /**
     * データ出力先オブジェクトを返却します。
     * 転送処理が完了した際、つまり{@link Porter#transport()}が完了した際に、{@link Porter}によりコールされます。
     * @return データ出力先オブジェクト
     */
    Object complete();
}
