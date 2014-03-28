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

package jp.co.ctc_g.jfw.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * このクラスは、文字列操作に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Strings {

    private Strings() {
    }

    private static final String BLANK_REGEXP = "\\s　";

    private static final Pattern BLANK_ALL_PATTERN = Pattern.compile("^[" + BLANK_REGEXP + "]*$");

    private static final Pattern BLANK_PATTERN = Pattern.compile("[" + BLANK_REGEXP + "]");

    private static final Pattern HEAD_BLANK_PATTERN = Pattern.compile("^[" + BLANK_REGEXP + "]+");

    private static final Pattern TAIL_BLANK_PATTERN = Pattern.compile("[" + BLANK_REGEXP + "]+$");

    private static final String PLACE_HOLDER_VALUE_REGEXP = "([a-zA-Z0-9\\.]+)(\\%[0-9a-zA-Z\\+\\-#, \\(\\)\\.\\$]+)?";

    private static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile("(?<!\\\\)\\$\\{(" + PLACE_HOLDER_VALUE_REGEXP + ")(?<!\\\\)\\}");

    private static final Pattern SNAKE_CASE_WORD = Pattern.compile("(^[a-z0-9]+|[A-Z][a-z0-9]+|[A-Z]+(?![a-z0-9]))");

    /**
     * 指定されたテンプレート文字列を、指定されたパラメータで置換します。
     * <pre class="brush:java">
     * String template = "${animal} Support APIs";
     * Map<String, String> param = Maps.hash("animal", "Tiger");
     * String result = Strings.substitute(template, param);
     * assert "Tiger Support APIs".equals(result);
     * </pre>
     * プレースホルダ（置換対象となる文字列）は、<code>${</code>と<code>}</code>で囲みます。
     * もし、この文字をテンプレート内で利用したいのであれば、バックスラッシュでエスケープできます。
     * また、この機能では、{@link java.util.Formatter}を利用したフォーマットも利用できます。
     * 例えば、数値の書式化は、
     * <pre class="brush:java">
     * String template = "10.3: ${number%010.3f}";
     * Map<String, ?> replace =  Maps.hash("number", 11.15f);
     * String result = Strings.substitute(seed, replace);
     * assert "10.3: 000011.150".equals(result);
     * </pre>
     * となります。
     * 本来の{@link java.util.Formatter}の書式化指定子の利用方法は、<code>引数位置%書式化指定子</code>ですが、
     * このメソッドでは、<code>プレースホルダの名前%書式化指定子</code>となります。
     * 書式化に利用する書式化指定子は、{@link java.util.Formatter}に準じます。
     * ですので、日付の書式化もできます。
     * <pre class="brush:java">
     * String template = "${time%tY}/${time%tm}/${time%td}";
     * Map<String, ?> param = Maps.hash("time", Dates.makeFrom(2008, 11, 15));
     * String result = Strings.substitute()
     * assert "2008/11/15".equals(result);
     * </pre>
     * @param string テンプレート文字列
     * @param replace 文字列を置換するパラメータ
     * @return パラメータにより一部が置換されたテンプレート文字列
     */
    public static String substitute(String string, Map<String, ? extends Object> replace) {

        return substitute(string, replace, null);
    }

    /**
     * 指定されたテンプレート文字列を、指定されたパラメータで置換します。
     * 詳細は、{@link #substitute(String, Map)}を参照してください。
     * このメソッドは、ロケールを受け取ることで書式化の際のロケーションを操作することができるようになっています。
     * @param string テンプレート文字列
     * @param replace 文字列を置換するパラメータ
     * @param locale 書式化の際に参照するロケーション
     * @return パラメータにより一部が置換されたテンプレート文字列
     */
    public static String substitute(String string, Map<String, ? extends Object> replace, Locale locale) {

        if (Strings.isEmpty(string))
            return string;
        if (replace == null || replace.isEmpty())
            return string;
        Matcher matcher = PLACE_HOLDER_PATTERN.matcher(string);
        StringBuilder result = new StringBuilder();
        int previousEndPos = 0;
        while (matcher.find()) {
            //String group = matcher.group(1);
            String name = matcher.group(2);
            String format = matcher.group(3);
            Object value = replace.get(name);
            if (!isEmpty(format)) {
                value = Formats.format(format, locale, value);
            }
            result.append(clearEscapedPlaceHolder(string.substring(previousEndPos, matcher.start())));
            result.append(value == null ? "" : value);
            previousEndPos = matcher.end();
        }
        if (previousEndPos > 0) {
            result.append(clearEscapedPlaceHolder(string.substring(previousEndPos, string.length())));
        } else {
            result.append(clearEscapedPlaceHolder(string));
        }
        return result.toString();
    }

    /**
     * {@link #substitute(String, Map)}で利用可能な形式のプレースホルダ文字列を探します。
     * <pre class="brush:java">
     * Strings.findPlaceHolder("${placeholder}"); // placeholder
     * Strings.findPlaceHolder("${FOO}"); // FOO
     * </pre>
     * このメソッドは{@link #substitute(String, Map)}のようなプレースホルダを置換するAPIを、
     * このクラスが一元的に管理できるようにする目的で公開されています。
     * @param string プレースホルダ形式の文字列
     * @return プレースホルダ文字列
     */
    public static String findPlaceHolder(String string) {

        if (Strings.isEmpty(string))
            return string;
        Matcher m = PLACE_HOLDER_PATTERN.matcher(string);
        return m.find() ? m.group(2) : null;
    }

    /**
     * プレースホルダ形式の文字列がエスケープされている可能性がある場合、
     * そのエスケープを取り除きます。
     * このメソッドは{@link #substitute(String, Map)}のようなプレースホルダを置換するAPIを、
     * このクラスが一元的に管理できるようにする目的で公開されています。
     * @param dirty エスケープされている可能性のある文字列
     * @return エスケープが取り除かれた文字列
     */
    public static String clearEscapedPlaceHolder(String dirty) {

        if (Strings.isEmpty(dirty))
            return dirty;
        return dirty.replace("\\${", "${").replace("\\}", "}");
    }

    /**
     * 第1引数の文字列が{@link Strings#isEmpty(CharSequence) 空文字}の場合第2引数を返却し、
     * そうでない場合には第1引数を返却します。
     * @param <T> 入出力型
     * @param returnIfNotEmpty 空文字出ない場合に返却される文字列
     * @param alternative 空文字の場合に返却される文字列
     * @return 第1引数の文字列が{@link Strings#isEmpty(CharSequence) 空文字}の場合第2引数、空文字でない場合は第1引数
     * @see Args#proper(Object, Object)
     */
    public static <T extends CharSequence> T proper(T returnIfNotEmpty, T alternative) {

        if (Strings.isEmpty(returnIfNotEmpty)) {
            return alternative;
        } else {
            return returnIfNotEmpty;
        }
    }

    /**
     * 引数の文字列を全て大文字のスネーク記法（単語区切りがアンダースコア）とみなして、
     * それをラクダ記法（単語区切りが大文字）に変換します。
     * 例えば、スネーク記法の<code>TIGER_SUPPORT_API</code>は、
     * ラクダ記法の<code>tigerSupportApi</code>に変換されます。
     * @param snake 全て大文字のスネーク記法の文字列
     * @return ラクダ記法
     */
    public static String toCamel(String snake) {

        if (Strings.isBlank(snake))
            return "";
        String[] fragments = snake.split("_");
        StringBuilder cameled = new StringBuilder();
        for (int i = 0; i < fragments.length; i++) {
            cameled.append(i == 0 ? fragments[i].toLowerCase() : Beans.capitalize(fragments[i].toLowerCase()));
        }
        return cameled.toString();
    }

    /**
     * 引数の文字列をラクダ記法（単語区切りが大文字）とみなして、
     * それを全て大文字のスネーク記法（単語区切りがアンダースコア）に変換します。
     * 例えば、ラクダ記法の<code>tigerSupportApi</code>は、
     * スネーク記法の<code>TIGER_SUPPORT_API</code>に変換されます。
     * @param camel ラクダ記法の文字列
     * @return スネーク記法（全て大文字）
     */
    public static String toSnake(String camel) {
        if (Strings.isBlank(camel))
            return "";

        ArrayList<String> words = new ArrayList<String>();

        Matcher matcher = SNAKE_CASE_WORD.matcher(camel);
        while(matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            String word = camel.substring(result.start(), result.end());

            words.add(word.toUpperCase());
        }
        return Strings.joinBy("_", words);
    }

    /**
     * 指定された配列を結合して文字列にします。
     * @param <T> 結合するオブジェクトの型
     * @param joinees 結合される配列
     * @return 結合後の文字列
     */
    public static <T> String join(T... joinees) {

        return joinBy("", joinees);
    }

    /**
     * 指定された文字列をセパレータとして、指定された配列を結合して文字列にします。
     * @param <T> 結合するオブジェクトの型
     * @param separator セパレータ
     * @param joinees 結合される配列
     * @return 結合後の文字列
     */
    public static <T> String joinBy(String separator, T... joinees) {

        if (joinees == null || joinees.length == 0)
            return "";
        separator = Args.proper(separator, "");
        if (joinees.length == 1) {
            return joinees[0].toString();
        } else {
            StringBuilder joined = new StringBuilder();
            joined.append(joinees[0]);
            for (int i = 1; i < joinees.length; i++) {
                joined.append(joinees[i - 1].equals("") ? "" : separator).append(joinees[i]);
            }
            return joined.toString();
        }
    }

    /**
     * 指定されたリストを結合して文字列にします。
     * @param <T> 結合するオブジェクトの型
     * @param joinees 結合されるリスト
     * @return 結合後の文字列
     */
    public static <T> String join(List<T> joinees) {

        return joinBy("", joinees);
    }

    /**
     * 指定された文字列をセパレータとして、指定されたリストを結合して文字列にします。
     * @param <T> 結合するオブジェクトの型
     * @param separator セパレータ
     * @param joinees 結合されるリスト
     * @return 結合後の文字列
     */
    public static <T> String joinBy(String separator, List<T> joinees) {

        if (joinees == null || joinees.size() == 0)
            return "";
        separator = separator == null ? "" : separator;
        if (joinees.size() == 1) {
            return joinees.get(0).toString();
        } else {
            StringBuilder joined = new StringBuilder();
            joined.append(joinees.get(0));
            for (int i = 1; i < joinees.size(); i++) {
                joined.append(separator).append(joinees.get(i));
            }
            return joined.toString();
        }
    }

    /**
     * 文字列がブランク文字で構成されているかどうかを判定します。
     * ブランク文字とは、\t, \n, \x, 0B, \f, \r, 半角スペース, 全角スペースのことを指します。
     * @param suspect 検査対象
     * @return ブランク文字で構成されている場合はtrue
     */
    public static boolean isBlank(CharSequence suspect) {

        return suspect == null || suspect.length() == 0 || BLANK_ALL_PATTERN.matcher(suspect).matches();
    }

    /**
     * 文字列がブランク文字を含んでいるかどうかを判定します。
     * ブランク文字とは、\t, \n, \x, 0B, \f, \r, 半角スペース, 全角スペースのことを指します。
     * @param suspect 検査対象
     * @return ブランク文字を含んでいる場合はtrue
     */
    public static boolean containBlank(CharSequence suspect) {

        return suspect == null || suspect.length() == 0 || BLANK_PATTERN.matcher(suspect).find();
    }

    /**
     * 文字列が含んでいるブランク文字を取り除きます。
     * ブランク文字とは、\t, \n, \x, 0B, \f, \r, 半角スペース, 全角スペースのことを指します。
     * @param target 変換対象
     * @return ブランク文字が取り除かれた文字列
     */
    public static String removeBlanks(CharSequence target) {

        if (target == null || target.length() == 0)
            return "";
        return BLANK_PATTERN.matcher(target).replaceAll("");
    }

    /**
     * 指定された文字列が空文字かどうかを検査します。
     * 空文字とは、<code>null</code>または長さが<code>0</code>の文字列のことです。
     * @param suspect 検査対象
     * @return 空文字の場合にtrue
     */
    public static boolean isEmpty(CharSequence suspect) {

        return isEmpty(suspect, false);
    }

    /**
     * 指定された文字列が空文字かどうかを検査します。
     * 空文字とは、<code>null</code>または長さが<code>0</code>の文字列のことです。
     * removeBlanks引数に<code>true</code>を渡すと、
     * 文字列中のブランク文字を全て取り除いた上で空文字かどうかを判定します。
     * @param suspect 検査対象
     * @param removeBlanks 検査前にブランク文字を取り除くかどうか
     * @return 空文字の場合にtrue
     */
    public static boolean isEmpty(CharSequence suspect, boolean removeBlanks) {

        if (removeBlanks) {
            suspect = removeBlanks(suspect);
        }
        return suspect == null || suspect.length() == 0;
    }

    /**
     * 指定された文字列を反転させます。
     * @param reversee 反転させる文字列
     * @return 反転された文字列
     */
    public static String reverse(String reversee) {

        if (isEmpty(reversee))
            return reversee;
        return new StringBuilder(reversee).reverse().toString();
    }

    /**
     * 先頭と末尾からブランク文字列を取り除きます。
     * ブランク文字とは、\t, \n, \x, 0B, \f, \r, 半角スペース, 全角スペースのことを指します。
     * これは、ユーザ入力値を検証する際の事前準備として有用です。
     * @param target 対象文字列
     * @return 変換結果
     */
    public static String trim(String target) {

        if (isEmpty(target))
            return target;
        String result = HEAD_BLANK_PATTERN.matcher(target).replaceAll("");
        result = TAIL_BLANK_PATTERN.matcher(result).replaceAll("");
        return result;
    }

    /**
     * 指定された正規表現で対象を分割します。
     * 例えば、セミコロン（:）で分割する場合、
     * <pre class="brush:java">
     * // s は {"a", "b", "c", "d"} と等しい
     * String[] s = Strings.split(":", "a:b:c:d");
     * </pre>
     * このメソッドは、正規表現のエスケープを有効にする点で優れています。
     * 例えば、セミコロン（:）で分割する場合、
     * <pre class="brush:java">
     * // s は {"a", "b", "c:d"} と等しい
     * String[] s = Strings.split(":", "a:b:c\:d");
     * </pre>
     * のように、バックスラッシュでのエスケープが有効になります。
     * さらに、分割完了後にはそのエスケープが取り除かれます。
     * @param regex 正規表現
     * @param target 分割対象文字列
     * @return 分割後の文字列
     */
    public static String[] split(String regex, String target) {

        String r = "(?<!\\\\)" + regex;
        String[] splitted = target.split(r);
        if (Arrays.isEmpty(splitted))
            return splitted;
        for (int i = 0; i < splitted.length; i++) {
            splitted[i] = splitted[i].trim().replaceAll("\\\\" + regex, regex);
        }
        return splitted;
    }

    /**
     * 二つのオブジェクトを比較します.
     * @param source 比較元の文字列
     * @param destination 比較対象の文字列
     * @return equalsIgnoreCase()メソッドにより比較した結果。 ただしsource又はdestinationの何れかがnullの場合はfalse、
     *         双方がnullの場合はtrueを返します。
     */
    public static boolean equalsIgnoreCase(String source, String destination) {

        return (source != null && destination != null) ? source.equalsIgnoreCase(destination) : (source == null && destination == null);
    }

    /**
     * 指定した文字列が、指定された接頭辞で始まるかどうかを判定します. <br>
     * このメソッドはstartsWith(text, prefix, true)を呼び出すのと同じです。
     * @param text 対象文字列
     * @param prefix 接頭辞
     * @return 指定した接頭辞で始まる場合はtrue、 そうでない場合はfalseを返します。
     *         また、textまたはprefixがnull又は空文字列を指定した場合は必ずfalseを返します。
     */
    public static boolean startsWith(String text, String prefix) {

        return Strings.startsWith(text, prefix, true);
    }

    /**
     * 指定した文字列が、指定された接頭辞で始まるかどうかを判定します.
     * @param text 対象文字列
     * @param prefix 接頭辞
     * @param textCompare 大文字、小文字を区別しない場合はtrue、 そうでない場合はfalseを指定します。
     * @return 指定した接頭辞で始まる場合はtrue、 そうでない場合はfalseを返します。
     *         また、textまたはprefixがnull又は空文字列を指定した場合は必ずfalseを返します。
     */
    public static boolean startsWith(String text, String prefix, boolean textCompare) {

        if (!Strings.isEmpty(text) && !Strings.isEmpty(prefix)) {
            String source = text.substring(0, prefix.length());
            if (source != null) {
                if (textCompare) {
                    return source.equalsIgnoreCase(prefix);
                } else {
                    return source.equals(prefix);
                }
            }
        }
        return false;
    }

    /**
     * 指定した文字列が、指定された接尾辞で終わるかどうかを判定します. <br>
     * このメソッドはendsWith(text, prefix, true)を呼び出すのと同じです。
     * @param text 対象文字列
     * @param prefix 接頭辞
     * @return 指定した接尾辞で始まる場合はtrue、 そうでない場合はfalseを返します。
     *         また、textまたはprefixがnull又は空文字列を指定した場合は必ずfalseを返します。
     */
    public static boolean endsWith(String text, String prefix) {

        return Strings.endsWith(text, prefix, true);
    }

    /**
     * 指定した文字列が、指定された接尾辞で終わるかどうかを判定します.
     * @param text 対象文字列
     * @param prefix 接頭辞
     * @param textCompare 大文字、小文字を区別しない場合はtrue、 そうでない場合はfalseを指定します。
     * @return 指定した接尾辞で始まる場合はtrue、 そうでない場合はfalseを返します。
     *         また、textまたはprefixがnull又は空文字列を指定した場合は必ずfalseを返します。
     */
    public static boolean endsWith(String text, String prefix, boolean textCompare) {

        if (!Strings.isEmpty(text) && !Strings.isEmpty(prefix)) {
            String source = text.substring(text.length() - prefix.length(), text.length());
            if (source != null) {
                if (textCompare) {
                    return source.equalsIgnoreCase(prefix);
                } else {
                    return source.equals(prefix);
                }
            }
        }
        return false;
    }

    /**
     * 文字列中の&"<>を実体参照へエンコードした文字列に変換します。
     *
     * @param html エスケープするHTML
     *
     * @return エスケープされた文字列
     */
    public static String escapeHTML(CharSequence html) {

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);
            switch (c) {
                case '&':
                    buff.append("&amp;");
                    break;
                case '"':
                    buff.append("&quot;");
                    break;
                case '<':
                    buff.append("&lt;");
                    break;
                case '>':
                    buff.append("&gt;");
                    break;
                default:
                    buff.append(c);
                    break;
            }
        }

        return buff.toString();
    }

    /**
     * 正規表現に指定する文字列をエスケープした文字列に変換します。
     * <ul>
     * <li>"."→"\\."</li>
     * <li>"?"→"\\?"</li>
     * <li>"*"→"\\*"</li>
     * <li>"+"→"\\+"</li>
     * <li>"|"→"\\|"</li>
     * <li>"["→"\\["</li>
     * <li>"]"→"\\]"</li>
     * <li>"("→"\\("</li>
     * <li>")"→"\\)"</li>
     * <li>"{"→"\\{"</li>
     * <li>"}"→"\\}"</li>
     * </ul>
     * @param text 正規表現のパターン
     * @return エスケープされた文字列
     */
    public static String escapeRegex(CharSequence text) {

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '.':
                    buff.append("\\.");
                    break;
                case '?':
                    buff.append("\\?");
                    break;
                case '*':
                    buff.append("\\*");
                    break;
                case '+':
                    buff.append("\\+");
                    break;
                case '\\':
                    if (i + 1 < text.length()) {
                        char next = text.charAt(i + 1);
                        if (('A' <= next && next <= 'Z') || ('a' <= next && next <= 'z')) {
                            buff.append("\\\\");
                        } else {
                            buff.append('\\');
                        }
                    } else {
                        buff.append('\\');
                    }
                    break;
                case '|':
                    buff.append("\\|");
                    break;
                case '[':
                    buff.append("\\[");
                    break;
                case ']':
                    buff.append("\\]");
                    break;
                case '(':
                    buff.append("\\(");
                    break;
                case ')':
                    buff.append("\\)");
                    break;
                case '{':
                    buff.append("\\{");
                    break;
                case '}':
                    buff.append("\\}");
                    break;
                default:
                    buff.append(c);
                    break;
            }
        }

        return buff.toString();
    }
}
