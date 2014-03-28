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
 * 移送途中にあるデータに対して任意の操作を加えるためのクラスです。
 * 移送途中とは、{@link SourceStrategy}により{@link PortablePair}が生成されてから、
 * {@link DestinationStrategy}に届けられる前までを指します。
 * このクラスはその間の{@link PortablePair}インスタンスを自由に操作できるため、
 * 容易に{@link DestinationStrategy}に届けられる{@link PortablePair}詐称することができます。
 * これにより、例えば{@link KeyExchangeFilter PortablePairの識別キーの変換}や
 * {@link DeclarationAwareBeanPropertyTypeConverter データ型の変換}を容易に実現することができます。
 * </p>
 * <p>
 * 一方で、このクラスに渡される{@link PortablePair}ですら、詐称されている可能性がある点には注意してください。
 * これが直接的な問題となることはまれですが、もしこのような状況で問題が発生する場合には、
 * {@link #manipulate(PortablePair)}の項目を参照してください。
 * </p>
 * @see Porter
 * @see PortablePair
 * @see SourceStrategy
 * @see DestinationStrategy
 * @see KeyExchangeFilter
 * @see DeclarationAwareBeanPropertyTypeConverter
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface ManipulationFilter {

    /**
     * 初期化します。
     * このメソッドが呼ばれるタイミングは、
     * このオブジェクトが所属する{@link Porter}の
     * {@link Porter#initialize() Porter#initialize()メソッド}が呼ばれた時です。
     * @param source データ入力元オブジェクト
     * @param destination データ出力先オブジェクト
     * @see Porter
     * @see Porter#initialize()
     */
    void initialize(Object source, Object destination);

    /**
     * 移送されるデータを操作します。
     * データとは、{@link PortablePair}オブジェクトのインスタンスです。
     * このデータは、{@link SourceStrategy}により生成されたそれで、
     * {@link DestinationStrategy}に届けられる前の状態です。
     * つまり、このメソッドを実装することで、好きなようにデータを加工してから
     * {@link DestinationStrategy}によって処理させることができます。
     * このメソッドにより{@link DestinationStrategy}を欺くことができるのと同様に、
     * このメソッドに引数として渡される{@link PortablePair}オブジェクトのインスタンスも、
     * {@link SourceStrategy}により生成されたまさにそのインスタンスかどうかは保証されません。
     * このメソッドが呼ばれる前に、実は他の{@link ManipulationFilter}がデータを加工している可能性もあります。
     * これが直接問題になることはまずありませんが、もしそのような挙動がどうしても困る場合には、
     * {@link Porter#addManipulationFilter(ManipulationFilter)}での設定順序を最も先にするか、
     * {@link Porter#setManipulationFilters(java.util.List)}にて、任意の順序付けのリストを設定してください。
     * また、このメソッドの戻り値にnullが指定されていた場合、
     * {@link Porter}は直ちに{@link SourceStrategy#hasNext()}を呼び出し、
     * 現在のデータに対する処理を取りやめ、次のデータ移送を開始します。
     * @param sourcePair {@link SourceStrategy}により生成され、{@link DestinationStrategy}に届けられる前の{@link PortablePair}
     * @return 操作済みの{@link PortablePair}。もしnullを返却した場合、{@link Porter}は直ちに{@link SourceStrategy#hasNext()}を呼び出し、次のデータ移送を開始します
     * @see Porter#transport()
     * @see Porter#addManipulationFilter(ManipulationFilter)
     * @see Porter#setManipulationFilters(java.util.List)
     * @see PortablePair
     * @see SourceStrategy#hasNext()
     */
    PortablePair manipulate(PortablePair sourcePair);
}
