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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * このクラスは、アノテーション操作に関するユーティリティ機能を提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Annotations {

    private Annotations() {
    }

    /**
     * 指定されたアノテーションの属性値を読み取ります。
     * 属性名が実行時にしか判明しない場合や、
     * 別々のアノテーションから同じ名前の属性を取得したい場合などに役立ちます。
     * アノテーションオブジェクトは{@code null}が許容されず、
     * 属性名も空文字は許可されません。
     * @param target 属性値を保有するアノテーション
     * @param attribute 取得したい属性の名前
     * @return 属性値
     */
    public static Object findAttributeValueFor(Annotation target, String attribute) {
        Args.checkNotNull(target);
        Args.checkNotBlank(attribute);
        Method reader = Reflects.findMethodNamed(attribute, target.annotationType());
        Object value = Reflects.invoke(reader, target);
        return value;
    }

    /**
     * 指定されたクラスから、指定されたアノテーションが付与されている公開メソッドを探します。
     * 全てのメソッドを対象としたい場合は、{@link #findDeclaredMethodsAnnotatedBy(Class, Class)}を利用してください。
     * @param decorator 付与されているアノテーション
     * @param target クラス
     * @return 指定されたアノテーションが付与されている公開メソッドの配列
     */
    public static Method[] findMethodsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Class<?> target) {
        return collectMethodsAnnotatedBy(decorator, target.getMethods());
    }

    /**
     * 指定されたクラスから、指定されたアノテーションが付与されている全てのメソッドを探します。
     * 指定されたクラスの親クラスのメソッドもまとめて検索対象とします。
     * よって、公開メソッドのみを対象とするのであれば、{@link #findMethodsAnnotatedBy(Class, Class)}の方が効率的です。
     * @param decorator 付与されているアノテーション
     * @param target クラス
     * @return 指定されたアノテーションが付与されている全てのメソッドの配列
     */
    public static Method[] findDeclaredMethodsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Class<?> target) {
        return collectMethodsAnnotatedBy(decorator, Reflects.findAllMethods(target));
    }

    /**
     * 指定されたメソッド配列の中から、指定されたアノテーションが付与されているメソッドを探します。
     * @param decorator 付与されているアノテーション
     * @param methods メソッド
     * @return 指定されたアノテーションが付与されている全てのメソッドの配列
     */
    public static Method[] collectMethodsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Method[] methods) {
        List<Method> annotatedMethods = new ArrayList<Method>(3);
        for (Method m : methods) {
            if (m.isAnnotationPresent(decorator)) {
                annotatedMethods.add(m);
            }
        }
        return annotatedMethods.toArray(new Method[0]);
    }

    /**
     * 指定されたクラスから、指定されたアノテーションが付与されている公開フィールドを探します。
     * 全てのフィールドを対象としたい場合は、{@link #findDeclaredFieldsAnnotatedBy(Class, Class)}を利用してください。
     * @param decorator 付与されているアノテーション
     * @param target クラス
     * @return 指定されたアノテーションが付与されている公開フィールドの配列
     */
    public static Field[] findFieldsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Class<?> target) {
        return collectFieldsAnnotatedBy(decorator, target.getFields());
    }

    /**
     * 指定されたクラスから、指定されたアノテーションが付与されている全てのフィールドを探します。
     * 指定されたクラスの親クラスのフィールドもまとめて検索対象とします。
     * よって、公開フィールドのみを対象とするのであれば、{@link #findFieldsAnnotatedBy(Class, Class)}の方が効率的です。
     * @param decorator 付与されているアノテーション
     * @param target クラス
     * @return 指定されたアノテーションが付与されている全てのフィールドの配列
     */
    public static Field[] findDeclaredFieldsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Class<?> target) {
        return collectFieldsAnnotatedBy(decorator, Reflects.findAllFields(target));
    }

    /**
     * 指定されたフィールド配列の中から、指定されたアノテーションが付与されているフィールドを探します。
     * @param decorator 付与されているアノテーション
     * @param fields フィールド
     * @return 指定されたアノテーションが付与されている全てのフィールドの配列
     */
    public static Field[] collectFieldsAnnotatedBy(
            Class<? extends Annotation> decorator,
            Field[] fields) {
        List<Field> annotatedFields = new ArrayList<Field>(3);
        for (Field f : fields) {
            if (f.isAnnotationPresent(decorator)) {
                annotatedFields.add(f);
            }
        }
        return annotatedFields.toArray(new Field[0]);
    }
}
