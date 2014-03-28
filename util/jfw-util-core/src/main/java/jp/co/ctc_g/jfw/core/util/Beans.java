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

package jp.co.ctc_g.jfw.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * このクラスは、JavaBeans仕様をサポートするためユーティリティを提供します。
 * </p>
 * <p>
 * このクラスでしばしば言及される疑似プロパティ（pseudoProperty）とは、
 * 通常のJavaBean仕様のプロパティだけでなく、パブリックフィールドも含まれます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Beans {

    private Beans() {
    }

    private static final ResourceBundle R = InternalMessages.getBundle(Beans.class);
    private static final Log L = LogFactory.getLog(Beans.class);

    private static final Pattern ACCESSOR_PATTERN = Pattern.compile("(get|set|is)[A-Z]{1}[a-zA-Z0-9_]*");
    private static final Pattern WRITER_PATTERN = Pattern.compile("set[A-Z]{1}[a-zA-Z0-9_]*");
    private static final Pattern READER_PATTERN = Pattern.compile("(get|is)[A-Z]{1}[a-zA-Z0-9_]*");
    private static final Pattern FIRST_UPPERCASE_PATTERN = Pattern.compile("^[a-z0-9]+[A-Z]");
    private static final Pattern DOUBLE_UPPERCASE_SEQUENCE_PATTERN = Pattern.compile("^[A-Z][A-Z][a-zA-Z0-9_]*");
    private static final Pattern ARRAY_PROPERTY_PATTERN = Pattern.compile("^([a-zA-Z][a-zA-Z0-9_]*)\\[([0-9]+)\\]$");

    /**
     * 指定された文字列の先頭を大文字にします。
     * 指定された文字列が{@link Strings#isEmpty(CharSequence) 空文字}の場合、
     * その文字列をそのまま返却します。
     * @param capitalizee 先頭を大文字にする文字列
     * @return 先頭が大文字になった文字列
     */
    public static String capitalize(String capitalizee) {
        if (Strings.isEmpty(capitalizee)) return capitalizee;
        if (capitalizee.length() > 1) {
            if (Character.isUpperCase(capitalizee.charAt(1))) {
                return capitalizee;
            } else {
                return new StringBuilder()
                        .append(capitalizee.substring(0, 1).toUpperCase())
                        .append(capitalizee.substring(1, capitalizee.length()))
                        .toString();
            }
        } else {
            return capitalizee.toUpperCase();
        }
    }

    /**
     * 指定された文字列の先頭を小文字にします。
     * 指定された文字列が{@link Strings#isEmpty(CharSequence) 空文字}の場合、
     * その文字列をそのまま返却します。
     * @param decapitalizee 先頭を小文字にする文字列
     * @return 先頭が小文字になった文字列
     */
    public static String decapitalize(String decapitalizee) {
        return decapitalize(decapitalizee, true);
    }

    /**
     * 指定された文字列の先頭を小文字にします。
     * 指定された文字列が{@link Strings#isEmpty(CharSequence) 空文字}の場合、
     * その文字列をそのまま返却します。
     * @param decapitalizee 先頭を小文字にする文字列
     * @param doubleUppercaseEnable trueを指定すると先頭2文字が大文字の場合は変換しない
     * @return 先頭が小文字になった文字列
     */
    public static String decapitalize(String decapitalizee, boolean doubleUppercaseEnable) {
        if (Strings.isEmpty(decapitalizee)) return decapitalizee;
        if (decapitalizee.length() > 1) {
            if (doubleUppercaseEnable && DOUBLE_UPPERCASE_SEQUENCE_PATTERN.matcher(decapitalizee).matches()) {
                return decapitalizee;
            } else {
                return new StringBuilder()
                        .append(decapitalizee.substring(0, 1).toLowerCase())
                        .append(decapitalizee.substring(1, decapitalizee.length()))
                        .toString();
            }
        } else {
            return decapitalizee.toLowerCase();
        }
    }

    /**
     * 指定された文字列がプロパティリーダ（getter）であるかどうかを判定します。
     * 例えば、<code>getFoo</code>や<code>isBar</code>はプロパティリーダです。
     * @param suspect 判定する文字列
     * @return プロパティリーダである場合true
     */
    public static boolean isPropertyReader(String suspect) {
        if (suspect == null) return false;
        if ("getClass".equals(suspect)) return false;
        return READER_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列がプロパティライタ（setter）であるかどうかを判定します。
     * 例えば、<code>setFoo</code>はプロパティライタです。
     * @param suspect 判定する文字列
     * @return プロパティライタである場合true
     */
    public static boolean isPropertyWriter(String suspect) {
        if (suspect == null) return false;
        return WRITER_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列がプロパティアクセサ（getterあるいはsetter）であるかどうかを判定します。
     * 例えば、<code>getFoo</code>や<code>isBar</code>、<code>setFoo</code>はプロパティアクセサです。
     * @param suspect 判定する文字列
     * @return プロパティアクセサである場合true
     */
    public static boolean isPropertyAccessor(String suspect) {
        if (suspect == null) return false;
        return ACCESSOR_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列をプロパティアクセサと見做して、プロパティ名を生成します。
     * つまり、<code>getFoo</code>を<code>foo</code>、
     * <code>isBar</code>を<code>bar</code>、<code>setFoo</code>を<code>foo</code>のように変換します。
     * @param accessorName プロパティ名に変換するアクセサ名
     * @return プロパティ名
     */
    public static String generatePropertyNameFor(String accessorName) {
        if (Strings.isEmpty(accessorName)) return accessorName;
        if (Beans.isPropertyAccessor(accessorName)) {
            Matcher matcher = FIRST_UPPERCASE_PATTERN.matcher(accessorName);
            return matcher.lookingAt() ?
                Beans.decapitalize(accessorName.substring(matcher.end() - 1)) : accessorName;
        } else {
            return accessorName;
        }
    }

    /**
     * 指定されたビーンクラスのインスタンスを生成します。
     * 指定されたビーンに{@code null}は認められません。
     * @param <T> 生成対象のクラス
     * @param clazz 対象クラス
     * @return 指定されたビーンクラスのインスタンス
     * @see jp.co.ctc_g.jfw.core.util.Reflects#make
     */
    public static <T> T make(Class<T> clazz) {
        Args.checkNotNull(clazz);
        return Reflects.make(clazz);
    }

    /**
     * 指定されたクラスのプロパティディスクリプタの一覧を取得します。
     * @param clazz 対象クラス
     * @return プロパティディスクリプタ
     */
    public static PropertyDescriptor[] findPropertyDescriptorsFor(Class<?> clazz) {
        Args.checkNotNull(clazz);
        return BeanUtils.getPropertyDescriptors(clazz);
    }

    /**
     * 指定されたクラスの、指定された名前を持つプロパティディスクリプタを取得します。
     * @param clazz 対象クラス
     * @param propertyName プロパティ名
     * @return プロパティディスクリプタ
     */
    public static PropertyDescriptor findPropertyDescriptorFor(Class<?> clazz, String propertyName) {
        Args.checkNotNull(clazz);
        return BeanUtils.getPropertyDescriptor(clazz, propertyName);
    }

    /**
     * 指定されたビーンの、指定されたプロパティを読み込みます。
     * 例えば、
     * <pre class="brush:java">
     * public class FooBean {
     *     private String bar = "propertyValue";
     *     public String getBar() {return bar;}
     *     public void setBar(String bar) {this.bar = bar;}
     * }
     *
     * FooBean b = new FooBean();
     * String value = Beans.readPropertyValueNamed("bar", b);
     * assert value.equals("propertyValue");
     * </pre>
     * となります。
     * さらに、ネストしたオブジェクトのプロパティを読み込むこともできます。
     * <pre class="brush:java">
     * public class BuzBean {
     *     private FooBean fooBean = new FooBean();
     *     public FooBean getFooBean() {return fooBean;}
     *     public void setFooBean(FooBean fooBean) {this.fooBean = fooBean}
     * }
     *
     * BuzBean b = new BuzBean();
     * String value = Beans.readPropertyValueNamed("fooBean.bar", b);
     * assert value.equals("propertyValue");
     * </pre>
     * ネストされるオブジェクトは、配列やリストでも構いません。
     * <pre class="brush:java">
     * public class ArrayBean {
     *     private String[] qux = {"1", "2", "3"};
     *     public String[] getQux() {return this.qux;}
     *     public void setQux(String[]) {this.qux = qux;}
     * }
     *
     * ArrayBean b = new ArrayBean();
     * String value = Beans.readPropertyValueNamed("qux[1]", b);
     * assert value.equals("2");
     * </pre>
     * @param propertyName プロパティ名
     * @param bean 対象のビーン
     * @return プロパティ値
     */
    public static Object readPropertyValueNamed(String propertyName, Object bean) {
        Args.checkNotBlank(propertyName);
        Args.checkNotNull(bean);
        Object temporaryPropertyValue = bean;
        String[] properties = propertyName.split("\\.");
        for (String property : properties) {
            Matcher matcher = ARRAY_PROPERTY_PATTERN.matcher(property);
            if (matcher.matches()) {
                String arrayPropertyName = matcher.group(1);
                Integer arrayPropertyIndex = Integer.valueOf(matcher.group(2));
                Object array = readPropertyValueNamed0(arrayPropertyName, temporaryPropertyValue);
                temporaryPropertyValue = readElementAt(arrayPropertyIndex, array);
            } else {
                temporaryPropertyValue = readPropertyValueNamed0(property, temporaryPropertyValue);
            }
        }
        return temporaryPropertyValue;
    }

    @SuppressWarnings("unchecked")
    private static Object readElementAt(int index, Object arrayOrList) {
        Args.checkPositiveOrZero(index);
        Object contained = null;
        Class<?> type = arrayOrList.getClass();
        if (type.isArray()) {
            Object[] array = (Object[]) arrayOrList;
            contained = readElementFromArrayAt(index, array);
        } else if (List.class.isAssignableFrom(type)) {
            List<Object> container = (List<Object>) arrayOrList;
            contained = readElementFromListAt(index, container);
        } else {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                L.debug(Strings.substitute(R.getString("D-UTIL#0003"), replace));
            }
        }
        return contained;
    }

    private static Object readElementFromArrayAt(int index, Object[] array) {
        if (array == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                L.debug(Strings.substitute(R.getString("D-UTIL#0011"), replace));
            }
            return null;
        }
        if (array.length <= index ) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("size", array.length);
                L.debug(Strings.substitute(R.getString("D-UTIL#0012"), replace));
            }
            return false;
        }
        return array[index];
    }

    private static Object readElementFromListAt(int index, List<Object> container) {
        if (container == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                L.debug(Strings.substitute(R.getString("D-UTIL#0013"), replace));
            }
            return false;
        }
        if (container.size() <= index ) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("size", container.size());
                L.debug(Strings.substitute(R.getString("D-UTIL#0014"), replace));
            }
            return false;
        }
        return container.get(index);
    }

    private static Object readPropertyValueNamed0(String propertyName, Object bean) {
        PropertyDescriptor pd = findPropertyDescriptorFor(bean.getClass(), propertyName);
        if (pd == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("class", bean.getClass().getName());
                replace.put("property", propertyName);
                L.debug(Strings.substitute(R.getString("D-UTIL#0015"), replace));
            }
            return null;
        }
        Method reader = pd.getReadMethod();
        if (reader == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("class", bean.getClass().getName());
                replace.put("property", propertyName);
                L.debug(Strings.substitute(R.getString("D-UTIL#0016"), replace));
            }
            return null;
        }
        return Reflects.invoke(reader, bean);
    }

    /**
     * 指定されたビーンの、指定されたプロパティに値を設定します。
     * 例えば、
     * <pre class="brush:java">
     * public class FooBean {
     *     private String bar;
     *     public String getBar() {return bar;}
     *     public void setBar(String bar) {this.bar = bar;}
     * }
     *
     * FooBean b = new FooBean();
     * Beans.writePropertyValueNamed("bar", b, "propertyValue");
     * assert b.getBar().equals("propertyValue");
     * </pre>
     * となります。
     * さらに、ネストしたオブジェクトのプロパティへ書き込むこともできます。
     * <pre class="brush:java">
     * public class BuzBean {
     *     private FooBean fooBean = new FooBean();
     *     public FooBean getFooBean() {return fooBean;}
     *     public void setFooBean(FooBean fooBean) {this.fooBean = fooBean}
     * }
     *
     * BuzBean b = new BuzBean();
     * Beans.writePropertyValueNamed("fooBean.bar", b, "propertyValue");
     * assert b.getBar().equals("propertyValue");
     * </pre>
     * ネストされるオブジェクトは、配列やリストでも構いません。
     * <pre class="brush:java">
     * public class ArrayBean {
     *     private String[] qux = new String[3];
     *     public String[] getQux() {return this.qux;}
     *     public void setQux(String[]) {this.qux = qux;}
     * }
     *
     * ArrayBean b = new ArrayBean();
     * Beans.writePropertyValueNamed("qux[1]", b, "propertyValue");
     * assert b.getQux()[1].equals("propertyValue");
     * </pre>
     * 書き込みの際は{@link PopulationSafeList}を利用すると、
     * {@link IndexOutOfBoundsException}を気にしなくて済むためより簡便です。
     * @param propertyName プロパティ名
     * @param bean 対象のビーン
     * @param newValue 設定する値
     * @see PopulationSafeList
     */
    public static void writePropertyValueNamed(String propertyName, Object bean, Object newValue) {
        Args.checkNotBlank(propertyName);
        Args.checkNotNull(bean);
        String[] properties = propertyName.split("\\.");
        String propertyToWrite = propertyName;
        Object writable = bean;
        if (properties.length > 1) {
            writable = readPropertyValueNamed(
                    Strings.joinBy(".", Arrays.slice(properties, 0, properties.length - 1)), bean);
            propertyToWrite = properties[properties.length - 1];
        }
        Matcher matcher = ARRAY_PROPERTY_PATTERN.matcher(propertyToWrite);
        if (matcher.matches()) {
            String arrayPropertyName = matcher.group(1);
            Integer arrayPropertyIndex = Integer.valueOf(matcher.group(2));
            Object array = readPropertyValueNamed(arrayPropertyName, writable);
            writeElementAt(arrayPropertyIndex, array, newValue);
        } else {
            writePropertyValueNamed0(propertyToWrite, writable, newValue);
        }
    }

    private static boolean writePropertyValueNamed0(String propertyName, Object bean, Object newValue) {
        PropertyDescriptor pd = findPropertyDescriptorFor(bean.getClass(), propertyName);
        if (pd == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("class", bean.getClass().getName());
                replace.put("property", propertyName);
                L.debug(Strings.substitute(R.getString("D-UTIL#0017"), replace));
            }
            return false;
        }
        Method writer = pd.getWriteMethod();
        if (writer == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("class", bean.getClass().getName());
                replace.put("property", propertyName);
                L.debug(Strings.substitute(R.getString("D-UTIL#0018"), replace));
            }
            return false;
        }
        Reflects.invoke(writer, bean, newValue);
        return true;
    }

    /**
     * 指定されたビーンの、指定された疑似プロパティの値を読み込みます。
     * {@link #readPropertyValueNamed(String, Object)}と利用方法は同じですが、
     * 疑似プロパティであるため、公開フィールドもプロパティとみなして値を読みます。
     * ただし、アクセサが定義されており、かつ公開フィールドでもある場合は、
     * アクセサによる読み込みを優先します。
     * @param propertyName プロパティ名
     * @param bean 読み込み対象
     * @return プロパティ値
     */
    public static Object readPseudoPropertyValueNamed(String propertyName, Object bean) {
        Args.checkNotBlank(propertyName);
        Args.checkNotNull(bean);
        Object temporaryPropertyValue = bean;
        String[] properties = propertyName.split("\\.");
        for (String property : properties) {
            Matcher matcher = ARRAY_PROPERTY_PATTERN.matcher(property);
            if (matcher.matches()) {
                String arrayPropertyName = matcher.group(1);
                Integer arrayPropertyIndex = Integer.valueOf(matcher.group(2));
                Object array = readPseudoPropertyValueNamed0(arrayPropertyName, temporaryPropertyValue);
                temporaryPropertyValue = readElementAt(arrayPropertyIndex, array);
            } else {
                temporaryPropertyValue = readPseudoPropertyValueNamed0(property, temporaryPropertyValue);
            }
        }
        return temporaryPropertyValue;
    }

    private static Object readPseudoPropertyValueNamed0(String propertyName, Object bean) {
        Object value = null;
        try {
            PropertyDescriptor pd =
                    findPropertyDescriptorFor(bean.getClass(), propertyName);
            // プロパティとして定義されている場合
            if (pd != null ) {
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    reader.setAccessible(true);
                    value = reader.invoke(bean);
                } else {
                    if (L.isDebugEnabled()) {
                        Map<String, Object> replace = new HashMap<String, Object>(1);
                        replace.put("property", propertyName);
                        L.debug(Strings.substitute(R.getString("E-UTIL#0012"), replace));
                    }
                }
            // 公開フィールドとして定義されている場合
            } else {
                Field f = bean.getClass().getField(propertyName);
                if (f != null && !Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    value = f.get(bean);
                } else {
                    if (L.isDebugEnabled()) {
                        Map<String, Object> replace = new HashMap<String, Object>(1);
                        replace.put("property", propertyName);
                        L.debug(Strings.substitute(R.getString("D-UTIL#0019"), replace));
                    }
                }
            }
        } catch (SecurityException e) {
            throw new InternalException(Beans.class, "E-UTIL#0010", e);
        } catch (NoSuchFieldException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("property", propertyName);
            throw new InternalException(Beans.class, "E-UTIL#0011", replace, e);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("property", propertyName);
            throw new InternalException(Beans.class, "E-UTIL#0012", replace, e);
        } catch (IllegalAccessException e) {
            throw new InternalException(Beans.class, "E-UTIL#0013", e);
        } catch (InvocationTargetException e) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("class", bean.getClass().getName());
            replace.put("property", propertyName);
            throw new InternalException(Beans.class, "E-UTIL#0014", replace, e);
        }
        return value;
    }

    /**
     * 指定されたビーンの、指定された疑似プロパティの値を設定します。
     * {@link #writePropertyValueNamed(String, Object, Object)}と利用方法は同じですが、
     * 疑似プロパティであるため、公開フィールドもプロパティとみなして値を書きます。
     * ただし、アクセサが定義されており、かつ公開フィールドでもある場合は、
     * アクセサによる書き込みを優先します。
     * @param propertyName プロパティ名
     * @param bean 書き込み対象のビーン
     * @param newValue 設定する値
     */
    public static void writePseudoPropertyValueNamed(String propertyName, Object bean, Object newValue) {
        Args.checkNotBlank(propertyName);
        Args.checkNotNull(bean);
        String[] properties = propertyName.split("\\.");
        String propertyToWrite = propertyName;
        Object writable = bean;
        if (properties.length > 1) {
            writable = readPseudoPropertyValueNamed(
                    Strings.joinBy(".", Arrays.slice(properties, 0, properties.length - 1)), bean);
            propertyToWrite = properties[properties.length - 1];
        }
        Matcher matcher = ARRAY_PROPERTY_PATTERN.matcher(propertyToWrite);
        if (matcher.matches()) {
            String arrayPropertyName = matcher.group(1);
            Integer arrayPropertyIndex = Integer.valueOf(matcher.group(2));
            Object array = readPseudoPropertyValueNamed0(arrayPropertyName, writable);
            writeElementAt(arrayPropertyIndex, array, newValue);
        } else {
            writePseudoPropertyValueNamed0(propertyToWrite, writable, newValue);
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean writeElementAt(int index, Object arrayOrList, Object newValue) {
        Args.checkPositiveOrZero(index);
        Class<?> type = arrayOrList.getClass();
        if (type.isArray()) {
            Object[] array = (Object[]) arrayOrList;
            return writeElementToArrayAt(index, array, newValue);
        } else if (List.class.isAssignableFrom(type)) {
            List<Object> container = (List<Object>) arrayOrList;
            return writeElementToListAt(index, container, newValue);
        } else {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("value", newValue);
                L.debug(Strings.substitute(R.getString("D-UTIL#0004"), replace));
            }
            return false;
        }
    }

    private static boolean writeElementToArrayAt(
            int index,
            Object[] array,
            Object newValue) {
        if (array == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("value", newValue);
                L.debug(Strings.substitute(R.getString("D-UTIL#0005"), replace));
            }
            return false;
        }
        if (array.length <= index ) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("value", newValue);
                replace.put("size", array.length);
                L.debug(Strings.substitute(R.getString("D-UTIL#0006"), replace));
            }
            return false;
        }
        array[index] = newValue;
        return true;
    }

    private static boolean writeElementToListAt(
            int index,
            List<Object> container,
            Object newValue) {
        if (container == null) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("value", newValue);
                L.debug(Strings.substitute(R.getString("D-UTIL#0007"), replace));
            }
            return false;
        }
        if (container.size() <= index ) {
            if (L.isDebugEnabled()) {
                Map<String, Object> replace = new HashMap<String, Object>(2);
                replace.put("index", index);
                replace.put("value", newValue);
                replace.put("size", container.size());
                L.debug(Strings.substitute(R.getString("D-UTIL#0008"), replace));
            }
            return false;
        }
        container.set(index, newValue);
        return true;
    }

    private static boolean writePseudoPropertyValueNamed0(String propertyName, Object bean, Object newValue) {
        try {
            PropertyDescriptor pd =
                    findPropertyDescriptorFor(bean.getClass(), propertyName);
            // プロパティとして定義されている場合
            if (pd != null ) {
                Method writer = pd.getWriteMethod();
                if (writer != null) {
                    writer.setAccessible(true);
                    writer.invoke(bean, newValue);
                    return true;
                } else {
                    if (L.isDebugEnabled()) {
                        Map<String, Object> replace = new HashMap<String, Object>(1);
                        replace.put("property", propertyName);
                        L.debug(Strings.substitute(R.getString("D-UTIL#0009"), replace));
                    }
                    return false;
                }
            // 公開フィールドとして定義されている場合
            } else {
                Field f = bean.getClass().getField(propertyName);
                if (f != null) {
                    f.setAccessible(true);
                    f.set(bean, newValue);
                    return true;
                } else {
                    if (L.isDebugEnabled()) {
                        Map<String, Object> replace = new HashMap<String, Object>(1);
                        replace.put("property", propertyName);
                        L.debug(Strings.substitute(R.getString("D-UTIL#0010"), replace));
                    }
                    return false;
                }
            }
        } catch (SecurityException e) {
            throw new InternalException(Beans.class, "E-UTIL#0010", e);
        } catch (NoSuchFieldException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("property", propertyName);
            throw new InternalException(Beans.class, "E-UTIL#0011", replace, e);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("property", propertyName);
            throw new InternalException(Beans.class, "E-UTIL#0012", replace, e);
        } catch (IllegalAccessException e) {
            throw new InternalException(Beans.class, "E-UTIL#0013", e);
        } catch (InvocationTargetException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("property", propertyName);
            replace.put("class", bean.getClass().getName());
            throw new InternalException(Beans.class, "E-UTIL#0014", replace, e);
        }
    }

    /**
     * 疑似プロパティを列挙します。
     * @param clazz 対象クラス
     * @return 疑似プロパティ名
     */
    public static String[] listPseudoPropertyNames(Class<?> clazz) {
        List<String> names = new ArrayList<String>();
        for (PropertyDescriptor pd : Beans.findPropertyDescriptorsFor(clazz)) {
            names.add(pd.getName());
        }
        for (Field f : clazz.getFields()) {
            String n = f.getName();
            if (!names.contains(n)) names.add(n);
        }
        return names.toArray(new String[0]);
    }

    /**
     * 指定されたクラスに宣言されているプロパティの型を返却します。
     * @param propertyName プロパティ名
     * @param target 対象クラス
     * @return 宣言されているプロパティの型
     */
    public static Class<?> detectDeclaredPropertyType(String propertyName, Class<?> target) {
        Args.checkNotBlank(propertyName);
        Args.checkNotNull(target);
        String[] properties = propertyName.split("\\.");
        Class<?> result = target;
        for (String property : properties) {
            PropertyDescriptor pd = findPropertyDescriptorFor(result, property);
            if (pd == null) {
                if (L.isDebugEnabled()) {
                    Map<String, Object> replace = new HashMap<String, Object>(1);
                    replace.put("property", propertyName);
                    replace.put("class", target.getClass().getName());
                    L.debug(Strings.substitute(R.getString("D-UTIL#0015"), replace));
                }
                return null;
            }
            result = pd.getPropertyType();
        }
        return result;
    }

}
