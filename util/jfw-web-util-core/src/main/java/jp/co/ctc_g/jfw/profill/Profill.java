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
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.Reflects;
import jp.co.ctc_g.jfw.profill.util.UpdateStampProvider;
import jp.co.ctc_g.jfw.profill.util.UpdateStamp;
import static jp.co.ctc_g.jfw.core.util.Args.checkNotNull;

/**
 * <p>
 * このクラスは Java ビーンプロパティを解析して自動的に最適な値を設定するエントリクラスです。
 * 同機能を利用する場合にはこのクラスの API を直接実行します。
 * </p>
 * <h2>基本的な利用方法</h2>
 * <p>
 * このクラスのインスタンスによってどのプロパティに対して値が設定されるかは、
 * このクラスのインスタンスに {@link #addFillingProvider(FillingProvider) 設定} された {@link FillingProvider} に付与されている
 *  {@link ProvideFillingFor} アノテーションによって決定されます。
 * シンプルな利用例を提示するために、
 *  {@link FillingProvider} にデフォルトで提供されている {@link UpdateStampProvider} を利用して説明します。
 * なお、 {@link UpdateStampProvider} には {@link ProvideFillingFor} アノテーションが以下のように付与されています。
 * <pre class="brush:java">
 *  &#64;ProvideFillingFor(annotation = UpdateStamp.class)
 *  public class LocalTimeUpdateStampProvider implements FillingProvider {
 *      ...
 *  }
 * </pre>
 * これは {@link UpdateStamp} アノテーションが付与されているプロパティに対して値を設定するという意味です。
 * そこで、以下のような Java ビーンを作成します。
 * <pre class="brush:java">
 * public class FooBean {
 * 
 *     private Timestamp stamp;
 *     
 *     public Timestamp getStamp() {
 *         return stamp;
 *     }
 *     
 *     &#64;UpdateStamp
 *     public void setStamp(Timestamp value) {
 *         stamp = value;
 *     }
 * }
 * </pre>
 * プロパティライターメソッドに {@link UpdateStamp} アノテーションがついています。
 * では準備が整いましたので、この Java ビーンに対して自動的に値を設定します。
 * <pre class="brush:java">
 * FooBean bean = new FooBean();
 * Profill profill = new Profill();
 * profill.addFillingProvider(new LocalTimeUpdateStampProvider());
 * profill.fill(bean);
 * assert bean.getStamp != null; // true
 * </pre>
 * 上記のとおり、以下のような順序で API をコールします。
 * <ol>
 * <li>{@link #Profill()} コンストラクタによるインスタンス生成</li>
 * <li>{@link #addFillingProvider(FillingProvider)} による {@link FillingProvider} の追加</li>
 * <li>{@link #fill(Object)} メソッドによる値設定</li>
 * </ol>
 * 
 * <h2>ネストされた Java ビーン</h2>
 * <p>
 * 上記のとおり、ある Java ビーンに自動的に値を設定するには、
 * {@link #fill(Object)} メソッドをコールします。
 * この時、もし引数に指定された Java ビーンが別の Java ビーンを「持って」いたとしても、
 * 引数に指定された Java ビーンだけが自動設定の対象となります。
 * このような「ネストされたビーン」をサポートするには、{@link Nested} を利用します。 
 * <pre class="brush:java">
 * public class ParentBean {
 * 
 *     private ChildBean chiild;
 *     private Timestamp stamp;
 *     
 *     public ChildBean getChild() {
 *         return child;
 *     }
 *     
 *     &#64;Profill.Nested
 *     public void setChild(ChildBean value) {
 *         child = value;
 *     }
 *     
 *     public Timestamp getStamp() {
 *         return stamp;
 *     }
 *     
 *     &#64;UpdateStamp
 *     public void setStamp(Timestamp value) {
 *         stamp = value;
 *     }
 * }
 * 
 * public class ChildBean {
 * 
 *     private Timestamp stamp;
 *     
 *     public Timestamp getStamp() {
 *         return stamp;
 *     }
 *     
 *     &#64;UpdateStamp
 *     public void setStamp(Timestamp value) {
 *         stamp = value;
 *     }
 * }
 * </pre>
 * このようにすると、ネストされた ChildBean も自動設定の対象となります。
 * ChildBean が<code>null</code>の場合、 {@link #tryToInstantiateIfNestedPropertyIsNull} が
 *  <code>true</code>であれば自動的に ChildBean のインスタンスを生成して、
 *  その生成されたインスタンスに対して自動的に値を設定します。
 * </p>
 * 
 * <h2>DIxAOP コンテナとの連携</h2>
 * <p>
 * このクラスは単体で利用することも可能ですが、DIxAOP コンテナと連携することでよりよく活用できます。
 * そのためには、{@link ProfillInterceptor} を利用します。
 * <pre class="brush:xml">
 * &lt;aop:config&gt;
 *   &lt;aop:pointcut id="servicePointcut" expression="execution(* * ..*Service.*(..))" /&gt;
 *   &lt;aop:advisor pointcut-ref="servicePointcut" advice-ref="profillInterceptor" /&gt;
 * &lt;/aop:config&gt;
 * 
 * &lt;bean id="profillInterceptor" class="jp.co.ctc_g.jfw.profill.ProfillInterceptor" /&gt;
 * &lt;bean class="jp.co.ctc_g.jfw.profill.ProfillFactoryBean"&gt;
 *   &lt;property name="providers"&gt;
 *     &lt;list&gt;
 *       &lt;bean class="jp.co.ctc_g.jfw.profill.util.LocalTimeUpdateStampProvider" /&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * {@link ProfillInterceptor} は Spring Framework のアドバイスとして作成されています。
 * よって、上記のように設定すると、
 * インタフェース名・クラス名が"Service"で終わるクラスの全てのメソッドの引数に対して、
 * 自動的に値を設定するようになります（この挙動が困る場合には
 *  {@link Profillable マーカーアノテーション} を利用してください）。
 * </p>
 * 
 * <h2>複数スレッドからのアクセス</h2>
 * <p>
 *  このクラスを複数のスレッドからのアクセスに対して安全にするためには、
 *  {@link #makeSafeAgainstMultiThreadedAccess()} をコールする必要があります。
 *  このメソッドを呼ぶことでこのインスタンスはスレッドセーフになります。ただし、
 *  <ul>
 *  <li>{@link #addFillingProvider(FillingProvider)}</li>
 *  <li>{@link #setTryToInstantiateIfNestedPropertyIsNull(boolean)} </li>
 *  </ul>
 *  の 2 つのメソッドを呼ぶことはできなくなります（{@link CannotModifyProfillException 例外} が発生します）。
 * </p>
 * @see FillingProvider
 * @see ProfillInterceptor
 * @see ProfillFactoryBean
 * @see Profillable
 * @see CannotModifyProfillException
 */
