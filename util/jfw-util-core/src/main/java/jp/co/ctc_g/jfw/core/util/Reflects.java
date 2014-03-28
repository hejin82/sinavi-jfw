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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.TargetThrowsException;

/**
 * <p>
 * このクラスは、リフレクションに関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Reflects {

    private Reflects() {
    }

    /**
     * 指定されたシグニチャを持つメソッドを探します。
     * メソッドが見つからない場合、<code>null</code>を返却します。
     * @param name メソッド名
     * @param target 検索対象クラス
     * @param argumentTypes 引数型
     * @return 見つかったメソッド
     */
    public static Method findMethodSigned(String name, Class<?> target, Class<?>... argumentTypes) {
        Args.checkNotNull(target);
        Args.checkNotEmpty(name);
        Method method = null;
        try {
            method = target.getMethod(name, argumentTypes);
        } catch (SecurityException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", target.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0015", replace, e);
        } catch (NoSuchMethodException e) {
            // メソッドが見つからない場合はnullを返却します
        }
        return method;
    }

    /**
     * 指定された名前のメソッドを探します。
     * メソッドがオーバロードされるなどして、
     * 同名のメソッドが複数存在した場合、
     * どのメソッドが選択されるかは不定です（実行環境に依存します）。
     * @param name メソッド名
     * @param target 検索対象クラス
     * @return 見つかったメソッド
     */
    public static Method findMethodNamed(String name, Class<?> target) {
        Args.checkNotEmpty(name);
        return findMethodNamed(Pattern.compile(name), target);
    }

    /**
     * 指定された名前のメソッドを探します。
     * メソッドがオーバロードされるなどして、
     * 同名のメソッドが複数存在した場合、
     * どのメソッドが選択されるかは不定です（実行環境に依存します）。
     * @param pattern メソッド名
     * @param target 検索対象クラス
     * @return 見つかったメソッド
     */
    public static Method findMethodNamed(Pattern pattern, Class<?> target) {
        Args.checkNotNull(target);
        Args.checkNotNull(pattern);
        Method method = null;
        for (Method m : target.getMethods()) {
            if (pattern.matcher(m.getName()).matches()) {
                method = m; break;
            }
        }
        return method;
    }

    /**
     * 指定された名前のフィールドを探します。
     * @param name フィールド名
     * @param target 検索対象クラス
     * @return 見つかったフィールド
     */
    public static Field findFieldNamed(String name, Class<?> target) {
        Args.checkNotEmpty(name);
        Field field = null;
        try {
            field = target.getField(name);
        } catch (NoSuchFieldException e) {
            return findFieldNamed(Pattern.compile(name), target);
        }
        return field;
    }

    /**
     * 指定された名前のフィールドを探します。
     * @param pattern フィールド名
     * @param target 検索対象クラス
     * @return 見つかったフィールド
     */
    public static Field findFieldNamed(Pattern pattern, Class<?> target) {
        Args.checkNotNull(target);
        Args.checkNotNull(pattern);
        Field field = null;
        for (Field f : target.getFields()) {
            if (pattern.matcher(f.getName()).matches()) {
                field = f; break;
            }
        }
        return field;
    }

    /**
     * 指定されたシグニチャを持つコンストラクタを探します。
     * コンストラクタが見つからない場合、<code>null</code>を返却します。
     * @param <T> 検索対象のクラスの型
     * @param target 検索対象クラス
     * @param argumentTypes 引数型
     * @return 見つかったコンストラクタ
     */
    public static <T> Constructor<T> findConstructorFor(Class<T> target, Class<?>... argumentTypes) {
        Constructor<T> con = null;
        try {
            con = target.getConstructor(argumentTypes);
        } catch (SecurityException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", target.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0016", replace, e);
        } catch (NoSuchMethodException e) {
            // コンストラクタが見つからない場合はnullを返却します
        }
        return con;
    }

    /**
     * 指定されたメソッドを指定されたインスタンスに対して起動します。
     * @param method 実行対象メソッド
     * @param instance thisとして解決するインスタンス
     * @param arguments 引数
     * @return 実行対象メソッドの実行結果
     * @throws TargetThrowsException 実行対象メソッドが例外を発生させた場合
     */
    public static Object invoke(Method method, Object instance, Object... arguments) {
        Object value = null;
        try {
            method.setAccessible(true);
            value = method.invoke(instance, arguments);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("method", method.getName());
            replace.put("arguments", Strings.joinBy(",", arguments));
            throw new InternalException(Reflects.class, "E-UTIL#0017", replace, e);
        } catch (IllegalAccessException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("method", method.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0018", replace, e);
        } catch (InvocationTargetException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("method", method.getName());
            throw new TargetThrowsException(Reflects.class, "E-UTIL#0019", replace, e.getCause());
        }
        return value;
    }

    /**
     * 指定されたフィールドの値を読みだします。
     * @param field 読取対象フィールド
     * @param instance thisとして解決するインスタンス
     * @return フィールド値
     */
    public static Object read(Field field, Object instance) {
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(instance);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("field", field.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0020", replace, e);
        } catch (IllegalAccessException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("field", field.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0021", replace, e);
        }
        return value;
    }

    /**
     * 指定されたフィールドに値を書き込みます。
     * @param field 書き込み対象フィールド
     * @param instance thisとして解決するインスタンス
     * @param newValue 新しい値
     */
    public static void write(Field field, Object instance, Object newValue) {
        try {
            field.setAccessible(true);
            field.set(instance, newValue);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("field", field.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0032", replace, e);
        } catch (IllegalAccessException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("field", field.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0033", replace, e);
        }
    }

    /**
     * 指定されたコンストラクタを実行します。
     * @param <T> このコンストラクタが生成するインスタンスの型
     * @param con コンストラクタ
     * @param arguments コンストラクタ引数
     * @return 生成されたインスタンス
     * @throws TargetThrowsException 実行対象コンストラクタが例外を発生させた場合
     */
    public static <T> T invoke(Constructor<T> con, Object... arguments) {
        T value = null;
        try {
            con.setAccessible(true);
            value = con.newInstance(arguments);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("constructor", con.getName());
            replace.put("arguments", Strings.joinBy(",", arguments));
            throw new InternalException(Reflects.class, "E-UTIL#0022", replace, e);
        } catch (IllegalAccessException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("constructor", con.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0023", replace, e);
        } catch (InvocationTargetException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("constructor", con.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0024", replace, e.getCause());
        } catch (InstantiationException e) {
            Map<String, String> replace = new HashMap<String, String>(2);
            replace.put("constructor", con.getName());
            replace.put("arguments", Strings.joinBy(",", arguments));
            throw new InternalException(Reflects.class, "E-UTIL#0025", replace, e);
        }
        return value;
    }

    /**
     * 指定されたクラスのインスタンスを、デフォルトコンストラクタを利用して生成します。
     * @param <T> 生成されるインスタンスの型
     * @param clazz 生成するクラス
     * @param args コンストラクタの型
     * @return 生成されたインスタンス
     * @throws TargetThrowsException コンストラクタが例外を発生させた場合
     */
    public static <T> T make(Class<T> clazz, Object... args){
        T t = null;
        try {
            Constructor<T> c = clazz.getDeclaredConstructor(toClassArray(args));
            c.setAccessible(true);
            t= c.newInstance(args);
        } catch (InstantiationException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", clazz.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0026", replace, e);
        } catch (IllegalAccessException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", clazz.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0027", replace, e);
        } catch (SecurityException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", clazz.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0028", replace, e);
        } catch (NoSuchMethodException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", clazz.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0026", replace, e);
        } catch (IllegalArgumentException e) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("class", clazz.getName());
            throw new InternalException(Reflects.class, "E-UTIL#0029", replace, e);
        } catch (InvocationTargetException e) {
            throw new TargetThrowsException(Reflects.class, "E-UTIL#0030", e.getCause());
        }
        return t;
    }

    @SuppressWarnings("rawtypes")
    private static Class[] toClassArray(Object... objects) {
        if(objects == null) {
            return new Class[0];
        }
        Class[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }


    /**
     * 指定されたクラスの全てのメソッドを返却します。
     * このメソッドの結果には、<code>private</code>や<code>protected</code>なメソッドも含まれます。
     * また、型階層をさかのぼりメソッドを収集します。
     * @param clazz 検索対象クラス
     * @return 見つかったメソッド
     */
    public static Method[] findAllMethods(Class<?> clazz) {
        Method[] mine = clazz.getDeclaredMethods();
        if (clazz.getSuperclass() != null) {
            Method[] parents = findAllMethods(clazz.getSuperclass());
            mine = Arrays.merge(mine, parents);
        }
        return mine;
    }

    /**
     * 指定されたクラスの全てのフィールドを返却します。
     * このメソッドの結果には、<code>private</code>や<code>protected</code>なフィールドも含まれます。
     * また、型階層をさかのぼりフィールドを収集します。
     * @param clazz 検索対象クラス
     * @return 見つかったフィールド
     */
    public static Field[] findAllFields(Class<?> clazz) {
        Field[] mine = clazz.getDeclaredFields();
        if (clazz.getSuperclass() != null) {
            Field[] parents = findAllFields(clazz.getSuperclass());
            mine = Arrays.merge(mine, parents);
        }
        return mine;
    }

}
