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

package jp.co.ctc_g.jfw.profill;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Strings;

import static jp.co.ctc_g.jfw.core.util.Args.checkNotNull;

/**
 * <p>
 * このクラスは Java ビーンのプロパティがある特定の形式にマッチするかどうかを検証します。
 * このクラスのインスタンスは {@link ProvideFillingFor} をもとに作成され、
 * <ul>
 * <li>アノテーションの有無</li>
 * <li>プロパティ名の等価性</li>
 * <li>型の互換性</li>
 * </ul>
 * の観点から検証します。
 * </p>
 * @see FillingProvider
 */
public class PropertyMatcher {

    /**
     * プロパティに付与されているかどうかを検証する対象のアノテーションです。
     */
    protected final Class<? extends Annotation> annotation;
    
    /**
     * プロパティが一致するかどうかを検証する対象の文字列です。
     */
    protected final String name;
    
    /**
     * プロパティ型が代入互換かどうかを検証する対象の型です。
     */
    protected final Class<?> type;
    
    /**
     * このクラスのインスタンスを生成します。
     * @param annotation プロパティに付与されているかどうかを検証する対象のアノテーション
     * @param name プロパティが一致するかどうかを検証する対象の文字列
     * @param type プロパティ型が代入互換かどうかを検証する対象の型
     * @see ProvideFillingFor.NA
     */
    public PropertyMatcher(
            Class<? extends Annotation> annotation,
            String name,
            Class<?> type) {
        checkNotNull(annotation);
        checkNotNull(name);
        checkNotNull(type);
        checkCondition(annotation, name, type);
        this.annotation = annotation;
        this.name = name;
        this.type = type;
    }
    
    /**
     * 条件の妥当性を検証します。
     * <code>annotation</code>, <code>name</code>, <code>type</code> が全て無効な場合は、
     * 妥当な条件ではないため例外が発生します。
     * @param at プロパティに付与されているかどうかを検証する対象のアノテーション
     * @param n プロパティが一致するかどうかを検証する対象の文字列
     * @param t プロパティ型が代入互換かどうかを検証する対象の型
     * @throws InternalException <code>annotation</code>, <code>name</code>, <code>type</code> が全て無効な場合(E-PROFILL#0002)
     */
    protected void checkCondition(
            Class<? extends Annotation> at,
            String n,
            Class<?> t) {
        if (at == ProvideFillingFor.NA.class
                && Strings.isEmpty(n)
                && t == ProvideFillingFor.NA.class) {
            throw new InternalException(PropertyMatcher.class, "E-PROFILL#0002");
        }
    }
    
    /**
     * 指定されたプロパティ記述子が条件にマッチするか検証します。
     * @param descriptor マッチ検証対象のプロパティ記述子
     * @param clazz 検証中の Java ビーンクラス
     * @return マッチした場合は <code>true</code>
     */
    public boolean match(PropertyDescriptor descriptor, Class<?> clazz) {
        return matchAnnotation(descriptor, clazz)
                && matchPropertyName(descriptor, clazz)
                && matchPropertyType(descriptor, clazz);
    }
    
    /**
     * 指定されたプロパティ記述子が条件にマッチするか検証します。
     * @param descriptor マッチ検証対象のプロパティ記述子
     * @param clazz 検証中の Java ビーンクラス
     * @return マッチした場合は <code>true</code>
     */
    protected boolean matchAnnotation(PropertyDescriptor descriptor, Class<?> clazz) {
        return  annotation == ProvideFillingFor.NA.class
                || (descriptor.getWriteMethod() != null ?
                        descriptor.getWriteMethod().isAnnotationPresent(annotation) : false);
    }
    
    /**
     * 指定されたプロパティ記述子が条件にマッチするか検証します。
     * @param descriptor マッチ検証対象のプロパティ記述子
     * @param clazz 検証中の Java ビーンクラス
     * @return マッチした場合は <code>true</code>
     */
    protected boolean matchPropertyName(PropertyDescriptor descriptor, Class<?> clazz) {
        return Strings.isEmpty(name)
                ||  descriptor.getName().matches(name);
    }
    
    /**
     * 指定されたプロパティ記述子が条件にマッチするか検証します。
     * @param descriptor マッチ検証対象のプロパティ記述子
     * @param clazz 検証中の Java ビーンクラス
     * @return マッチした場合は <code>true</code>
     */
    protected boolean matchPropertyType(PropertyDescriptor descriptor, Class<?> clazz) {
        return type == ProvideFillingFor.NA.class
                || descriptor.getPropertyType() == type;
    }

    /**
     * プロパティに付与されているかどうかを検証する対象のアノテーションを返却します。
     * @return プロパティに付与されているかどうかを検証する対象のアノテーション
     */
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    /**
     * プロパティが一致するかどうかを検証する対象の文字列を返却します。
     * @return プロパティが一致するかどうかを検証する対象の文字列
     */
    public String getName() {
        return name;
    }

    /**
     * プロパティ型が代入互換かどうかを検証する対象の型を返却します。
     * @return プロパティ型が代入互換かどうかを検証する対象の型
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * <p>
     * このオブジェクトのハッシュ値は、内部の
     * <code>annotation</code>, <code>name</code>, <code>type</code> のハッシュ値を基に算出しています。
     * </p>
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int p = 37, r = 7;
        r = p * r + annotation.hashCode();
        r = p * r + name.hashCode();
        r = p * r + type.hashCode();
        return r;
    }

    /**
     * <p>
     * このオブジェクトの等価性は、内部の
     * <code>annotation</code>, <code>name</code>, <code>type</code> の等価性の論理積です。
     * </p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PropertyMatcher)) return false;
        PropertyMatcher u = (PropertyMatcher) obj;
        return annotation == u.getAnnotation()
                && name.equals(u.name) // propertyName は null にならない（コンストラクタ参照）
                && type == u.type;
    }    
}
