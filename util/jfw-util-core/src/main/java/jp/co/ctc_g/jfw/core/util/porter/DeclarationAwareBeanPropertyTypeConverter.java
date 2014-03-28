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
import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverter;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverters;

/**
 * <p>
 * このクラスは、Javaビーンプロパティの宣言型に基づき型変換を実行する{@link ManipulationFilter}です。
 * このフィルタを利用する場合、<strong>出力先は必ずJavaビーンでなければなりません</strong>。
 * このクラスを利用すると、データ出力先のJavaビーンの型とポータブルペアの型が一致しない可能性がある場合、
 * 自動的にJavaビーンの宣言型に変換させることができます。
 * </p>
 * <p>
 * このクラスが実行する型変換ロジックは、{@link TypeConverters}に依存しています。
 * よって、{@link TypeConverters}で変更したデフォルトの変換ロジックは、
 * そのままこのクラスにも適用され有効に機能します。
 * 一方、このインスタンスに個別に設定された変換ロジックは、
 * このインスタンスにおいてのみ有効であり、{@link TypeConverters}に影響を与えることはありません。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see TypeConverters
 * @see TypeConverter
 * @see ManipulationFilter
 */
public class DeclarationAwareBeanPropertyTypeConverter implements ManipulationFilter {

    private Map<String, TypeConverter<?>> operations;
    private Class<?> destinationType;

    /**
     * このクラスのインスタンスを生成します。
     */
    public DeclarationAwareBeanPropertyTypeConverter() {
        operations = new HashMap<String, TypeConverter<?>>();
    }

    /**
     * 出力先オブジェクトをJavaビーンとみなして、このインスタンスを初期化します。
     * @param source データ入力元オブジェクト
     * @param destination データ出力先オブジェクト
     * @see ManipulationFilter#initialize(Object, Object)
     */
    public void initialize(Object source, Object destination) {
        this.destinationType = destination.getClass();
    }

    /**
     * 現在処理中のペアに対して、出力先ビーンプロパティの宣言型に基づいて型変換を実行します。
     * この変換は、{@link TypeConverters}を利用しています。
     * @param pair 現在処理中のペア
     * @return {@link PortablePair}
     * @see TypeConverter
     * @see TypeConverters
     */
    public PortablePair manipulate(PortablePair pair) {
        Object value = pair.getValue();
        if (value == null) return pair;
        Class<?> to = Beans.detectDeclaredPropertyType(pair.getKey().toString(), destinationType);
        if (to == null || to.isAssignableFrom(value.getClass())) return pair;
        Object converted = null;
        if (operations.containsKey(to.getName())) {
            TypeConverter<?> op = operations.get(to.getName());
            converted = TypeConverters.convert(op, value, to);
        } else {
            converted = TypeConverters.convert(value, to);
        }
        return new PortablePair(pair.getKey(), converted);
    }

    /**
     * 変換ロジックを追加します。
     * これにより、現在の{@link TypeConverters}の変換ロジックとは異なった変換ロジックを、
     * このインスタンスにだけ有効にします。
     * @param <T> 変換対象となる型
     * @param convertTo 変換対象完全修飾クラス名
     * @param op 変換ロジッククラス
     * @return このインスタンス
     * @see TypeConverter
     * @see TypeConverters
     */
    public <T> DeclarationAwareBeanPropertyTypeConverter registerOperation(
            String convertTo, TypeConverter<T> op) {
        Args.checkNotNull(convertTo);
        Args.checkNotNull(op);
        operations.put(convertTo, op);
        return this;
    }

    /**
     * 変換ロジックを追加します。
     * これにより、現在の{@link TypeConverters}の変換ロジックとは異なった変換ロジックを、
     * このインスタンスにだけ有効にします。
     * @param ops 変換対象完全修飾クラス名がキー、変換ロジッククラスが値のマップ
     * @return このインスタンス
     * @see TypeConverter
     * @see TypeConverters
     */
    public DeclarationAwareBeanPropertyTypeConverter registerOperations(
            Map<String, TypeConverter<?>> ops) {
        Args.checkNotNull(ops);
        for (String to : ops.keySet()) {
            this.operations.put(to, ops.get(to));
        }
        return this;
    }

    /**
     * 現在設定されている変換ロジックをクリアします。
     */
    public void clear() {
        operations.clear();
    }
}
