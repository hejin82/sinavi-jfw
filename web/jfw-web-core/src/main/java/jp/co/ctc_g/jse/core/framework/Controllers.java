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

import java.lang.reflect.Method;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.method.HandlerMethod;

/**
 * <p>
 * このクラスは、Spring MVCのコントローラに関連した定数を提供します。
 * 提供されている定数は下記の通りです。
 * <ul>
 *   <li>スコープを表現する定数</li>
 *   <li>パスの処理方法を示すプレフィックスを表現する定数</li>
 * </ul>
 * </p>
 * 
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Controllers {

    private Controllers() {}

    /**
     * requestスコープの識別子です。
     */
    public static final String SCOPE_REQUEST = "request";
    
    /**
     * flashスコープの識別子です。
     */
    public static final String SCOPE_FLASH = "flash";
    
    /**
     * sessionスコープの識別子です。
     */
    public static final String SCOPE_SESSION = "session";

    /**
     * リダイレクトする際のパスプリフィックスです。
     */
    public static final String REDIRECT = "redirect:";

    /**
     * フォワードする際のパスプリフィックスです。
     */
    public static final String FORWARD = "forward:";
    
    /**
     * 例外情報を保存するキーです。
     */
    public static final String EXCEPTION_KEY = "jp.co.ctc_g.jse.core.framework.EXCEPTION";

    /**
     * Spring MVC のコントローラメソッドであるかどうかを判定します。
     * @param handler チェック対象のメソッド
     * @return 指定されたメソッドがSpring MVC のコントローラメソッドである場合に{@code true}を返却します。
     */
    public static boolean isHandlerMethod(Object handler) {
        if (handler != null && handler instanceof HandlerMethod) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    static SessionAttributeComplete findSessionAttributeCompleteAnnotation(Method method) {
        SessionAttributeComplete complete = AnnotationUtils.getAnnotation(method, SessionAttributeComplete.class);
        if (complete != null) {
            return complete;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    static boolean isSessionAttributeComplete(Method method) {
        SessionAttributeComplete complete = AnnotationUtils.getAnnotation(method, SessionAttributeComplete.class);
        if (complete != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    static String[] findClearSessionAttributeNames(Method method) {
        SessionAttributeComplete complete = findSessionAttributeCompleteAnnotation(method);
        if (complete != null) {
            return complete.value();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    static Class<?>[] findClearSessionAttributeTypes(Method method) {
        SessionAttributeComplete complete = findSessionAttributeCompleteAnnotation(method);
        if (complete != null) {
            return complete.types();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    static String[] findSessionAttributeNames(Class<?> controllerClass) {
        SessionAttributes attrs = AnnotationUtils.findAnnotation(controllerClass, SessionAttributes.class);
        if (attrs != null) {
          return attrs.value();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    static Class<?>[] findSessionAttributeTypes(Class<?> controllerClass) {
        SessionAttributes attrs = AnnotationUtils.findAnnotation(controllerClass, SessionAttributes.class);
        if (attrs != null) {
          return attrs.types();
        }
        return null;
    }

    static boolean exceptionMatch(Class<?> clazz, Class<?> targetClazz) {
        if (clazz.equals(targetClazz)) {
            return true;
        }
        if (clazz.equals(Throwable.class)) {
            return false;
        } else {
            return exceptionMatch(clazz.getSuperclass(), targetClazz);
        }
    }
}
