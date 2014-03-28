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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * このクラスは、Strutsのフォームに対するリクエストパラメータのポピュレーション時に、
 * 安全にリスト型で受け取るためのユーティリティです。
 * </p>
 * <p>
 * 例えば、以下のようなフォーム要素があるとします。
 * </p>
 * <pre>
 * &lt;html:form action="/com/com/sample"&gt;
 *     &lt;html:text property="params[0]" /&gt;
 *     &lt;html:text property="params[1]" /&gt;
 * &lt;/form&gt;
 * </pre>
 * <p>
 * そして、このパラメータを以下のように受け取るとします。
 * </p>
 * <pre>
 * public class SampleForm extends BrandForm {
 *
 *     private List&lt;String&gt; params;
 *
 *     public SampleForm() {
 *         params = new ArrayList();
 *     }
 *
 *     // プロパティ省略
 * }
 * </pre>
 * <p>
 * この場合、Strutsは指定されているフォームに対して、
 * param[0]とparam[1]でポピュレーションしてきます。
 * その際に、Strutsはリストのサイズをチェックしません。
 * これにより、paramsのインデクス番号1に対して直接セットしてくる可能性があります。
 * もちろん、その場合は例外が発生します。
 * これを防ぐために、以下のようにします。
 * </p>
 * <pre>
 * public class SampleForm extends BrandForm {
 *
 *     private List&lt;String&gt; params;
 *
 *     public SampleForm() {
 *         params = new PopulationSafeList&lt;String&gt;(new ArrayList&lt;String&gt;());
 *     }
 *
 *     // プロパティ省略
 * }
 * </pre>
 * <p>
 * Strutsがインデクス番号1に最初にセットしようとした場合、
 * 上記コードでは、インデクス番号0番に<code>null</code>を設定してから、
 * インデクス番号1へのセットを許可します。
 * これにより例外の発生を防ぐことができます。
 * </p>
 * <p>
 * セットと同様に、ゲット時にも不要な例外の発生を防ぐことができます。
 * その場合も、最初にインデクス番号4が指定された場合には内部的に0から3を生成します。
 * </p>
 * <p>
 * デフォルトでは<code>null</code>で要素を埋めますが、
 * 内部リストが<code>null</code>を許可しないリストである場合や、
 * カスタムオブジェクトを返却したい場合などもあります。
 * 例えば、空文字を設定したい場合は以下のようにします。
 * </p>
 * <pre>
 * List&lt;String&gt; list
 *         = new PopulationSafeList&lt;String&gt;(
 *                 new ArrayList&lt;String&gt;(), "");
 * </pre>
 * <p>
 * そうではなく、独自作成クラスのインスタンスを要素に設定したい場合は、
 * </p>
 * <pre>
 * List&lt;Hoge&gt; list
 *         = new PopulationSafeList&lt;Hoge&gt;(
 *                 new ArrayList&lt;Hoge&gt;(), Hoge.class);
 * </pre>
 * <p>
 * というように指定します。Hoge.classは要素毎にインスタンスが生成されます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @param <E> このリストが保持する要素の型
 * @see java.util.List
 */
public class PopulationSafeList<E> implements List<E> {

    private List<E> internal;
    private E toBePad;
    private Class<E> bean;

    /**
     * デフォルトコンストラクタです。
     */
    public PopulationSafeList() {}

    /**
     * 指定されたリストに関連付けて、このクラスを生成します。
     * @param internal このクラスが内部で利用するリスト
     */
    public PopulationSafeList(List<E> internal) {
        if (internal == null) throw new IllegalArgumentException();
        this.internal = internal;
    }

    /**
     * 指定されたリストに関連付けて、このクラスを生成します。
     * もし、不正なインデクスでのアクセスが発生した場合、
     * 例外が発生しないように指定された要素でパディングします。
     * @param internal このクラスが内部で利用するリスト
     * @param toBePad パディングに利用されるオブジェクト
     */
    public PopulationSafeList(List<E> internal, E toBePad) {
        this(internal);
        this.toBePad = toBePad;
    }

    /**
     * 指定されたリストに関連付けて、このクラスを生成します。
     * もし、不正なインデクスでのアクセスが発生した場合、
     * 例外が発生しないように指定されたクラスからインスタンスを生成し、
     * それを利用して要素をパディングします。
     * @param internal このクラスが内部で利用するリスト
     * @param bean パディングに利用する際にインスタンスを生成されるクラス
     */
    public PopulationSafeList(List<E> internal, Class<E> bean) {
        this(internal);
        this.bean = bean;
    }

    /**
     * 要素を取得します。
     * もし、インデクスが不正で例外が発生する可能性がある場合、
     * 指定されている要素を返却します。
     * @param index 取得する要素のインデクス
     * @return 指定されたインデクスの要素
     */
    public E get(int index) {
        if (internal.size() <= index) {
            for (int i = internal.size(); i < index + 1; i++) {
                internal.add(getContainableElement());
            }
        }
        return internal.get(index);
    }

    /**
     * 要素を設定します。
     * もし、インデクスが不正で例外が発生する可能性がある場合、
     * 指定されている要素で必要な分だけ要素をパディングします。
     * @param index 設定する要素のインデクス
     * @param element 設定する要素
     * @return 指定されたインデクスの要素
     */
    public E set(int index, E element) {
        if (internal.size() <= index) {
            for (int i = internal.size(); i < index + 1; i++) {
                internal.add(getContainableElement());
            }
        }
        return internal.set(index, element);
    }

    private E getContainableElement() {
        E element = null;
        if (toBePad != null) {
            element = toBePad;
        } else if (bean != null) {
            element = Reflects.make(bean);
        }
        return element;
    }

    // 委譲メソッド ------------------------------------------------------------

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(E o) {
        return internal.add(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, E element) {
        internal.add(index, element);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends E> c) {
        return internal.addAll(c);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        return internal.addAll(index, c);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#clear()
     */
    public void clear() {
        internal.clear();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> c) {
        return internal.containsAll(c);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return internal.equals(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#hashCode()
     */
    public int hashCode() {
        return internal.hashCode();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return internal.indexOf(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#iterator()
     */
    public Iterator<E> iterator() {
        return internal.iterator();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return internal.lastIndexOf(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#listIterator()
     */
    public ListIterator<E> listIterator() {
        return internal.listIterator();
    }


    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<E> listIterator(int index) {
        return internal.listIterator(index);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#remove(int)
     */
    public E remove(int index) {
        return internal.remove(index);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return internal.remove(o);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        return internal.removeAll(c);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        return internal.retainAll(c);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#size()
     */
    public int size() {
        return internal.size();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#subList(int, int)
     */
    public List<E> subList(int fromIndex, int toIndex) {
        return internal.subList(fromIndex, toIndex);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return internal.toArray();
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     * @see java.util.List#toArray(Object[])
     */
    public <T> T[] toArray(T[] a) {
        return internal.toArray(a);
    }

    /**
     * このメソッドは内部のリスト実装へ委譲として実装されています。
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public String toString() {
        return internal.toString();
    }
}