public class Profill {

    /**
     * 現在スレッドセーフモードの場合は <code>true</code>です。
     */
    protected final AtomicBoolean inThreadSafeMode;
    
    /**
     * {@link Nested ネスト}されたプロパティが <code>null</code>の場合に
     * インスタンスを生成してプロパティ値を自動設定するならば <code>true</code>です。
     */
    protected boolean tryToInstantiateIfNestedPropertyIsNull;
    
    /**
     * Java ビーンのプロパティがマッチした場合に起動する {@link FillingProvider} のマップです。
     */
    protected LinkedHashMap<PropertyMatcher, FillingProvider> matchers;
    
    /**
     * Java ビーンのプロパティを解析した結果のキャッシュです。
     */
    protected ConcurrentHashMap<String, ProfillDefinition> definitions;
    
    /**
     * このクラスのインスタンスを生成します。
     */
    public Profill() {
        inThreadSafeMode = new AtomicBoolean();
        matchers = new LinkedHashMap<PropertyMatcher, FillingProvider>();
        definitions = new ConcurrentHashMap<String, ProfillDefinition>();
    }
    
    /**
     * このオブジェクトを複数のスレッドからのアクセスに対して安全なようにします。
     * そのかわり、{@link #addFillingProvider(FillingProvider)} と
     *  {@link #setTryToInstantiateIfNestedPropertyIsNull(boolean)} を呼ぶことはできなくなります。
     */
    public void makeSafeAgainstMultiThreadedAccess() {
        inThreadSafeMode.set(true);
    }
    
