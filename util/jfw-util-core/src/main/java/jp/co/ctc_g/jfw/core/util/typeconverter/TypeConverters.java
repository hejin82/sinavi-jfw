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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.CoreInternals;
import jp.co.ctc_g.jfw.core.internal.TargetThrowsException;
import jp.co.ctc_g.jfw.core.util.Args;
import jp.co.ctc_g.jfw.core.util.Classes;
import jp.co.ctc_g.jfw.core.util.Reflects;

/**
 * <p>
 * このクラスは型変換を実行するためのユーティリティクラスです。
 * また、型変換オブジェクト（{@link TypeConverter}）のレジストリでもあります。
 * </p>
 * <h4>単純な利用</h4>
 * <p>
 * 型変換を実行することは、非常に簡単です。
 * </p>
 * <pre class="brush:java">
 * Integer converted = TypeConverters.convert("1", Integer.class);
 * </pre>
 * <p>
 * 上記コードは、{@link String}で表現された文字列型の1を、
 * {@link Integer}で表現された数値型の1に変換します。
 * このような変換定義を独自に追加するには、クラスコンフィグオーバライドを利用してください。
 * </p>
 * <h4>クラスコンフィグオーバライド</h4>
 * <p>
 * このクラスのクラスコンフィグオーバライドは特殊であり、
 * プロパティファイルに、変換先型の完全修飾クラス名を記述し、
 * 値にそのコンバータクラスを記述します。
 * よって、他のクラスのように決定されているキーはありません。
 * デフォルトでは、以下のコンバータが定義されています。
 * </p>
 * <pre>
 * java.lang.String=jp.co.ctc_g.jfw.core.util.typeconverter.StringConverter
 * java.lang.Integer=jp.co.ctc_g.jfw.core.util.typeconverter.IntegerConverter
 * java.lang.Long=jp.co.ctc_g.jfw.core.util.typeconverter.LongConverter
 * java.math.BigInteger=jp.co.ctc_g.jfw.core.util.typeconverter.BigIntegerConverter
 * java.lang.Float=jp.co.ctc_g.jfw.core.util.typeconverter.FloatConverter
 * java.lang.Double=jp.co.ctc_g.jfw.core.util.typeconverter.DoubleConverter
 * java.math.BigDecimal=jp.co.ctc_g.jfw.core.util.typeconverter.BigDecimalConverter
 * java.util.Date=jp.co.ctc_g.jfw.core.util.typeconverter.DateConverter
 * java.sql.Date=jp.co.ctc_g.jfw.core.util.typeconverter.SqlDateConverter
 * java.sql.Timestamp=jp.co.ctc_g.jfw.core.util.typeconverter.TimestampConverter
 * </pre>
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
 *    <td>変換先型の完全修飾クラス名</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      コンバータ実装クラスの完全修飾クラス名
 *    </td>
 *    <td>
 *     なし
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see TypeConverter
 * @see jp.co.ctc_g.jfw.core.util.Beans
 * @see jp.co.ctc_g.jfw.core.util.porter.DeclarationAwareBeanPropertyTypeConverter
 */
public final class TypeConverters {

    private TypeConverters() {
    }

    private static final String CONVERTER_METHOD_NAME = "convert";

    private static Map<String, TypeConverter<?>> converters;

    static {
        Config c = CoreInternals.getConfig(TypeConverters.class);
        registerConverters(c);
    }

    private static void registerConverters(Config c) {
        Set<Object> keys = c.keys();
        converters = new HashMap<String, TypeConverter<?>>();
        for (Object key : keys) {
            String type = (String) key;
            String value = c.find(type);
            Class<TypeConverter<?>> clazz = Classes.<TypeConverter<?>>forName(value);
            TypeConverter<?> converter = Reflects.make(clazz);
            converters.put(type, converter);
        }
    }

    /**
     * 指定された値を指定された型に変換します。
     * @param <T> 変換先型
     * @param value 型変換元オブジェクト
     * @param convertTo 変換後のクラス
     * @return 指定された型に変換されたオブジェクト
     * @throws TypeConversionException 型変換に失敗した場合（E-UTIL-TC#0002）
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> convertTo) {
        Args.checkNotNull(value);
        Args.checkNotNull(convertTo);
        if (convertTo.isAssignableFrom(value.getClass())) return convertTo.cast(value);
        String to = convertTo.getName();
        if (!converters.containsKey(to)) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("from", value.getClass().getName());
            replace.put("to", convertTo.getName());
            throw new TypeConversionException("E-UTIL-TC#0002", TypeConverters.class, replace);
        }
        TypeConverter<T> converter = (TypeConverter<T>) converters.get(to);
        return convert(converter, value, convertTo);
    }

    /**
     * 指定されたコンバータを使って、指定された値を指定された型に変換します。
     * @param <T> 変換先型
     * @param converter 型変換処理オブジェクト
     * @param value 型変換元オブジェクト
     * @param to 変換後のクラス
     * @return 指定された型に変換されたオブジェクト
     * @throws TypeConversionException 型変換に失敗した場合（E-UTIL-TC#0002）
     */
    public static <T> T convert(TypeConverter<?> converter, Object value, Class<T> to) {
        Args.checkNotNull(converter);
        Args.checkNotNull(value);
        Args.checkNotNull(to);
        if (to.isAssignableFrom(value.getClass())) return to.cast(value);
        Method m = Reflects.findMethodSigned(
                CONVERTER_METHOD_NAME,
                converter.getClass(),
                value.getClass());
        if (m == null) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("from", value.getClass().getName());
            replace.put("to", to.getName());
            throw new TypeConversionException("E-UTIL-TC#0002", TypeConverters.class, replace);
        }
        m.setAccessible(true);
        Object converted = null;
        try {
            converted = Reflects.invoke(m, converter, value);
        } catch (TargetThrowsException e) {
            if (e.getCause() instanceof TypeConversionException) {
                throw (TypeConversionException) e.getCause();
            } else {
                throw e;
            }
        }
        return to.cast(converted);
    }

}
