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

package jp.co.ctc_g.jfw.paginate;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.web.util.internal.WebUtilInternals;

/**
 * <p>
 * このクラスは、ページングを行う際にページ番号をもとにページの開始位置や終了位置を計算する機能を提供します。
 * </p>
 * <p>
 * デフォルトでは1ページあたりの表示件数は10件に設定されていますが、コンフィグレーションをオーバライドすることにより、
 * 全体の設定を変更することができます。
 * 変更する際はFrameworkReresources.propertiesなどのプロパティファイルに
 * キー：jp.co.ctc_g.jfw.paginate.Paginates.display_records_per_pageで値をオーバライドしてください。
 * また、全体の設定は変更せずに特定のページングのみ表示件数を変更したい場合は、
 * {@link Paginates#set(Paginatable, int, int)}を利用し、
 * ページの開始位置の計算時指定してください。
 * </p>
 */
public final class Paginates {

    private Paginates() {

    }

    /**
     * 1ページあたりの表示件数です。デフォルトでは10件に設定されています。
     * 設定を変更する場合はキー：jp.co.ctc_g.jfw.paginate.Paginates.display_records_per_pageで値をオーバライドしてください。
     */
    public static final int DISPLAY_RECORDS_PER_PAGE;

    static {
        Config c = WebUtilInternals.getConfig(Paginates.class);
        DISPLAY_RECORDS_PER_PAGE = Integer.valueOf(c.find("display_records_per_page")).intValue();
    }

    /**
     * <p>
     * この列挙型は、ページの開始番号を0から始めるか1から始めるかを表します。
     * </p>
     */
    public enum Origin {

        /**
         * ページの開始番号が0を表します。
         */
        ZERO(0),

        /**
         * ページの開始番号が1を表します。
         * J-Frameworkのページング機構では、Origin.FIRSTがデフォルト値とします。
         */
        FIRST(1);

        private int origin;

        private Origin(int origin) {
            this.origin = origin;
        }

        /**
         * ページの開始番号を取得します。
         * @return ページの開始番号
         */
        public int getOrigin() {
            return origin;
        }

        /**
         * 指定されたページの開始位置であるかどうかを返します。
         * 開始位置÷1ページあたりの表示件数のあまりが開始番号であるかどうかを判断します。
         * @param offset ページの開始位置
         * @param limit 1ページあたりの表示件数
         * @return 開始番号であるかどうか true:開始番号、false:それ以外
         */
        public boolean isBeginPageIndex(int offset, int limit) {
            return limit == 1 || offset % limit == origin;
        }
    }

