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

import java.math.BigDecimal;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * このクラスは小数値を扱うプロパティ・エディタです。
 * ビーンの小数型のプロパティを編集する機能を提供します。<br/>
 * 小数型とは以下の型を意味します。{@link Float}、{@link Double} についてはサポートしていません。ご注意ください。<br/>
 * <ul>
 *     <li>{@link BigDecimal}</li>
 * </ul>
 *
 * {@code Spring MVC} はこのプロパティ・エディタを用いてリクエスト・パラメータのBeanへのバインディング、および、
 * Beanのプロパティ値の文字列への変換を行います。バインディングを行う際は文字列として送られてくるリクエスト・パラメータを
 * 該当のBeanのプロパティの型に型変換する必要があり、型変換可能であるかどうかのチェックは 正規表現 {@code ^[-]?([0-9]*[.][0-9]+)$}
 * に従っているかどうかで判断されます。
 * なお、正規表現に違反するリクエスト・パラメータが送信された場合は{@code J-Framework} によって入力値検証違反と同様に処理されます。
 * </p>
 * <h4>設定方法</h4>
 * <p>
 * 設定方法についてはパッケージの説明を参照して下さい。
 * </p>
 * <h4>小数値を扱うその他のプロパティ・エディタ</h4>
 * <p>
 * {@code J-Framework} では、小数値を扱うプロパティ・エディタが用意されています。これらのプロパティ・エディタの違いは、
 * 受け付け可能な小数値のフォーマットです。クライアントサイドで許容する小数値のフォーマットに合わせて適宜プロパティ・エディタを選択して下さい。
 * <ul>
 *     <li>HalfwidthDecimalWithCommaEditor</li>
 *     <li>HalfAndFullwidthDecimalEditor</li>
 *     <li>HalfAndFullwidthDecimalWithCommaEditor</li>
 * </ul>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see AbstractNumberEditor
 * @see HalfwidthDecimalWithCommaEditor
 * @see HalfAndFullwidthDecimalEditor
 * @see HalfAndFullwidthDecimalWithCommaEditor
 */
public class HalfwidthDecimalEditor extends AbstractNumberEditor {

    private static final String PATTERN_STR = "^[-]?([0-9]*[.][0-9]+)$";

    //private static final String FORMAT = "##0.0######################################################################################################################################################################################################################################################################################################################################";

    /**
     * デフォルトコンストラクタです。
     */
    public HalfwidthDecimalEditor() {}

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param propertyType 変換先の型
     * @param allowEmpty trueの場合、空を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public HalfwidthDecimalEditor(Class<? extends Number> propertyType,
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
    public HalfwidthDecimalEditor(Class<? extends Number> propertyType,
            boolean allowEmpty,
            String message)
            throws IllegalArgumentException {

        if (propertyType == null) {
            throw new InternalException(HalfwidthDecimalEditor.class, "E-NUMBER_EDITOR#0003");
        }

        this.pattern = PATTERN_STR;
        //this.format = new DecimalFormat(FORMAT);
        this.propertyType = propertyType;
        this.allowEmpty = allowEmpty;
        this.message = !Strings.isEmpty(message) ? message : Editors.NUMERIC_FORMAT_MESSAGE;
    }

    @Override
    protected String formatIfNeeded(Object value) {
        //return format.format(value);
        return ((BigDecimal)value).toPlainString();
    }
}