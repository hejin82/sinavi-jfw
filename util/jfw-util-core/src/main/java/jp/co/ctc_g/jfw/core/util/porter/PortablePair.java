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

package jp.co.ctc_g.jfw.core.util.porter;


/**
 * <p>
 * このクラスは、あるオブジェクトからあるオブジェクトへ渡される単一の値とその識別子を表現します。
 * このクラスは、{@link Porter#process()}内の処理において、ある種の伝達オブジェクトの役割を果たします。
 * </p>
 * <p>
 * このクラスのインスタンスの典型的なライフサイクルは次の通りです。
 * まず、{@link SourceStrategy#nextPair()}によりインスタンスが生成されます。
 * 次に、{@link ManipulationFilter#manipulate(PortablePair)}により何らかのフィルタリング処理が行なわれます。
 * この処理は複数回実行される可能性があります。
 * 最後に、{@link DestinationStrategy#assign(PortablePair)}に渡されます。
 * 一方で、このクラスは不変オブジェクトとして設計されているため、フィルタリング処理において他のインスタンスと差し替えられることがあります。
 * </p>
 * <p>
 * PortablePairは不変オブジェクトとして設計されています。
 * 移送される値とその識別子の両方またはいづれか一方が、可変オブジェクトである可能性もあります。
 * このクラスはその可能性については感知せず、また、移送処理の主担当である{@link Porter}も基本的には感知しません。
 * ただし、このような挙動は今後もそのようであることを保証するものではありませんし、
 * 任意に拡張されたストラテジやフィルタがこの動作を期待することを推奨しません。
 * </p>
 * <p>
 * また、このクラスのインスタンスの同値性及びハッシュ値は、内包される値と識別子に依存します。
 * このクラスの{@link PortablePair#equals(Object)}メソッドは、内包される値と識別子のそれぞれに対してequalsメソッドを呼び出し、
 * その結果が双方ともにtrueであった場合、同値であるとみなします。
 * また、hashCodeもほぼ同様に、内包される値と識別子のそれぞれに対してhashCodeメソッドを呼び出し、その和をハッシュ値とします。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class PortablePair {

    private final Object key;
    private final Object value;

    /**
     * PortablePairのインスタンスを生成します。
     * この場合、識別子及び値はnullとして生成されます。
     * 順序が重要になるような移送の場合には、このコンストラクタが利用される可能性があります。
     */
    public PortablePair() {
        this(null, null);
    }

    /**
     * PortablePairのインスタンスを生成します。
     * この場合、識別子はnullとして生成されます。
     * @param value 移送される値
     */
    public PortablePair(Object value) {
        this(null, value);
    }

    /**
     * PortablePairのインスタンスを生成します。
     * 移送値及び識別子は省略することが許されるため、nullを渡すことができます。
     * @param key 値の識別子
     * @param value 移送される値
     */
    public PortablePair(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 設定されている移送値の識別子を返却します。
     * PortablePairはこの識別子が実際にはどのような型であるかについては無関心です。
     * この識別子の型を決定するのは{@link SourceStrategy}クラスです。
     * @return 移送値の識別子となるオブジェクト
     */
    public Object getKey() {
        return key;
    }

    /**
     * 設定されている移送値を返却します。
     * PortablePairはこの移送値が実際にはどのような型であるかについては無関心です。
     * この移送値の型を決定するのは{@link SourceStrategy}クラスです。
     * @return 移送値として設定されているオブジェクト
     */
    public Object getValue() {
        return value;
    }

    /**
     * このクラスのインスタンスの文字列表現です。
     * 内包される値と識別子それぞれに対してtoString()メソッドを呼び出します。
     * @see java.lang.Object#toString()
     * @return キー・バリュー形式の文字列
     */
    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();
        description.append(super.toString()).append(" {");
        description.append("key = ").append(key != null ? key.toString() : "null");
        description.append(", value = ").append(value != null ? value.toString() : "null").append("}");
        return description.toString();
    }

    /**
     * 同値性を比較します。
     * 内包される値と識別子のそれぞれに対してequals()メソッドを呼び出し、
     * その結果が双方ともにtrueであった場合、同値であるとみなします。
     * また、値や識別子がnullとnullの場合でも、同値であるとみなします。
     * つまり、
     * <pre>
     * new PortablePair().equals(new PortablePair());
     * </pre>
     * は<code>true</code>を返却します。
     * @param another {@link PortablePair}のインスタンス
     * @return キー・バリューが同じかどうか
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object another) {
        if (another == null) return false;
        if (!(another instanceof PortablePair)) return false;
        PortablePair friend = (PortablePair) another;
        boolean keyEquiv = (key == null && friend.getKey() == null) ||
                (key != null && key.equals(friend.getKey()));
        boolean valueEquiv = (value == null && friend.getValue() == null) ||
                (value != null && value.equals(friend.getValue()));
        return keyEquiv && valueEquiv;
    }

    /**
     * ハッシュ値を生成します。
     * 内包される値と識別子のそれぞれに対してhashCode()メソッドを呼び出し、
     * それぞれの結果の和をハッシュ値であるとします。
     * @see java.lang.Object#hashCode()
     * @return ハッシュ値
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 + (key != null ? key.hashCode() : 0);
        hash = 31 + (value != null ? value.hashCode() : 0);
        return hash;
    }

}
