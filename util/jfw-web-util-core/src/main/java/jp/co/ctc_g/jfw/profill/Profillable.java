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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * この注釈は、プロパティ値を自動的に設定可能であること示します。
 * 主として {@link ProfillInterceptor} と併せて利用します。
 * </p>
 * <p>
 * {@link ProfillInterceptor} を利用すると、AOP を利用してプロパティ値を設定できて便利ですが、
 * 必要のないオブジェクトまで解析してしまいます。
 * タイトなパフォーマンスが要求される場合、このような不要な処理はできるだけ避けたいものです。
 * この注釈でプロパティ値を自動設定したいオブジェクトをマークすることで、
 * そのオブジェクトだけが解析されるようになります。
 * <pre class="brush:xml">
 * &lt;aop:config&gt;
 *   &lt;aop:pointcut id="servicePointcut" expression="execution(* * ..*Service.*(..))" /&gt;
 *   &lt;aop:advisor pointcut-ref="servicePointcut" advice-ref="profillInterceptor" /&gt;
 * &lt;/aop:config&gt;
 * 
 * &lt;bean id="profillInterceptor" class="jp.co.ctc_g.jfw.profill.ProfillInterceptor"&gt;
 *   &lt;property name="parameterAnnotationRequired" value="true" /&gt;
 * &lt;/bean&gt;
 * &lt;bean class="jp.co.ctc_g.jfw.profill.ProfillFactoryBean"&gt;
 *   &lt;property name="providers"&gt;
 *     &lt;list&gt;
 *       &lt;bean class="jp.co.ctc_g.jfw.profill.util.LocalTimeUpdateStampProvider" /&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 重要なポイントは、{@link ProfillInterceptor} の <code>parameterAnnotationRequired</code> を
 *  <code>true</code> にしていることです。
 * これでこの注釈が付与されている引数のみが解析対象となります。
 * </p>
 * @see Profill
 * @see ProfillInterceptor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface Profillable {
}
