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

package jp.co.ctc_g.jse.core.framework;

import jp.co.ctc_g.jse.core.util.web.beans.PropertyEditingException;

import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;

/**
 * <p>
 * このクラスは、バインド・エラーを処理する{@link org.springframework.validation.BindingErrorProcessor}インタフェースの実装で、
 * J-Framework独自のストラテジをSpring MVCに挿入するためのものです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see DefaultBindingErrorProcessor
 */
public class JseBindingErrorProcessor extends DefaultBindingErrorProcessor implements InitializingBean {
    
    private JseLocalValidatorFactoryBean factoryBean;

    /**
     * デフォルトコンストラクタです。
     */
    public JseBindingErrorProcessor() {}

    /**
     * {@link JseLocalValidatorFactoryBean}インスタンスを設定します。
     * @param factoryBean {@link JseLocalValidatorFactoryBean}インスタンス
     */
    public final void setJseLocalValidatorFactoryBean(JseLocalValidatorFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPropertyAccessException(PropertyAccessException e, BindingResult bindingResult) {

        TypeOrFormatMismatchException converted = null;
        if (Controllers.exceptionMatch(e.getCause().getClass(), PropertyEditingException.class)) {
            converted = new TypeOrFormatMismatchException(e, bindingResult);
            super.processPropertyAccessException(converted, bindingResult);
        } else {
            super.processPropertyAccessException(e, bindingResult);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (factoryBean == null) {
            factoryBean = new JseLocalValidatorFactoryBean();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[] getArgumentsForBindError(final String objectName, String field) {
        return new Object[] { factoryBean.getMessageSourceResolvable(objectName, field) };
    }
}
