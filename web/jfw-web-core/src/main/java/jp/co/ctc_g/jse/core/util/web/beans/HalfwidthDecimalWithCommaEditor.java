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
 * このクラスは小数値を扱うプロパティ・エディタです。
 * Beanの小数型のプロパティを編集する機能を提供します。<br/>
 * 機能の大部分は {@link HalfwidthDecimalEditor} と同様です。違いは受け付け可能な小数値のフォーマットです。
 * このクラスは、正規表現 {@code ^[-]?([0-9]{0,3}([,][0-9]{3})*[.][0-9]+)$} に従った半角カンマ区切りの小数値を扱うことができます。
 * </p>
 * <h4>小数のスケールについて</h4>
 * <p>
 * Beanのプロパティにバインド後、文字列に変換する際の小数のスケールには注意が必要です。<br/>
 * 入力検証失敗後の入力値の表示、または、入力画面後に表示する確認画面での入力値の表示においては入力時の小数値のスケールが維持されない
 * 場合があります。（なお、データストアから取得した値をBeanに保持しJSPで表示するような場合も同様です。）<br/>
 * 変換例は下記の通りです。小数が{@code 0}のみで構成される場合はかならずスケールは1となります。
 * このクラスはBeanの小数値型のプロパティに対して一括で設定されることを前提としているため、プロパティによって変わる小数値のスケールを管理できない
 * ことに起因しています。
 * <ul>
 *     <li>{@code 1,000.00} は {@code 1,000.0} として表示</li>
 *     <li>{@code 1,000.000} は {@code 1,000.0} として表示</li>
 *     <li>{@code 1,000.000000} は {@code 1,000.0} として表示</li>
 * </ul>
 * なお、上記の振る舞いが許容できない場合は、独自にプロパティ・エディタを実装する必要があります。
 * </p>
 * <h4>小数値を扱うその他のプロパティ・エディタ</h4>
 * <p>
 * {@code J-Framework} では、小数値を扱うプロパティ・エディタが用意されています。これらのプロパティ・エディタの違いは、
 * 受け付け可能な小数値のフォーマットです。クライアントサイドで許容する小数値のフォーマットに合わせて適宜プロパティ・エディタを選択して下さい。
 * <ul>
 *     <li>HalfwidthDecimalEditor</li>
 *     <li>HalfAndFullwidthDecimalEditor</li>
 *     <li>HalfAndFullwidthDecimalWithCommaEditor</li>
 * </ul>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see AbstractNumberEditor
 * @see HalfwidthDecimalEditor
 * @see HalfAndFullwidthDecimalEditor
 * @see HalfAndFullwidthDecimalWithCommaEditor
 */
public class HalfwidthDecimalWithCommaEditor extends AbstractNumberEditor {

    private static final String PATTERN_STR = "^[-]?([0-9]{0,3}([,][0-9]{3})*[.][0-9]+)$";

    private static final String FORMAT = "###,##0.0######################################################################################################################################################################################################################################################################################################################################";


    /**
     * デフォルトコンストラクタです。
     */
    public HalfwidthDecimalWithCommaEditor() {}

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param propertyType 変換先の型
     * @param allowEmpty trueの場合、空を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public HalfwidthDecimalWithCommaEditor(Class<? extends Number> propertyType,
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
    public HalfwidthDecimalWithCommaEditor(Class<? extends Number> propertyType,
            boolean allowEmpty,
            String message)
            throws IllegalArgumentException {

        if (propertyType == null) {
            throw new InternalException(HalfwidthDecimalWithCommaEditor.class, "E-NUMBER_EDITOR#0002");
        }

        this.pattern = PATTERN_STR;
        this.format = new DecimalFormat(FORMAT);
        this.propertyType = propertyType;
        this.allowEmpty = allowEmpty;
        this.message = !Strings.isEmpty(message) ? message : Editors.NUMERIC_FORMAT_MESSAGE;
    }

    @Override
    protected String formatIfNeeded(Object value) {
        return format.format(value);
    }
}