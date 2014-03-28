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

package jp.co.ctc_g.jfw.core.jdbc.mybatis.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverters;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * MyBatis の TypeHandler です。
 * このTypeHandlerはJDBCタイプ NUMERICとJavaタイプ BigInteger のマッピングを扱います。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see BaseTypeHandler
 */
public class BigIntegerTypeHandler extends BaseTypeHandler<BigInteger> {

    /**
     * デフォルトコンストラクタです。
     */
    public BigIntegerTypeHandler() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            BigInteger parameter, JdbcType jdbcType) throws SQLException {
        ps.setBigDecimal(i, TypeConverters.convert(parameter, BigDecimal.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        BigDecimal b = rs.getBigDecimal(columnName);
        return b != null ? TypeConverters.convert(b, BigInteger.class) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        BigDecimal b = rs.getBigDecimal(columnIndex);
        return b != null ? TypeConverters.convert(b, BigInteger.class) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        BigDecimal b = cs.getBigDecimal(columnIndex);
        return b != null ? TypeConverters.convert(b, BigInteger.class) : null;
    }
}
