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

package jp.co.ctc_g.jfw.profill.util;

import java.security.Principal;

/**
 * <p>
 * このクラスは、{@link Principal} を保持します。
 * </p>
 * @see PrincipalFilter
 * @see PrincipalAwareUpdateUserProvider
 */
public final class PrincipalKeeper {

    private PrincipalKeeper() {
    }

    private static final ThreadLocal<Principal> PRINCIPAL = new ThreadLocal<Principal>();

    /**
     * 指定された {@link Principal} を現在のスレッドに関連付けて保存します。
     * @param principal 認証オブジェクト
     */
    public static void setPrincipal(Principal principal) {
        PRINCIPAL.set(principal);
    }

    /**
     * 現在のスレッドに関連付けられている {@link Principal} を返却します。
     * @return 認証オブジェクト
     */
    public static Principal getPrincipal() {
        return PRINCIPAL.get();
    }

    /**
     * スレッドローカル変数を削除します。
     */
    public static void remove() {
        PRINCIPAL.remove();
    }
}
