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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * このクラスはある順序付けられた集合(リスト)から取り出された部分集合を表現します。
 * よって、このクラスにより表現される順序付けられた要素の集合には、
 * より大きいあるいは同じ大きさの全体集合が背後にあると暗黙的に認められます。
 * </p>
 * <p>
 * このクラスは、
 * JDKのコレクションフレームワークとして動作させることもできます。
 * ですが、部分集合であることを表現するため、以下のプロパティが定義されています。
 * </p>
 * <table>
 *   <thead>
 *     <tr>
 *       <th>プロパティ名</th><th>役割</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>elementCount</td>
 *       <td>背後にある全体集合の要素数</td>
 *     </tr>
 *     <tr>
 *       <td>currentPartNumber</td>
 *       <td>この部分集合の全体集合に対するインデクス(1..N)</td>
 *     </tr>
 *     <tr>
 *       <td>partCount</td>
 *       <td>背後にある全体集合を構成する部分集合の総数</td>
 *     </tr>
 *     <tr>
 *       <td>elementCountPerPart</td>
 *       <td>1つの部分集合に含まれる要素数</td>
 *     </tr>
 *   </tbody>
 * </table>
 * <p>
 * 例えば、要素数100の全体集合から1つの部分集合につき20要素が含まれる場合を考えてみます。
 * その場合、elementCountは100、elementCountPerPartは20、そして、
 * partCountはelementCountをelementCountPerPartで割ったものに等しいので、5となります。
 * </p>
 * <p>
 * このクラスは上記プロパティの制御に関する知識を何も持ちません。
 * 値の形式について(負の数でないか、0でないかなど)の検証は実行しますが、
 * 例えば、elementCountPerPartを10から20に変更したからといって、
 * このクラスが動的に全体集合を参照し、
 * 新しい10要素を取得して自身の要素数を20にするなどということはありません。
 * </p>
 * <p>
 * このクラスは値を保持することだけに専念するため、
 * 背後にある全体集合の形式は問いません。
 * 例えば、この方法が最も頻繁に利用されると思いますが、
 * <b>データベースへの問い合わせ結果</b>でも構いません。
 * その場合は、概念的には部分集合はページの概念に一致します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @param <E> このコンテナクラスが格納する要素の型
 */
public class PartialList<E> implements List<E>, Serializable {

    private static final long serialVersionUID = -3019211327244722216L;

    private int partIndex;
    private int elementCount;
    private int partCount;
    private int elementCountParPart;

    private List<E> substance;

    /**
     * デフォルトのリスト実装を利用してこのクラスのインスタンスを作成します。
     * デフォルトのリスト実装は{@link ArrayList}です。
     */
    public PartialList() {
        this.substance = new ArrayList<E>();
    }

    /**
     * デフォルトのリスト実装にサイズを指定してこのクラスのインスタンスを作成します。
     * デフォルトのリスト実装は{@link ArrayList}です。
     * @param size リストのサイズ
     */
    public PartialList(int size) {
        this.substance = new ArrayList<E>(size);
    }

    /**
     * 指定されたリスト実装を利用してこのクラスのインスタンスを作成します。
     * @param substance リスト実装オブジェクト
     */
    public PartialList(List<E> substance) {
        this.substance = substance;
    }

    // 部分集合プロパティ --------------------------------------------------------------

    /**
     * 背後にある全体集合の要素数を返却します。
     * @return 背後にある全体集合の要素数
     */
    public int getElementCount() {
        return elementCount;
    }

    /**
     * 背後にある全体集合の要素数を設定します。
     * このメソッドの引数に負の数を指定することはできません。
     * @param elementCount 背後にある全体集合の要素数
     * @throws IllegalArgumentException 負の数が指定された場合
     */
    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    /**
     * この部分集合の全体集合に対するインデクス(1..N)を返却します。
     * このコンテナが要素を保持していない場合など現在の部分インデクスが不明確な場合、
     * このメソッドが0を返却する場合があります。
     * とはいえ、要素数が空であるかどうかは、{{@link #size()}や{@link #isEmpty()}を利用するべきであり、
     * 要素数の確認のためにこのメソッドを呼び出すべきではありません。
     * @return この部分集合の全体集合に対するインデクス(1..N)
     */
    public int getPartIndex() {
        return partIndex;
    }

