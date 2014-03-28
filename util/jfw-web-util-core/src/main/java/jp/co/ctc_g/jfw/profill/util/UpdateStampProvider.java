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
import java.util.Date;

import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverters;
import jp.co.ctc_g.jfw.profill.FillingProvider;
import jp.co.ctc_g.jfw.profill.MatchedProperty;
import jp.co.ctc_g.jfw.profill.ProvideFillingFor;

/**
 * <p>
 * このクラスは、更新日時情報を Java ビーンに設定するためのプロバイダです。
 * たとえば Java ビーンが RDBMS のテーブルを表現している場合、
 * 更新日時カラムに対応する更新日時プロパティがあるでしょう。
 * このプロバイダはそのプロパティに自動的に最適な値を格納します。
 * その際、プロパティは {@link UpdateStamp} アノテーションで修飾する必要があります。
 * <pre class="brush:java">
 * &#64;UpdateStamp
 * public void setUpdateStamp(Timestamp value) {
 *     updateStamp = value;
 * }
 * </pre>
 * 更新日時を {@link Date} 型で管理している場合には、そのように型を宣言すれば自動的に変換されます。
 * <pre class="brush:java">
 * &#64;UpdateStamp
 * public void setUpdateDate(Date value) {
 *     updateDate = value;
 * }
 * </pre>
 * 実際には {@link TypeConverters} で変換可能であればどのような型でも対応しています。
 * </p>
 * 
 * <h2>データベーストランザクションとの同期</h2>
 * <p>
 * このプロバイダは、デフォルトでは、更新日時として現在時刻を利用します。
 * ただし、たいていの Web アプリケーションでは、
 * 更新日時とは<strong>トランザクション開始時刻</strong>です。
 * そこでこのプロバイダはトランザクションと同期できるように設計されています。
 * <pre class="brush:java">
 * LocalTimeUpdateStampProvider provider = new LocalTimeUpdateStampProvider();
 * provider.setTransactionAware(true);
 * </pre>
 * DI コンテナに登録して利用している場合には、
 * <pre class="brush:xml">
 * &lt;bean class="jp.co.ctc_g.jfw.profill.util.LocalTimeUpdateStampProvider"&gt;
 *   &lt;property name="transactionAware" value="true" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * です。注意点として、トランザクションと同期するには {@link TransactionTimeInterceptor} を、
 * トランザクション境界と同じポイントカットに適用する必要があります。
 * 適用方法については {@link TransactionTimeInterceptor} の Javadoc を参照してください。
 * また、アプリケーションサーバがクラスタ構成であるなどの理由により、
 * 更新日時を別のサーバに問い合わせたりする場合があります。
 * その場合は、{@link TransactionTimeGenerator} を利用してください。
 * </p>
 * @see TransactionTimeInterceptor
 * @see TransactionTimeKeeper
 */
@ProvideFillingFor(annotation = UpdateStamp.class)
public class UpdateStampProvider implements FillingProvider {

    private boolean transactionAware;
    private boolean useStrictType;
    
    /**
     * デフォルトコンストラクタです。
     */
    public UpdateStampProvider() {}

    /**
     * {@inheritDoc}
     * <p>
     * プロパティが {@link Date} 型で宣言されていた場合、
     * デフォルトでは精度保持のため {@link Timestamp} インスタンスを設定します。
     * ただし、「{@link Date} と {@link Timestamp} は概念継承ではなく機能継承」であるため、
     * 同値性やハッシュ値の問題があります。
     * そのため型互換性がないものとしてプログラミングすることが
     * 推奨されています(詳細は {@link Timestamp} の Javadoc をご覧ください)。
     * これらを踏まえて、プロパティが {@link Date} 型で宣言されていた場合
     * かつ {@link #isUseStrictType()} が <code>true</code> の場合、
     * {@link Date} インスタンスを設定します。
     * <p>
     */
    @Override
    public Object provide(MatchedProperty property, Object bean) {
        Class<?> type = property.getType();
        if ((type == Timestamp.class) || (type == Date.class && !useStrictType)) {
            return getTimestamp();
        } else if (type == Date.class && useStrictType) {
            return getDate();
        } else {
            return TypeConverters.convert(getTimestamp(), type);
        }
    }
    
    /**
     * 更新日時を取得します。
     * @return 更新日時
     */
    protected Timestamp getTimestamp() {
        return transactionAware ? getSynchronizedTransactionTimestamp() : getLocalTimestamp();
    }
    
    /**
     * 更新日時を取得します。
     * @return 更新日時
     */
    protected Date getDate() {
        return transactionAware ? getSynchronizedTransactionDate() : getLocalDate();
    }
    
    /**
     * トランザクションと同期した更新日時を返却します。
     * @return トランザクションと同期した更新日時
     */
    protected Timestamp getSynchronizedTransactionTimestamp() {
        return TransactionTimeKeeper.getBeginTimeAsTimestamp();
    }
    
    /**
     * トランザクションと同期した更新日時を返却します。
     * @return トランザクションと同期した更新日時
     */
    protected Date getSynchronizedTransactionDate() {
        return TransactionTimeKeeper.getBeginTimeAsDate();
    }
    
    /**
     * トランザクションと同期していない更新日時を返却します。
     * @return トランザクションと同期していない更新日時
     */
    protected Timestamp getLocalTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * トランザクションと同期していない更新日時を返却します。
     * @return トランザクションと同期していない更新日時
     */
    protected Date getLocalDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * トランザクションと同期するかどうかを返却します。
     * @return トランザクションと同期するかどうか
     */
    public boolean isTransactionAware() {
        return transactionAware;
    }

    /**
     * トランザクションと同期するかどうかを設定します。
     * @param transactionAware トランザクションと同期するかどうか
     */
    public void setTransactionAware(boolean transactionAware) {
        this.transactionAware = transactionAware;
    }

    /**
     * {@link Date} 型のプロパティに対して、
     * {@link Timestamp} インスタンスを許可するかどうかを返却します。
     * <strong>{@link Date} 型プロパティに厳密に {@link Date} インスタンスを要求する</strong>場合、
     * <code>true</code> を返却します。
     * @return {@link Date} 型のプロパティに対して {@link Timestamp} インスタンスを許可するかどうか
     */
    public boolean isUseStrictType() {
        return useStrictType;
    }

    /**
     * {@link Date} 型のプロパティに対して、
     * {@link Timestamp} インスタンスを許可するかどうかを設定します。
     * <strong>{@link Date} 型プロパティに厳密に {@link Date} インスタンスを要求する</strong>場合、
     * <code>true</code> を設定します。
     * @param useStrictType {@link Date} 型のプロパティに対して {@link Timestamp} インスタンスを許可するかどうか
     */
    public void setUseStrictType(boolean useStrictType) {
        this.useStrictType = useStrictType;
    }
}
