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

import java.util.HashMap;
import java.util.Map;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、あるポータブルペアのキー名を別の名前に変換するための{@link ManipulationFilter}です。
 * 例えば、{@link BeanPorter}では、
 * Javaビーン名がソースオブジェクトとデスティネーションオブジェクトで異なる場合の解決方法として、
 * このクラスを利用しています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see ManipulationFilter
 */
public class KeyExchangeFilter implements ManipulationFilter {

    private Map<String, String> rules;

    /**
     * このクラスのインスタンスを生成します。
     */
    public KeyExchangeFilter() {
        rules = new HashMap<String, String>();
    }

    /**
     * このクラスでは固有の振る舞いが必要ないため、空実装しています。
     * @param source データ入力元オブジェクト
     * @param destination データ出力先オブジェクト
     * @see ManipulationFilter#initialize(Object, Object)
     */
    public void initialize(Object source, Object destination) {
    }

    /**
     * 設定されている変換ルールに基づいて、ポータブルペアのキー名を変換します。
     * ただし、ポータブルペアは不変オブジェクトであり、
     * <strong>キー名の変更はインスタンスの再生成</strong>を意味します。
     * @param sourcePair 現在処理中のペア
     * @return {@link PortablePair}
     * @see PortablePair
     * @see ManipulationFilter#manipulate(PortablePair)
     */
    public PortablePair manipulate(PortablePair sourcePair) {
        String name = rules.get(sourcePair.getKey().toString());
        return name != null ? new PortablePair(name, sourcePair.getValue()) : sourcePair;
    }

    /**
     * 新しい変換ルールを追加します。
     * 既に同様のキー名に対するルールが設定されている場合、
     * 新しいルールで上書きされます。
     * @param sourceKeyName 変換対象キー名
     * @param destinationKeyName 変換後キー名
     * @return このインスタンス
     */
    public KeyExchangeFilter exchange(String sourceKeyName, String destinationKeyName) {
        Args.checkNotEmpty(sourceKeyName);
        Args.checkNotEmpty(destinationKeyName);
        rules.put(sourceKeyName, destinationKeyName);
        return this;
    }

    /**
     * 新しい変換ルールをまとめて追加します。
     * 引数のマップには、
     * キーに変換元のポータブルペアのキー名、
     * 値に変換後のポータブルペアのキー名が設定されている必要があります。
     * 引数に<code>null</code>は認められませんが、
     * サイズのチェックは行ないません。
     * @param exchangeRules 変換ルール
     * @return このインスタンス
     */
    public KeyExchangeFilter exchangeAll(Map<String, String> exchangeRules) {
        Args.checkNotNull(exchangeRules);
        this.rules.putAll(exchangeRules);
        return this;
    }

    /**
     * 設定されている変換ルールをクリアします。
     * @return このインスタンス
     */
    public KeyExchangeFilter clear() {
        rules.clear();
        return this;
    }

}
