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

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Lists;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.framework.Controllers;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、画面ID制約による画面遷移管理を実行するクラスです。
 * このクラスを開発者が直接利用することはありません。
 * フレームワーク内部で暗黙的に利用されています。
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
 *    <td>default_permit_pattern</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      デフォルトの許可パターンです（正規表現）。
 *    </td>
 *    <td>
 *      .*
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>default_reject_pattern</td>
 *    <td>java.lang.String</td>
 *    <td>
 *      デフォルトの拒否パターンです（正規表現）。
 *    </td>
 *    <td>
 *      （空文字）
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>always_check_unless_constraints_are_specified</td>
 *    <td>java.lang.Boolean</td>
 *    <td>
 *      {@link ViewIdConstraint}が指定されていないメソッドに対して、
 *      チェックを実行するかどうかを指定します。
 *    </td>
 *    <td>
 *      false
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class ViewTransitionKeeper implements Serializable {

    private static final long serialVersionUID = -3906415450711174149L;

    private static final Logger L = LoggerFactory.getLogger(ViewTransitionKeeper.class);
    private static final ResourceBundle R = InternalMessages.getBundle(ViewTransitionKeeper.class);

    private static final Collection<String > AVAILABLE_SCOPE =
            Lists.gen(Controllers.SCOPE_SESSION);

    /**
     * デフォルトの許可パターンです。
     */
    protected static final Pattern DEFAULT_ALLOW_PATTERN;

    /**
     * デフォルトの拒否パターンです。
     */
    protected static final Pattern DEFAULT_EXCEPT_PATTERN;

    /**
     * {@link ViewIdConstraint}が指定されていないメソッドに対して、
     * チェックを実行するかどうかのデフォルト値です。
     */
    protected static final boolean ALWAYS_CHECK;

    static {
        Config c = WebCoreInternals.getConfig(ViewTransitionKeeper.class);
        // デフォルトの許可パターン
        String p = c.find("default_permit_pattern");
        DEFAULT_ALLOW_PATTERN = createPattern(p);
        // デフォルトの拒否パターン
        String r = c.find("default_reject_pattern");
        DEFAULT_EXCEPT_PATTERN = createPattern(r);
        // 常にチェックするかどうか
        ALWAYS_CHECK = Boolean.valueOf(c.find("always_check_unless_constraints_are_specified"));
    }

    /**
     * 許可パターンです。
     */
    protected Pattern allowPattern = DEFAULT_ALLOW_PATTERN;

    /**
     * 拒否パターンです。
     */
    protected Pattern exceptPattern = DEFAULT_EXCEPT_PATTERN;

    /**
     * {@link ViewIdConstraint}が指定されていないメソッドに対して、
     * チェックを実行するかどうかです。
     */
    protected boolean checkRequired = ALWAYS_CHECK;

    /**
     * 検査対象の画面IDが保持されているスコープです。
     */
    protected String scope;

    /**
     * デフォルトコンストラクタです。
     */
    public ViewTransitionKeeper() {}

    /**
     * 指定されたクラスに設定されている{@link ViewIdConstraint}から、
     * このクラスのインスタンスを生成します。
     * @param annotated クラス
     */
    public ViewTransitionKeeper(Class<?> annotated) {
        boolean hasInitialized = initializeIfNeeded(annotated);
        if (L.isDebugEnabled() && hasInitialized) {
            Map<String, String> replace = Maps
                    .hash("class", annotated.getName())
                    .map("check", String.valueOf(ALWAYS_CHECK));
            L.debug(Strings.substitute(R.getString("D-VID#0001"), replace));
        }
    }

    /**
     * 指定されたメソッドに設定されている{@link ViewIdConstraint}から、
     * このクラスのインスタンスを生成します。
     * @param annotated メソッド
     */
    public ViewTransitionKeeper(Method annotated) {
        boolean hasInitialized = initializeIfNeeded(annotated);
        if (L.isDebugEnabled() && hasInitialized) {
            Map<String, String> replace = Maps
                    .hash("class", annotated.getDeclaringClass().getName())
                    .map("method", annotated.getName())
                    .map("check", String.valueOf(ALWAYS_CHECK));
            L.debug(Strings.substitute(R.getString("D-VID#0001"), replace));
        }
    }

    /**
     * 指定された注釈付けされた要素を利用してこのインスタンスを初期化します。
     * もし{@link ViewIdConstraint}が付加されていない場合、このメソッドは直ちに処理を終了します。
     * @param annotated {@link ViewIdConstraint}が付加されている（と想定されている）要素
     * @return 初期化した場合には<code>true</code>
     */
    protected boolean initializeIfNeeded(AnnotatedElement annotated) {
        if (!annotated.isAnnotationPresent(ViewIdConstraint.class)) return false;
        ViewIdConstraint c = annotated.getAnnotation(ViewIdConstraint.class);
        String specified = null;
        // 許可パターン
        specified = c.allow();
        this.allowPattern = "".equals(specified) ?
                DEFAULT_ALLOW_PATTERN : createPattern(specified) ;
        // 拒否パターン
        specified = c.except();
        this.exceptPattern = "".equals(specified) ?
                DEFAULT_EXCEPT_PATTERN : createPattern(specified);
        this.checkRequired = ALWAYS_CHECK || (this.allowPattern != null || this.exceptPattern != null);
        scope = AVAILABLE_SCOPE.contains(c.scope()) ? c.scope() : Controllers.SCOPE_SESSION;
        return true;
    }

    private static Pattern createPattern(String regex) {
        // 空文字が指定されていた場合、制約検証対象としないためnull返却
        if (regex == null || "".equals(regex)) return null;
        Pattern p = null;
        PatternSyntaxException pse = null;
        try {
            p = Pattern.compile(pack(regex));
        } catch (PatternSyntaxException e) {
            pse = e;
        }
        if (pse != null || p == null) {
            throw new InternalException(
                    ViewTransitionKeeper.class,
                    "E-VID#0002",
                    Maps.hash("regex", regex),
                    pse);
        }
        return p;
    }

    private static String pack(String regex) {
        return Strings.join("^", regex, "$");
    }

    /**
     * このインスタンスに設定されている画面ID許可/拒否パターンを利用して、
     * 指定された画面IDからの遷移が有効かどうかを判定します。
     * @param vid 遷移元画面ID
     * @throws InvalidViewTransitionException 画面遷移が不正だった場合
     */
    public void check(ViewId vid) throws InvalidViewTransitionException {
        if (checkRequired) {
            if (exceptPattern == null && allowPattern == null) {
                throw new InternalException(ViewTransitionKeeper.class, "E-VID#0003");
            }
            if (allowPattern != null && !permit(vid)) {
                throw new InvalidViewTransitionException();
            }
            if (exceptPattern != null && reject(vid)) {
                throw new InvalidViewTransitionException();
            }
        }
    }

    /**
     * 指定された画面IDが許可れさているかどうかを返却します。
     * @param vid 画面ID
     * @return 許可されている場合は<code>true</code>
     */
    protected boolean permit(ViewId vid) {
        return allowPattern.matcher(vid.getId()).matches();
    }

    /**
     * 指定された画面IDが拒否れさているかどうかを返却します。
     * @param vid 画面ID
     * @return 拒否されている場合は<code>true</code>
     */
    protected boolean reject(ViewId vid) {
        return exceptPattern.matcher(vid.getId()).matches();
    }

    /**
     * このインスタンスによる画面IDのチェックが必要かどうかを確認します。
     * @return このインスタンスによる画面IDチェックが必要な場合には<code>true</code>
     */
    public boolean isCheckRequired() {
        return checkRequired;
    }
    
    /**
     * このインスタンスにより検証される画面IDが保持されているスコープを返却します。
     * @return スコープ
     */
    public String getScope() {
        return scope;
    }
}
