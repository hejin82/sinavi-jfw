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

import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;

/**
 * <p>
 * このクラスは、{@link HttpSession} にストアする際のキー文字列を修飾します。
 * {@code Spring MVC} のデフォルトの振る舞いは、{@link ModelAttribute}注釈の{@code value}属性
 * によって定義されるキー文字列が利用されます。このようなキー文字列の指定は、コントローラ間でキーの一意性を
 * 保証するのが困難なため、このクラスがコントローラの{@code FQCN}　をプレフィックとして付加し。コントローラ間でキーの
 * 衝突を回避します。<br/>
 * なお、{@link Model}にバインドする際のキー文字列に影響はないため、ビューからの参照は、
 * {@link ModelAttribute}注釈の{@code value}属性で指定したキー文字列が利用可能です。
 * </p>
 * @see SessionAttributeStore
 * @author ITOCHU Techno-Solutions Corporation.
 * @see SessionAttributeStore
 */
public class ControllerFqcnPrefixingSessionAttributeStore
        implements SessionAttributeStore {

    private static final String SESSTION_ATTRIBUTE_KEY_PREFIX_SEPARATOR = ".";

    /**
     * デフォルトコンストラクタです。
     */
    public ControllerFqcnPrefixingSessionAttributeStore() {}

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * @see org.springframework.web.bind.support.SessionAttributeStore#storeAttribute(org.springframework.web.context.request.WebRequest, java.lang.String, java.lang.Object)
     */
    @Override
    public void storeAttribute(WebRequest request,
            String attributeName,
            Object attributeValue) {

        request.setAttribute(getSessionAttributeName(attributeName), attributeValue, WebRequest.SCOPE_SESSION);
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * @see org.springframework.web.bind.support.SessionAttributeStore#retrieveAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
     */
    @Override
    public Object retrieveAttribute(WebRequest request,
            String attributeName) {

        return request.getAttribute(getSessionAttributeName(attributeName), WebRequest.SCOPE_SESSION);
    }

    /**
     * <p>
     * {@inheritDoc}
     * </p>
     * @see org.springframework.web.bind.support.SessionAttributeStore#cleanupAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
     */
    @Override
    public void cleanupAttribute(WebRequest request,
            String attributeName) {

        request.removeAttribute(getSessionAttributeName(attributeName), WebRequest.SCOPE_SESSION);
    }

    /**
     * <p>
     * {@link ModelAttribute}注釈によって指定されたモデル名をコントローラのFQCN用いて修飾した文字列を返却します。
     * </p>
     * @param attributeName {@link ModelAttribute}注釈によって指定されたモデル名
     * @return コントローラのFQCNをプレフィックスとした付加した修飾された文字列
     */
    protected String getSessionAttributeName(String attributeName) {
        PostBackManager manager = PostBackManager.getCurrentPostBackManager();
        return manager.getTargetControllerType().getName() + SESSTION_ATTRIBUTE_KEY_PREFIX_SEPARATOR + attributeName;
    }

}