    /**
     * この部分集合の全体集合に対するインデクス(1..N)を設定します。
     * このメソッドの引数に負の数を指定することはできません。
     * このコンテナが要素を保持していない場合など現在の部分インデクスが不明確であることを表現するために、
     * 0を設定することが許されます。
     * @param currentPartIndex この部分集合の全体集合に対するインデクス(1..N)
     * @throws IllegalArgumentException 負の数が指定された場合
     */
    public void setPartIndex(int currentPartIndex) {
        this.partIndex = currentPartIndex;
    }

    /**
     * 背後にある全体集合を構成する部分集合の総数を返却します。
     * もしelementCountPerPartプロパティが5でelementCountが9である場合、partCountは2となります。
     * つまり、全ての要素を充分に収容可能なように算出されます。
     * このコンテナが要素を保持していない場合、このメソッドが0を返却します。
     * とはいえ、要素数が空であるかどうかは、{{@link #size()}や{@link #isEmpty()}を利用するべきであり、
     * 要素数の確認のためにこのメソッドを呼び出すべきではありません。
     * @return 背後にある全体集合を構成する部分集合の総数
     */
    public int getPartCount() {
        return partCount;
    }

    /**
     * 背後にある全体集合を構成する部分集合の総数を設定します。
     * このメソッドの引数に負の数を指定することはできません。
     * このコンテナの背後にある全体集合の要素数が0の場合、
     * このメソッドには0を設定することが許されます。
     * @param partCount 背後にある全体集合を構成する部分集合の総数
     * @throws IllegalArgumentException 負の数が指定された場合
     */
    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }

    /**
     * 1つの部分集合に含まれる要素数を返却します。
     * このメソッドは正の整数のみを返却し、負の数や0を返却することはありません。
     * @return 1つの部分集合に含まれる要素数
     */
    public int getElementCountPerPart() {
        return elementCountParPart;
    }

    /**
     * 1つの部分集合に含まれる要素数を設定します。
     * このメソッドの引数には正の整数のみが許容され、負の数や0を指定することはできません。
     * @param elementCountPerPart 1つの部分集合に含まれる要素数
     * @throws IllegalArgumentException 負の数や0が指定された場合
     */
    public void setElementCountPerPart(int elementCountPerPart) {
        this.elementCountParPart = elementCountPerPart;
    }

    /**
     * この部分集合が保持する先頭要素の全体集合における位置を返却します。
     * @return この部分集合が保持する先頭要素の全体集合における位置
     */
    public int getElementBeginIndex() {
        return (getPartIndex() - 1) * getElementCountPerPart() + 1;
    }

    /**
     * この部分集合が保持する先頭要素の全体集合における位置を返却します。
     * @return この部分集合が保持する先頭要素の全体集合における位置
     */
    public int getElementEndIndex() {
        return (getPartIndex() - 1) * getElementCountPerPart() + size();
    }

    // タグライブラリ公開用静的ユーティリティメソッド ----------------------------------------

    // リストインタフェース実装 ----------------------------------------------------------

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(E o) {
        return substance.add(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, E element) {
        substance.add(index, element);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends E> c) {
        return substance.addAll(c);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        return substance.addAll(index, c);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#clear()
     */
    public void clear() {
        substance.clear();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return substance.contains(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> c) {
        return substance.containsAll(c);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return substance.equals(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#get(int)
     */
    public E get(int index) {
        return substance.get(index);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#hashCode()
     */
    public int hashCode() {
        return substance.hashCode();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return substance.indexOf(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return substance.isEmpty();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#iterator()
     */
    public Iterator<E> iterator() {
        return substance.iterator();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return substance.lastIndexOf(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#listIterator()
     */
    public ListIterator<E> listIterator() {
        return substance.listIterator();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<E> listIterator(int index) {
        return substance.listIterator(index);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#remove(int)
     */
    public E remove(int index) {
        return substance.remove(index);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return substance.remove(o);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        return substance.removeAll(c);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        return substance.retainAll(c);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#set(int, java.lang.Object)
     */
    public E set(int index, E element) {
        return substance.set(index, element);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#size()
     */
    public int size() {
        return substance.size();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#subList(int, int)
     */
    public List<E> subList(int fromIndex, int toIndex) {
        return substance.subList(fromIndex, toIndex);
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return substance.toArray();
    }

    /**
     * {@inheritDoc}
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * @see java.util.List#toArray(Object[])
     */
    public <T> T[] toArray(T[] a) {
        return substance.toArray(a);
    }

    // ---------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * このメソッドは、内部のリスト実装への委譲として実装されています。
     */
    @Override
    public String toString() {
        return substance.toString();
    }

}
