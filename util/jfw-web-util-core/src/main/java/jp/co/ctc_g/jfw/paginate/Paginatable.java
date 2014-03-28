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

/**
 * <p>
 * このインタフェースは、ページング結果を{@link jp.co.ctc_g.jfw.core.util.PartialList}に加工する際に
 * 必要となるページの開始位置やページの終了位置、1ページに表示する件数を取得します。
 * </p>
 * <p>
 * 
 * </p>
 * @see PaginatableSupport
 * @see Paginates
 */
public interface Paginatable {

    /**
     * データを取得するページの開始位置を取得します。
     * @return ページの開始位置
     */
    int getOffset();
    
    /**
     * データを取得するページの開始位置を設定します。
     * @param offset ページの開始位置
     */
    void setOffset(int offset);
    
    /**
     * 1ページに表示するデータ件数を取得します。
     * @return 1ページに表示するデータ件数
     */
    int getLimit();
    
    /**
     * 1ページに表示するデータ件数を設定します。
     * @param limit 1ページに表示するデータ件数
     */
    void setLimit(int limit);
    
    /**
     * データを取得するページの終了位置を取得します。
     * @return ページの終了位置
     */
    int getTail();
    
    /**
     * データを取得するページの終了位置を設定します。
     * @param tail ページの終了位置
     */
    void setTail(int tail);
}
