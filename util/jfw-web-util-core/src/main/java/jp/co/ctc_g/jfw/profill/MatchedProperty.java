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

import jp.co.ctc_g.jfw.core.util.Arrays;
import jp.co.ctc_g.jfw.core.util.Reflects;

import static jp.co.ctc_g.jfw.core.util.Args.checkNotNull;

/**
 * <p>
 * このクラスは、{@link ProvideFillingFor} の定義にマッチしたプロパティの情報を保持します。
 * {@link PropertyDescriptor} の薄いラッパークラスです。
 * </p>
 * @see Profill
 * @see FillingProvider
 * @see ProvideFillingFor
 */
public class MatchedProperty {

    /**
     * マッチしたプロパティ記述子
     */
    protected final PropertyDescriptor descriptor;
    
    /**
     * コンストラクタです。
     * @param descriptor プロパティ記述子
     */
    public MatchedProperty(PropertyDescriptor descriptor) {
        checkNotNull(descriptor);
        this.descriptor = descriptor;
    }
    
    /**
     * プロパティの名前を返却します。
     * @return プロパティの名前
     * @see PropertyDescriptor#getName()
     */
    public String getName() {
        return descriptor.getName();
    }
    
    /**
     * プロパティの型を返却します。
     * @return プロパティの型
     * @see PropertyDescriptor#getPropertyType()
     */
    public Class<?> getType() {
        return descriptor.getPropertyType();
    }
    
    /**
     * <storng>書き込みプロパティ</storng>に設定されているアノテーションを返却します。
     * @return <storng>書き込みプロパティ</storng>に設定されているアノテーション
     * @see PropertyDescriptor#getWriteMethod()
     * @see java.lang.reflect.Method#getAnnotations()
     */
    public Annotation[] getAnnotations() {
        return descriptor.getWriteMethod().getAnnotations();
    }
    
    /**
     * <storng>書き込みプロパティ</storng>に設定されている指定されたアノテーションを返却します。
     * @param <T> アノテーション
     * @param annotation 取得したいアノテーション
     * @return <storng>書き込みプロパティ</storng>に設定されている指定されたアノテーション
     * @see PropertyDescriptor#getWriteMethod()
     * @see java.lang.reflect.Method#getAnnotations()
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return descriptor.getWriteMethod().getAnnotation(annotation);
    }
    
    /**
     * <storng>書き込みプロパティ</storng>の引数に付与されているアノテーションを取得します。
     * 書き込みプロパティであるため引数は 1 つであるため、
     * このメソッドはアノテーションの配列を返却します。
     * @return <storng>書き込みプロパティ</storng>の引数に付与されているアノテーション
     * @see PropertyDescriptor#getWriteMethod()
     * @see java.lang.reflect.Method#getAnnotations()
     */
    public Annotation[] getParameterAnnotation() {
        Annotation[][] annotations = descriptor.getWriteMethod().getParameterAnnotations();
        if (Arrays.isNotEmpty(annotations)) {
            return annotations[0];
        } else {
            return null;
        }
    }
    
    /**
     * プロパティが読み込み専用かどうかを返却します。
     * 読み込み専用の場合 <code>true</code> を返却します。
     * @return プロパティが読み込み専用かどうか
     */
    public boolean isReadable() {
        return descriptor.getReadMethod() != null;
    }
    
    /**
     * プロパティが書き込み専用かどうかを返却します。
     * 書き込み専用の場合 <code>true</code> を返却します。
     * @return プロパティが書き込み専用かどうか
     */
    public boolean isWritable() {
        return descriptor.getWriteMethod() != null;
    }
    
    /**
     * プロパティに設定されている現在の値を返却します。
     * このプロパティが書き込み専用の場合、このメソッドは <code>null</code> を返却します。
     * @param bean 対象の Java ビーン
     * @return プロパティの値
     */
    public Object getCurrentValue(Object bean) {
        if (isReadable()) {
            return Reflects.invoke(descriptor.getReadMethod(), bean);
        }
        return null;
    }
    
    /**
     * このインスタンスがラップしている {@link PropertyDescriptor} を返却します。
     * @return このインスタンスがラップしている {@link PropertyDescriptor}
     */
    protected PropertyDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int p = 37, r = 7;
        r = p * r + descriptor.hashCode();
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MatchedProperty)) return false;
        MatchedProperty u = (MatchedProperty) obj;
        return descriptor.equals(u.descriptor);
    }
}
