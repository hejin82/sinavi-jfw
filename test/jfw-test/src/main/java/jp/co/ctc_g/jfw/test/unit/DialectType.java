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

package jp.co.ctc_g.jfw.test.unit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import jp.co.ctc_g.jfw.core.internal.InternalException;

/**
 * <p>
 * この列挙は、RDBMSプロダクトを表現します。
 * これは、RDBMSプロダクトの方言毎に処理を変更したい場合などに利用します。
 * </p>
 * <p>
 * この列挙は、RDBMSプロダクトを特定するためのメソッドを提供しています。
 * 以下のように利用します。
 * </p>
 * <pre>
 * DataSource source = // 何らかのデータソース取得処理
 * DialectType dialect = DialectType.detect(source);
 * </pre>
 * <p>
 * <p>
 * <i>
 * この列挙で使用されている名前は、
 * そのプロダクトを保持する各社あるいは各団体に帰属するものです。
 * </i>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public enum DialectType {

    /**
     * RDBMSプロダクトにOracleを利用していることを表現します。
     */
    ORACLE("ORACLE"),

    /**
     * RDBMSプロダクトにSQL Serverを利用していることを表現します。
     */
    SQLSERVER("SQL SERVER"),

    /**
     * RDBMSプロダクトにH2を利用していることを表現します。
     */
    H2("H2");

    private Pattern pattern;

    private DialectType(String vender) {
        pattern = Pattern.compile(vender, Pattern.CASE_INSENSITIVE);
    }

    private boolean match(String vender) {
        return pattern.matcher(vender).find();
    }

    /**
     * 指定された文字列をRDBMSプロダクト名と見なして、
     * この列挙の対応する値を返却します。
     * 一般的に、引数で指定されるRDBMSプロダクト名は、
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()}で取得された文字列です。
     * @param vender RDBMSプロダクト名
     * @return この列挙の対応する値
     */
    public static DialectType detect(String vender) {
        DialectType detected = null;
        for (DialectType type : DialectType.values()) {
            if (type.match(vender)) {
                detected = type; break;
            }
        }
        return detected;
    }

    /**
     * 指定されたデータソースから、
     * この列挙の対応する値を返却します。
     * このメソッドはRDBMSとの接続を行ないます。
     * その際に、{@link DataSource#getConnection()}を利用して、
     * コネクションを取得しようとします。
     * よって、コネクション取得の際に認証が必要となる形式、
     * つまり、{@link DataSource#getConnection(String, String)}
     * でなければならないようなデータソースコンフィギュレーションの場合は、
     * 例外を発生させて処理を終了します。
     * そのような場合は、{@link #detect(Connection)}を利用してください。
     * @param source 方言を特定したいデータソース
     * @return この列挙の対応する値
     */
    public static DialectType detect(DataSource source) {
        DialectType detected = null;
        Connection connection = null;
        InternalException exception = null;
        try {
            connection = source.getConnection();
            detected = detect(connection);
        } catch (SQLException e) {
            exception = new InternalException(DialectType.class, "E-TEST#0021", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    if (exception != null) {
                        throw exception;
                    } else {
                        throw new InternalException(DialectType.class, "E-TEST#0022", e);
                    }
                }
            }
        }
        return detected;
    }

    /**
     * 指定されたデータベース接続から、
     * この列挙の対応する値を返却します。
     * このメソッドは、引数に与えられたコネクションを閉じません。
     * コネクションは呼び出し元が閉じる必要があります。
     * @param connection 方言を特定したいデータソース
     * @return この列挙の対応する値
     */
    public static DialectType detect(Connection connection) {
        DialectType detected = null;
        try {
            String product = connection.getMetaData().getDatabaseProductName();
            detected = detect(product);
        } catch (SQLException e) {
            throw new InternalException(DialectType.class, "E-TEST#0022", e);
        }
        return detected;
    }
}
