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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * <p>
 * このクラスは、書式化に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Formats {

    private Formats() {
    }

    /**
     * 指定された書式化指定子に従って、日付オブジェクトを文字列に変換します。
     * このメソッドは、{@link SimpleDateFormat}を利用した書式化を行ないます。
     * よって、書式化指定子は{@link SimpleDateFormat}に準じてください。
     * @param date 変換対象の日付
     * @param pattern 書式化指定子
     * @return 書式化された日付文字列
     */
    public static String simpleDateFormat(Date date, String pattern) {
        if (date == null) return "";
        Args.checkNotBlank(pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(true);
        return sdf.format(date);
    }

    /**
     * 指定された書式化指定子に従って、オブジェクトを文字列に変換します。
     * このメソッドは、{@link java.util.Formattable}を利用した書式化を行ないます。
     * よって、書式化指定子は{@link java.util.Formattable}に準じてください。
     * @param format 書式化指定子を含む文字列
     * @param args 置換オブジェクト
     * @return 書式化された文字列
     */
    public static String format(String format, Object... args) {
        return format(format, Locale.getDefault(), args);
    }

    /**
     * 指定された書式化指定子に従って、オブジェクトを文字列に変換します。
     * このメソッドは、{@link java.util.Formattable}を利用した書式化を行ないます。
     * よって、書式化指定子は{@link java.util.Formattable}に準じてください。
     * @param format 書式化指定子を含む文字列
     * @param locale 書式化の際のロケール
     * @param args 置換オブジェクト
     * @return 書式化された文字列
     */
    @SuppressWarnings("resource")
    public static String format(String format, Locale locale, Object... args) {
        if (format == null || "".equals(format)) return "";
        if (args.length == 0) return format;
        if (locale == null) locale = Locale.getDefault();
        StringBuilder appendable = new StringBuilder();
        Formatter formatter = new Formatter(appendable, locale);
        formatter.format(format, args);
        return appendable.toString();
    }

    /*
         書式化TLDを作成する際に実装してください

    public static String applyComma(Number number) {
        if (number == null) return "";
        return applyComma(number.toString());
    }

    public static String applyComma(String number) {
        if (number == null) return "";


        return "";
    }
    */

}
