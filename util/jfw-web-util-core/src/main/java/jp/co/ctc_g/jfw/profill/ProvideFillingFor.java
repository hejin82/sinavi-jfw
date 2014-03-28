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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * この注釈は {@link FillingProvider} が値を提供するプロパティの条件を示します。
 * </p>
 * <p>
 * 条件は論理積です。例えば以下のような定義があったとします。
 * <pre class="brush:java">
 * &#64;ProvideFillingFor(annotation = Foo.class, type = String.class)
 * </pre>
 * 上記のように定義すると、マッチするプロパティ定義は以下の通りです。
 * <pre class="brush:java">
 * public class BuzBean {
 * 
 *     private String property;
 * 
 *     public String getProperty() {
 *         return property;
 *     }
 * 
 *     &#64;Foo
 *     public void setProperty(String value) {
 *         property = value;
 *     }
 * }
 * </pre>
 * </p>
 * @see FillingProvider
 * @see PropertyMatcher
 * @see MatchedProperty
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ProvideFillingFor {

    /**
     * この注釈は何も設定されていないことを示すプレースホルダです。
     */
    public @interface NA {
    }
    
    /**
     * プロパティに付与されているかどうかを検証する対象のアノテーションを返却します。
     */
    Class<? extends Annotation> annotation() default NA.class;
    
    /**
     * プロパティが一致するかどうかを検証する対象の文字列を返却します。
     */
    String name() default "";
    
    /**
     * プロパティ型が代入互換かどうかを検証する対象の型を返却します。
     */
    Class<?> type() default NA.class;
}
