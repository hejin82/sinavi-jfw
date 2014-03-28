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

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Arrays;
import jp.co.ctc_g.jfw.core.util.Classes;
import jp.co.ctc_g.jfw.core.util.Reflects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * <p>
 * このクラスは {@link Profill} を生成するためのファクトリです。
 * Spring Framework の DI コンテナで利用することを想定しています。
 * {@link ProfillInterceptor} を利用する場合、
 * 少なくとも 1 つはこのクラスをコンテナに登録する必要があります（さもないと例外が発生します）。
 * コンテナの登録方法については {@link Profill} の Javadoc をご覧ください。
 * </p>
 * @see Profill
 * @see ProfillInterceptor
 */
public class ProfillFactoryBean implements FactoryBean<Profill>, BeanFactoryAware, InitializingBean {

    private String[] providerRefs;
    
    private String[] providerTypes;
    
    private FillingProvider[] providers;
    
    private BeanFactory factory;
    
    private Profill profill;
    
    private int autoWireType = AutowireCapableBeanFactory.AUTOWIRE_NO;

    /**
     * デフォルトコンストラクタです。
     */
    public ProfillFactoryBean() {}

    /**
     * {@inheritDoc}
     * <p>
     * このインスタンスに設定されている {@link #setProviderRefs(String[])}、
     * {@link #setProviderTypes(String[])}、
     * {@link #setProviders(FillingProvider[])} を参照して、
     * 内部の {@link Profill} に対して {@link Profill#addFillingProvider(FillingProvider)} を呼び出します。 
     * </p>
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (factory == null) throw new InternalException(ProfillFactoryBean.class, "E-PROFILL#0005");
        profill = createDefault();
        if (!Arrays.isEmpty(providerRefs)) {
            for (String ref : providerRefs) {
                FillingProvider provider = factory.getBean(ref, FillingProvider.class);
                profill.addFillingProvider(provider);
            }
        }
        if (!Arrays.isEmpty(providerTypes)) {
            for (String type : providerTypes) {
                Class<?> clazz = Classes.forName(type);
                FillingProvider provider = (FillingProvider) Reflects.make(clazz);
                if (factory instanceof AutowireCapableBeanFactory) {
                    ((AutowireCapableBeanFactory) factory).autowireBeanProperties(
                            provider, getAutoWireType(), true);
                }
                profill.addFillingProvider(provider);
            }
        }
        if (!Arrays.isEmpty(providers)) {
            for (FillingProvider provider : providers) {
                profill.addFillingProvider(provider);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }
    
    /**
     * このインスタンスが利用する {@link Profill} インスタンスを生成します。
     * @return このインスタンスが利用する {@link Profill} インスタンス
     */
    protected Profill createDefault() {
        Profill p = new Profill();
        return p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profill getObject() throws Exception {
        if (profill == null) {
            afterPropertiesSet();
        }
        return profill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return Profill.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
    
    /**
     * このインスタンスが利用する {@link FillingProvider} の Bean ID を返却します。
     * @return このインスタンスが利用する {@link FillingProvider} の Bean ID
     */
    public String[] getProviderRefs() {
        return providerRefs;
    }

    /**
     * このインスタンスが利用する {@link FillingProvider} の Bean ID を設定します。
     * @param providerRefs このインスタンスが利用する {@link FillingProvider} の Bean ID
     */
    public void setProviderRefs(String[] providerRefs) {
        this.providerRefs = providerRefs;
    }

    /**
     * このインスタンスが利用する {@link FillingProvider} の実装クラス名を取得します。
     * @return このインスタンスが利用する {@link FillingProvider} の実装クラス名
     */
    public String[] getProviderTypes() {
        return providerTypes;
    }

    /**
     * このインスタンスが利用する {@link FillingProvider} の実装クラス名を設定します。
     * @param providerTypes このインスタンスが利用する {@link FillingProvider} の実装クラス名
     */
    public void setProviderTypes(String[] providerTypes) {
        this.providerTypes = providerTypes;
    }

    /**
     * {@link FillingProvider} にインジェクションする際の設定を取得します。
     * @return {@link FillingProvider} にインジェクションする際の設定
     */
    public int getAutoWireType() {
        return autoWireType;
    }

    /**
     * {@link FillingProvider} にインジェクションする際の設定を変更します。
     * @param autoWireType {@link FillingProvider} にインジェクションする際の設定
     */
    public void setAutoWireType(int autoWireType) {
        this.autoWireType = autoWireType;
    }

    /**
     * このインスタンスが利用する {@link FillingProvider} のインスタンスを取得します。
     * @return このインスタンスが利用する {@link FillingProvider} のインスタンス
     */
    public FillingProvider[] getProviders() {
        return providers;
    }

    /**
     * このインスタンスが利用する {@link FillingProvider} のインスタンスを設定します。
     * @param providers このインスタンスが利用する {@link FillingProvider} のインスタンス
     */
    public void setProviders(FillingProvider[] providers) {
        this.providers = providers;
    }
}
