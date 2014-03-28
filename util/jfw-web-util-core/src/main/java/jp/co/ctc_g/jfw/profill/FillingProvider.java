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

/**
 * <p>
 * このインタフェースはプロパティに設定する値を提供します。
 * 特別な事情がない限り、{@link ProvideFillingFor} アノテーションとセットで利用します。
 * </p>
 * <p>
 * 例えば、Java ビーンに文字列を設定するプロバイダを作成してみます。
 * <pre class="brush:java">
 * &#64;ProvideFillingFor(annotation = StringLiteral.class)
 * public class AnnotatedStringLiteralFillingProvider implements FillingProvider {
 * 
 *     &#64;Override
 *     public Object provide(MatchedProperty property, Object bean) {
 *         StringLiteral annotation = property.getAnnotation(StringLiteral.class);
 *         return annotation.value();
 *     }
 * }
 * 
 * &#64;Documented
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;Target(ElementType.METHOD)
 * public &#64;interface StringLiteral {
 * 
 *     String value();
 * }
 * </pre>
 * このプロバイダは、StringLiteral アノテーションが付加されている Java ビーンプロパティに対して、
 * StringLiteral の value() 値を設定します。
 * </p>
 * @see Profill
 * @see ProvideFillingFor
 */
public interface FillingProvider {

    /**
     * マッチしたプロパティに対して、設定すべき値を返却します。
     * @param property マッチしたプロパティ
     * @param bean 設定対象の Java ビーン
     * @return 設定すべき値
     */
    Object provide(MatchedProperty property, Object bean);
}
