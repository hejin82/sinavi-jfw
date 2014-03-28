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

import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Args;
import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverter;

/**
 * <p>
 * このクラスは、BeanからBeanへと値を移し替えます。
 * 退屈なgetとsetの交互呼び出しの苦痛から開発者を開放することがその主な役割です。
 * 移送元Beanと移送先Beanのプロパティ名が異なる場合でも、簡単な設定でその違いを吸収できます。
 * さらに、プロパティの型が移送元と移送先で異なる場合には、
 * 移送先Beanのプロパティ型に移送元Beanのプロパティ値を自動的にコンバートします。
 * </p>
 * <p>
 * BeanPorterは以下のストラテジとフィルタを組み合わせた{@link Porter}の拡張です。
 * </p>
 * <ul>
 * <li>{@link SourceStrategy}に{@link BeanSourceStrategy}を採用</li>
 * <li>{@link DestinationStrategy}に{@link BeanDestinationStrategy}を採用</li>
 * <li>{@link ManipulationFilter}に{@link KeyExchangeFilter}を採用</li>
 * <li>{@link ManipulationFilter}に{@link DeclarationAwareBeanPropertyTypeConverter}を採用</li>
 * </ul>
 * <p>
 * 最も簡単な利用方法は以下のようにします。以下の例では、AbcクラスとDefクラスの2つの間で値移送を行なっています。
 * </p>
 * <pre>
 * public class Abc {
 *     private String stringField;
 *     public String getStringField() {return stringField;}
 *     public void setStringField(String sf) {stringField = sf;}
 * }
 *
 * public class Def {
 *     private String stringField;
 *     public String getStringField() {return stringField;}
 *     public void setStringField(String sf) {stringField = sf;}
 * }
 * </pre>
 * <p>
 * もちろん、上記の通り、このクラスに設定する入力(Abcクラス)及び出力(Defクラス)は
 * 双方共にJavaBeans仕様に沿ったアクセサメソッドが定義されているクラスでなければなりません。
 * </p>
 * <pre>
 * Abc source = new Abc();
 * source.setStringField("Bean Potter and Deathly Hallow");
 * Def dest = new BeanPorter(source).create(Def.class);
 * assert source.getStringField().equals(dest.getStringField());
 * </pre>
 * <p>
 * この例では、プロパティ名が同じ場合を取り上げました。
 * しかしながら、そうそう都合のよいことばかりではありません。プロパティ名が異なる場合を見てみましょう。
 * </p>
 * <pre>
 * public class Ghi {
 *     private String stringField;
 *     public String getStringField() {return stringField;}
 *     public void setStringField(String sf) {stringField = sf;}
 * }
 *
 * public class Jkl {
 *     private String stringProperty;
 *     public String getStringProperty() {return stringProperty;}
 *     public void setStringProperty(String sp) {stringProperty = sp;}
 * }
 * </pre>
 * <p>
 * GhiクラスはstringFieldプロパティを持ちますが、JklクラスはstringFieldプロパティを持たず、
 * その代りに、stringPropertyプロパティを持っています。
 * このように名前の異なる2つのプロパティ間で値の移し替えを行いたい場合、
 * このクラスの{@link #exchange(String, String)}メソッドを利用します。
 * </p>
 * <pre>
 * Ghi source = new Ghi();
 * source.setStringField("Bean Potter and Philosopher's Stone");
 * Jkl dest = new BeanPorter(source)
 *         .exchange("stringField", "stringProperty")
 *         .create(Jkl.class);
 * assert source.getStringField().equals(dest.getStringProperty());
 * </pre>
 * <p>
 * 移送元と移送先のプロパティの型の違いは移送先の型に合わせて移送元のプロパティ値が自動的にコンバートされますが、
 * より詳細にこの挙動を制御する必要があるかもしれません。
 * そのような場合には、{@link #convert(Class, TypeConverter)}を利用してください。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see Porter
 * @see SourceStrategy
 * @see BeanSourceStrategy
 * @see DestinationStrategy
 * @see BeanDestinationStrategy
 * @see ManipulationFilter
 * @see KeyExchangeFilter
 * @see DeclarationAwareBeanPropertyTypeConverter
 */
public class BeanPorter extends Porter {

    private static final ResourceBundle R = InternalMessages.getBundle(BeanPorter.class);

    private KeyExchangeFilter exchanger;
    private PortablePairSelector selector;
    private DeclarationAwareBeanPropertyTypeConverter typeConverter;

    /**
     * デフォルトコンストラクタです。
     */
    public BeanPorter() {}

    /**
     * 指定されたオブジェクトを移送元ソースとして、インスタンスを生成します。
     * @param source 値移送元オブジェクト
     */
    public BeanPorter(Object source) {
        super();
        prepare();
        setSource(source);
    }

    /**
     * このインスタンスを利用する準備をします。
     * このメソッドはコンストラクタから呼び出されます。
     */
    protected void prepare() {
        exchanger = new KeyExchangeFilter();
        typeConverter = new DeclarationAwareBeanPropertyTypeConverter();
        selector = new PortablePairSelector();
        setSourceStrategy(new BeanSourceStrategy());
        setDestinationStrategy(new BeanDestinationStrategy());
        addManipulationFilter(selector);
        addManipulationFilter(exchanger);
        addManipulationFilter(typeConverter);
    }

    /**
     * このBeanPorterに移送先クラスを設定し、プロパティ値をソースから移送します。
     * 設定されるクラスはJavaBeans仕様に則ってプロパティを公開している必要があり、
     * かつ、JavaBeans仕様に則ったコンストラクタ（つまりデフォルトコンストラクタ）が定義されている必要があります。
     * @param <T> 移送先クラスの型
     * @param clazz 移送先クラス
     * @return 移送元の公開プロパティの値が移し替えられた移送先オブジェクト
     */
    public <T> T create(Class<T> clazz) {
        Args.checkNotNull(clazz);
        T t = Beans.make(clazz);
        return copyTo(t);
    }

    /**
     * 指定されたデータ出力先に対して値を移送します。
     * {@link #create(Class)}とは異なり、引数にインスタンスを受け取ります。
     * @param <T> 移送先クラスの型
     * @param destination 移送先Javaビーンのインスタンス
     * @return 移送元の公開プロパティの値が移し替えられた移送先オブジェクト
     */
    @SuppressWarnings("unchecked")
    public <T> T copyTo(T destination) {
        Args.checkNotNull(destination);
        setDestination(destination);
        return (T) process();
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
    public BeanPorter exchange(String src, String dest) {
        Args.checkNotEmpty(src);
        Args.checkNotEmpty(dest);
        exchanger.exchange(src, dest);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     */
    public BeanPorter clearExchange() {
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
    public <T> BeanPorter convert(Class<T> conversionType, TypeConverter<T> op) {
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
    public BeanPorter clearConverter() {
        typeConverter.clear();
        return this;
    }

    /**
     * 指定されたカラム名を無視して、移送対象から外します。
     * @param columnName 無視するカラム名
     * @return このインスタンス
     */
    public BeanPorter ignore(String columnName) {
        selector.ignore(columnName);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     */
    public BeanPorter clearIgnorance() {
        selector.clear();
        return this;
    }
}
