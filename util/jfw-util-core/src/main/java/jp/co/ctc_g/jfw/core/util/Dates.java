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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * このクラスは、日付操作に関するユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Dates {

    private static final Log L = LogFactory.getLog(Dates.class);
    private static final ResourceBundle R = InternalMessages.getBundle(Dates.class);
    /**
     * 年.
     */
    public static final DateType YEAR = new DateType("YEAR");
    /**
     * 月.
     */
    public static final DateType MONTH = new DateType("MONTH");
    /**
     * 日.
     */
    public static final DateType DAY = new DateType("DAY");
    /**
     * 時.
     */
    public static final DateType HOUR = new DateType("HOUR");
    /**
     * 分.
     */
    public static final DateType MINUTE = new DateType("MINUTE");
    /**
     * 秒.
     */
    public static final DateType SECOND = new DateType("SECOND");
    /**
     * ミリ.
     */
    public static final DateType MSEC = new DateType("MSEC");
    /**
     * ナノ.
     */
    public static final int NANOS_2_MSEC = 1000000;
    /**
     * Calendarクラスの月を表すint配列.
     */
    static final int[] CALENDAR_MONTH = new int[] {
        Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
        Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER,
        Calendar.NOVEMBER, Calendar.DECEMBER
    };
    /**
     * Calendarクラスの月と対応する実際の月を表すint配列.
     */
    private static final int[] REAL_MONTH = new int[] {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
    };
    /**
     * 年月日の各数値の最大値を示す数値配列です.
     */
    private static final int[] MAX_DATE = new int[] {
        9999, 12, 31
    };
    /**
     * 年月日の各数値の最小値を示す数値配列です.
     */
    private static final int[] MIN_DATE = new int[] {
        1, 1, 1
    };
    /**
     * 時分秒の各数値の最大値を示す数値配列です.
     */
    private static final int[] MAX_TIMES = new int[] {
        23, 59, 59
    };
    /**
     * 時分秒の各数値の最小値を示す数値配列です.
     */
    private static final int[] MIN_TIMES = new int[] {
        0, 0, 0
    };
    /**
     * 各月の日にちの最大値を示す数値配列です.
     */
    private static final int[] DAY_OF_MONTH = new int[] {
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    private Dates() {
    }

    /**
     * 指定された日付文字列を指定された日付パターンに従って解析し、
     * {@link Date}オブジェクトを返却します。
     * 引数が<code>null</code>の場合や、サイズを持っていない場合、
     * このメソッドは<code>null</code>を返却します。
     * 日付パターンは{@link SimpleDateFormat}に準じます。
     * 日付パターンが不正であるなどの理由により、
     * 解析に失敗した場合、このメソッドはnullを返却します。
     * @param source 日付の文字列表現
     * @param pattern 日付パターン
     * @return 指定された日付を表現する{@link Date}オブジェクト
     */
    public static Date makeFrom(String source, String pattern) {
        if (Strings.isEmpty(source) || Strings.isEmpty(pattern)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(true);
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(source, pos);
        if (pos.getErrorIndex() != -1 && L.isDebugEnabled()) {
            Map<String, Object> replace = new HashMap<String, Object>(3);
            replace.put("pattern", pattern);
            replace.put("index", pos.getErrorIndex() + 1);
            replace.put("target", source);
            L.debug(Strings.substitute(R.getString("D-UTIL#0002"), replace));
        }
        return date;
    }

    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @return 日付
     */
    public static Date makeFrom(int year, int month) {
        return makeFrom(year, month, 1, 0, 0, 0, 0);
    }

    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日付
     */
    public static Date makeFrom(int year, int month, int day) {
        return makeFrom(year, month, day, 0, 0, 0, 0);
    }

    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hourOfDay 時
     * @return 日付
     */
    public static Date makeFrom(int year, int month, int day, int hourOfDay) {
        return makeFrom(year, month, day, hourOfDay, 0, 0, 0);
    }


    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hourOfDay 時
     * @param minute 分
     * @return 日付
     */
    public static Date makeFrom(int year, int month, int day, int hourOfDay, int minute) {
        return makeFrom(year, month, day, hourOfDay, minute, 0, 0);
    }

    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hourOfDay 時
     * @param minute 分
     * @param second 秒
     * @return 日付
     */
    public static Date makeFrom(int year, int month, int day, int hourOfDay, int minute, int second) {
       return makeFrom(year, month, day, hourOfDay, minute, second, 0);
    }

    /**
     * 指定された引数から、日付を作成します。
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hourOfDay 時
     * @param minute 分
     * @param second 秒
     * @param millis ミリ秒
     * @return 日付
     */
    public static Date makeFrom(int year, int month, int day, int hourOfDay, int minute, int second, int millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.set(year, month - 1 /* zero origin */, day, hourOfDay, minute, second);
        calendar.set(Calendar.MILLISECOND, millis);
        return calendar.getTime();
    }

    /**
     * 次の年を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date nextYear(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 次の月を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date nextMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 次の日を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date nextDay(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 前の年を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date previousYear(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 前の月を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date previousMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 前の日を求めます。
     * @param date 基準となる日付
     * @return 新しい日付
     */
    public static Date previousDay(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 年を取得します。
     * @param date 基準となる日付
     * @return 年
     */
    public static int getYear(Date date) {
        if (date == null) return -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 月を取得します。
     * この月は、直感通り、1の場合は1月を意味し2の場合は2月を意味します。
     * @param date 基準となる日付
     * @return 月
     */
    public static int getMonth(Date date) {
        if (date == null) return -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 日を取得します。
     * @param date 基準となる日付
     * @return 日
     */
    public static int getDay(Date date) {
        if (date == null) return -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 曜日を取得します。
     * @param date 基準となる日付
     * @return 曜日
     */
    public static int getDayOfWeek(Date date) {
        if (date == null) return -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * その月の最後の日を取得します。
     * @param date 基準となる日付
     * @return その月の最後の日
     */
    public static int lastDay(Date date) {
        if (date == null) return -1;
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(true);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 指定した年がうるう年かチェックします.
     * @param year 評価する年
     * @return 指定した年がうるう年の場合はtrue、 そうでない場合はfalseを返します.
     */
    public static boolean isLeapYear(int year) {

        return ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    }

    /**
     * 指定した日付が月に対して有効かチェックします.
     * @param year 評価する年
     * @param month 評価する月
     * @param day 評価する日
     * @return 指定した日付が正常な場合はtrue、 そうでない場合はfalseを返します.
     */
    public static boolean isDate(int year, int month, int day) {

        return enableRange(MAX_DATE, MIN_DATE, new int[] {
            year, month, day
        }) && enableDayOfMonth(year, month, day);
    }

    /**
     * 指定した時間が有効かチェックします.
     * @param hour 評価する時間
     * @param minutes 評価する分数
     * @param second 評価する秒数
     * @return 指定した時間が正常な場合はtrue、 そうでない場合はfalseを返します.
     */
    public static boolean isTime(int hour, int minutes, int second) {

        return enableRange(MAX_TIMES, MIN_TIMES, new int[] {
            hour, minutes, second
        });
    }

    /**
     * 現在の年を返します.
     * @return 現在の年
     */
    public static int getYear() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.YEAR);
    }

    /**
     * 現在の月を返します.
     * @return 現在の月
     */
    public static int getMonth() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return Dates.toRealMonth(currentCalendar.get(Calendar.MONTH));
    }

    /**
     * Calendarクラスが表す現在の月を返します.
     * @return 現在の月
     */
    public static int getCalendarMonth() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.MONTH);
    }

    /**
     * 現在の日を返します.
     * @return 現在の日
     */
    public static int getDay() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.DATE);
    }

    /**
     * 現在の時を返します.
     * @return 現在の時
     */
    public static int getHour() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 現在の分を返します.
     * @return 現在の分
     */
    public static int getMinutes() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.MINUTE);
    }

    /**
     * 現在のミリ秒を返します.
     * @return 現在のミリ秒
     */
    public static int getMillisecond() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.MILLISECOND);
    }

    /**
     * 現在の秒を返します.
     * @return 現在の秒
     */
    public static int getSecond() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.SECOND);
    }

    /**
     * 年の初めから現在が何日目かを返します.
     * @return 年の初めから現在までの日数
     */
    public static int getDayOfYear() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 曜日を返します.
     * @return 現在の曜日を示す数値（java.util.Calendar参照）
     */
    public static int getDayOfWeek() {

        Calendar currentCalendar = Dates.getCalendarInstance();
        return currentCalendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 指定した{@link java.sql.Timestamp java.sql.Timestamp}の日付を持つ
     * {@link java.util.Date java.util.Date} を返します.
     * @param date タイムスタンプ
     * @return 指定した日付の{@link java.util.Date java.util.Date}
     */
    public static java.util.Date getDate(Timestamp date) {

        return new java.util.Date(date.getTime());
    }

    /**
     * 現在の日付から生成される{@link java.util.Date java.util.Date}を返します.
     * @return 現在の日付を示す{@link java.util.Date java.util.Date}
     */
    public static java.util.Date getDate() {

        return new java.util.Date(System.currentTimeMillis());
    }

    /**
     * 指定した日付から生成される{@link java.util.Date java.util.Date}を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 指定した日付を示す{@link java.util.Date java.util.Date}
     */
    public static java.util.Date getDate(int year, int month, int day) {

        return new java.util.Date(Dates.getTime(year, month, day));
    }

    /**
     * 指定した日付から生成される{@link java.util.Date java.util.Date}を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param minutes 分
     * @param second 秒
     * @return 指定した日付から表される{@link java.util.Date java.util.Date}
     */
    public static java.util.Date getDate(int year, int month, int day, int hour, int minutes,
        int second) {

        return new java.util.Date(Dates.getTime(year, month, day, hour, minutes, second));
    }

    /**
     * 指定した日付（{@link java.util.Date java.util.Date}）から生成される{@link java.sql.Date java.sql.Date}を返します.
     * @param date 日付
     * @return 指定した日付を示す{@link java.sql.Date java.sql.Date}
     */
    public static java.sql.Date getSqlDate(java.util.Date date) {

        return new java.sql.Date(date.getTime());
    }

    /**
     * 現在の日付から生成される{@link java.sql.Date java.sql.Date}を返します.
     * @return 現在の日付を示す{@link java.sql.Date java.sql.Date}
     */
    public static java.sql.Date getSqlDate() {

        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * 現在の時間から生成されるjava.sql.Timeを返します.
     * @return 現在の時間を示すjava.sql.Time
     */
    public static Time getSqlTime() {

        return new Time(System.currentTimeMillis());
    }

    /**
     * 指定した日付（{@link java.util.Date java.util.Date}） から{@link java.sql.Timestamp java.sql.Timestamp}を返します.
     * @param date 日付
     * @return 指定した日付と時間を示す{@link java.sql.Timestamp java.sql.Timestamp}
     */
    public static Timestamp getSqlTimestamp(java.util.Date date) {

        return new Timestamp(date.getTime());
    }

    /**
     * 現在の日付と時間を含む{@link java.sql.Timestamp java.sql.Timestamp}を返します.
     * @return 現在の日付と時間を示す{@link java.sql.Timestamp java.sql.Timestamp}
     */
    public static Timestamp getSqlTimestamp() {

        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 指定した日付から生成される{@link java.sql.Date java.sql.Date}を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 指定した日付から表される{@link java.sql.Date java.sql.Date}
     */
    public static java.sql.Date getSqlDate(int year, int month, int day) {

        return new java.sql.Date(Dates.getTime(year, month, day));
    }

    /**
     * 指定した時間から生成されるjava.sql.Timeを返します.
     * @param hour 時
     * @param minutes 分
     * @param second 秒
     * @return 指定した時間から表されるjava.sql.Time
     */
    public static Time getSqlTime(int hour, int minutes, int second) {

        Calendar currentCalendar = Dates.getCalendarInstance();
        currentCalendar.set(Dates.getYear(), Dates.getMonth(), Dates.getDay(), hour,
                            minutes, second);
        return new Time(currentCalendar.getTime().getTime());
    }

    /**
     * 指定した日付と時間から生成される{@link java.sql.Timestamp java.sql.Timestamp}を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param minutes 分
     * @param second 秒
     * @return 指定した日付と時間を表す{@link java.sql.Timestamp java.sql.Timestamp}
     */
    public static Timestamp getSqlTimestamp(int year, int month, int day, int hour, int minutes,
        int second) {

        return new Timestamp(Dates.getTime(year, month, day, hour, minutes, second));
    }

    /**
     * 指定した日付からlongで表される時間を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日付からlongで表される時間
     */
    public static long getTime(int year, int month, int day) {

        return Dates.getTime(year, month, day, 0, 0, 0);
    }

    /**
     * 指定した日付と時間からlongで表される時間を返します.
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param minutes 分
     * @param second 秒
     * @return 日付と時間からlongで表される時間
     */
    public static long getTime(int year, int month, int day, int hour, int minutes, int second) {

        Calendar currentCalendar = Dates.getCalendarInstance(year, month, day, hour, minutes,
                                                                second);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        return currentCalendar.getTime().getTime();
    }

    /**
     * カレンダーのインスタンスを取得します.
     * @return Calendarクラスのインスタンス
     */
    public static Calendar getCalendarInstance() {

        return Calendar.getInstance();
    }

    /**
     * 年月日・時分秒からカレンダーのインスタンスを取得します.
     * @param year 年
     * @param month 月（１～１２）
     * @param day 日
     * @param hour 時
     * @param minutes 分
     * @param second 秒
     * @return Calendarクラスのインスタンス
     */
    public static Calendar getCalendarInstance(int year, int month, int day, int hour, int minutes,
        int second) {

        Calendar currentCalendar = Dates.getCalendarInstance();
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, hour, minutes, second);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        return currentCalendar;
    }

    /**
     * 日付からカレンダーのインスタンスを取得します.
     * @param date 日付
     * @return 日付を元とするカレンダー
     */
    public static Calendar getCalendarInstance(Date date) {

        Calendar currentCalendar = Dates.getCalendarInstance();
        currentCalendar.setTime(date);
        return currentCalendar;
    }

    /**
     * Calendarクラスから取得した月を、実際の月へ変換します.
     * @param value {@link java.util.Calendar#get(int) Calendar#get(Calendar.MONTH)}により取得した値
     * @return 実際の月（1～12）
     */
    public static int toRealMonth(int value) {

        for (int index = 0; index <= Dates.CALENDAR_MONTH.length - 1; index++) {
            if (Dates.CALENDAR_MONTH[index] == value) {
                return Dates.REAL_MONTH[index];
            }
        }
        throw new IllegalArgumentException("can use the value of 0 to 11. ["
            + String.valueOf(value) + "]");
    }

    /**
     * 実際の月を、Calendarクラスの月の値へ変換します.
     * @param value 実際の月（1～12）
     * @return Calendarクラスの月の値
     */
    public static int toCalendarMonth(int value) {

        return Dates.CALENDAR_MONTH[value - 1];
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.<br>
     * これは{#difference(long, long) difference(long, long)} メソッドの第２引数に現在の日付を指定するのと同じです.
     * @param date 比較する日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(long date) {

        return Dates.getDate().getTime() - date;
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.<br>
     * これは{#difference({@link java.util.Date java.util.Date},
     * {@link java.util.Date java.util.Date}) difference(Date, Date)} メソッドの第２引数に現在の日付を指定するのと同じです.
     * @param date 比較する日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(java.util.Date date) {

        return Dates.getDate().getTime() - date.getTime();
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.<br>
     * これは{#difference({@link java.sql.Timestamp java.sql.Timestamp},
     * {@link java.sql.Timestamp java.sql.Timestamp}) difference(Date, Date)}
     * メソッドの第２引数に現在の日付を指定するのと同じです.
     * @param date 比較する日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(Timestamp date) {

        return Dates.getDate().getTime() - date.getTime();
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.
     * @param date1 比較元の日付
     * @param date2 比較先の日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(long date1, long date2) {

        return date2 - date1;
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.
     * @param date1 比較元の日付
     * @param date2 比較先の日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(java.util.Date date1, java.util.Date date2) {

        return date2.getTime() - date1.getTime();
    }

    /**
     * 現在の日付と指定した日付の差分を取得します.
     * @param date1 比較元の日付
     * @param date2 比較先の日付
     * @return 日付の差を表すミリ秒の値
     */
    public static long difference(Timestamp date1, Timestamp date2) {

        return date2.getTime() - date1.getTime();
    }

    /**
     * <ul>
     * パラメータのクラスでインスタンスして返します.
     * </ul>
     * @param dateClass java.util.Dateのサブクラス
     * @param millis ミリ秒
     * @return date インスタンス化したdate
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static Date instantiateDate(Class dateClass, long millis) {

        java.util.Date retObj = null;
        Constructor constructor = null;

        try {
            constructor = dateClass.getConstructor(new Class[] {
                long.class
            });
            retObj = (java.util.Date) constructor.newInstance(new Object[] {
                new Long(millis)
            });

        } catch (SecurityException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        } catch (NoSuchMethodException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        } catch (IllegalArgumentException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        } catch (InstantiationException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        } catch (IllegalAccessException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        } catch (InvocationTargetException e) {
            throw new InternalException(Dates.class, "E-UTIL#0035", e);
        }

        return retObj;
    }

    /**
     * 指定の日時に指定の年数を加算した日時を返します.
     * @param date 対象の日時
     * @param inclement 加算する年数.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addYear(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        currentCalendar.set(currentCalendar.get(Calendar.YEAR) + inclement, currentCalendar
            .get(Calendar.MONTH), currentCalendar.get(Calendar.DATE), currentCalendar
            .get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), currentCalendar
            .get(Calendar.SECOND));

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);
    }

    /**
     * 指定の日時に指定の月数を加算した日時を返します.<br>
     * 対象の月が月末であるとき、変更後の日時も月末に合わせます.<br>
     * 例:5月31日に1ヶ月分を足すと6月30日として返される.
     * @param date 対象の日時
     * @param inclement 加算する月数.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addMonth(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int calendarMonth = currentCalendar.get(Calendar.MONTH);
        int month = Dates.toRealMonth(calendarMonth);
        month += inclement;
        for (; (month) <= 0 || (month) >= 13;) {
            if ((month) >= 13) {
                year++;
                month -= 12;
            } else {
                year--;
                month += 12;
            }
        }
        int day = currentCalendar.get(Calendar.DATE);
        int dayOfMonth = DAY_OF_MONTH[calendarMonth];
        if (day == dayOfMonth) {
            Date firstDate = Dates.getDate(year, month, 1);
            Date endOfMonth = Dates.endOfMonth(firstDate);
            Calendar inclementCalendar = Dates.getCalendarInstance(endOfMonth);
            day = inclementCalendar.get(Calendar.DATE);
        }
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, currentCalendar
            .get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), currentCalendar
            .get(Calendar.SECOND));

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);
    }

    /**
     * 指定の日時に指定の日数を加算した日時を返します.
     * @param date 対象の日時
     * @param inclement 加算する日数.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addDay(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int month = Dates.toRealMonth(currentCalendar.get(Calendar.MONTH));
        int day = currentCalendar.get(Calendar.DATE);
        if (inclement >= 0) {
            for (int index = 0; index < inclement; index++) {
                day++;
                if (!enableDayOfMonth(year, month, day)) {
                    day = 1;
                    month++;
                    if (month >= 13 || !enableDayOfMonth(year, month, day)) {
                        month = 1;
                        year++;
                    }
                }
            }
        } else {
            for (int index = inclement; index < 0; index++) {
                day--;
                if (day <= 0) {
                    month--;
                    if (month <= 0) {
                        year--;
                        if (year <= 0) {
                            throw new InternalException(Dates.class, "E-UTIL#0036");
                        } else {
                            month = 12;
                            day = DAY_OF_MONTH[11];
                        }
                    } else {
                        day = DAY_OF_MONTH[month - 1];
                        if (!isLeapYear(year) && month == 2) {
                            day--;
                        }
                    }
                }
            }
        }
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, currentCalendar
            .get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), currentCalendar
            .get(Calendar.SECOND));

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);

    }

    /**
     * 指定の日時に指定の時間を加算した日時を返します.
     * @param date 対象の日時
     * @param inclement 加算する時間.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addHour(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int month = Dates.toRealMonth(currentCalendar.get(Calendar.MONTH));
        int day = currentCalendar.get(Calendar.DATE);
        int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        if (inclement >= 0) {
            for (int index = 0; index < inclement; index++) {
                hour++;
                if (hour >= 24) {
                    hour = 0;
                    day++;
                    if (!enableDayOfMonth(year, month, day)) {
                        day = 1;
                        month++;
                        if (month >= 13 || !enableDayOfMonth(year, month, day)) {
                            month = 1;
                            year++;
                        }
                    }
                }
            }
        } else {
            for (int index = inclement; index < 0; index++) {
                hour--;
                if (hour < 0) {
                    hour = 23;
                    day--;
                    if (day <= 0) {
                        month--;
                        if (month <= 0) {
                            year--;
                            if (year <= 0) {
                                throw new InternalException(Dates.class, "E-UTIL#0036");
                            } else {
                                month = 12;
                                day = DAY_OF_MONTH[11];
                            }
                        } else {
                            day = DAY_OF_MONTH[month - 1];
                            if (!isLeapYear(year) && month == 2) {
                                day--;
                            }
                        }
                    }
                }
            }
        }
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, hour, currentCalendar
            .get(Calendar.MINUTE), currentCalendar.get(Calendar.SECOND));

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);

    }

    /**
     * 指定の日時に指定の分数を加算した日時を返します.
     * @param date 対象の日時
     * @param inclement 加算する分数.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addMinute(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int month = Dates.toRealMonth(currentCalendar.get(Calendar.MONTH));
        int day = currentCalendar.get(Calendar.DATE);
        int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = currentCalendar.get(Calendar.MINUTE);
        if (inclement >= 0) {
            for (int index = 0; index < inclement; index++) {
                minute++;
                if (minute >= 60) {
                    minute = 0;
                    hour++;
                    if (hour >= 24) {
                        hour = 0;
                        day++;
                        if (!enableDayOfMonth(year, month, day)) {
                            day = 1;
                            month++;
                            if (month >= 13 || !enableDayOfMonth(year, month, day)) {
                                month = 1;
                                year++;
                            }
                        }
                    }
                }
            }
        } else {
            for (int index = inclement; index < 0; index++) {
                minute--;
                if (minute < 0) {
                    minute = 59;
                    hour--;
                    if (hour < 0) {
                        hour = 23;
                        day--;
                        if (day <= 0) {
                            month--;
                            if (month <= 0) {
                                year--;
                                if (year <= 0) {
                                    throw new InternalException(Dates.class, "E-UTIL#0036");
                                } else {
                                    month = 12;
                                    day = DAY_OF_MONTH[11];
                                }
                            } else {
                                day = DAY_OF_MONTH[month - 1];
                                if (!isLeapYear(year) && month == 2) {
                                    day--;
                                }
                            }
                        }
                    }
                }
            }
        }
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, hour, minute,
                            currentCalendar.get(Calendar.SECOND));

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);

    }

    /**
     * 指定の日時に指定の秒数を加算した日時を返します.
     * @param date 対象の日時
     * @param inclement 加算する秒数.マイナスの場合は過去の日時を返します.
     * @return 変更された日時
     */
    public static Date addSecond(Date date, int inclement) {

        if (inclement == 0) {
            return date;
        }
        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int month = Dates.toRealMonth(currentCalendar.get(Calendar.MONTH));
        int day = currentCalendar.get(Calendar.DATE);
        int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = currentCalendar.get(Calendar.MINUTE);
        int second = currentCalendar.get(Calendar.SECOND);
        if (inclement >= 0) {
            for (int index = 0; index < inclement; index++) {
                second++;
                if (second >= 60) {
                    second = 0;
                    minute++;
                    if (minute >= 60) {
                        minute = 0;
                        hour++;
                        if (hour >= 24) {
                            hour = 0;
                            day++;
                            if (!enableDayOfMonth(year, month, day)) {
                                day = 1;
                                month++;
                                if (month >= 13 || !enableDayOfMonth(year, month, day)) {
                                    month = 1;
                                    year++;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int index = inclement; index < 0; index++) {
                second--;
                if (second < 0) {
                    second = 59;
                    minute--;
                    if (minute < 0) {
                        minute = 59;
                        hour--;
                        if (hour < 0) {
                            hour = 23;
                            day--;
                            if (day <= 0) {
                                month--;
                                if (month <= 0) {
                                    year--;
                                    if (year <= 0) {
                                        throw new InternalException(Dates.class, "E-UTIL#0036");
                                    } else {
                                        month = 12;
                                        day = DAY_OF_MONTH[11];
                                    }
                                } else {
                                    day = DAY_OF_MONTH[month - 1];
                                    if (!isLeapYear(year) && month == 2) {
                                        day--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        currentCalendar.set(year, Dates.toCalendarMonth(month), day, hour, minute, second);

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);

    }

    /**
     * <ul>
     * 指定の日付に指定のミリ秒を加算した日時を返します.
     * </ul>
     * @param date 日時
     * @param increment 増加量
     * @return date 増加した後の日時
     */
    public static Date addMillisecond(Date date, int increment) {

        if (increment == 0) {
            return date;
        }

        long time = date.getTime();
        time = time + increment;

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);
    }

    /**
     * <p>
     * 日時の指定した位置以降の日時情報を削除します.
     * </p>
     * @param date 指定された日時
     * @param type 日時の種類（月、日、時、分、秒、ミリ秒）
     * @return 切り捨てられた日時情報
     */
    public static Date truncate(Date date, DateType type) {

        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);
        int day = currentCalendar.get(Calendar.DATE);
        int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = currentCalendar.get(Calendar.MINUTE);
        int second = currentCalendar.get(Calendar.SECOND);
        int millis = currentCalendar.get(Calendar.MILLISECOND);

        if (Objects.equals(type, Dates.YEAR)) {
            month = 0;
            day = 1;
            hour = 0;
            minute = 0;
            second = 0;
            millis = 0;
        } else if (Objects.equals(type, Dates.MONTH)) {
            day = 1;
            hour = 0;
            minute = 0;
            second = 0;
            millis = 0;
        } else if (Objects.equals(type, Dates.DAY)) {
            hour = 0;
            minute = 0;
            second = 0;
            millis = 0;
        } else if (Objects.equals(type, Dates.HOUR)) {
            minute = 0;
            second = 0;
            millis = 0;
        } else if (Objects.equals(type, Dates.MINUTE)) {
            second = 0;
            millis = 0;
        } else if (Objects.equals(type, Dates.SECOND)) {
            millis = 0;
        } else if (Objects.equals(type, Dates.MSEC)) {
            /*
             * Timestampの時だけナノ秒を落とす それ以外は、ナノ秒の精度をもっていないので、落とす必要がない
             */
            if (date instanceof java.sql.Timestamp) {
                // ナノ秒を落とす
                java.sql.Timestamp timestamp = (java.sql.Timestamp) date;
                millis = timestamp.getNanos() / NANOS_2_MSEC;
            }
        }

        currentCalendar.set(year, month, day, hour, minute, second);
        currentCalendar.set(Calendar.MILLISECOND, millis);

        long afterMillis = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), afterMillis);
    }

    /**
     * <p>
     * 指定した日付の月末を返します.
     * </p>
     * @param date 対象の日付
     * @return 対象日付の月の月末の日
     */
    public static Date endOfMonth(Date date) {

        Calendar currentCalendar = Dates.getCalendarInstance(date);
        int day = DAY_OF_MONTH[currentCalendar.get(Calendar.MONTH)];

        if (!isLeapYear(currentCalendar.get(Calendar.YEAR))
            && currentCalendar.get(Calendar.MONTH) == 1) {
            day--;
        }

        currentCalendar.set(currentCalendar.get(Calendar.YEAR),
                            currentCalendar.get(Calendar.MONTH), day, currentCalendar
                                .get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE),
                            currentCalendar.get(Calendar.SECOND));
        currentCalendar.set(Calendar.MILLISECOND, 0);

        long time = currentCalendar.getTimeInMillis();

        /*
         * パラメータの型のまま返すために、こうする Calendarから取得しちゃうと、インスタンスがjava.util.Dateになってしまう
         */
        return instantiateDate(date.getClass(), time);

    }

    /**
     * 2つの指定した日付範囲が重なっているかチェックします.
     * @param fromDate1 日付範囲１開始日
     * @param toDate1 日付範囲１終了日
     * @param fromDate2 日付範囲2開始日
     * @param toDate2 日付範囲2終了日
     * @return true:重なっている、false:重なっていない
     */
    public static boolean isDateOverlap(Date fromDate1, Date toDate1, Date fromDate2, Date toDate2) {

        fromDate1 = truncate(fromDate1, Dates.DAY);
        toDate1 = truncate(toDate1, Dates.DAY);
        fromDate2 = truncate(fromDate2, Dates.DAY);
        toDate2 = truncate(toDate2, Dates.DAY);
        if ((fromDate1.equals(fromDate2) || toDate1.equals(toDate2) || // 開始日、終了日がそれぞれ同じ日か？
            fromDate1.equals(toDate2) || toDate1.equals(fromDate2))
            || (fromDate1.before(fromDate2) && toDate1.after(fromDate2)) || // または、開始日１～終了日１の間に開始日２があるか？
            (fromDate2.before(fromDate1) && toDate2.after(fromDate1))) // または、開始日２～終了日２の間に開始日１があるか？
            return true;
        return false;
    }

    /**
     * 日付範囲2が日付範囲1に完全に含まれているかチェックします.
     * @param fromDate1 日付範囲１開始日
     * @param toDate1 日付範囲１終了日
     * @param fromDate2 日付範囲2開始日
     * @param toDate2 日付範囲2終了日
     * @return true:含まれている、false:含まれていない
     */
    public static boolean isDateInclude(Date fromDate1, Date toDate1, Date fromDate2, Date toDate2) {

        fromDate1 = truncate(fromDate1, Dates.DAY);
        toDate1 = truncate(toDate1, Dates.DAY);
        fromDate2 = truncate(fromDate2, Dates.DAY);
        toDate2 = truncate(toDate2, Dates.DAY);
        if ((fromDate1.before(fromDate2) || fromDate1.equals(fromDate2))
            && // 開始日1～終了日1の間に開始日2があるか？
            (toDate1.after(fromDate2) || toDate1.equals(fromDate2))
            && (fromDate1.before(toDate2) || fromDate1.equals(toDate2)) && // かつ、開始日1～終了日1の間に終了日2があるか？
            (toDate1.after(toDate2) || toDate1.equals(toDate2)))
            return true;
        return false;
    }

    /**
     * 日付が月に対して有効かチェックします.
     * @param year 評価する年
     * @param month 評価する月
     * @param day 評価する日
     * @return 指定した日付の日にちが日付として正常な場合はtrue、 そうでない場合はfalseを返します.
     */
    protected static boolean enableDayOfMonth(int year, int month, int day) {

        int maxDay = DAY_OF_MONTH[month - 1];
        return day <= maxDay && enableLeapYear(year, month, day);
    }

    /**
     * 指定した日付がうるう年として有効かチェックします.
     * @param year 評価する年
     * @param month 評価する月
     * @param day 評価する日
     * @return 指定した日付がうるう年として正常な場合、 またはうるう年でない場合はtrue、 そうでない場合はfalseを返します.
     */
    protected static boolean enableLeapYear(int year, int month, int day) {

        if (month == 2) {
            boolean result = Dates.isLeapYear(year);
            if (!result && day == 29) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 指定した数値の範囲が、指定した最大と最小の数値範囲かどうかをまとめてチェックします.<br>
     * 全ての配列は同じ要素数である必要があります.
     * @param max 範囲の最大値（配列）
     * @param min 範囲の最小値（配列）
     * @param values 評価する値（配列）
     * @return 配列の値が全て範囲内であればtrue、そうでない場合はfalseを返します.
     */
    protected static boolean enableRange(int[] max, int[] min, int[] values) {

        for (int index = 0; index <= values.length - 1; index++) {
            if (values[index] > max[index] || values[index] < min[index]) {
                return false;
            }
        }
        return true;
    }


}

/**
 * <p>
 * 日付型.
 * </p>
 * @author ITOCHU Techno-Solutions Corporation
 */
class DateType {

    /**
     * 名前.
     */
    private String name;

    /**
     * コンストラクタ
     * @param name 名前
     */
    DateType(String name) {

        this.name = name;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return this.name;
    }

}
