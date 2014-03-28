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

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Arrays;
import jp.co.ctc_g.jfw.core.util.Classes;
import jp.co.ctc_g.jfw.core.util.GenCall;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * このクラスは、Spring Framework のアドバイスとして {@link Profill} を動作させるためのインターセプタです。
 * </p>
 * @see Profill
 * @see Profillable
 */
public class ProfillInterceptor implements MethodInterceptor, BeanFactoryAware, InitializingBean {

    private BeanFactory factory;
    private boolean parameterAnnotationRequired;
    private String[] markerAnnotationTypes = Arrays.gen(Profillable.class.getName());
    private Profill profill;
    
    /**
     * <code>parameterAnnotationRequired</code> が <code>true</code> の場合、
     * このアノテーションが付与されている引数が解析対象になります。
     */
    protected Class<? extends Annotation>[] markers;
    
    /**
     * デフォルトコンストラクタです。
     */
    public ProfillInterceptor() {}

    /**
     * {@inheritDoc}
     * <p>
     * 引数の Java ビーンを解析して、必要であれば Java ビーンのプロパティに値を設定します。
     * </p>
     * @see Profill#fill(Object)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (profill != null) {
            Object[] arguments = invocation.getArguments();
            Annotation[][] pas = invocation.getMethod().getParameterAnnotations();
            for (int i = 0; i < arguments.length; i++) {
                if (isProfillable(pas[i])) {
                    profill.fill(arguments[i]);
                }
            }
        }
        return invocation.proceed();
    }
    
    /**
     * 引数に付与されていたアノテーションから、
     * 現在の引数の Java ビーンを解析するべきかどうかを返却します。
     * 解析すべき場合、このメソッドは <code>true</code> を返却します。
     * @param annotations 引数に付与されていたアノテーション
     * @return 現在の引数の Java ビーンを解析するべきかどうか
     */
    protected boolean isProfillable(Annotation[] annotations) {
        if (!parameterAnnotationRequired) return true;
        if (Arrays.isEmpty(annotations)) return false;
        for (Annotation a : annotations) {
            for (Class<? extends Annotation> marker :markers) {
                if (marker == a.annotationType()) return true;
            }
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@link Profill} のインスタンスが設定されていない場合、
     * DI コンテナから {@link Profill} インスタンスを<strong>型マッチで取得</strong>します。
     * そして、{@link Profill} インスタンスをスレッドセーフにします。
     * </p>
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (profill == null) {
            if (factory == null) throw new InternalException(ProfillInterceptor.class, "E-PROFILL#0004");
            profill = factory.getBean(Profill.class);
        }
        profill.makeSafeAgainstMultiThreadedAccess();
        if (parameterAnnotationRequired && !Arrays.isEmpty(markerAnnotationTypes)) {
            markers = Arrays.gen(markerAnnotationTypes.length, new GenCall<Class<? extends Annotation>>() {
                @Override public Class<? extends Annotation> gen(int index, int total) {
                    return Classes.forName(markerAnnotationTypes[index]);
                }
            });
        }
    }

    /**
     * 現在設定されている {@link Profill} インスタンスを返却します。
     * @return {@link Profill} インスタンス
     */
    public Profill getProfill() {
        return profill;
    }

    /**
     * {@link Profill} インスタンスを設定します。
     * @param profill {@link Profill} インスタンス
     */
    public void setProfill(Profill profill) {
        this.profill = profill;
    }

    /**
     * 引数にマーカーアノテーションが付与されている場合に限り、解析対象とするかどうかを返却します。
     * デフォルトでは、{@link Profillable} がマーカーアノテーションです。
     * @return マーカーアノテーションが付与されている場合に限り解析するかどうか
     */
    public boolean isParameterAnnotationRequired() {
        return parameterAnnotationRequired;
    }

    /**
     * 引数にマーカーアノテーションが付与されている場合に限り、解析対象とするかどうかを設定します。
     * デフォルトでは、{@link Profillable} がマーカーアノテーションです。
     * @param parameterAnnotationRequired マーカーアノテーションが付与されている場合に限り解析するかどうか
     */
    public void setParameterAnnotationRequired(boolean parameterAnnotationRequired) {
        this.parameterAnnotationRequired = parameterAnnotationRequired;
    }

    /**
     * マーカーアノテーションを取得します。
     * @return マーカーアノテーション
     * @see #isParameterAnnotationRequired()
     */
    public String[] getMarkerAnnotationTypes() {
        return markerAnnotationTypes;
    }

    /**
     * マーカーアノテーションを設定します。
     * @param markerAnnotationTypes マーカーアノテーション
     * @see #isParameterAnnotationRequired()
     */
    public void setMarkerAnnotationTypes(String[] markerAnnotationTypes) {
        this.markerAnnotationTypes = markerAnnotationTypes;
    }
}
