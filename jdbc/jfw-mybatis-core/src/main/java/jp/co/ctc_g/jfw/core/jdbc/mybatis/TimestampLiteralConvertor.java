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

import static jp.co.ctc_g.jfw.core.util.Args.checkNotNull;

import java.sql.Timestamp;

/**
 * <p>
 * {@link java.sql.Timestamp}型のパラメータをリテラル文字列に変換します。
 * </p>
 * <h3>変換内容</h3>
 * <p>
 * JDBC タイムスタンプエスケープ形式 {@code yyyy-mm-dd hh:mm:ss.fffffffff} で{@code String}型に変換し、
 * シングルクォーテーションによって囲まれた文字列をリテラル文字列として返却します。
 * </p>
 */
public class TimestampLiteralConvertor implements LiteralConvertor<Timestamp> {

    private static final String QUOTE_STRING = "'";

    /**
     * デフォルトコンストラクタです。
     */
    public TimestampLiteralConvertor() {}

    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.LiteralConvertor#convert(java.lang.Object)
     */
    @Override
    public String convert(Timestamp target) {
        checkNotNull(target);

        StringBuilder sb = new StringBuilder();
        sb.append(QUOTE_STRING);
        sb.append(target.toString());
        sb.append(QUOTE_STRING);

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.ctc_g.jfw.core.jdbc.mybatis.LiteralConvertor#getJavaType()
     */
    @Override
    public Class<?> getJavaType() {
        return Timestamp.class;
    }

}
