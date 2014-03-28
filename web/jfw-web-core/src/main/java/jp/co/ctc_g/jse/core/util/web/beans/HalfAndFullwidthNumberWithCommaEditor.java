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
 * ビーンの整数型のプロパティを編集する機能を提供します。<br/>
 * 機能の大部分は {@link HalfwidthNumberEditor} と同様です。違いは受け付け可能な整数値のフォーマットです。
 * このクラスは、正規表現 {@code ^[-－]?([0０]|([1-9１-９][0-9０-９]{0,2}([,，][0-9０-９]{3})*))$} に従いカンマ付きの
 * 半角・全角のリクエスト・パラメータを受け付けることが可能ですが、Beanにバインディングされた後、例えば入力検証失敗後の入力値の表示、
 * または、入力画面後に表示する確認画面での入力値の表示はカンマ付きの半角整数値となります。<br/>
 * 全角で入力された場合に全角に復元されることを期待する場合は、Bean のプロパティを文字列型として定義するか要件を満たすプロパティ・エディタを
 * 独自に実装する必要があります。
 * </p>
 * <h4>整数値を扱うその他のプロパティ・エディタ</h4>
 * <p>
 * {@code J-Framework} では、整数値を扱うプロパティ・エディタが用意されています。これらのプロパティ・エディタの違いは、
 * 受け付け可能な整数値のフォーマットです。クライアントサイドで許容する整数値のフォーマットに合わせて適宜プロパティ・エディタを選択して下さい。
 * <ul>
 *     <li>HalfwidthNumberEditor</li>
 *     <li>HalfwidthNumberWithCommaEditor</li>
 *     <li>HalfAndFullwidthNumberEditor</li>
 * </ul>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see AbstractNumberEditor
 * @see HalfwidthNumberEditor
 * @see HalfwidthNumberWithCommaEditor
 * @see HalfAndFullwidthNumberEditor
 */
public class HalfAndFullwidthNumberWithCommaEditor extends AbstractNumberEditor {

    private static final String PATTERN_STR = "^[-－]?([0０]|([1-9１-９][0-9０-９]{0,2}([,，][0-9０-９]{3})*))$";

    private static final String FORMAT = "###,###";

    /**
     * デフォルトコンストラクタです。
     */
    public HalfAndFullwidthNumberWithCommaEditor() {}

    /**
     * <p>
     * コンストラクタです。
     * </p>
     * @param propertyType 変換先の型
     * @param allowEmpty trueの場合、空を許可する
     * @throws IllegalArgumentException 不正な引数が指定された場合
     */
    public HalfAndFullwidthNumberWithCommaEditor(Class<? extends Number> propertyType,
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
    public HalfAndFullwidthNumberWithCommaEditor(Class<? extends Number> propertyType,
            boolean allowEmpty,
            String message)
            throws IllegalArgumentException {

        if (propertyType == null) {
            throw new InternalException(HalfAndFullwidthNumberWithCommaEditor.class, "E-NUMBER_EDITOR#0001");
        }

        this.pattern = PATTERN_STR;
        this.format = new DecimalFormat(FORMAT);
        this.propertyType = propertyType;
        this.allowEmpty = allowEmpty;
        this.message = !Strings.isEmpty(message) ? message : Editors.NUMERIC_FORMAT_MESSAGE;
        this.needFullWidthToHalfWidth = true;
    }


    @Override
    protected String formatIfNeeded(Object value) {
        return format.format(value);
    }
}