    /**
     * ページの開始位置と終了位置から1ページあたりの表示件数を計算します。
     * ページの開始位置がページの終了位置より大きい値が指定された場合は、例外が発生します。
     * @param offset ページの開始位置
     * @param tail ページの終了位置
     * @return 1ページ当たりの表示件数
     */
    public static int getElementCountPerPart(int offset, int tail) {
        if (offset > tail) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0003",
                    Paginates.class,
                    Maps.hash("offset", String.valueOf(offset)).map("tail", String.valueOf(tail)));
        }
        return tail - offset + 1;
    }

    /**
     * ページの開始位置とページの終了位置から1ページあたりの件数を計算します。
     * @param offset ページの開始位置
     * @param limit ページの終了位置
     * @param total 総数
     * @return ページのインデクス
     */
    public static int getPartCount(int offset, int limit, int total) {
        return (int) Math.ceil((double) total / limit);
    }

    /**
     * ページの開始位置と終了位置、開始番号から現在のページのインデクスを計算します。
     * 例えば、ページの開始位置が11で終了位置が20、ページの開始番号がOrigin.FIRSTのときには1を返します。
     * @param offset ページの開始位置
     * @param limit ページの終了位置
     * @param total 総数
     * @return ページのインデクス
     */
    public static int getPartIndex(int offset, int limit, int total) {
        return getPartIndex(offset, limit, total, Origin.FIRST);
    }

    /**
     * ページの開始位置と終了位置、開始番号から現在のページのインデクスを計算します。
     * 例えば、ページの開始位置が11で終了位置が20、ページの開始番号がOrigin.FIRSTのときには1を返します。
     * @param offset ページの開始位置
     * @param limit ページの終了位置
     * @param total 総数
     * @param origin ページの開始番号
     * @return ページのインデクス
     */
    public static int getPartIndex(int offset, int limit, int total, Origin origin) {
        if (!origin.isBeginPageIndex(offset, limit)) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0002",
                    Paginates.class,
                    Maps.hash("offset", String.valueOf(offset)).map("limit", String.valueOf(limit)));
        }
        return (int) Math.ceil((double) offset / limit) - (origin.getOrigin() - 1);
    }

    /**
     * ページ番号と1ページあたりの表示件数から開始位置を計算します。
     * 例えば、ページ番号が2で1ページあたりの表示件数が20のときには21を返します。
     * ページ番号や1ページあたりの表示件数が1よりも小さいときは例外が発生します。
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     * @return 開始位置
     */
    public static int getOffset(int partNumber, int elementCountPerPart) {
       return getOffset(partNumber, elementCountPerPart, Origin.FIRST);
    }

    /**
     * ページ番号と1ページあたりの表示件数から開始位置を計算します。
     * 例えば、ページ番号が2で1ページあたりの表示件数が20のときには21を返します。
     * ページ番号や1ページあたりの表示件数が1よりも小さいときは例外が発生します。
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     * @param origin ページの開始番号
     * @return 開始位置
     */
    public static int getOffset(int partNumber, int elementCountPerPart, Origin origin) {
        if (partNumber < 1) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0004",
                    Paginates.class,
                    Maps.hash("partNumber", String.valueOf(partNumber)));
        }
        if (elementCountPerPart < 1) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0005",
                    Paginates.class,
                    Maps.hash("elementCountPerPart", String.valueOf(elementCountPerPart)));
        }
        return (partNumber - 1) * elementCountPerPart + origin.getOrigin();
    }

    /**
     * ページ番号と1ページあたりの表示件数からページの終了位置を計算します。
     * 例えば、ページ番号が2で1ページあたりの表示件数が20のときには40を返します。
     * ページ番号や1ページあたりの表示件数が1よりも小さいときは例外が発生します。
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     * @return ページの終了位置
     */
    public static int getTail(int partNumber, int elementCountPerPart) {
        return getTail(partNumber, elementCountPerPart, Origin.FIRST);
    }

    /**
     * ページ番号と1ページあたりの表示件数からページの終了位置を計算します。
     * 例えば、ページ番号が2で1ページあたりの表示件数が20のときには40を返します。
     * ページ番号や1ページあたりの表示件数が1よりも小さいときは例外が発生します。
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     * @param origin ページの開始番号
     * @return ページの終了位置
     */
    public static int getTail(int partNumber, int elementCountPerPart, Origin origin) {
        if (partNumber < 1) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0004",
                    Paginates.class,
                    Maps.hash("partNumber", String.valueOf(partNumber)));
        }
        if (elementCountPerPart < 1) {
            throw new UncalculatablePagingException(
                    "E-PAGINATE#0005",
                    Paginates.class,
                    Maps.hash("elementCountPerPart", String.valueOf(elementCountPerPart)));
        }
        return partNumber * elementCountPerPart + (origin.getOrigin() - 1);
    }

    /**
     * ページ番号と{@link Paginatable}インタフェースを用いてページの開始位置や終了位置を計算します。
     * @param paginatable {@link Paginatable}インタフェース
     * @param partNumber ページ番号
     */
    public static void set(Paginatable paginatable, int partNumber) {

        set(paginatable, partNumber, DISPLAY_RECORDS_PER_PAGE, Origin.FIRST);
    }

    /**
     * ページ番号と1ページあたりの表示件数、{@link Paginatable}インタフェースを用いてページの開始位置や終了位置を計算します。
     * @param paginatable {@link Paginatable}インタフェース
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     */
    public static void set(Paginatable paginatable, int partNumber, int elementCountPerPart) {
        set(paginatable, partNumber, elementCountPerPart, Origin.FIRST);
    }

    /**
     * ページ番号と1ページあたりの表示件数、ページ開始番号、{@link Paginatable}インタフェースを用いてページの開始位置や終了位置を計算します。
     * @param paginatable {@link Paginatable}インタフェース
     * @param partNumber ページ番号
     * @param elementCountPerPart 1ページあたりの表示件数
     * @param origin ページ開始番号
     */
    public static void set(Paginatable paginatable, int partNumber, int elementCountPerPart, Origin origin) {
        int offset = getOffset(partNumber, elementCountPerPart, origin);
        int tail = getTail(partNumber, elementCountPerPart, origin);
        int limit = elementCountPerPart;
        paginatable.setOffset(offset);
        paginatable.setLimit(limit);
        paginatable.setTail(tail);
    }
}
