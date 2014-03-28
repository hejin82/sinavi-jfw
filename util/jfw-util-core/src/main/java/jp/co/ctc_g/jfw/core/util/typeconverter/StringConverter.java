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
import java.sql.Timestamp;
import java.util.Date;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.CoreInternals;
import jp.co.ctc_g.jfw.core.util.Formats;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * {@link String}に変換するためのコンバータクラスです。
 * </p>
 * <h4>クラスコンフィグオーバライド</h4>
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
 *    <td>date_pattern</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      {@link Date}を文字列化する際の変換パターン定義です。
 *    </td>
 *    <td>
 *     yyyy/MM/dd
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>sql_date_pattern</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      {@link java.sql.Date}を文字列化する際の変換パターン定義です。
 *    </td>
 *    <td>
 *     yyyy/MM/dd
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>timestamp_pattern</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      {@link java.sql.Timestamp}を文字列化する際の変換パターン定義です。
 *    </td>
 *    <td>
 *     yyyy/MM/dd HH:mm:ss
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class StringConverter implements TypeConverter<String> {

    protected static final String DATE_PATTERN;
    protected static final String SQL_DATE_PATTERN;
    protected static final String TIMESTAMP_PATTERN;

    static {
        Config c = CoreInternals.getConfig(StringConverter.class);
        DATE_PATTERN = c.find("date_pattern");
        SQL_DATE_PATTERN = c.find("sql_date_pattern");
        TIMESTAMP_PATTERN = c.find("timestamp_pattern");
    }

    /**
     * デフォルトコンストラクタです。
     */
    public StringConverter() {}

    /**
     * {@link Integer}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Integer target) {
        return target.toString();
    }

    /**
     * {@link Long}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Long target) {
        return target.toString();
    }

    /**
     * {@link BigInteger}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(BigInteger target) {
        return target.toString();
    }

    /**
     * {@link Float}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Float target) {
        return target.toString();
    }

    /**
     * {@link Double}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Double target) {
        return target.toString();
    }

    /**
     * {@link BigDecimal}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(BigDecimal target) {
        return target.toString();
    }

    /**
     * {@link Date}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Date target) {
        return Strings.isEmpty(DATE_PATTERN) ? target.toString() : Formats.simpleDateFormat(target, DATE_PATTERN);
    }

    /**
     * {@link java.sql.Date}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(java.sql.Date target) {
        return Strings.isEmpty(SQL_DATE_PATTERN) ? target.toString() : Formats.simpleDateFormat(target, SQL_DATE_PATTERN);
    }

    /**
     * {@link Timestamp}を変換します。
     * @param target 変換対象
     * @return 変換後
     */
    public String convert(Timestamp target) {
        return Strings.isEmpty(TIMESTAMP_PATTERN) ? target.toString() : Formats.simpleDateFormat(target, TIMESTAMP_PATTERN);
    }
}