    /**
     * 指定されたプロバイダをこのインスタンスに関連付けます。
     * もし {@link #inThreadSafeMode} が <code>true</code> の場合、
     * このメソッドは例外を発生させます。
     * また、引数に指定された {@link FillingProvider} の実装クラスに、
     * {@link ProvideFillingFor} アノテーションが指定されていない場合も例外を発生させます。
     * @param provider プロバイダ
     * @throws CannotModifyProfillException {@link #inThreadSafeMode} が <code>true</code> の場合(E-PROFILL#0003)
     */
    public void addFillingProvider(FillingProvider provider) {
        throwIfInThreadSafeMode();
        checkNotNull(provider);
        if (!matchers.containsValue(provider)) {
            PropertyMatcher matcher = createPropertyMatcher(provider);
            matchers.put(matcher, provider);
        }
    }
    
    /**
     * 指定された Java ビーンのプロパティを解析し、値を設定します。
     * @param bean プロパティに値を設定する Java ビーン
     */
    public void fill(Object bean) {
        checkNotNull(bean);
        String name = bean.getClass().getName();
        ProfillDefinition definition = definitions.get(name);
        if (definition == null) {
            definition = createDefinition(bean.getClass());
            definitions.putIfAbsent(name, definition);
        }
        apply(definition, bean);
    }
    
    /**
     * 指定された定義のとおりに、指定された Java ビーンに値を設定します。
     * @param definition 設定するプロパティに関する定義
     * @param bean プロパティに値を設定する Java ビーン
     */
    protected void apply(ProfillDefinition definition, Object bean) {
        Map<MatchedProperty, FillingProvider> providers = definition.getProviders();
        Set<MatchedProperty> sets = providers.keySet();
        for (MatchedProperty matched : sets) {
            FillingProvider provider = providers.get(matched);
            Object value = provider.provide(matched, bean);
            Method writer = matched.getDescriptor().getWriteMethod();
            Reflects.invoke(writer, bean, value);
        }
        Map<MatchedProperty, ProfillDefinition> nesteds = definition.getNesteds();
        Set<MatchedProperty> nestedsets = nesteds.keySet();
        for (MatchedProperty matched : nestedsets) {
            ProfillDefinition nested = nesteds.get(matched);
            Object currentValue = matched.getCurrentValue(bean);
            if (currentValue != null) {
                apply(nested, currentValue);
            } else if (tryToInstantiateIfNestedPropertyIsNull && matched.isWritable()) {
                Object newValue = Reflects.make(matched.getType());
                Reflects.invoke(matched.getDescriptor().getWriteMethod(), bean, newValue);
                apply(nested, newValue);
            }
        }
    }
    
