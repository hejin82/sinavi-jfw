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
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Args;
import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverter;

/**
 * <p>
 * このクラスは、{@link Map}をJavaビーンのプロパティに移送します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see MapSourceStrategy
 * @see BeanDestinationStrategy
 * @see KeyExchangeFilter
 * @see PortablePairSelector
 * @see DeclarationAwareBeanPropertyTypeConverter
 * @see Porter
 */
public class MapToBeanPorter extends Porter {

    private static final ResourceBundle R = InternalMessages.getBundle(MapToBeanPorter.class);

    private KeyExchangeFilter exchanger;
    private PortablePairSelector selector;
    private DeclarationAwareBeanPropertyTypeConverter typeConverter;

    /**
     * デフォルトコンストラクタです。
     */
    public MapToBeanPorter() {}

    /**
     * 指定されたマップをデータ入力元として、このインスタンスを生成します。
     * @param source データ入力元
     */
    public MapToBeanPorter(Map<String, ?> source) {
        super();
        Args.checkNotNull(source);
        prepare();
        setSource(source);
    }

    /**
     * このインスタンスを初期化します。
     * このメソッドはコンストラクタから呼び出されます。
     */
    protected void prepare() {
        exchanger = new KeyExchangeFilter();
        selector = new PortablePairSelector();
        typeConverter = new DeclarationAwareBeanPropertyTypeConverter();
        setSourceStrategy(new MapSourceStrategy());
        setDestinationStrategy(new BeanDestinationStrategy());
        addManipulationFilter(selector);
        addManipulationFilter(exchanger);
        addManipulationFilter(typeConverter);
    }

    /**
     * 指定されたデータ出力先クラスのインスタンスを生成し、
     * データ入力元から取得したデータを移送します。
     * @param <T> データ出力先の型
     * @param beanClass データ出力先クラス
     * @return データが移送された出力先クラスのインスタンス
     */
    public <T> T create(Class<T> beanClass) {
        Args.checkNotNull(beanClass);
        setDestination(Beans.make(beanClass));
        return beanClass.cast(process());
    }

    /**
     * プロパティ名の変換定義を追加します。
     * 移送元の公開プロパティ名と移送先の公開プロパティ名が一致しないけれど値を移し替えたい場合に利用します。
     * 利用例についてはこのクラスのクラス解説を参照してください。
     * このメソッドは、{@link KeyExchangeFilter#exchange(String, String)}への単純な委譲です。
     * 当該クラスのJavadocも役に立つことと思います。
     * @param src 移送元クラスにて公開されているプロパティ名
     * @param dest 移送先クラスにて公開されているプロパティ名
     * @return 指定のプロパティ名変換定義が追加されたBeanPorterのインスタンス
     * @see KeyExchangeFilter
     * @see KeyExchangeFilter#exchange(String, String)
     */
    public MapToBeanPorter exchange(String src, String dest) {
        Args.checkNotEmpty(src);
        Args.checkNotEmpty(dest);
        exchanger.exchange(src, dest);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     */
    public MapToBeanPorter clearExchange() {
        exchanger.clear();
        return this;
    }

    /**
     * 指定された型への変換オペレーションを追加します。 BeanPorterは
     * {@link DeclarationAwareBeanPropertyTypeConverter}を利用して、
     * 自動的に型のコンバートを行ないますが、この変換処理はデフォルトで定義されているコンバータに従います。
     * そのような挙動では問題がある場合に、このメソッドを利用して、新規にコンバータを追加してください。
     * デフォルトで定義されているコンバータの種類については、
     * {@link DeclarationAwareBeanPropertyTypeConverter}を参照してください。
     * 新しいコンバータの作成方法及び登録方法については、特に、
     * {@link DeclarationAwareBeanPropertyTypeConverter#registerOperation(String, TypeConverter)}
     * を参照してください。
     * @param <T> コンバート対象となる型
     * @param conversionType コンバート対象となる型を表現するクラスオブジェクト
     * @param op コンバートロジックが記述されたオブジェクト
     * @return 指定されたコンバータが追加されたBeanPorterインスタンス
     * @see DeclarationAwareBeanPropertyTypeConverter
     * @see DeclarationAwareBeanPropertyTypeConverter#registerOperation
     */
    public <T> MapToBeanPorter convert(Class<T> conversionType, TypeConverter<T> op) {
        assert typeConverter != null: R.getString("A-UTIL_PORTER#0001");
        Args.checkNotNull(conversionType);
        Args.checkNotNull(op);
        typeConverter.registerOperation(conversionType.getName(), op);
        return this;
    }

    /**
     * 設定されているコンバータを消去します。
     * これにより、デフォルトのコンバータ定義が利用されます。
     * @return このインスタンス
     * @see TypeConverter
     * @see DeclarationAwareBeanPropertyTypeConverter
     */
    public MapToBeanPorter clearConverter() {
        typeConverter.clear();
        return this;
    }

    /**
     * 指定されたカラム名を無視して、移送対象から外します。
     * @param columnName 無視するカラム名
     * @return このインスタンス
     */
    public MapToBeanPorter ignore(String columnName) {
        selector.ignore(columnName);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     */
    public MapToBeanPorter clearIgnorance() {
        selector.clear();
        return this;
    }
}
