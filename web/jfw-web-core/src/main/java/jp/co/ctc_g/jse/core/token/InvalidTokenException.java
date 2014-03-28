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

package jp.co.ctc_g.jse.core.token;

import java.util.Map;

import jp.co.ctc_g.jfw.core.exception.ApplicationRecoverableException;
import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

/**
 * <p>
 * このクラスは、無効な(不正な)トランザクショントークンが検出された場合に発生する例外を表現します。
 *  * J-Framework2.x系における{@link AsynchronousTokenException}と同じ内容を意味しますが、
 * このクラスは{@link RecoverableException}を継承しています。
 * </p>
 * <h4>クラスコンフィグオーバーライド</h4>
 * <table class="property_file_override_info">
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>型</th>
 *    <th>効果</th>
 *    <th>デフォルト</th>
 *   </tr><tr>
 *   </tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>code</td>
 *    <td>{@link String}</td>
 *    <td>デフォルトのエラーコードです。</td>
 *    <td>W-EXT#0001</td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class InvalidTokenException extends ApplicationRecoverableException {

    private static final long serialVersionUID = -7568464013283573004L;

    private static final String DEFAULT_CODE = "W-EXT#0001";

    protected static final String CODE;

    static {
        Config c = WebCoreInternals.getConfig(InvalidTokenException.class);
        String code = c.find("code");
        if (Strings.isEmpty(code)) {
            CODE = DEFAULT_CODE;
        } else {
            CODE = code;
        }
    }

    /**
     *
     */
    public InvalidTokenException() {
        super(CODE);
    }

    /**
     * このクラスのインスタンスを生成します。
     * @param code エラーコード
     * @param args メッセージ置換パラメータ
     */
    public InvalidTokenException(String code, Map<String, String> args) {
        super(code, args);
    }

    /**
     * このクラスのインスタンスを生成します。
     * @param code エラーコード
     * @param cause この例外発生の原因
     * @param args メッセージ置換パラメータ
     */
    public InvalidTokenException(String code, Throwable cause, Map<String, String> args) {
        super(code, cause, args);
    }

    /**
     * このクラスのインスタンスを生成します。
     * @param code エラーコード
     * @param cause この例外発生の原因
     */
    public InvalidTokenException(String code, Throwable cause) {
        super(code, cause);
    }

    /**
     * このクラスのインスタンスを生成します。
     * @param code エラーコード
     */
    public InvalidTokenException(String code) {
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