    /**
     * 指定された Java ビーンクラスを解析し、
     * 現在設定されている {@link FillingProvider} に対応するプロパティの定義を作成します。 
     * @param clazz 解析対象の Java ビーンクラス
     * @return 値を設定するプロパティについての定義情報
     */
    protected ProfillDefinition createDefinition(Class<?> clazz) {
        ProfillDefinition definition = new ProfillDefinition();
        definition.setName(clazz.getName());
        for (PropertyDescriptor descriptor : Beans.findPropertyDescriptorsFor(clazz)) {
            for (PropertyMatcher matcher : matchers.keySet()) {
                if (matcher.match(descriptor, clazz)) {
                    MatchedProperty matched = new MatchedProperty(descriptor);
                    definition.addProvider(matched, matchers.get(matcher));
                }
            }
            if (isNested(descriptor)) {
                Class<?> type = descriptor.getPropertyType();
                if (definitions.get(type.getName()) == null) {
                    ProfillDefinition nested = createDefinition(type);
                    definitions.putIfAbsent(type.getName(), nested);
                    definition.addNested(new MatchedProperty(descriptor), nested);
                }
            }
        }
        return definition;
    }
    
    /**
     * 指定されたプロパティ記述子がネストされた Java ビーンであるかどうかを判定します。
     * @param descriptor プロパティ記述子
     * @return ネストされている場合には <code>true</code>
     */
    protected boolean isNested(PropertyDescriptor descriptor) {
        return
                (descriptor.getReadMethod() != null ?
                        descriptor.getReadMethod().isAnnotationPresent(Nested.class) : false)
                ||
                (descriptor.getWriteMethod() != null ?
                        descriptor.getWriteMethod().isAnnotationPresent(Nested.class) : false);
    }
    
    /**
     * {@link FillingProvider} に付与されている {@link ProvideFillingFor} アノテーションから、
     * {@link PropertyMatcher} を作成します。
     * もし {@link ProvideFillingFor} アノテーションが付与されていない場合、例外が発生します。
     * @param provider プロバイダ
     * @return このプロバイダに対応するプロパティかどうかを判定するための {@link PropertyMatcher}
     * @throws InternalException {@link ProvideFillingFor} アノテーションが付与されていない場合(E-PROFILL#0001)
     */
    protected PropertyMatcher createPropertyMatcher(FillingProvider provider) {
        ProvideFillingFor pff = provider.getClass().getAnnotation(ProvideFillingFor.class);
        if (pff == null) {
            throw new InternalException(Profill.class, "E-PROFILL#0001");
        }
        return new PropertyMatcher(pff.annotation(), pff.name(), pff.type());
    }
    
    /**
     * {@link Nested ネスト}されたプロパティが <code>null</code>の場合に
     * インスタンスを生成してプロパティ値を自動設定するかどうかを返却します。
     * <code>true</code> の場合、インスタンスを生成してプロパティ値を自動設定します。
     * @return インスタンスを生成してプロパティ値を自動設定するかどうか
     */
    public boolean  isTryToInstantiateIfNestedPropertyIsNull() {
        return tryToInstantiateIfNestedPropertyIsNull;
    }
    
    /**
     * {@link Nested ネスト}されたプロパティが <code>null</code>の場合に
     * インスタンスを生成してプロパティ値を自動設定するかどうかを設定します。
     * <code>true</code> の場合、インスタンスを生成してプロパティ値を自動設定します。
     * @param value インスタンスを生成してプロパティ値を自動設定するかどうか
     */
    public void setTryToInstantiateIfNestedPropertyIsNull(boolean value) {
        throwIfInThreadSafeMode();
        tryToInstantiateIfNestedPropertyIsNull = value;
    }
    
    /**
     * 現在がスレッドセーフモードである場合に例外を発生させます。
     * @throws CannotModifyProfillException 現在がスレッドセーフモードである場合(E-PROFILL#0003)
     */
    protected final void throwIfInThreadSafeMode() {
        if (inThreadSafeMode.get()) {
            throw new CannotModifyProfillException("E-PROFILL#0003", Profill.class);
        }
    }
    
    /**
     * <p>
     * ある Java ビーンのプロパティが別の Java ビーンをネストしていることを示すアノテーションです。
     * 詳細については {@link Profill} の Javadoc をご覧ください。
     * </p>
     * @see Profill
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Inherited
    public @interface Nested {
    }
}
