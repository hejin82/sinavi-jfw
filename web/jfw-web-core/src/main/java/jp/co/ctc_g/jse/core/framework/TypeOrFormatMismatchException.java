/*
 *  Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package jp.co.ctc_g.jse.core.framework;

import jp.co.ctc_g.jse.core.util.web.beans.PropertyEditingException;

import org.springframework.beans.PropertyAccessException;
import org.springframework.validation.BindingResult;


/**
 * <p>
 * このクラスはリクエスト・パラメータのBeanへのバインディング、および、入力値検証に関するエラーを
 * {@code J-Framework} が内部的に処理する際に利用する例外クラスです。<br/>
 * </p>
 * @author ITOCHU Techno-Solutions corporation.
 */
public class TypeOrFormatMismatchException extends PropertyAccessException {

    private static final long serialVersionUID = -6556493952780712851L;

    private final PropertyAccessException e;

    /**
     * コンストラクタです。
     * @param e 例外
     * @param binding バインディング結果
     */
    public TypeOrFormatMismatchException(PropertyAccessException e, BindingResult binding) {
        super(e.getPropertyChangeEvent(), generateMessage(e, binding), null);
        this.e = e;
    }

    protected static String generateMessage(PropertyAccessException e, BindingResult binding) {
        PropertyEditingException ex = (PropertyEditingException) e.getCause();
        return ex.getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorCode() {
        return ((PropertyEditingException) e.getCause()).getCode();
    }

}
