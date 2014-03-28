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

package jp.co.ctc_g.jse.core.csv;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.internal.TargetThrowsException;
import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConversionException;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverters;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.MappingStrategy;

/**
 * <p>
 * このクラスはOpenCSVの拡張クラスです.
 * </p>
 * <p>
 * OpenCSVでは1行単位のコンバートに対応していないため、拡張しました。
 * このクラスは基盤内部でのみ利用可能です。
 * </p>
 * @param <T> マッピング対象の型
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class CSVToBeanMapping<T> extends CsvToBean<T> {

    private static final ResourceBundle R = InternalMessages.getBundle(CSVToBeanMapping.class);

    private ByteEditor byteEditor = new ByteEditor();

    private IntegerEditor integerEditor = new IntegerEditor();

    private LongEditor longEditor = new LongEditor();

    private ShortEditor shortEditor = new ShortEditor();

    /**
     * デフォルトコンストラクタです。
     */
    public CSVToBeanMapping() {}

    /**
     * 1行読み込んだデータを指定されたマッピング情報に従って
     * インスタンスを生成します。
     * @param mapper {@link MappingStrategy}
     * @param csv CSV読込ユーティリティ
     * @param line 1レコードのデータ配列
     * @return T 読込後のデータ
     */
    public T parse(MappingStrategy<T> mapper, CSVReader csv, String[] line) {
        if (line == null || line.length == 0) { 
            return null;
        }
        try {
            mapper.captureHeader(csv);
            return convert(mapper, line);
        } catch (BindException e) {
            throw e;
        } catch (IOException e) {
            throw new InternalException(CSVToBeanMapping.class, "E-CSV#0011", e);
        } catch (Exception e) {
            throw new InternalException(CSVToBeanMapping.class, "E-CSV#0012", e);
        }
    }

    private T convert(MappingStrategy<T> mapper, String[] line) throws IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
        T bean = mapper.createBean();
        List<BindError> errors = new ArrayList<BindError>();
        for (int col = 0; col < line.length; col++) {
            PropertyDescriptor prop = mapper.findDescriptor(col);
            if (prop != null) {
                Class<?> propertyType = prop.getPropertyType();
                String propertyName = prop.getName();
                String value = checkForTrim(line[col], prop);
                if (Strings.isEmpty(value)) {
                    continue;
                }
                try {
                    Object obj = convertValue(value, prop);
                    Object converted = TypeConverters.convert(obj, propertyType);
                    Beans.writePropertyValueNamed(propertyName, bean, converted);
                } catch (TargetThrowsException e) {
                    errors.add(new BindError(bean.getClass().getSimpleName(), propertyName, e.getMessage()));
                } catch (TypeConversionException e) {
                    errors.add(new BindError(bean.getClass().getSimpleName(), propertyName, e.getMessage()));
                } catch (NumberFormatException e) {
                    errors.add(new BindError(bean.getClass().getSimpleName(), propertyName, e.getMessage()));
                }
            }
        }
        if (!errors.isEmpty()) throw new BindException(R.getString("E-CSV#0010"), errors);
        return bean;
    }

    private String checkForTrim(String s, PropertyDescriptor prop) {
        return trimmableProperty(prop) ? s.trim() : s;
    }

    private boolean trimmableProperty(PropertyDescriptor prop) {
        return !prop.getPropertyType().getName().contains("String");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PropertyEditor getPropertyEditor(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException {
        if (desc.getPropertyType() == Byte.class) {
            return byteEditor;
        } else if (desc.getPropertyType() == Integer.class) {
            return integerEditor;
        } else if (desc.getPropertyType() == Long.class) {
            return longEditor;
        } else if (desc.getPropertyType() == Short.class) { 
            return shortEditor;
        }
        return super.getPropertyEditor(desc);
    }

    /**
     * OpenCSV内部でのみ使用される、Numberプロパティエディタです。
     *
     * <p>
     * このクラスは、整数型ラッパークラスのデフォルトプロパティエディタが、
     * 文字列 "08"、"09" を８進数としてデコードしてしまう問題を回避するために
     * 作成されました。
     * </p>
     *
     * @author ITOCHU Techno-Solutions Corporation.
     */
    protected abstract static class NumberEditor extends PropertyEditorSupport {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getJavaInitializationString() {
            Object value = getValue();
            return (value != null) ? value.toString() : "null";
        }

    }

    /**
     * OpenCSV内部でのみ使用される、Byteプロパティエディタです。
     *
     * <p>
     * このクラスは、整数型ラッパークラスのデフォルトプロパティエディタが、
     * 文字列 "08"、"09" を８進数としてデコードしてしまう問題を回避するために
     * 作成されました。
     * </p>
     *
     * @author ITOCHU Techno-Solutions Corporation.
     */
    protected static class ByteEditor extends NumberEditor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getJavaInitializationString() {
            Object value = getValue();
            return (value != null) ? "((byte)" + value + ")" : "null";
        }

        /**
         * 10進数としてデコードします。
         *
         * {@inheritDoc}
         */
        @Override
        public void setAsText(String text) {
            setValue((text == null) ? null : Byte.valueOf(text));
        }
    }

    /**
     * OpenCSV内部でのみ使用される、Integerプロパティエディタです。
     *
     * <p>
     * このクラスは、整数型ラッパークラスのデフォルトプロパティエディタが、
     * 文字列 "08"、"09" を８進数としてデコードしてしまう問題を回避するために
     * 作成されました。
     * </p>
     *
     * @author ITOCHU Techno-Solutions Corporation.
     */
    protected static class IntegerEditor extends NumberEditor {

        /**
         * 10進数としてデコードします。
         *
         * {@inheritDoc}
         */
        @Override
        public void setAsText(String text) {
            setValue((text == null) ? null : Integer.valueOf(text));
        }

    }

    /**
     * OpenCSV内部でのみ使用される、Longプロパティエディタです。
     *
     * <p>
     * このクラスは、整数型ラッパークラスのデフォルトプロパティエディタが、
     * 文字列 "08"、"09" を８進数としてデコードしてしまう問題を回避するために
     * 作成されました。
     * </p>
     *
     * @author ITOCHU Techno-Solutions Corporation.
     */
    protected static class LongEditor extends NumberEditor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getJavaInitializationString() {
            Object value = getValue();
            return (value != null) ? value + "L" : "null";
        }

        /**
         * 10進数としてデコードします。
         *
         * {@inheritDoc}
         */
        @Override
        public void setAsText(String text) {
            setValue((text == null) ? null : Long.valueOf(text));
        }

    }

    /**
     * OpenCSV内部でのみ使用される、Shortプロパティエディタです。
     *
     * <p>
     * このクラスは、整数型ラッパークラスのデフォルトプロパティエディタが、
     * 文字列 "08"、"09" を８進数としてデコードしてしまう問題を回避するために
     * 作成されました。
     * </p>
     *
     * @author ITOCHU Techno-Solutions Corporation.
     */
    protected static class ShortEditor extends NumberEditor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getJavaInitializationString() {
            Object value = getValue();
            return (value != null) ? "((short)" + value + ")" : "null";
        }

        /**
         * 10進数としてデコードします。
         *
         * {@inheritDoc}
         */
        @Override
        public void setAsText(String text) {
            setValue((text == null) ? null : Short.valueOf(text));
        }

    }
}
