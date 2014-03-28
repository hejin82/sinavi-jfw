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

package jp.co.ctc_g.jfw.core.jdbc;

import java.util.List;

import jp.co.ctc_g.jfw.core.util.PartialList;
import jp.co.ctc_g.jfw.paginate.Paginatable;
import jp.co.ctc_g.jfw.paginate.Paginates;
import jp.co.ctc_g.jfw.paginate.Paginates.Origin;

import org.apache.ibatis.session.RowBounds;

/**
 * <p>
 * このクラスは、ページング結果を {@link PartialList} に加工する {@link PaginatedResultHandler}の実装クラスです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see Paginatable
 * @see Paginates
 * @see Origin
 */
public class PartialListResultHandler implements PaginatedResultHandler {

    private Origin paginatableOrigin = Origin.FIRST;
    private Origin rowBoundsOrigin = Origin.ZERO;

    /**
     * デフォルトコンストラクタです。
     */
    public PartialListResultHandler() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> createPaginatedResult(
            String statement,
            Paginatable parameter,
            Integer total,
            List<E> result) {
        int offset = parameter.getOffset();
        int limit = parameter.getLimit();
        return create(offset, limit, total, result, paginatableOrigin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> createPaginatedResult(
            String statement,
            Object parameter,
            RowBounds rowBounds,
            Integer total,
            List<E> result) {
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        return create(offset, limit, total, result, rowBoundsOrigin);
    }

    /**
     * 指定された引数から {@link PartialList}のプロパティ値を計算してインスタンスを生成します。
     * プロパティ値の計算には {@link Paginates} を利用しています。
     * @param offset 検索結果オフセット
     * @param limit 検索結果リミット
     * @param total 検索結果件数
     * @param result 検索結果
     * @param origin オフセットのオリジン（0オリジン or 1オリジン）
     * @return {@link PartialList}のインスタンス
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <E> PartialList<E> create(int offset, int limit, int total, List<E> result, Origin origin) {
        PartialList partial = new PartialList(result);
        partial.setElementCount(total);
        partial.setElementCountPerPart(limit);
        partial.setPartCount(Paginates.getPartCount(offset, limit, total));
        partial.setPartIndex(Paginates.getPartIndex(offset, limit, total, origin));
        return partial;
    }

    /**
     * {@link Paginatable}が利用された場合のオリジンを返却します。
     * @return オリジン
     * @see Origin
     */
    public String getPaginatableOrigin() {
        return paginatableOrigin.name();
    }

    /**
     * {@link Paginatable}が利用された場合のオリジンを設定します。
     * @param paginatableOrigin オリジン
     * @see Origin
     */
    public void setPaginatableOrigin(String paginatableOrigin) {
        this.paginatableOrigin = Origin.valueOf(paginatableOrigin);
    }

    /**
     * {@link RowBounds}が利用された場合のオリジンを返却します。
     * @return オリジン
     * @see Origin
     */
    public String getRowBoundsOrigin() {
        return rowBoundsOrigin.name();
    }

    /**
     * {@link RowBounds}が利用された場合のオリジンを設定します。
     * @param rowBoundsOrigin オリジン
     * @see Origin
     */
    public void setRowBoundsOrigin(String rowBoundsOrigin) {
        this.rowBoundsOrigin = Origin.valueOf(rowBoundsOrigin);
    }
}
