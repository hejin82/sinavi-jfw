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

package jp.co.ctc_g.jfw.core.jdbc.mybatis;


import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * {@code java.util.Date}をリテラル文字列に変換します。
 * </p>
 * <p>
 * このクラスは、日付・時間パターン文字列 {@code yyyy-MM-dd} で{@code String}型に変換し、
 * シングルクォーテーションによって囲まれた文字列をリテラル文字列として返却します。
 * </p>
 */
public class DateLiteralConvertor implements LiteralConvertor<Date> {

    private static final String QUOTE_STRING = "'";

    /**
     * デフォルトコンストラクタです。
     */
    public DateLiteralConvertor() {}

    @Override
    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.LiteralConvertor#convert(java.lang.Object)
     */
    public String convert(Date target) {
        Args.checkNotNull(target);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder builder = new StringBuilder();
        builder.append(QUOTE_STRING);
        builder.append(f.format(target));
        builder.append(QUOTE_STRING);

        return builder.toString();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.LiteralConvertor#getJavaType()
     */
    public Class<?> getJavaType() {
        return Date.class;
    }
}
