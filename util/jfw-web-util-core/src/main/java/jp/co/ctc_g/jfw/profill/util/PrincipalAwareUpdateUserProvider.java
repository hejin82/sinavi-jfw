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

import jp.co.ctc_g.jfw.profill.FillingProvider;
import jp.co.ctc_g.jfw.profill.MatchedProperty;
import jp.co.ctc_g.jfw.profill.ProvideFillingFor;

/**
 * <p>
 * このクラスは、更新者情報を Java ビーンに設定するためのプロバイダです。
 * たとえば Java ビーンが RDBMS のテーブルを表現している場合、
 * 更新者カラムに対応する更新者プロパティがあるでしょう。
 * このプロバイダはそのプロパティに自動的に最適な値を格納します。
 * その際、プロパティは {@link UpdateUser} アノテーションで修飾する必要があります。
 * <pre class="brush:java">
 * &#64;UpdateUser
 * public void setUpdateUserId(String value) {
 *     updateUserId = value;
 * }
 * </pre>
 * なお、このプロバイダを利用するには、{@link PrincipalFilter} を設定する必要があります。
 * </p>
 * @see UpdateUser
 * @see PrincipalKeeper
 * @see PrincipalFilter
 */
@ProvideFillingFor(annotation = UpdateUser.class)
public class PrincipalAwareUpdateUserProvider implements FillingProvider {

    private String defaultUserName = "";

    /**
     * デフォルトコンストラクタです。
     */
    public PrincipalAwareUpdateUserProvider() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Object provide(MatchedProperty property, Object bean) {
        Principal principal = PrincipalKeeper.getPrincipal();
        if (principal != null) {
            return principal.getName();
        } else {
            return defaultUserName;
        }
    }

    /**
     * {@link Principal} が設定されていない場合にプロパティに設定する文字列を返却します。
     * @return {@link Principal} が設定されていない場合にプロパティに設定する文字列
     */
    public String getDefaultUserName() {
        return defaultUserName;
    }

    /**
     * {@link Principal} が設定されていない場合にプロパティに設定する文字列を設定します。
     * @param defaultUserName {@link Principal} が設定されていない場合にプロパティに設定する文字列
     */
    public void setDefaultUserName(String defaultUserName) {
        this.defaultUserName = defaultUserName;
    }
}
