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

package jp.co.ctc_g.jfw.core.util.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.CoreInternals;
import jp.co.ctc_g.jfw.core.util.Strings;

/**
 * <p>
 * このクラスは、SQLに関連するユーティリティ機能を提供します。
 * このクラスはJ-Framework固有のJDBCアクセスライブラリに依存しません。
 * </p>
 * <h4>エスケープ機能</h4>
 * <p>
 * SQL問合を実行する場合、Like句などを含むいくつかの条件節においては、
 * クエリをエスケープする必要があります。
 * このクラスは、エスケープのための機能を提供しています。
 * デフォルトエスケープ文字はバックスラッシュで、
 * エスケープ対象文字は、
 * <code>%</code>,
 * <code>_</code>,
 * <code>\</code>,
 * <code>％</code>,
 * <code>＿</code>,
 * <code>￥</code>
 * です(カンマは含みません)。
 * </p>
 * <pre>
 * String needToEscape = "100% orange juice";
 * String escaped = SQLs.escape(needToEscape); // 100\% orange juice
 * </pre>
 * <p>
 * また、ワイルドカード文字を自動で付加するには、
 * </p>
 * <pre>
 * String query = "100% orange juice";
 * String sw = SQLs.likeStartsWith(query); // 100\% orange juice%
 * String ew = SQLs.likeEndsWith(query); // %100\% orange juice
 * String co = SQLs.likeContains(query); // %100\% orange juice%
 * </pre>
 * <p>
 * のようにします。
 * JDBCエスケープ構文を利用したい場合、
 * このクラスが提供している公開静的フィールドを利用すると便利です。
 * </p>
 * <pre>
 * SQLs.ESCAPE_CLAUSE
 * </pre>
 * <p>
 * この定数は(デフォルトでは)、{escape '\'}を意味します。
 * </p>
 * <h4>クラスコンフィグオーバライド</h4>
 * <p>
 * このクラスが提供しているクラスコンフィグオーバライド項目は以下の通りです。
 * </p>
 * <table class="property_file_override_info">
 *  <thead>
 *   <tr>
 *    <th>キー</th>
 *    <th>型</th>
 *    <th>効果</th>
 *    <th>デフォルト</th>
 *   <tr>
 *  </thead>
 *  <tbody>
 *   <tr>
 *    <td>escape_meta_charactor</td>
 *    <td>{@link java.lang.String}
 *    <td>
 *     エスケープに利用する文字です。
 *    </td>
 *    <td>
 *     バックスラッシュ(<code>\</code>)
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>escape_target</td>
 *    <td>{@link java.lang.String}
 *    <td>
 *     エスケープに対象文字です。
 *    </td>
 *    <td>
 *     %％_＿
 *    </td>
 *   </tr>
 *   <tr>
 *    <td>wild_card</td>
 *    <td>{@link java.lang.String}
 *    <td>
 *     ワイルドカード文字です。
 *    </td>
 *    <td>
 *     %
 *    </td>
 *   </tr>
 *  </tbody>
 * </table>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class SQLs {

    private SQLs(){
    }

    private enum Deco {

        START_WITH {
            String decorate(String decoratable) {
                return decoratable + SQLs.WILD_CARD;
            }
        },

        END_WITH {
            String decorate(String decoratable) {
                return SQLs.WILD_CARD + decoratable;
            }
        },

        CONTAIN  {
            String decorate(String decoratable) {
                return SQLs.WILD_CARD + decoratable + SQLs.WILD_CARD;
            }
        };

        abstract String decorate(String decoratable);
    }

    /**
     * エスケープに利用する文字です。
     * デフォルトではバックスラッシュです。
     */
    public static final String ESCAPE_META_CHARACTOR;

    /**
     * ワイルドカード文字です。
     * デフォルトでは%です。
     */
    public static final String WILD_CARD;

    /**
     * エスケープ対象文字です。
     * デフォルトでは%％_＿です。
     */
    public static final String ESCAPE_TARGET;

    /**
     * JDBCエスケープ構文です。
     * デフォルトでは、{escape '\'}です。
     */
    public static final String ESCAPE_CLAUSE;

    private static final Pattern TARGET_PATTERN;

    static {
        Config c = CoreInternals.getConfig(SQLs.class);
        ESCAPE_META_CHARACTOR = c.find("escape_meta_charactor");
        ESCAPE_TARGET = c.find("escape_target");
        WILD_CARD = c.find("wild_card");
        ESCAPE_CLAUSE = "{escape \'" +  ESCAPE_META_CHARACTOR + "\'}";
        TARGET_PATTERN = Pattern.compile("([" + ESCAPE_TARGET + "])");
    }

    /**
     * 指定された文字列をエスケープします。
     * エスケープとは、{@link #ESCAPE_TARGET}の文字に対して、
     * {@link #ESCAPE_META_CHARACTOR}を前置することです。
     * 指定された文字列が{@link Strings#isBlank(CharSequence) 空}である場合、
     * このメソッドは、引数に指定された文字列を返却します。
     * @param toBeEscaped エスケープするべき文字列
     * @return エスケープ済み文字列
     */
    public static String escape(String toBeEscaped) {
        if (Strings.isEmpty(toBeEscaped)) return toBeEscaped;
        toBeEscaped = toBeEscaped.replace(
                ESCAPE_META_CHARACTOR,
                ESCAPE_META_CHARACTOR + ESCAPE_META_CHARACTOR);
        Matcher m = TARGET_PATTERN.matcher(toBeEscaped);
        StringBuilder result = new StringBuilder();
        int previousStopPosition = 0;
        while (m.find()) {
            String group = m.group(1);
            result.append(toBeEscaped.substring(previousStopPosition, m.start()));
            result.append(ESCAPE_META_CHARACTOR);
            result.append(group);
            previousStopPosition = m.end();
        }
        if (previousStopPosition > 0) {
            result.append(toBeEscaped.subSequence(previousStopPosition, toBeEscaped.length()));
        } else {
            result.append(toBeEscaped);
        }
        return result.toString();
    }

    private static String escape(String toBeEscaped, Deco deco) {
        if (Strings.isEmpty(toBeEscaped)) return toBeEscaped;
        String escaped = escape(toBeEscaped);
        if (deco == null) return escaped;
        String decorated = deco.decorate(escaped);
        return decorated;
    }

    /**
     * 指定された文字列をエスケープした後、前方一致で検索可能なように、
     * 文字列の末尾にワイルドカード文字を付加します。
     * @param likeClauseValue 前方一致させたい文字列
     * @return 前方一致クエリに利用する文字列
     */
    public static String likeStartsWith(String likeClauseValue) {
        return escape(likeClauseValue, Deco.START_WITH);
    }

    /**
     * 指定された文字列をエスケープした後、後方一致で検索可能なように、
     * 文字列の先頭にワイルドカード文字を付加します。
     * @param likeClauseValue 後方一致させたい文字列
     * @return 後方一致クエリに利用する文字列
     */
    public static String likeEndsWith(String likeClauseValue) {
        return escape(likeClauseValue, Deco.END_WITH);
    }

    /**
     * 指定された文字列をエスケープした後、内容一致で検索可能なように、
     * 文字列の先頭と末尾にワイルドカード文字を付加します。
     * @param likeClauseValue 内容一致させたい文字列
     * @return 内容一致クエリに利用する文字列
     */
    public static String likeContains(String likeClauseValue) {
        return escape(likeClauseValue, Deco.CONTAIN);
    }
}
