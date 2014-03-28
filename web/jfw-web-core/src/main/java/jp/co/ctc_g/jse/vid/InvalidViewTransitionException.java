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

package jp.co.ctc_g.jse.vid;

import java.util.Map;

import jp.co.ctc_g.jfw.core.exception.ApplicationRecoverableException;
import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

/**
 * <p>
 * この例外は、不正な画面遷移が検出されたことを表現します。
 * </p>
 * <h4>クラスコンフィグオーバライド</h4>
 * <p>
 * 以下の{@link Config クラスコンフィグオーバライド}用のキーが公開されています。
 * </p>
 * <table class="property_file_override_info">
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>型</th>
 *    <th>効果</th>
 *    <th>デフォルト</th>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>code</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      この例外のコードです。これを変更することにより、
 *      例外コードと附随するメッセージを変更することができます。
 *    </td>
 *    <td>
 *      W-VID#0003
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see ViewIdConstraint
 * @see ViewId
 * @see ViewTransitionKeeper
 * @see ViewIdDefinitionTag
 */
public class InvalidViewTransitionException extends ApplicationRecoverableException {

    private static final long serialVersionUID = 1615199635947087158L;

    private static final String DEFAULT_CODE = "W-VID#0003";

    protected static final String CODE;

    static {
        Config c = WebCoreInternals.getConfig(InvalidViewTransitionException.class);
        String code = c.find("code");
        if (Strings.isEmpty(code)) {
            CODE = DEFAULT_CODE;
        } else {
            CODE = code;
        }
    }

    /**
     * 指定された例外コード、メッセージ置換オブジェクトを利用して、
     * このクラスのインスタンスを生成します。
     */
    public InvalidViewTransitionException() {
        super(CODE);
    }

    /**
     * 指定された例外コード、メッセージ置換オブジェクトを利用して、
     * このクラスのインスタンスを生成します。
     * @param code 例外コード
     * @param args メッセージ置換オブジェクト
     */
    public InvalidViewTransitionException(String code, Map<String, String> args) {
        super(code, args);
    }

    /**
     * 指定された例外コード、原因例外、メッセージ置換オブジェクトを利用して、
     * このクラスのインスタンスを生成します。
     * @param code 例外コード
     * @param cause 原因例外
     * @param args メッセージ置換オブジェクト
     */
    public InvalidViewTransitionException(String code, Throwable cause, Map<String, String> args) {
        super(code, cause, args);
    }

    /**
     * 指定された例外コード、原因例外を利用して、
     * このクラスのインスタンスを生成します。
     * @param code 例外コード
     * @param cause 原因例外
     */
    public InvalidViewTransitionException(String code, Throwable cause) {
        super(code, cause);
    }

    /**
     * 指定された例外コードを利用して、
     * このクラスのインスタンスを生成します。
     * @param code 例外コード
     */
    public InvalidViewTransitionException(String code) {
        super(code);
    }

    /**
     * デフォルトエラーコードの場合、メッセージがJ-Framework内部に保存されているため、
     * このメソッドを拡張してエラーコードがデフォルトであるかどうか判断しています。
     * {@inheritDoc}
     */
    @Override
    protected String generateMessage(String code, Map<String, ?> args) {
        if (DEFAULT_CODE.equals(getCode())) {
            return InternalMessages.getBundle(this.getClass()).getString(DEFAULT_CODE);
        } else {
            return super.generateMessage(code, args);
        }
    }
}
