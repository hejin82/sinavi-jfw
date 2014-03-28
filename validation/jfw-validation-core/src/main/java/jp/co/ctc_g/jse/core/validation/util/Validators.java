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

package jp.co.ctc_g.jse.core.validation.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、バリデーションに関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Validators {
        
    private static final Logger L = LoggerFactory.getLogger(Validators.class);

    private static final String BLANK_REGEXP = "\\s　";

    private static final Pattern BLANK_ALL_PATTERN = Pattern.compile("^[" + BLANK_REGEXP + "]*$");

    private static final Pattern BLANK_PATTERN = Pattern.compile("[" + BLANK_REGEXP + "]");

    private static final Pattern ALPHABET_PATTERN = Pattern.compile("^\\p{Alpha}+$");

    private static final Pattern ALPHAMERIC_PATTERN = Pattern.compile("^\\p{Alnum}+$");

    private static final Pattern HALFWIDTH_KATAKANA_PATTERN = Pattern.compile("^[\uff66-\uff9f]+$");

    private static final Pattern KATAKANA_PATTERN = Pattern.compile("^[\u30a1-\u30ed\u30ef\u30f2-\u30f4\u30fc]+$");

    private static final Pattern HIRAGANA_PATTERN = Pattern.compile("^[\u3041-\u308d\u308f\u3092-\u3093\u30fc]+$");

    private static final Pattern IPV4_PATTERN = Pattern.compile("^((?:1(?:0\\d?|1\\d?|2\\d?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?)?|2(?:[6789]|5[0-5]?|0\\d?|1\\d?|2\\d?|3\\d?|4\\d?)?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?|0)\\."
            + "(?:1(?:0\\d?|1\\d?|2\\d?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?)?|2(?:[6789]|5[0-5]?|0\\d?|1\\d?|2\\d?|3\\d?|4\\d?)?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?|0)\\."
            + "(?:1(?:0\\d?|1\\d?|2\\d?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?)?|2(?:[6789]|5[0-5]?|0\\d?|1\\d?|2\\d?|3\\d?|4\\d?)?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?|0)\\."
            + "(?:1(?:0\\d?|1\\d?|2\\d?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?)?|2(?:[6789]|5[0-5]?|0\\d?|1\\d?|2\\d?|3\\d?|4\\d?)?|3\\d?|4\\d?|5\\d?|6\\d?|7\\d?|8\\d?|9\\d?|0))"
            + "(?:/([0-9]|[12]\\d|3[0-2]))?$");

    private static final Pattern ZIP1_PATTERN = Pattern.compile("^(?:1(?:5[012345678]|7[013456789]|9[012345678]|3[01234567]|1[0123456]|4[0123456]|2[01345]|0\\d|6\\d|8"
            + "\\d)|2(?:9[023456789]|0[12345678]|2[01234567]|6[01234567]|8[23456789]|1[0123456]|3\\d|4\\d|5\\d|7\\d"
            + ")|6(?:2[012345679]|0[01234567]|8[0123459]|1\\d|3\\d|4\\d|5\\d|6\\d|7\\d|9\\d)|5(?:4[012345679]|8[012"
            + "345679]|0\\d|1\\d|2\\d|3\\d|5\\d|6\\d|7\\d|9\\d)|7(?:2[012356789]|4[012345679]|0\\d|1\\d|3\\d|5\\d|6"
            + "\\d|7\\d|8\\d|9\\d)|4(?:9[012345678]|2[01245678]|0\\d|1\\d|3\\d|4\\d|5\\d|6\\d|7\\d|8\\d)|0(?:3[0134"
            + "56789]|0[1234567]|1\\d|2\\d|4\\d|5\\d|6\\d|7\\d|8\\d|9\\d)|9(?:0[01234567]|7[01234569]|1\\d|2\\d|3"
            + "\\d|4\\d|5\\d|6\\d|8\\d|9\\d)|3(?:0\\d|1\\d|2\\d|3\\d|4\\d|5\\d|6\\d|7\\d|8\\d|9\\d)|8(?:0\\d|1\\d|2"
            + "\\d|3\\d|4\\d|5\\d|6\\d|7\\d|8\\d|9\\d))$");

    private static final Pattern ZIP2_PATTERN = Pattern.compile("^\\d{4}$");

    private static final String WINDOWS31_J = "Windows-31J";

    private Validators() {}

    /**
     * 文字列がブランク文字で構成されているかどうかを判定します。
     * ブランク文字とは、\t, \n, \x, 0B, \f, \r, 半角スペース, 全角スペースのことを指します。
     * @param suspect 検査対象
     * @return ブランク文字で構成されている場合はtrue
     */
    public static boolean isBlank(CharSequence suspect) {
        return isEmpty(suspect) || BLANK_ALL_PATTERN.matcher(suspect).matches();
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
     * 指定された文字列が空文字かどうかを検査します。
     * 空文字とは、<code>null</code>または長さが<code>0</code>の文字列のことです。
     * @param suspect 検査対象
     * @return 空文字の場合にtrue
     */
    public static boolean isEmpty(CharSequence suspect) {
        return suspect == null || suspect.length() == 0;
    }

    /**
     * 指定された文字列がnullかどうかを検査します。
     * @param suspect 検査対象
     * @return nullの場合にtrue
     */
    public static boolean isNull(Object suspect) {
        return suspect == null;
    }

    /**
     * 指定された文字列が正規表現<code>^\\p{Alpha}+$</code>に一致するかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isAlphabet(CharSequence suspect) {
        return ALPHABET_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が正規表現<code>^\\p{Alnum}+$</code>に一致するかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isAlphameric(CharSequence suspect) {
        return ALPHAMERIC_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が正規表現<code>^[\uff66-\uff9f]+$</code>に一致するかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isHalfwidthKatakana(CharSequence suspect) {
        return HALFWIDTH_KATAKANA_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が正規表現<code>^[\u30a1-\u30ed\u30ef\u30f2-\u30f4\u30fc]+$</code>に一致するかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isKatakana(CharSequence suspect) {
        return KATAKANA_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が正規表現<code>^[\u3041-\u308d\u308f\u3092-\u3093\u30fc]+$</code>に一致するかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isHiragana(CharSequence suspect) {
        return HIRAGANA_PATTERN.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が指定されたパターンに一致するかどうかを検査します。
     * @param pattern パターン
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isMatches(Pattern pattern, CharSequence suspect) {
        return pattern.matcher(suspect).matches();
    }

    /**
     * 指定された文字列が指定された正規表現に一致するかどうかを検査します。
     * @param regex 正規表現
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isMatches(String regex, CharSequence suspect) {
        return Pattern.matches(regex, suspect);
    }

    /**
     * 検証対象の文字列を"Windows-31J"でバイト長に変換し、取得したバイト長が検証対象の文字列の長さの×2倍であるかどうかを検査します。
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static boolean isZenkaku(CharSequence suspect) {
        try {
            byte[] sjis = suspect.toString().getBytes(WINDOWS31_J);
            return sjis.length == (suspect.length() * 2);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * <p>
     * 指定された文字列が以下の２つの正規表現に一致するかどうかを検査します。
     * <pre>
     * <code>
     * // 郵便番号の3桁部分のチェック
     * ^(?:1(?:5[012345678]|7[013456789]|9[012345678]|3[01234567]|1[0123456]|4[0123456]|2[01345]|0\d|6\d|8\d)|2(?:9[023456789]|0[12345678]|2[01234567]|6[01234567]|8[23456789]|1[0123456]|3\d|4\d|5\d|7\d)|6(?:2[012345679]|0[01234567]|8[0123459]|1\d|3\d|4\d|5\d|6\d|7\d|9\d)|5(?:4[012345679]|8[012345679]|0\d|1\d|2\d|3\d|5\d|6\d|7\d|9\d)|7(?:2[012356789]|4[012345679]|0\d|1\d|3\d|5\d|6\d|7\d|8\d|9\d)|4(?:9[012345678]|2[01245678]|0\d|1\d|3\d|4\d|5\d|6\d|7\d|8\d)|0(?:3[013456789]|0[1234567]|1\d|2\d|4\d|5\d|6\d|7\d|8\d|9\d)|9(?:0[01234567]|7[01234569]|1\d|2\d|3\d|4\d|5\d|6\d|8\d|9\d)|3(?:0\d|1\d|2\d|3\d|4\d|5\d|6\d|7\d|8\d|9\d)|8(?:0\d|1\d|2\d|3\d|4\d|5\d|6\d|7\d|8\d|9\d))$
     * </code>
     * </pre>
     * <pre>
     * <code>
     * // 郵便番号の4桁部分のチェック
     * ^\d{4}$
     * </code>
     * </pre>
     * </p>
     * @param suspect 検査対象
     * @param separator 区切り文字
     * @return 一致する場合にtrue
     */
    public static boolean isZipCode(CharSequence suspect, String separator) {
        String target = suspect.toString();
        if (target.length() != (7 + separator.length())) { return false; }
        String zip1 = target.substring(0, 3);
        String sep = target.substring(3, 3 + separator.length());
        String zip2 = target.substring(3 + separator.length(), target.length());
        if (!ZIP1_PATTERN.matcher(zip1).matches()) { return false; }
        if (!separator.equals(sep)) { return false; }
        if (!ZIP2_PATTERN.matcher(zip2).matches()) { return false; }
        return true;
    }

    /**
     * <p>
     * 指定された文字列が以下の正規表現に一致するかどうかを検査します。
     * <pre>
     * <code>
     * ^(
     * (?:1(?:0\d?|1\d?|2\d?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?)?|2(?:[6789]|5[0-5]?|0\d?|1\d?|2\d?|3\d?|4\d?)?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?|0)\.
     * (?:1(?:0\d?|1\d?|2\d?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?)?|2(?:[6789]|5[0-5]?|0\d?|1\d?|2\d?|3\d?|4\d?)?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?|0)\.
     * (?:1(?:0\d?|1\d?|2\d?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?)?|2(?:[6789]|5[0-5]?|0\d?|1\d?|2\d?|3\d?|4\d?)?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?|0)\.
     * (?:1(?:0\d?|1\d?|2\d?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?)?|2(?:[6789]|5[0-5]?|0\d?|1\d?|2\d?|3\d?|4\d?)?|3\d?|4\d?|5\d?|6\d?|7\d?|8\d?|9\d?|0))
     * (?:/([0-9]|[12]\d|3[0-2])
     * )?$
     * </code>
     * </pre>
     * </p>
     * @param suspect 検査対象
     * @return 一致する場合にtrue
     */
    public static Matcher isIPv4(CharSequence suspect) {
        return IPV4_PATTERN.matcher(suspect);
    }

    /**
     * 指定された文字列がIPv4アドレスであるかどうかを検査します。
     * @param suspect 検査対象
     * @param only CIDR表記のIPアドレスブロック
     * @return 一致する場合にtrue
     */
    public static boolean isIPv4(CharSequence suspect, String[] only) {
        Matcher m = isIPv4(suspect);
        if (!m.matches()) { return false; }
        String ipaddr = m.group(1);
        String cidr = m.group(2);
        if (cidr != null) { return false; }
        if (only.length == 0) { return true; }
        for (String network : only) {
            if (validateNetwork(ipaddr, network)) return true;
        }
        return false;
    }

    /**
     * 指定された文字列の文字コードが'\u0020'から'\u007e'の範囲であるかどうか検証します。
     * @param suspect 検査対象
     * @return 範囲内の場合にtrue
     */
    public static boolean isASCII(CharSequence suspect) {
        for (int i = 0; i < suspect.length(); i++) {
            char c = suspect.charAt(i);
            if (c < '\u0020' || c > '\u007e') { return false; }
        }
        return true;
    }

    /**
     * 指定された文字列が日付かどうかを検査します。
     * 検証アルゴリズムは、{@link GenericValidator#isDate(String, String, boolean)}を利用しています。
     * @param suspect 検査対象
     * @param datePattern 日付の書式を指定します。{@link SimpleDateFormat}の日付/時刻パターンで指定します。
     * @param strict 厳密に日付書式を検証するかを指定します。 値が<code>true</code>であれば、日付書式と文字長が等しいかどうかを検証します。
     * @return GenericValidator#isDate(String, String, boolean)の結果
     */
    public static boolean isDate(CharSequence suspect, String datePattern, boolean strict) {
        return GenericValidator.isDate(suspect.toString(), datePattern, strict);
    }

    /**
     * 指定された文字列が小数かつ指定された有効桁数・小数点桁数以内かどうかを検査します。
     * @param suspect 検査対象
     * @param signed 符号の有無
     * @param precision 有効桁数
     * @param scale 小数桁数
     * @return 一致するときにtrue
     */
    public static boolean isDecimal(CharSequence suspect, boolean signed, int precision, int scale) {
        BigDecimal number = toBigDecimal(replaceDecimalValue(suspect));
        if (number == null) return true;
        return isDecimal(number, signed, precision, scale);
    }

    /**
     * 指定された数値が小数かつ指定された有効桁数・小数点桁数以内かどうかを検査します。
     * @param suspect 検査対象
     * @param signed 符号の有無
     * @param precision 有効桁数
     * @param scale 小数桁数
     * @return 一致するときにtrue
     */
    public static boolean isDecimal(Number suspect, boolean signed, int precision, int scale) {
        BigDecimal number = toBigDecimal(suspect);
        return isDecimal(number, signed, precision, scale);
    }

    /**
     * 指定された文字列がメールアドレスの形式かどうかを検査します。
     * 検証アルゴリズムは、{@link GenericValidator#isEmail(String)}を利用しています。
     * @param suspect 検査対象
     * @return GenericValidator#isEmail(String)の結果
     */
    public static boolean isEmail(CharSequence suspect) {
        return GenericValidator.isEmail(suspect.toString());
    }

    /**
     * 指定された文字列の長さが指定したサイズと等しいか、それよりも大きいかを検査します。
     * アルゴリズムは、{@link GenericValidator#minLength(String, int)}を利用しています。
     * @param suspect 検査対象
     * @param size サイズ
     * @return GenericValidator#minLength(String, int)の結果
     */
    public static boolean minLength(CharSequence suspect, int size) {
        return GenericValidator.minLength(suspect.toString(), size);
    }

    /**
     * 指定された文字列の長さが指定したサイズと等しいか、それよりも小さいかを検査します。
     * アルゴリズムは、{@link GenericValidator#maxLength(String, int)}を利用しています。
     * @param suspect 検査対象
     * @param size サイズ
     * @return GenericValidator#maxLength(String, int)の結果
     */
    public static boolean maxLength(CharSequence suspect, int size) {
        return GenericValidator.maxLength(suspect.toString(), size);
    }
    
    /**
     * 指定された文字列のバイト数が指定したサイズと等しいかを検査します。
     * @param suspect 検査対象
     * @param size サイズ
     * @param encoding 文字エンコーディング
     * @return サイズが等しいときにtrue
     * @throws UnsupportedEncodingException 
     */
    public static boolean equalsByteLength(CharSequence suspect, int size, String encoding) throws UnsupportedEncodingException {
        return suspect.toString().getBytes(encoding).length == size;
    }
    
    /**
     * 指定された文字列のバイト数が指定したサイズと等しいか、それよりも大きいかを検査します。
     * @param suspect 検査対象
     * @param size サイズ
     * @param encoding 文字エンコーディング
     * @return サイズが等しいか、それよりも大きいときにtrue
     * @throws UnsupportedEncodingException 
     */
    public static boolean minByteLength(CharSequence suspect, int size, String encoding) throws UnsupportedEncodingException {
        return suspect.toString().getBytes(encoding).length >= size;
    }

    /**
     * 指定された文字列のバイト数が指定したサイズと等しいか、それよりも小さいかを検査します。
     * @param suspect 検査対象
     * @param size サイズ
     * @param encoding 文字エンコーディング
     * @return サイズが等しいか、それよりも小さいときにtrue
     * @throws UnsupportedEncodingException 
     */
    public static boolean maxByteLength(CharSequence suspect, int size, String encoding) throws UnsupportedEncodingException {
        return suspect.toString().getBytes(encoding).length <= size;
    }

    /**
     * 指定された数値が比較対象の数値よりも大きいかどうかを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の数値
     * @return 大きいときにfalse
     */
    public static boolean greaterThan(Number suspect, BigDecimal target) {
        if (suspect == null) return true;
        BigDecimal number = toBigDecimal(suspect);
        return number.compareTo(target) >= 1;
    }

    /**
     * 指定された数値が比較対象の数値と等しいか、それよりも大きいかどうかを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の数値
     * @return 等しいか、大きいときにfalse
     */
    public static boolean greaterThanEqualsTo(Number suspect, BigDecimal target) {
        if (suspect == null) return true;
        BigDecimal number = toBigDecimal(suspect);
        return number.compareTo(target) >= 0;
    }

    /**
     * 指定された数値が比較対象の数値よりも小さいかどうかを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の数値
     * @return 小さいときにfalse
     */
    public static boolean lessThan(Number suspect, BigDecimal target) {
        if (suspect == null) return true;
        BigDecimal number = toBigDecimal(suspect);
        return number.compareTo(target) <= -1;
    }

    /**
     * 指定された数値が比較対象の数値と等しいか、それよりも小さいかどうかを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の数値
     * @return 等しいか、小さいときにfalse
     */
    public static boolean lessThanEqualsTo(Number suspect, BigDecimal target) {
        if (suspect == null) return true;
        BigDecimal number = toBigDecimal(suspect);
        return number.compareTo(target) <= 0;
    }
    
    /**
     * 指定された値が比較対象の値と等しいかどうかを検査します。
     * アルゴリズムは、{@link java.lang.Object#equals(Object)}を利用します。
     * @param suspect 検査対象
     * @param target 比較対象
     * @return 一致するときにtrue
     */
    public static boolean equalsTo(Object suspect, Object target) {
        if (suspect == null) return true;
        return suspect.equals(target);
    }

    /**
     * 指定された日付が比較対象の日付よりも前かを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の日付
     * @return 前のときにfalse
     */
    public static boolean before(Date suspect, Date target) {
        if (suspect == null) return true;
        return suspect.before(target);
    }

    /**
     * 指定された日付が比較対象の日付と同じかそれよりも前かを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の日付
     * @return 同じか、前のときにfalse
     */
    public static boolean beforeEqualsTo(Date suspect, Date target) {
        if (suspect == null) return true;
        return suspect.before(target) || suspect.equals(target);
    }

    /**
     * 指定された日付が比較対象の日付よりも後かを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の日付
     * @return 後のときにfalse
     */
    public static boolean after(Date suspect, Date target) {
        if (suspect == null) return true;
        return suspect.after(target);
    }

    /**
     * 指定された日付が比較対象の日付と同じかそれよりも後かを検査します。
     * @param suspect 検査対象
     * @param target 比較対象の日付
     * @return 同じか、後のときにfalse
     */
    public static boolean afterEqualsTo(Date suspect, Date target) {
        if (suspect == null) return true;
        return suspect.after(target) || suspect.equals(target);
    }
    
    /**
     * BigDecimal型に変換します。
     * @param suspect Numberオブジェクト
     * @return 数値型
     */
    public static BigDecimal toBigDecimal(Number suspect) {
        if (suspect instanceof BigDecimal) {
            return (BigDecimal) suspect;
        } else {
            return new BigDecimal(suspect.toString());
        }
    }
    
    /**
     * BigDecimal型に変換します。
     * @param suspect 文字列
     * @return 数値型
     */
    public static BigDecimal toBigDecimal(CharSequence suspect) {
        return toBigDecimal(suspect, false);
    }
    
    /**
     * BigDecimal型に変換します。
     * @param suspect オブジェクト
     * @return 数値型
     */
    public static BigDecimal toBigDecimal(Object suspect) {
        if (suspect instanceof BigDecimal) return (BigDecimal) suspect;
        return toBigDecimal(suspect.toString(), false);
    }
    
    /**
     * BigDecimal型に変換します。
     * @param suspect 文字列
     * @param throwing trueのときに例外をスローします
     * @return 数値型
     */
    public static BigDecimal toBigDecimal(CharSequence suspect, boolean throwing) {
        BigDecimal value = null;
        try {
            value = new BigDecimal(suspect.toString());
        } catch (NumberFormatException e) {
            L.debug("指定された値({})が数値形式ではありません。数値形式で指定してください", new Object[]{suspect});
            if (throwing) {
                throw e;
            }
        }
        return value;
    }
    
    /**
     * Date型に変換します。
     * @param suspect 文字列
     * @param pattern 日付の書式
     * @return 日付型
     */
    public static Date toDate(CharSequence suspect, String pattern) {
        return toDate(suspect, pattern, false);
    }
    
    /**
     * Date型に変換します。
     * @param suspect オブジェクト
     * @param pattern 日付の書式
     * @return 日付型
     */
    public static Date toDate(Object suspect, String pattern) {
        if (suspect instanceof Date) return (Date) suspect;
        return toDate(suspect.toString(), pattern, false);
    }
    
    /**
     * Date型に変換します。
     * @param suspect オブジェクト
     * @param pattern 日付の書式
     * @param throwing trueのときに例外をスローします
     * @return 日付型
     */
    public static Date toDate(CharSequence suspect, String pattern, boolean throwing) {
        Date value = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            value = sdf.parse(suspect.toString());
        } catch (ParseException e) {
            L.debug("指定された値({})が日付形式ではありません。日付形式で指定してください", new Object[]{suspect});
            if (throwing) {
                throw new IllegalArgumentException(e);
            }
        }
        return value;
    }
    
    protected static boolean isDecimal(BigDecimal number, boolean signed, int precision, int scale) {
        if ((!signed) && (number.signum() < 0)) { return false; }
        int expectedIntPrecision = precision - scale;
        int actualPrecision = number.precision();
        int actualScale = number.scale();
        int actualIntPrecision = actualPrecision - actualScale;
        return (expectedIntPrecision >= actualIntPrecision && scale >= actualScale);
    }
    
    private static boolean validateNetwork(String ipaddr, String network) {
        Matcher m = IPV4_PATTERN.matcher(network);
        if (!m.matches()) { return false; }
        String netaddr = m.group(1);
        String cidr = m.group(2);
        if (cidr == null) { return false; }
        int host = toInt(ipaddr);
        return (toInt(netaddr) <= host && host <= toBcast(netaddr, cidr));
    }

    private static int toBcast(String addr, String cidr) {
        int a = toInt(addr);
        int c = Integer.parseInt(cidr);
        return a | ~(0xffffffff << (32 - c));
    }

    private static int toInt(String addr) {
        String[] parts = addr.split("\\.");
        int i0 = Integer.parseInt(parts[0]);
        int i1 = Integer.parseInt(parts[1]);
        int i2 = Integer.parseInt(parts[2]);
        int i3 = Integer.parseInt(parts[3]);
        return (i0 << 24) + (i1 << 16) + (i2 << 8) + i3;
    }

    private static String replaceDecimalValue(CharSequence suspect) {
        String source = suspect.toString();
        source = source.replace("－", "-");
        source = source.replace("，", "");
        source = source.replace(",", "");
        source = source.replace("．", ".");
        return source;
    }
}
