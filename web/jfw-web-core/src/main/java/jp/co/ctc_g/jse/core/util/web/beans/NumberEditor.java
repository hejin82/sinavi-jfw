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

package jp.co.ctc_g.jse.core.util.web.beans;

import java.text.NumberFormat;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * {@code J-Framework} ではリクエストパラメータをドメインにバインドしますが、
 * この処理は{@link java.beans.PropertyEditor}を利用し実現されています。<br/>
 * <ul>
 *     <li>{@link HalfwidthDecimalEditor}</li>
 *     <li>{@link HalfwidthNumberWithCommaEditor}</li>
 *     <li>{@link HalfAndFullwidthNumberEditor}</li>
 *     <li>{@link HalfAndFullwidthNumberWithCommaEditor}</li>
 *     <li>{@link HalfwidthDecimalEditor}</li>
 *     <li>{@link HalfwidthDecimalWithCommaEditor}</li>
 *     <li>{@link HalfAndFullwidthDecimalEditor}</li>
 *     <li>{@link HalfAndFullwidthDecimalWithCommaEditor}</li>
 * </ul>
 *   <ul>
 *     <li>{@link Byte}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link java.math.BigInteger}</li>
 *     <li>{@link java.math.BigDecimal}</li>
 *   </ul>
 * は一般的によく利用される形式に対して利用可能な以下のプロパティ・エディタを提供しているため
 * これらを利用して設定を行うのが推奨されるアプローチとなります。
 * ただし、上記のプロパティ・エディタで要件をカバーできない場合が想定されるため、カスタマイズされたプロパティ・エディタを構成する
 * ことを目的にこのプロパティ・エディタを提供しています。
 * なお、このクラスがサポート可能な数値型は以下の通りです。
 *   <ul>
 *     <li>{@link Byte}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link java.math.BigInteger}</li>
 *     <li>{@link Float}</li>
 *     <li>{@link Double}</li>
 *     <li>{@link java.math.BigDecimal}</li>
 *   </ul>
 * </p>
 * <h4>バインディングの概要</h4>
 * <p>
 * カスタム・プロパティ・エディタを構成するにはバインディングの概要を利用する必要があります。
 * バインディング処理は3つのフェーズにより構成されています。<br/>
 *
 * 第１フェーズではString型のリクエスト・パラメータの入力値のフォーマット検証を行います。<br/>
 * 受け付け可能なフォーマットは正規表現で指定可能し引数としてセットします。<br/>
 * 第2フェーズでは文字列を{@link Number}型にパースします。<br/>
 * 文字列から{@link Number}型オブジェクトへのパース処理は{@link NumberFormat#parse(String)}を用いて行います。
 * NumberFormatに設定するフォーマットと第１フェーズの入力値のフォーマット検証で指定するフォーマットは
 * 同期をとる必要があります。つまり、第１フェーズで受け付け可能なフォーマットに従った文字列は第２フェーズで{@link Number}型オブジェクトへ
 * パース可能な文字列でなければならないということです。<br/>
 * 第3フェーズではString型から該当のプロパティの型への型変換を行います。<br/>
 * この型変換処理は{@link jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverter}を利用します。型変換の詳細について確認したい場合は、
 * {@link jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverter}を参照して下さい。<br/>
 * このようなバインディングプロセスを理解し適切にコンストラクタの引数を設定することにより、有用なカスタム・プロパティ・エディタを構成することができます。
 * </p>
 * <h4>設定方法</h4>
 * <h5>アプリケーション全体のデフォルトの振る舞いを設定</h5>
 * <p>
 * アプリケーション全体のデフォルトの挙動として設定する場合は、{@link jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar}を
 * オーバーライドした実装を{@code Spring MVC}のコンテキストXMLに設定します。
 * Beanとして登録を行い {@link org.springframework.web.bind.support.ConfigurableWebBindingInitializer} の {@code propertyEditorRegistrars}プロパティに設定します。<br/>
 * 設定例は下記の通りです。
 * </p>
 * <pre>
 * &lt;bean id="configurableWebBindingInitializer" class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer"&gt;
 *   &lt;property name="validator" ref="validator" /&gt;
 *   &lt;property name="conversionService" ref="conversionService" /&gt;
 *   &lt;property name="bindingErrorProcessor" ref="bindingErrorProcessor" /&gt;
 *   &lt;property name="propertyEditorRegistrars"&gt;
 *     &lt;list&gt;
 *       &lt;ref bean="defaultDatePropertyEditorRegistrar"/&gt;
 *       &lt;ref bean="defaultNumberPropertyEditorRegistrar"/&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * &lt;bean id="defaultNumberPropertyEditorRegistrar" class="jp.co.ctc_g.xxx.DefaultCustomNumberPropertyEditorRegistrar"&gt;
 *   &lt;property name="allowEmpty" value="true" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * <h5>各コントローラ個別の振る舞いに合わせた設定</h5>
 * <p>
 * コントローラ内の{@link org.springframework.web.bind.annotation.InitBinder}で注釈されたメソッドでプロパティ・エディタを設定することにより該当のコントローラが提供する処理に適したバインディングを行うことができます。<br/>
 * 設定は{@code NumberEditor}のインスタンスを生成し、{@link WebDataBinder#registerCustomEditor(Class<?>, String, PropertyEditor)}を利用して{@link WebBinder}にレジストしるこにより行います。<br/>
 * この設定は該当のコントーラに対するリクエスト全てにおいて有効になります。{@link NumberEditor}のインスタンス生成時に第１引数に指定した型のプロパティ 全てに対してプロパティ・エディタが有効になります。<br/>
 * 設定例を以下に示します。<br/>
 * この例の場合は、{@code FooController}に対するリクエストにて{@link java.math.BigDecimal}型プロパティに対するバインディングが必要とされた場合、すべてにおいて設定したプロパティ・エディタが有効になります。
 * </p>
 * <pre>
 * &#64;Controller
 * public class FooController {
 *
 *   &#64;InitBinder
 *   public void initBinderForm(WebDataBinder binder) {
 *       NumberEditor numberEditor = new NumberEditor(BigDecimal.class, true);
 *       binder.registerCustomEditor(BigDecimal.class, numberEditor);
 *   }
 * </pre>
 * <p>
 * また、特定プロパティに対してのみプロパティ・エディタを設定したい場合は、{@link org.springframework.web.bind.WebDataBinder#registerCustomEditor(Class, String, java.beans.PropertyEditor)}を利用してプロパティ・エディタを
 * レジストすることにり第2引数で指定したプロパティに対するバインディング時のみにプロパティ・エディタを有効にすることができます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar
 * @see org.springframework.web.bind.support.ConfigurableWebBindingInitializer
 * @see java.beans.PropertyEditorSupport
 */
public class NumberEditor extends AbstractNumberEditor {

    /**
     * デフォルトコンストラクタです。
     */
    public NumberEditor() {}

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param numberClass 変換先の型
     * @param format {@link NumberFormat}のインスタンス
     * @param pattern 数値フォーマットのパターン
     * @param allowEmpty trueの場合、空を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public NumberEditor(Class<? extends Number> numberClass,
            NumberFormat format,
            String pattern,
            boolean allowEmpty)
            throws IllegalArgumentException {
        this(numberClass, format, pattern, allowEmpty, false, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param numberClass 変換先の型
     * @param format {@link NumberFormat}のインスタンス
     * @param pattern 数値フォーマットのパターン
     * @param allowEmpty trueの場合、空を許可する
     * @param needFullWidthToHalfWidth trueの場合、全角を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public NumberEditor(Class<? extends Number> numberClass,
            NumberFormat format,
            String pattern,
            boolean allowEmpty,
            boolean needFullWidthToHalfWidth)
            throws IllegalArgumentException {
        this(numberClass, format, pattern, allowEmpty, needFullWidthToHalfWidth, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param numberClass 変換先の型
     * @param format {@link NumberFormat}のインスタンス
     * @param pattern 数値フォーマットのパターン
     * @param allowEmpty trueの場合、空を許可する
     * @param message メッセージのコード
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public NumberEditor(Class<? extends Number> numberClass,
            NumberFormat format,
            String pattern,
            boolean allowEmpty,
            String message)
            throws IllegalArgumentException {

        this(numberClass, format, pattern, allowEmpty, false, message);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param numberClass 変換先の型
     * @param format {@link NumberFormat}のインスタンス
     * @param pattern 数値フォーマットのパターン
     * @param allowEmpty trueの場合、空を許可する
     * @param needFullWidthToHalfWidth trueの場合、全角を許可する
     * @param message メッセージのコード
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public NumberEditor(Class<? extends Number> numberClass,
            NumberFormat format,
            String pattern,
            boolean allowEmpty,
            boolean needFullWidthToHalfWidth,
            String message)
            throws IllegalArgumentException {

        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
            throw new InternalException(NumberEditor.class, "E-PROPERTY_EDITOR#0001");
        }

        if (format == null) {
            throw new InternalException(NumberEditor.class, "E-PROPERTY_EDITOR#0002");
        }

        if (Strings.isEmpty(pattern)) {
            throw new InternalException(NumberEditor.class, "E-PROPERTY_EDITOR#0003");
        }

        this.propertyType = numberClass;
        this.format = format;
        this.pattern = pattern;
        this.allowEmpty = allowEmpty;
        this.needFullWidthToHalfWidth = needFullWidthToHalfWidth;
        this.message = !Strings.isEmpty(message) ? message : Editors.NUMERIC_FORMAT_MESSAGE;
    }

    @Override
    protected String formatIfNeeded(Object value) {
        return format.format(value);
    }
}