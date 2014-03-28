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

package jp.co.ctc_g.jfw.core.util.typeconverter;

import java.math.BigDecimal;
import java.math.BigInteger;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.CoreInternals;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * {@link BigDecimal}に変換するためのコンバータクラスです。
 * </p>
 * <p>
 * 以下の{@link Config クラスコンフィグオーバライド}用のキーが公開されています。
 * </p>
 * <table class="property_file_override_info">
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>型</th>
 *    <th>効果</th>
 *    <th>デフォルト</th>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>convert_empty_string_to_null</td>
 *    <td>java.lang.Boolean</td>
 *    <td>
 *      この値がtrueの場合、空文字列をnullに変換します。
 *      falseの場合、0.0に変換します。
 *    </td>
 *    <td>
 *      false
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class BigDecimalConverter implements TypeConverter<BigDecimal> {

    private static final Boolean CONVERT_EMPTY_STRING_TO_NULL;

    static {
        Config c = CoreInternals.getConfig(BigDecimalConverter.class);
        CONVERT_EMPTY_STRING_TO_NULL = Boolean.valueOf(c.find("convert_empty_string_to_null", "false"));
    }

    /**
     * デフォルトコンストラクタです。
     */
    public BigDecimalConverter() {}

    /**
     * {@link Integer}を変換します。
     * @param i 変換対象
     * @return 変換後
     */
    public BigDecimal convert(Integer i) {
        return new BigDecimal(i);
    }

    /**
     * {@link Long}を変換します。
     * @param l 変換対象
     * @return 変換後
     */
    public BigDecimal convert(Long l) {
        return new BigDecimal(l);
    }

    /**
     * {@link BigInteger}を変換します。
     * @param bi 変換対象
     * @return 変換後
     */
    public BigDecimal convert(BigInteger bi) {
        return new BigDecimal(bi);
    }

    /**
     * {@link Float}を変換します。
     * @param f 変換対象
     * @return 変換後
     */
    public BigDecimal convert(Float f) {
        return new BigDecimal(f);
    }

    /**
     * {@link Double}を変換します。
     * @param d 変換対象
     * @return 変換後
     */
    public BigDecimal convert(Double d) {
        return new BigDecimal(d.toString());
    }

    /**
     * {@link String}を変換します。
     * @param v 変換対象
     * @return 変換後
     */
    public BigDecimal convert(String v) {
        if (Strings.isEmpty(v)) return CONVERT_EMPTY_STRING_TO_NULL ? null : BigDecimal.ZERO;
        return new BigDecimal(v);
    }
}
