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

import java.text.DecimalFormat;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * このクラスは整数値を扱うプロパティ・エディタです。
 * Beanの整数型のプロパティを編集する機能を提供します。<br/>
 * 整数型とは以下の型を意味します。
 * <ul>
 *     <li>{@link Byte}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link java.math.BigInteger}</li>
 * </ul>
 * {@code Spring MVC} はこのプロパティ・エディタを用いてリクエスト・パラメータのBeanへのバインディング、および、
 * Beanのプロパティ値の文字列への変換を行います。バインディングを行う際は文字列として送られてくるリクエスト・パラメータを
 * 該当のBeanのプロパティの型に型変換する必要があり、型変換可能であるかどうかのチェックは 正規表現 {@code ^[-]?([0]|[1-9][0-9]*)$}
 * に従っているかどうかで判断されます。
 * なお、正規表現に違反するリクエスト・パラメータが送信された場合は{@code J-Framework} によって入力値検証違反と同様に処理されます。
 * </p>
 * <h4>設定方法</h4>
 * <p>
 * {@link J-Framework} ではこのプロパティ・エディタを {@link jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar} によって共通の設定として
 * アプリケーション起動時に初期化します。これは下記のマッピングに従ってリクエスト・パラメータをBeanのプロパティに対してバインドする際のデフォルトの
 * プロパティ・エディタとして設定されることを意味します。
 * <ul>
 *     <li>{@link Byte} 型プロパティ : HalfwidthNumberEditor</li>
 *     <li>{@link Short} 型プロパティ : HalfwidthNumberEditor</li>
 *     <li>{@link Integer} 型プロパティ : HalfwidthNumberEditor</li>
 *     <li>{@link Long} 型プロパティ : HalfwidthNumberEditor</li>
 *     <li>{@link java.math.BigInteger} 型プロパティ : HalfwidthNumberEditor</li>
 *     <li>{@link java.math.BigDecimal} 型プロパティ : HalfwidthDecimalEditor</li>
 * </ul>
 * 上記のマッピングに従い整数型は正規表現{@code ^[-]?([0]|[1-9][0-9]*)$}、小数型は正規表現{@code ^[-]?([0-9]*[.][0-9]+)$}
 * に従った入力値を要求します。この書式を変更するには  {@link jp.co.ctc_g.jse.core.framework.JseDefaultNumberPropertyEditorRegistrar}にて他の書式をサポートする
 * プロパティ・エディタ、あるいは、独自に実装するプロパティ・エディタを該当の型にマッピングします。<br/>
 *
 * また、プロパティによって受付可能な日付書式が異なる場合は、{@code Controller} の {@link org.springframework.web.bind.annotation.InitBinder} で注釈されたメソッドで
 * プロパティごとにプロパティ・エディタを適切に設定する必要があります。<br/>
 * 設定例は以下の通りです。<br/>
 * {@link java.math.BigDecimal}型のプロパティ{@code bar}に対して、 カンマ付き半角小数値 のリクエスト・パラメータのバインドを許容する際の設定です。
 * <pre>
 * &#64;Controller
 * public class FooController {
 *
 *   &#64;InitBinder
 *   public void setupPropertyEditor(WebDataBinder binder) {
 *       HalfwidthDecimalWithCommaEditor editor = new HalfwidthDecimalWithCommaEditor(BigDecimal.cass, true);
 *       binder.registerCustomEditor(BigDecimal.class, "bar", editor);
 *   }
 * </pre>
 * <h4>整数値を扱うその他のプロパティ・エディタ</h4>
 * <p>
 * {@code J-Framework} では、整数値を扱うプロパティ・エディタが用意されています。これらのプロパティ・エディタの違いは、
 * 受け付け可能な整数値のフォーマットです。クライアントサイドで許容する整数値のフォーマットに合わせて適宜プロパティ・エディタを選択して下さい。
 * <ul>
 *     <li>HalfwidthNumberWithCommaEditor</li>
 *     <li>HalfAndFullwidthNumberEditor</li>
 *     <li>HalfAndFullwidthNumberWithCommaEditor</li>
 * </ul>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see AbstractNumberEditor
 * @see HalfwidthNumberWithCommaEditor
 * @see HalfAndFullwidthNumberEditor
 * @see HalfAndFullwidthNumberWithCommaEditor
 */
public class HalfwidthNumberEditor extends AbstractNumberEditor {

    private static final String PATTERN_STR = "^[-]?([0]|[1-9][0-9]*)$";

    private static final String FORMAT = "###";

    /**
     * デフォルトコンストラクタです。
     */
    public HalfwidthNumberEditor() {}

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param propertyType 変換先の型
     * @param allowEmpty trueの場合、空を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public HalfwidthNumberEditor(Class<? extends Number> propertyType,
            boolean allowEmpty) throws IllegalArgumentException {
        this(propertyType, allowEmpty, null);
    }

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param propertyType 変換先の型
     * @param allowEmpty trueの場合、空を許可する
     * @param message メッセージのコード
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public HalfwidthNumberEditor(Class<? extends Number> propertyType,
            boolean allowEmpty,
            String message)
            throws IllegalArgumentException {

        if (propertyType == null) {
            throw new InternalException(HalfwidthNumberEditor.class, "E-NUMBER_EDITOR#0001");
        }

        this.pattern = PATTERN_STR;
        this.format = new DecimalFormat(FORMAT);
        this.propertyType = propertyType;
        this.allowEmpty = allowEmpty;
        this.message = !Strings.isEmpty(message) ? message : Editors.NUMERIC_FORMAT_MESSAGE;
    }
}