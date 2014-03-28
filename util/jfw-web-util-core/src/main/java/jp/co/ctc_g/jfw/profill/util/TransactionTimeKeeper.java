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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * このクラスは、トランザクション開始時刻を保持します。
 * 主として {@link TransactionTimeInterceptor} から利用されています。
 * </p>
 * @see TransactionTimeInterceptor
 * @see UpdateStampProvider
 */
public final class TransactionTimeKeeper {

    private TransactionTimeKeeper() {

    }

    /*
     * 最初のトランザクションが開始された時刻です。
     */
    private static final ThreadLocal<Timestamp> BEGIN_TIME = new ThreadLocal<Timestamp>();

    /*
     * トランザクションがネストされた回数です。
     * あるトランザクションから別のトランザクションが発行されたりしても、
     * 最初のトランザクションの終了を正しく検出できるようにするためのカウンタです。
     */
    private static final ThreadLocal<Integer> COUNTER = new ThreadLocal<Integer>();

    /**
     * トランザクションを開始したことを通知します。
     * もし既存のトランザクションがなければ、
     * 開始時間を格納します。
     * もし既存のトランザクションがあれば、何もせずに既存のトランザクションの開始時間を返却します。
     * @return トランザクション開始時間
     */
    public static Timestamp beginTransaction() {
        return beginTransaction(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * トランザクションを開始したことを通知します。
     * もし既存のトランザクションがなければ、
     * 指定された開始時間を格納します。
     * もし既存のトランザクションがあれば、
     * 何もせずに既存のトランザクションの開始時間を返却します。
     * @param beginTime 直近のトランザクションの開始時間
     * @return トランザクション開始時間
     */
    public static Timestamp beginTransaction(Timestamp beginTime) {
        Timestamp stamp = BEGIN_TIME.get();
        if (stamp == null) {
            stamp = beginTime;
            BEGIN_TIME.set(stamp);
            COUNTER.set(0);
        } else {
            COUNTER.set(COUNTER.get().intValue() + 1);
        }
        return stamp;
    }

    /**
     * トランザクション開始時刻を {@link Timestamp} 型で取得します。
     * @return トランザクション開始時刻
     */
    public static Timestamp getBeginTimeAsTimestamp() {
        return BEGIN_TIME.get();
    }

    /**
     * トランザクション開始時刻を {@link Date} 型で取得します。
     * @return トランザクション開始時刻
     */
    public static Date getBeginTimeAsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(BEGIN_TIME.get().getTime());
        calendar.set(Calendar.YEAR, 1970);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * トランザクションを終了したことを通知します。
     * もしトランザクションがネストされていない場合には、
     * このメソッドはトランザクション終了時刻を返却します。
     * もしトランザクションがネストされていた場合には、
     * このメソッドは <code>null</code>を返却します。
     * なお、このメソッドが返却するトランザクション終了時間は、
     * ローカルサーバ時間です。
     * @return トランザクション終了時刻
     */
    public static Timestamp endTransaction() {
        if (COUNTER.get().intValue() > 0) {
            COUNTER.set(COUNTER.get() - 1);
            return null;
        } else {
            BEGIN_TIME.set(null);
            COUNTER.set(null);
            Timestamp stamp = new Timestamp(System.currentTimeMillis());
            return stamp;
        }
    }

    /**
     * スレッドローカル変数を削除します。
     */
    public static void remove() {
        BEGIN_TIME.remove();
        COUNTER.remove();
    }
}
